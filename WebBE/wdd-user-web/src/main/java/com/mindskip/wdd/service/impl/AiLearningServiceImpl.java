package com.mindskip.wdd.service.impl;

import com.mindskip.wdd.domain.ExamPaperAnswer;
import com.mindskip.wdd.domain.User;
import com.mindskip.wdd.domain.enums.ExamPaperAnswerStatusEnum;
import com.mindskip.wdd.service.AiLearningService;
import com.mindskip.wdd.service.ExamPaperAnswerService;
import com.mindskip.wdd.utility.DateTimeUtil;
import com.mindskip.wdd.viewmodel.ai.AiLearningWorkspaceVM;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/** 使用本地 MySQL 考试记录生成 AI 学习中心数据。 */
@Service
@AllArgsConstructor
public class AiLearningServiceImpl implements AiLearningService {
    private static final int TREND_LIMIT = 6;
    private static final int RECENT_LIMIT = 6;

    private final ExamPaperAnswerService examPaperAnswerService;

    @Override
    public AiLearningWorkspaceVM getWorkspace(User user, String departmentName) {
        List<ExamPaperAnswer> records = examPaperAnswerService.getLearningRecords(user.getId());
        if (records == null) records = new ArrayList<>();
        records.sort(this::compareDesc);

        AiLearningWorkspaceVM workspace = new AiLearningWorkspaceVM();
        workspace.setStudent(buildStudent(user, departmentName));
        workspace.setCounts(buildCounts(records));

        List<ExamPaperAnswer> validFinalized = new ArrayList<>();
        for (ExamPaperAnswer record : records) {
            if (isFinalized(record) && scoreRate(record) != null) validFinalized.add(record);
        }
        validFinalized.sort(this::compareAsc);

        List<ExamPaperAnswer> trendRecords = tail(validFinalized, TREND_LIMIT);
        workspace.setTrend(toEvidenceList(trendRecords));
        workspace.setRecentExams(toEvidenceList(records.subList(0, Math.min(RECENT_LIMIT, records.size()))));
        workspace.setSummary(buildSummary(records, trendRecords, workspace.getCounts()));
        workspace.setInsights(buildInsights(validFinalized, workspace.getCounts()));
        workspace.setReport(buildReport(trendRecords, validFinalized.size(), workspace.getSummary()));
        workspace.setKnowledgePoints(buildKnowledgePointState());
        workspace.setMeta(buildMeta());
        return workspace;
    }

    private AiLearningWorkspaceVM.StudentVM buildStudent(User user, String departmentName) {
        AiLearningWorkspaceVM.StudentVM student = new AiLearningWorkspaceVM.StudentVM();
        student.setUserName(user.getUserName());
        student.setRealName(user.getRealName());
        student.setWorkNo(user.getWorkNo());
        student.setDepartmentName(departmentName);
        student.setJobTitle(user.getJobTitle());
        return student;
    }

    private AiLearningWorkspaceVM.CountsVM buildCounts(List<ExamPaperAnswer> records) {
        AiLearningWorkspaceVM.CountsVM counts = new AiLearningWorkspaceVM.CountsVM();
        counts.setTotal(records.size());
        for (ExamPaperAnswer record : records) {
            Integer status = record.getStatus();
            if (status != null && status == ExamPaperAnswerStatusEnum.Complete.getCode()) {
                counts.setFinalized(counts.getFinalized() + 1);
                if (scoreRate(record) == null) counts.setInvalidFinalized(counts.getInvalidFinalized() + 1);
                else counts.setValidFinalized(counts.getValidFinalized() + 1);
            } else if (status != null && status == ExamPaperAnswerStatusEnum.WaitJudge.getCode()) {
                counts.setWaitingReview(counts.getWaitingReview() + 1);
            } else if (status != null && status == ExamPaperAnswerStatusEnum.WaitCheck.getCode()) {
                counts.setWaitingVerification(counts.getWaitingVerification() + 1);
            } else if (status != null && status == ExamPaperAnswerStatusEnum.CheckError.getCode()) {
                counts.setVerificationFailed(counts.getVerificationFailed() + 1);
            } else counts.setOther(counts.getOther() + 1);
        }
        return counts;
    }

