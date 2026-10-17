package com.mindskip.wdd.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.mindskip.wdd.domain.QuestionArchive;
import com.mindskip.wdd.domain.enums.QuestionTypeEnum;
import com.mindskip.wdd.utility.ExamUtil;
import com.mindskip.wdd.utility.HtmlUtil;
import com.mindskip.wdd.viewmodel.excel.ExcelResult;
import com.mindskip.wdd.viewmodel.excel.ListenerParameter;
import com.mindskip.wdd.viewmodel.excel.ShortAnswerVM;
import com.mindskip.wdd.viewmodel.question.QuestionEditRequestVM;
import com.mindskip.wdd.viewmodel.question.QuestionLike;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @version 1.7.0
 * @description: 简答题解析
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
@AllArgsConstructor
public class ShortAnswerVMListener extends AnalysisEventListener<ShortAnswerVM> {

    private List<ShortAnswerVM> shortAnswerVMS;
    private ListenerParameter listenerParameter;
    private Boolean escapeContent;


    @Override
    public void invoke(ShortAnswerVM data, AnalysisContext context) {
        if (null == data) return;
        try {
            ExcelResult excelResult = listenerParameter.getBeanValidator().validate(data);
            if (excelResult.getSuccess()) {
                if (ExamUtil.scoreFromVM(data.getScore()).equals(0)) {
                    shortAnswerVMS.add(data);
                    data.setResult("分数填写错误");
                    return;
                }

                QuestionEditRequestVM questionEditRequestVM = listenerParameter.getQuestionMapping().toQuestionEditRequestVM(data);
                if (escapeContent) {
                    questionEditRequestVM.setTitle(HtmlUtil.escape(data.getTitle()));
                    questionEditRequestVM.setCorrect(HtmlUtil.escape(data.getCorrect()));
                    questionEditRequestVM.setAnalyze(HtmlUtil.escape(data.getAnalyze()));
                }

                if (StringUtils.isNotBlank(data.getLevel())) {
                    QuestionArchive questionArchive = listenerParameter.getQuestionArchiveService().getByLevel(data.getLevel());
                    if (null != questionArchive) {
                        questionEditRequestVM.setQuestionArchiveId(questionArchive.getId());
                    } else {
                        shortAnswerVMS.add(data);
                        data.setResult("分类未找到");
                        return;
                    }
                }

                Long existQuestion = listenerParameter.getQuestionService().checkLikeQuestion(new QuestionLike(null, questionEditRequestVM.getQuestionArchiveId(), QuestionTypeEnum.ShortAnswer.getCode(), questionEditRequestVM.getTitle()));
                if (null != existQuestion) {
                    shortAnswerVMS.add(data);
                    data.setResult("已存在重复题目");
                    return;
                }

                questionEditRequestVM.setQuestionType(QuestionTypeEnum.ShortAnswer.getCode());
                questionEditRequestVM.setItems(new ArrayList<>());
                listenerParameter.getQuestionService().insertQuestion(questionEditRequestVM, listenerParameter.getCreateUser());

            } else {
                shortAnswerVMS.add(data);
                data.setResult(excelResult.getMessage());
            }
        } catch (Exception ex) {
            shortAnswerVMS.add(data);
            data.setResult(ex.getMessage());
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {

    }
}
