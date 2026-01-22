package com.mindskip.wdd.listener;

import cn.hutool.core.bean.DynaBean;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.mindskip.wdd.domain.QuestionArchive;
import com.mindskip.wdd.domain.enums.QuestionTypeEnum;
import com.mindskip.wdd.utility.ExamUtil;
import com.mindskip.wdd.utility.HtmlUtil;
import com.mindskip.wdd.viewmodel.excel.ExcelResult;
import com.mindskip.wdd.viewmodel.excel.ListenerParameter;
import com.mindskip.wdd.viewmodel.excel.MultipleVM;
import com.mindskip.wdd.viewmodel.question.QuestionEditItemVM;
import com.mindskip.wdd.viewmodel.question.QuestionEditRequestVM;
import com.mindskip.wdd.viewmodel.question.QuestionLike;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @version 1.7.0
 * @description: 多选题解析
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
 */
@AllArgsConstructor
public class MultipleVMListener extends AnalysisEventListener<MultipleVM> {

    private List<MultipleVM> multipleVMS;
    private ListenerParameter listenerParameter;
    private Boolean escapeContent;


    @Override
    public void invoke(MultipleVM data, AnalysisContext context) {
        if (null == data) return;
        try {
            ExcelResult excelResult = listenerParameter.getBeanValidator().validate(data);
            if (excelResult.getSuccess()) {
                if (ExamUtil.scoreFromVM(data.getScore()).equals(0)) {
                    multipleVMS.add(data);
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
                        multipleVMS.add(data);
                        data.setResult("分类未找到");
                        return;
                    }
                }


                DynaBean bean = DynaBean.create(data);
                List<QuestionEditItemVM> questionEditItemVMS = new ArrayList<>();
                char prefix = 'A';
                for (int i = 1; i < 9; i++) {
                    String value = bean.get(String.valueOf((char) ((int) 'a' + (i - 1))));
                    if (StringUtils.isNotBlank(value)) {
                        QuestionEditItemVM questionEditItemVM = new QuestionEditItemVM();
                        questionEditItemVM.setPrefix(String.valueOf(prefix));
                        if (escapeContent) {
                            questionEditItemVM.setContent(HtmlUtil.escape(value));
                        } else {
                            questionEditItemVM.setContent(value);
                        }
                        questionEditItemVMS.add(questionEditItemVM);
                    }
                    prefix = (char) (((int) prefix) + 1);
                }

                if (questionEditItemVMS.size() == 0) {
                    multipleVMS.add(data);
                    data.setResult("选项不能为空");
                    return;
                }

                List<String> correctArray = new ArrayList<>();
                for (char item : data.getCorrect().toCharArray()) {
                    correctArray.add(String.valueOf(item));
                }

                for (String correct : correctArray) {
                    Boolean correctFind = questionEditItemVMS.stream().anyMatch(qItem -> qItem.getPrefix().equals(correct));
                    if (!correctFind) {
                        multipleVMS.add(data);
                        data.setResult("标答不正确");
                        return;
                    }
                }


                Long existQuestion = listenerParameter.getQuestionService().checkLikeQuestion(new QuestionLike(null, questionEditRequestVM.getQuestionArchiveId(), QuestionTypeEnum.MultipleChoice.getCode(), questionEditRequestVM.getTitle()));
                if (null != existQuestion) {
                    multipleVMS.add(data);
                    data.setResult("已存在重复题目");
                    return;
                }

                questionEditRequestVM.setCorrectArray(correctArray);
                questionEditRequestVM.setQuestionType(QuestionTypeEnum.MultipleChoice.getCode());
                questionEditRequestVM.setItems(questionEditItemVMS);

                listenerParameter.getQuestionService().insertQuestion(questionEditRequestVM, listenerParameter.getCreateUser());
            } else {
                multipleVMS.add(data);
                data.setResult(excelResult.getMessage());
            }
        } catch (Exception ex) {
            multipleVMS.add(data);
            data.setResult(ex.getMessage());
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {

    }
}