    private AiLearningWorkspaceVM.SummaryVM buildSummary(List<ExamPaperAnswer> records,
            List<ExamPaperAnswer> trendRecords,
            AiLearningWorkspaceVM.CountsVM counts) {
        AiLearningWorkspaceVM.SummaryVM summary = new AiLearningWorkspaceVM.SummaryVM();
        List<Double> scores = new ArrayList<>();
        for (ExamPaperAnswer record : trendRecords) scores.add(scoreRate(record));
        summary.setTrendSampleCount(scores.size());
        if (!scores.isEmpty()) {
            double total = 0D;
            for (Double score : scores) total += score;
            summary.setRecentAveragePercent(clampPercent(total / scores.size()));
        }
        if (scores.size() > 1) {
            summary.setChangeFromFirstPercentPoints(roundOne(scores.get(scores.size() - 1) - scores.get(0)));
        }
        if (scores.size() >= 3) {
            double mean = 0D;
            for (Double score : scores) mean += score;
            mean /= scores.size();
            double variance = 0D;
            for (Double score : scores) variance += Math.pow(score - mean, 2);
            summary.setVolatilityPercentPoints(roundOne(Math.sqrt(variance / scores.size())));
        }

        int correctTotal = 0;
        int questionTotal = 0;
        int accuracySamples = 0;
        for (ExamPaperAnswer record : records) {
            if (!isFinalized(record)) continue;
            Integer correct = record.getQuestionCorrect();
            Integer count = record.getQuestionCount();
            if (correct != null && count != null && count > 0 && correct >= 0 && correct <= count) {
                correctTotal += correct;
                questionTotal += count;
                accuracySamples++;
            }
        }
        summary.setAccuracySampleCount(accuracySamples);
        if (questionTotal > 0) summary.setQuestionAccuracyPercent(clampPercent(correctTotal * 100D / questionTotal));
        if (!records.isEmpty()) summary.setFinalizedPercent(clampPercent(counts.getFinalized() * 100D / records.size()));
        return summary;
    }

    private List<AiLearningWorkspaceVM.InsightVM> buildInsights(List<ExamPaperAnswer> validFinalized,
            AiLearningWorkspaceVM.CountsVM counts) {
        List<AiLearningWorkspaceVM.InsightVM> insights = new ArrayList<>();
        if (validFinalized.isEmpty()) {
            insights.add(insight("分析样本不足", "当前没有分数已定稿的正式考试。", "0 场"));
        } else {
            ExamPaperAnswer lowest = Collections.min(validFinalized, Comparator.comparingDouble(item -> scoreRate(item)));
            ExamPaperAnswer latest = validFinalized.get(validFinalized.size() - 1);
            insights.add(insight(String.format("最低得分出现在《%s》", paperName(lowest)),
                    String.format("该场得分率为 %d%%，建议优先复盘失分题目和作答过程。", clampPercent(scoreRate(lowest))),
                    String.format("失分 %s 分", formatScoreLoss(lowest))));
            Integer latestCorrect = latest.getQuestionCorrect();
            Integer latestCount = latest.getQuestionCount();
            boolean validSummary = latestCorrect != null && latestCount != null && latestCount > 0
                    && latestCorrect >= 0 && latestCorrect <= latestCount;
            insights.add(insight("最近一次正式考试作答线索",
                    validSummary ? String.format("最近完成《%s》，完全答对 %d / %d 题。", paperName(latest), latestCorrect, latestCount)
                            : String.format("《%s》缺少有效题目汇总。", paperName(latest)),
                    validSummary ? String.format("%d 题需复盘", latestCount - latestCorrect) : "无题目汇总"));
        }
        if (counts.getVerificationFailed() > 0) {
            insights.add(insight("存在核验失败记录", "建议在考试记录中检查并联系管理员处理。",
                    String.format("%d 条异常", counts.getVerificationFailed())));
        }
        if (counts.getWaitingReview() > 0 || counts.getWaitingVerification() > 0) {
            insights.add(insight("存在成绩尚未定稿的记录",
                    String.format("待批改 %d 条，待核验 %d 条；定稿前不计入趋势。",
                            counts.getWaitingReview(), counts.getWaitingVerification()),
                    String.format("%d 条待处理", counts.getWaitingReview() + counts.getWaitingVerification())));
        }
        if (counts.getInvalidFinalized() > 0) {
            insights.add(insight("存在分数字段异常的已完成记录", "这些记录不会作为 0 分进入趋势。",
                    String.format("%d 条异常", counts.getInvalidFinalized())));
        }
        return insights;
    }

    private AiLearningWorkspaceVM.ReportVM buildReport(List<ExamPaperAnswer> trendRecords, int evidenceExamCount,
            AiLearningWorkspaceVM.SummaryVM summary) {
        AiLearningWorkspaceVM.ReportVM report = new AiLearningWorkspaceVM.ReportVM();
        report.setEvidenceExamCount(evidenceExamCount);
        if (trendRecords.isEmpty()) {
            report.setSummary("需要先完成考试，才能形成可验证的学习结论。");
            return report;
        }
        if (trendRecords.size() == 1) {
            report.setSummary(String.format("当前只有 1 场已定稿正式考试，得分率为 %d%%，样本不足以判断趋势。",
                    summary.getRecentAveragePercent()));
            return report;
        }
        double change = summary.getChangeFromFirstPercentPoints() == null ? 0D : summary.getChangeFromFirstPercentPoints();
        String direction = change > 0 ? String.format("末场较本窗口首场高 %.1f 个百分点", Math.abs(change))
                : change < 0 ? String.format("末场较本窗口首场低 %.1f 个百分点", Math.abs(change))
                : "末场与本窗口首场得分率相同";
        report.setSummary(String.format("最近 %d 场平均得分率为 %d%%，%s。中间场次存在波动，应结合具体答卷复盘。",
                trendRecords.size(), summary.getRecentAveragePercent(), direction));
        return report;
    }

