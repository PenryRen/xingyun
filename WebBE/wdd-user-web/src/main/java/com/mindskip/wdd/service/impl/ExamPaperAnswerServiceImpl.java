package com.mindskip.wdd.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mindskip.wdd.domain.ExamPaperAnswer;
import com.mindskip.wdd.domain.ExamPaperAnswerJson;
import com.mindskip.wdd.domain.ExamPaperCache;
import com.mindskip.wdd.domain.User;
import com.mindskip.wdd.domain.enums.ExamPaperAnswerStatusEnum;
import com.mindskip.wdd.domain.enums.QuestionTypeEnum;
import com.mindskip.wdd.domain.frame.*;
import com.mindskip.wdd.mapping.ExamPaperAnswerMapping;
import com.mindskip.wdd.mapping.ExamPaperMapping;
import com.mindskip.wdd.repository.ExamPaperAnswerJsonMapper;
import com.mindskip.wdd.repository.ExamPaperAnswerMapper;
import com.mindskip.wdd.service.ExamPaperAnswerService;
import com.mindskip.wdd.service.ExamPaperQuestionAnswerService;
import com.mindskip.wdd.service.enums.ResultEnum;
import com.mindskip.wdd.utility.ExamUtil;
import com.mindskip.wdd.utility.HtmlUtil;
import com.mindskip.wdd.viewmodel.exam.answer.*;
import com.mindskip.wdd.viewmodel.exam.paper.ExamPaperDoPaperVM;
import com.mindskip.wdd.viewmodel.exam.paper.ExamPaperDoResponseVM;
import com.mindskip.wdd.viewmodel.exam.paper.ExamPaperDoTitle;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @version 1.7.0
 * @description: 答卷
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
 */
@Service
@AllArgsConstructor
public class ExamPaperAnswerServiceImpl extends ServiceImpl<ExamPaperAnswerMapper, ExamPaperAnswer> implements ExamPaperAnswerService {

    private final ExamPaperAnswerMapper examPaperAnswerMapper;
    private final ExamPaperAnswerMapping examPaperAnswerMapping;
    private final ExamPaperQuestionAnswerService examPaperQuestionAnswerService;
    private final ExamPaperAnswerJsonMapper examPaperAnswerJsonMapper;
    private final ExamPaperMapping examPaperMapping;


    @Override
    public ExamPaperAnswerStatusEnum getStatus(Long paperAnswerId) {
        Integer status = examPaperAnswerMapper.getStatus(paperAnswerId);
        return ExamPaperAnswerStatusEnum.fromCode(status);
    }

    @Override
    @Transactional
    public ExamPaperAnswerResult submit(ExamPaperCache examPaperCache, ExamPaperAnswerFrame examPaperAnswerFrame, User createUser) {
        SelectByUserVM selectByUserVM = new SelectByUserVM();
        selectByUserVM.setPaperId(examPaperAnswerFrame.getPaperId());
        selectByUserVM.setUserId(createUser.getId());
        ExamPaperAnswer examPaperAnswer = examPaperAnswerMapper.selectByPaperUserId(selectByUserVM);
        if (null != examPaperAnswer) {
            return new ExamPaperAnswerResult(ResultEnum.FAIL, "该试卷只能做一次");
        }

        Integer sumScore = examPaperAnswerFrame.getQuestionAnswerFrameList().stream().mapToInt(questionAnswerFrame -> {
            QuestionFrame questionFrame = examPaperCache.getQuestionFrameList().stream().filter(qf -> qf.getQuestionId().equals(questionAnswerFrame.getQuestionId())).findFirst().get();
            return questionAnswerJudge(questionAnswerFrame, questionFrame);
        }).sum();

        return new ExamPaperAnswerResult(ResultEnum.SUCCESS, ExamUtil.scoreToVM(sumScore));
    }

    @Override
    public PageInfo<ExamPaperAnswer> page(ExamPaperAnswerPageRequestVM requestVM) {
        return PageHelper.startPage(requestVM.getPageIndex(), requestVM.getPageSize(), "id desc").doSelectPageInfo(() ->
                examPaperAnswerMapper.page(requestVM));
    }


