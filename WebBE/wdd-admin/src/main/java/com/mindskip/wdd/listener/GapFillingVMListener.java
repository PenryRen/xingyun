package com.mindskip.wdd.listener;

import cn.hutool.core.bean.DynaBean;
import cn.hutool.core.util.ReUtil;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.mindskip.wdd.domain.QuestionArchive;
import com.mindskip.wdd.domain.enums.QuestionTypeEnum;
import com.mindskip.wdd.utility.ExamUtil;
import com.mindskip.wdd.utility.HtmlUtil;
import com.mindskip.wdd.viewmodel.excel.ExcelResult;
import com.mindskip.wdd.viewmodel.excel.GapFillingVM;
import com.mindskip.wdd.viewmodel.excel.ListenerParameter;
import com.mindskip.wdd.viewmodel.question.QuestionEditItemVM;
import com.mindskip.wdd.viewmodel.question.QuestionEditRequestVM;
import com.mindskip.wdd.viewmodel.question.QuestionLike;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @version 1.7.0
 * @description: 填空题解析
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
 */
@AllArgsConstructor
public class GapFillingVMListener extends AnalysisEventListener<GapFillingVM> {

    private List<GapFillingVM> gapFillingVMS;
    private ListenerParameter listenerParameter;
    private Boolean escapeContent;


    @Override
    public void invoke(GapFillingVM data, AnalysisContext context) {
        if (null == data) return;
        try {
            ExcelResult excelResult = listenerParameter.getBeanValidator().validate(data);
            if (excelResult.getSuccess()) {
                if (ExamUtil.scoreFromVM(data.getScore()).equals(0)) {
                    gapFillingVMS.add(data);
                    data.setResult("分数填写错误");
                    return;
                }

                QuestionEditRequestVM questionEditRequestVM = listenerParameter.getQuestionMapping().toQuestionEditRequestVM(data);
                if (escapeContent) {
                    questionEditRequestVM.setTitle(HtmlUtil.escape(data.getTitle()));
                    questionEditRequestVM.setAnalyze(HtmlUtil.escape(data.getAnalyze()));
                }

                if (StringUtils.isNotBlank(data.getLevel())) {
                    QuestionArchive questionArchive = listenerParameter.getQuestionArchiveService().getByLevel(data.getLevel());
                    if (null != questionArchive) {
                        questionEditRequestVM.setQuestionArchiveId(questionArchive.getId());
                    } else {
                        gapFillingVMS.add(data);
                        data.setResult("分类未找到");
                        return;
                    }
                }

                DynaBean bean = DynaBean.create(data);
                List<QuestionEditItemVM> questionEditItemVMS = new ArrayList<>();
                StringBuilder stringBuilder = new StringBuilder(data.getTitle());
                Pattern pattern = Pattern.compile("\\$\\d\\$");
                Matcher matcher = pattern.matcher(stringBuilder);
                Integer index = 0;
                while (matcher.find()) {
                    ++index;
                    String uuid = UUID.randomUUID().toString();
                    String gapFormat = String.format("<span class=\"gapfilling-span\" data-gap-key=\"%s\">%d</span>", uuid, index);
                    stringBuilder = stringBuilder.replace(matcher.start(), matcher.end(), gapFormat);
                    matcher = pattern.matcher(stringBuilder);

                    String value = bean.get(String.valueOf((char) ((int) 'a' + (index - 1))));
                    if (StringUtils.isNotBlank(value)) {
                        QuestionEditItemVM questionEditItemVM = new QuestionEditItemVM();
                        questionEditItemVM.setItemUuid(uuid);
                        questionEditItemVM.setPrefix(index.toString());
                        questionEditItemVM.setScore(data.getScore());
                        if (escapeContent) {
                            questionEditItemVM.setContent(HtmlUtil.escape(value));
                        } else {
                            questionEditItemVM.setContent(value);
                        }
                        questionEditItemVMS.add(questionEditItemVM);
                    }
                }


                if (questionEditItemVMS.size() == 0) {
                    gapFillingVMS.add(data);
                    data.setResult("答案不能为空");
                    return;
                }

                String title = ReUtil.replaceAll(data.getTitle(), pattern, "");
                Long existQuestion = listenerParameter.getQuestionService().checkLikeQuestion(new QuestionLike(null, questionEditRequestVM.getQuestionArchiveId(), QuestionTypeEnum.GapFilling.getCode(), title));
                if (null != existQuestion) {
                    gapFillingVMS.add(data);
                    data.setResult("已存在重复题目");
                    return;
                }

                Integer questionScore = ExamUtil.scoreFromVM(data.getScore()) * questionEditItemVMS.size();
                questionEditRequestVM.setScore(ExamUtil.scoreToVM(questionScore));
                questionEditRequestVM.setTitle(stringBuilder.toString());
                questionEditRequestVM.setQuestionType(QuestionTypeEnum.GapFilling.getCode());
                questionEditRequestVM.setItems(questionEditItemVMS);
                listenerParameter.getQuestionService().insertQuestion(questionEditRequestVM, listenerParameter.getCreateUser());
            } else {
                gapFillingVMS.add(data);
                data.setResult(excelResult.getMessage());
            }
        } catch (Exception ex) {
            gapFillingVMS.add(data);
            data.setResult(ex.getMessage());
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {

    }
}
