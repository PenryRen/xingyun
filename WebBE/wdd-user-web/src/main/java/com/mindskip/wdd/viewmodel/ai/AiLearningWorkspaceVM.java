package com.mindskip.wdd.viewmodel.ai;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * AI 学习中心聚合数据。
 * 所有统计均由 WebBE 基于当前登录用户的本地 MySQL 数据计算。
 */
@Data
public class AiLearningWorkspaceVM {
    private StudentVM student = new StudentVM();
    private CountsVM counts = new CountsVM();
    private SummaryVM summary = new SummaryVM();
    private List<ExamEvidenceVM> trend = new ArrayList<>();
    private List<ExamEvidenceVM> recentExams = new ArrayList<>();
    private List<InsightVM> insights = new ArrayList<>();
    private ReportVM report = new ReportVM();
    private KnowledgePointStateVM knowledgePoints = new KnowledgePointStateVM();
    private MetaVM meta = new MetaVM();

    @Data
    public static class StudentVM {
        private String userName;
        private String realName;
        private String workNo;
        private String departmentName;
        private String jobTitle;
    }

    @Data
    public static class CountsVM {
        private int total;
        private int finalized;
        private int validFinalized;
        private int invalidFinalized;
        private int waitingReview;
        private int waitingVerification;
        private int verificationFailed;
        private int other;
    }

    @Data
    public static class SummaryVM {
        private int trendSampleCount;
        private Integer recentAveragePercent;
        private Double changeFromFirstPercentPoints;
        private Double volatilityPercentPoints;
        private Integer questionAccuracyPercent;
        private int accuracySampleCount;
        private Integer finalizedPercent;
    }

    @Data
    public static class ExamEvidenceVM {
        private Long id;
        private String paperName;
        private String createTime;
        private Integer status;
        private String statusName;
        private Boolean watch;
        private Double scoreRatePercent;
        private Integer questionCorrect;
        private Integer questionCount;
    }

    @Data
    public static class InsightVM {
        private String title;
        private String description;
        private String evidence;
    }

    @Data
    public static class ReportVM {
        private String summary;
        private int evidenceExamCount;
    }

    @Data
    public static class KnowledgePointStateVM {
        private boolean available;
        private String reasonCode;
        private String message;
    }

    @Data
    public static class MetaVM {
        private String source;
        private String scope;
        private boolean complete;
        private String generatedAt;
    }
}