    @Override
    public ExamPaperAnswerEditResponseVM toExamPaperAnswerEditResponseVM(ExamPaperCache examPaperCache, ExamPaperAnswer examPaperAnswer) {

        //用户答案
        ExamPaperAnswerInfoResponseVM examPaperAnswerInfoResponseVM = examPaperAnswerMapping.toExamPaperAnswerInfoResponseVM(examPaperAnswer);
        ExamPaperAnswerJson examPaperAnswerJson = examPaperAnswerJsonMapper.selectById(examPaperAnswer.getAnswerFrameId());
        ExamPaperAnswerFrame examPaperAnswerFrame = examPaperAnswerJson.getContent();
        List<QuestionAnswerFrame> questionAnswerFrameList = examPaperAnswerFrame.getQuestionAnswerFrameList();
        examPaperAnswerInfoResponseVM.setQuestionAnswerFrameList(questionAnswerFrameList);


        ExamPaperDoResponseVM examPaperDoResponseVM = new ExamPaperDoResponseVM();
        /*ExamPaper examPaper = examPaperCache.getExamPaper();*/
        ExamPaperFrame examPaperFrame = examPaperCache.getExamPaperFrame();
        List<QuestionFrame> questionFrameList = examPaperCache.getQuestionFrameList();
        ExamPaperDoPaperVM examPaperDoPaperVM = examPaperMapping.toExamPaperDoResponseVM(examPaperCache);
        List<ExamPaperDoTitle> examPaperDoTitleList = new ArrayList<>(examPaperFrame.getExamPaperItemFrames().size());


        Integer itemOrder = 0;
        Boolean questionItemMess = examPaperCache.getQuestionItemMess() != null && examPaperCache.getQuestionItemMess();
        for (ExamPaperItemFrame paperItemFrame : examPaperFrame.getExamPaperItemFrames()) {
            ExamPaperDoTitle title = examPaperMapping.toExamPaperDoTitle(paperItemFrame);
            List<QuestionFrame> questionFrameVMList = new ArrayList<>(paperItemFrame.getExamPaperItemQuestionFrames().size());

            for (ExamPaperItemQuestionFrame ignored : paperItemFrame.getExamPaperItemQuestionFrames()) {
                //用户提交顺序
                Long questionId = questionAnswerFrameList.get(itemOrder).getQuestionId();
                ExamPaperItemQuestionFrame sortExamPaperItemQuestionFrame = paperItemFrame.getExamPaperItemQuestionFrames().stream().filter(qf -> qf.getId().equals(questionId)).findFirst().get();
                QuestionFrame questionFrame = questionFrameList.stream().filter(qf -> qf.getQuestionId().equals(questionId)).findFirst().get();
                QuestionAnswerFrame questionAnswerFrame = questionAnswerFrameList.stream().filter(qa -> qa.getQuestionId().equals(questionFrame.getQuestionId())).findFirst().get();
                questionFrameAndAnswer(sortExamPaperItemQuestionFrame, questionFrame, questionAnswerFrame, questionItemMess, ++itemOrder);
                questionFrameVMList.add(questionFrame);
            }

            title.setQuestionFrameList(questionFrameVMList);
            examPaperDoTitleList.add(title);
        }
        examPaperDoPaperVM.setExamPaperDoTitleList(examPaperDoTitleList);
        examPaperDoResponseVM.setPaper(examPaperDoPaperVM);

        ExamPaperAnswerEditResponseVM examPaperAnswerEditResponseVM = new ExamPaperAnswerEditResponseVM();
        examPaperAnswerEditResponseVM.setPaper(examPaperDoPaperVM);
        examPaperAnswerEditResponseVM.setAnswer(examPaperAnswerInfoResponseVM);
        return examPaperAnswerEditResponseVM;
    }

    @Override
    public Boolean needJudge(QuestionFrame questionFrame) {
        QuestionTypeEnum questionTypeEnum = QuestionTypeEnum.fromCode(questionFrame.getQuestionType());
        if (QuestionTypeEnum.ShortAnswer == questionTypeEnum) {
            return true;
        }
        return false;
    }