    private AiLearningWorkspaceVM.KnowledgePointStateVM buildKnowledgePointState() {
        AiLearningWorkspaceVM.KnowledgePointStateVM state = new AiLearningWorkspaceVM.KnowledgePointStateVM();
        state.setAvailable(false);
        state.setReasonCode("QUESTION_TAGS_NOT_AVAILABLE");
        state.setMessage("题目尚未接入知识点标签，当前不生成推测性薄弱知识点。");
        return state;
    }

    private AiLearningWorkspaceVM.MetaVM buildMeta() {
        AiLearningWorkspaceVM.MetaVM meta = new AiLearningWorkspaceVM.MetaVM();
        meta.setSource("LOCAL_MYSQL");
        meta.setScope("CURRENT_USER_FORMAL_EXAMS");
        meta.setComplete(true);
        meta.setGeneratedAt(DateTimeUtil.dateTimeFullFormat(new Date()));
        return meta;
    }

    private List<AiLearningWorkspaceVM.ExamEvidenceVM> toEvidenceList(List<ExamPaperAnswer> records) {
        List<AiLearningWorkspaceVM.ExamEvidenceVM> result = new ArrayList<>();
        for (ExamPaperAnswer record : records) {
            AiLearningWorkspaceVM.ExamEvidenceVM evidence = new AiLearningWorkspaceVM.ExamEvidenceVM();
            evidence.setId(record.getId());
            evidence.setPaperName(record.getPaperName());
            evidence.setCreateTime(record.getCreateTime() == null ? null : DateTimeUtil.dateTimeFullFormat(record.getCreateTime()));
            evidence.setStatus(record.getStatus());
            ExamPaperAnswerStatusEnum status = ExamPaperAnswerStatusEnum.fromCode(record.getStatus());
            evidence.setStatusName(status == null ? "其他状态" : status.getName());
            evidence.setWatch(Boolean.TRUE.equals(record.getWatch()));
            Double rate = scoreRate(record);
            evidence.setScoreRatePercent(rate == null ? null : roundTwo(rate));
            evidence.setQuestionCorrect(record.getQuestionCorrect());
            evidence.setQuestionCount(record.getQuestionCount());
            result.add(evidence);
        }
        return result;
    }

    private List<ExamPaperAnswer> tail(List<ExamPaperAnswer> records, int limit) {
        int start = Math.max(0, records.size() - limit);
        return new ArrayList<>(records.subList(start, records.size()));
    }

    private AiLearningWorkspaceVM.InsightVM insight(String title, String description, String evidence) {
        AiLearningWorkspaceVM.InsightVM item = new AiLearningWorkspaceVM.InsightVM();
        item.setTitle(title);
        item.setDescription(description);
        item.setEvidence(evidence);
        return item;
    }

    private boolean isFinalized(ExamPaperAnswer record) {
        return record.getStatus() != null && record.getStatus() == ExamPaperAnswerStatusEnum.Complete.getCode();
    }

    private Double scoreRate(ExamPaperAnswer record) {
        Integer total = record.getPaperScore();
        Integer score = record.getUserScore();
        if (total == null || score == null || total <= 0 || score < 0 || score > total) return null;
        return score * 100D / total;
    }

    private String paperName(ExamPaperAnswer record) {
        return record.getPaperName() == null || record.getPaperName().trim().isEmpty() ? "未命名试卷" : record.getPaperName();
    }

    private String formatScoreLoss(ExamPaperAnswer record) {
        if (record.getPaperScore() == null || record.getUserScore() == null) return "--";
        BigDecimal loss = BigDecimal.valueOf(Math.max(0, record.getPaperScore() - record.getUserScore()))
                .divide(BigDecimal.TEN, 1, RoundingMode.HALF_UP).stripTrailingZeros();
        return loss.toPlainString();
    }

    private int compareAsc(ExamPaperAnswer left, ExamPaperAnswer right) {
        int dateCompare = compareNullableDate(left.getCreateTime(), right.getCreateTime());
        return dateCompare != 0 ? dateCompare : compareNullableLong(left.getId(), right.getId());
    }

    private int compareDesc(ExamPaperAnswer left, ExamPaperAnswer right) { return -compareAsc(left, right); }

    private int compareNullableDate(Date left, Date right) {
        if (left == null && right == null) return 0;
        if (left == null) return -1;
        if (right == null) return 1;
        return left.compareTo(right);
    }

    private int compareNullableLong(Long left, Long right) {
        if (left == null && right == null) return 0;
        if (left == null) return -1;
        if (right == null) return 1;
        return left.compareTo(right);
    }

    private int clampPercent(double value) { return (int) Math.max(0, Math.min(100, Math.round(value))); }
    private Double roundOne(double value) { return BigDecimal.valueOf(value).setScale(1, RoundingMode.HALF_UP).doubleValue(); }
    private Double roundTwo(double value) { return BigDecimal.valueOf(value).setScale(2, RoundingMode.HALF_UP).doubleValue(); }
}