    /**
     * 判断是否需要核验
     *
     * @param questionFrame the question frame
     * @return the boolean
     */
    @Override
    public Boolean needCheck(QuestionFrame questionFrame) {
        QuestionTypeEnum questionTypeEnum = QuestionTypeEnum.fromCode(questionFrame.getQuestionType());
        if (QuestionTypeEnum.TrainOperate  == questionTypeEnum) {
            return true;
        }
        return false;
    }

    @Override
    public Integer questionAnswerJudge(QuestionAnswerFrame questionAnswerFrame, QuestionFrame questionFrame) {
        QuestionTypeEnum questionTypeEnum = QuestionTypeEnum.fromCode(questionFrame.getQuestionType());
        switch (questionTypeEnum) {
            case SingleChoice:
            case TrueFalse:
                if (questionFrame.getCorrectKey().equals(questionAnswerFrame.getContentKey())) {
                    questionAnswerFrame.setCustomerScore(questionAnswerFrame.getQuestionScore());
                    questionAnswerFrame.setCustomerScoreVM(ExamUtil.scoreToVM(questionAnswerFrame.getCustomerScore()));
                    questionAnswerFrame.setDoRight(true);
                } else {
                    questionAnswerFrame.setCustomerScore(0);
                    questionAnswerFrame.setCustomerScoreVM("0");
                    questionAnswerFrame.setDoRight(false);
                }
                return questionAnswerFrame.getCustomerScore();
            case MultipleChoice:
                String questionStr = questionFrame.getCorrectArrayKey().stream().sorted().map(d -> d.toString()).collect(Collectors.joining());
                String userAnswerStr = questionAnswerFrame.getContentArrayKey().stream().sorted().map(d -> d.toString()).collect(Collectors.joining());
                if (userAnswerStr.equals(questionStr)) {
                    questionAnswerFrame.setCustomerScore(questionAnswerFrame.getQuestionScore());
                    questionAnswerFrame.setCustomerScoreVM(ExamUtil.scoreToVM(questionAnswerFrame.getCustomerScore()));
                    questionAnswerFrame.setDoRight(true);
                } else {
                    questionAnswerFrame.setCustomerScore(0);
                    questionAnswerFrame.setCustomerScoreVM("0");
                    questionAnswerFrame.setDoRight(false);
                }
                return questionAnswerFrame.getCustomerScore();
            case UncertainMultipleChoice:
                Boolean fullRight = null;
                Integer rightScore = 0;
                List<String> questionStrList = questionFrame.getCorrectArrayKey().stream().sorted().map(d -> d.toString()).collect(Collectors.toList());
                List<String> userAnswerStrList = questionAnswerFrame.getContentArrayKey().stream().sorted().map(d -> d.toString()).collect(Collectors.toList());
                if (questionStrList.size() < userAnswerStrList.size()) { //多选了
                    fullRight = false;
                } else if (questionStrList.size() == userAnswerStrList.size()) {
                    if (questionStrList.stream().collect(Collectors.joining()).equals(userAnswerStrList.stream().collect(Collectors.joining()))) { //完全正确
                        fullRight = true;
                    } else { //选错了
                        fullRight = false;
                    }
                } else {
                    for (String userItem : userAnswerStrList) {
                        if (questionStrList.stream().anyMatch(qsi -> qsi.equals(userItem))) {
                            QuestionItemFrame questionItemFrame = questionFrame.getQuestionItemFrames().stream().filter(qf -> qf.getKey().equals(Integer.parseInt(userItem))).findFirst().get();
                            rightScore += questionItemFrame.getScore();
                        } else { //选错了
                            fullRight = false;
                            break;
                        }
                    }
                }
                if (null == fullRight) { //按比例给分,半对
                    float percent = (float) questionAnswerFrame.getQuestionScore() / questionFrame.getScore();
                    questionAnswerFrame.setCustomerScore(Math.round(rightScore * percent));
                    questionAnswerFrame.setCustomerScoreVM(ExamUtil.scoreToVM(questionAnswerFrame.getCustomerScore()));
                    questionAnswerFrame.setDoRight(false);
                } else if (fullRight) {
                    questionAnswerFrame.setCustomerScore(questionAnswerFrame.getQuestionScore());
                    questionAnswerFrame.setCustomerScoreVM(ExamUtil.scoreToVM(questionAnswerFrame.getCustomerScore()));
                    questionAnswerFrame.setDoRight(true);
                } else {
                    questionAnswerFrame.setCustomerScore(0);
                    questionAnswerFrame.setCustomerScoreVM("0");
                    questionAnswerFrame.setDoRight(false);
                }
                return questionAnswerFrame.getCustomerScore();
            case GapFilling:
                Integer sumScore = 0;
                Integer doRightItem = 0;
                for (int i = 0; i < questionAnswerFrame.getContentArray().size(); i++) {
                    QuestionItemFrame correctItem = questionFrame.getQuestionItemFrames().get(i);
                    String answerContent = questionAnswerFrame.getContentArray().get(i);
                    String correctItemContent = correctItem.getContent();
                    if (null != answerContent) {
                        if (correctItemContent.contains("|")) {
                            List<String> correctList = Arrays.asList(correctItemContent.split("\\|"));
                            for (String gapCorrectItem : correctList) {
                                if (HtmlUtil.clearAnswerHtml(gapCorrectItem).equals(HtmlUtil.clearAnswerHtml(answerContent))) {
                                    sumScore += correctItem.getScore();
                                    ++doRightItem;
                                    break;
                                }
                            }
                        } else {
                            if (HtmlUtil.clearAnswerHtml(answerContent).equals(HtmlUtil.clearAnswerHtml(correctItemContent))) {
                                sumScore += correctItem.getScore();
                                ++doRightItem;
                            }
                        }
                    }
                }
                Boolean doRight = doRightItem == questionFrame.getQuestionItemFrames().size();
                if (doRight) {
                    questionAnswerFrame.setCustomerScore(questionAnswerFrame.getQuestionScore());
                    questionAnswerFrame.setCustomerScoreVM(ExamUtil.scoreToVM(questionAnswerFrame.getCustomerScore()));
                    questionAnswerFrame.setDoRight(true);
                } else {
                    float percent = (float) questionAnswerFrame.getQuestionScore() / questionFrame.getScore();
                    questionAnswerFrame.setCustomerScore(Math.round(sumScore * percent));
                    questionAnswerFrame.setCustomerScoreVM(ExamUtil.scoreToVM(questionAnswerFrame.getCustomerScore()));
                    questionAnswerFrame.setDoRight(false);
                }
                return questionAnswerFrame.getCustomerScore();
            default:
                return 0;
        }
    }

    @Override
    public void xssClear(QuestionAnswerFrame questionAnswerFrame) {
        QuestionTypeEnum questionTypeEnum = QuestionTypeEnum.fromCode(questionAnswerFrame.getQuestionType());
        switch (questionTypeEnum) {
            case GapFilling:
                for (int i = 0; i < questionAnswerFrame.getContentArray().size(); i++) {
                    String itemContent = questionAnswerFrame.getContentArray().get(i);
                    questionAnswerFrame.getContentArray().set(i, HtmlUtil.xssClear(itemContent));
                }
                break;
            case ShortAnswer:
                questionAnswerFrame.setContent(HtmlUtil.xssClear(questionAnswerFrame.getContent()));
                break;
        }
    }

    @Override
    public void questionFrameAndAnswer(ExamPaperItemQuestionFrame examPaperItemQuestionFrame, QuestionFrame questionFrame, QuestionAnswerFrame questionAnswerFrame, Boolean questionItemMess, Integer itemOrder) {
        questionFrame.setItemOrder(itemOrder);
        questionFrame.setTrickScore(examPaperItemQuestionFrame.getTrickScore());
        //题目选项顺序还原
        if (questionItemMess) {
            examPaperQuestionAnswerService.questionItemMessRestore(questionFrame, questionAnswerFrame);
        }
    }
}
