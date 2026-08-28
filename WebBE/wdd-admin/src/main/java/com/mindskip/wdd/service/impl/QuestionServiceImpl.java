package com.mindskip.wdd.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mindskip.wdd.domain.Question;
import com.mindskip.wdd.domain.QuestionJson;
import com.mindskip.wdd.domain.User;
import com.mindskip.wdd.domain.enums.QuestionStatusEnum;
import com.mindskip.wdd.domain.enums.QuestionTypeEnum;
import com.mindskip.wdd.domain.frame.QuestionFrame;
import com.mindskip.wdd.domain.frame.QuestionItemFrame;
import com.mindskip.wdd.domain.other.QuestionRandomItem;
import com.mindskip.wdd.mapping.QuestionMapping;
import com.mindskip.wdd.repository.QuestionJsonMapper;
import com.mindskip.wdd.repository.QuestionMapper;
import com.mindskip.wdd.service.QuestionService;
import com.mindskip.wdd.utility.HtmlUtil;
import com.mindskip.wdd.viewmodel.question.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @version 1.7.0
 * @description: 题目
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
@Service
@AllArgsConstructor
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper, Question> implements QuestionService {

    private final QuestionMapper questionMapper;
    private final QuestionMapping questionMapping;
    private final QuestionJsonMapper questionJsonMapper;


    @Override
    public PageInfo<Question> page(QuestionPageRequestVM requestVM) {
        return PageHelper.startPage(requestVM.getPageIndex(), requestVM.getPageSize(), "id desc").doSelectPageInfo(() ->
                questionMapper.page(requestVM)
        );
    }

    /**
     * 插入题目
     *
     * @param questionEditRequestVM the question edit request vm
     * @param createUser            the create user
     * @return the question
     */
    @Override
    @Transactional
    public Question insertQuestion(QuestionEditRequestVM questionEditRequestVM, User createUser) {
        xssClear(questionEditRequestVM);
        QuestionFrame questionFrame = questionMapping.toQuestionFrame(questionEditRequestVM);
        QuestionJson questionJson = new QuestionJson(questionFrame);
        List<QuestionItemFrame> questionItemFrameList = questionMapping.toQuestionItemFrameList(questionEditRequestVM.getItems());
        questionCorrectFromVM(questionEditRequestVM, questionFrame, questionItemFrameList);
        questionFrame.setQuestionItemFrames(questionItemFrameList);

        Question question = questionMapping.toQuestion(questionEditRequestVM);
        question.setQuestionFrameId(questionFrame.getId());
        question.setDeleted(false);
        question.setStatus(QuestionStatusEnum.OK.getCode());
        question.setCreateUser(createUser.getId());
        question.setCreateDepartmentId(createUser.getDepartmentId());
        question.setCreateTime(new Date());
        questionMapper.insert(question);

        questionFrame.setQuestionId(question.getId());


        questionJsonMapper.insert(questionJson);
        return question;
    }

    /**
     * 更新题目
     *
     * @param questionEditRequestVM the question edit request vm
     * @return the question
     */
    @Override
    @Transactional
    public Question updateQuestion(QuestionEditRequestVM questionEditRequestVM) {
        xssClear(questionEditRequestVM);
        Question question = questionMapper.selectById(questionEditRequestVM.getId());

        QuestionJson questionJson = questionJsonMapper.selectById(question.getQuestionFrameId());
        QuestionFrame questionFrame = questionJson.getContent();
        List<QuestionItemFrame> newQuestionItemFrameList = questionMapping.toQuestionItemFrameList(questionEditRequestVM.getItems());
        questionMapping.toQuestionFrame(questionEditRequestVM, questionFrame);
        questionCorrectFromVM(questionEditRequestVM, questionFrame, newQuestionItemFrameList);
        questionFrame.setQuestionItemFrames(newQuestionItemFrameList);

        questionJsonMapper.updateById(questionJson);

        questionMapping.toQuestion(questionEditRequestVM, question);
        questionMapper.updateById(question);
        return question;
    }

    /**
     * 组装题目对象
     *
     * @param id the id
     * @return the question edit request vm
     */
    @Override
    public QuestionEditRequestVM selectQuestionEditRequestVM(Long id) {
        Question question = questionMapper.selectById(id);
        QuestionEditRequestVM questionEditRequestVM = questionMapping.toQuestionEditRequestVM(question);
        QuestionJson questionJson = questionJsonMapper.selectById(question.getQuestionFrameId());
        QuestionFrame questionFrame = questionJson.getContent();
        questionMapping.toQuestionEditRequestVM(questionFrame, questionEditRequestVM);
        questionCorrectToVM(questionFrame, questionEditRequestVM);
        List<QuestionEditItemVM> questionEditItemVMS = questionMapping.toQuestionEditItemVMList(questionFrame.getQuestionItemFrames());
        questionEditRequestVM.setItems(questionEditItemVMS);
        return questionEditRequestVM;
    }

    @Override
    public List<Long> randomQuestion(QuestionRandom questionRandom) {
        return questionMapper.randomQuestion(questionRandom);
    }

    @Override
    public List<QuestionRandomItem> questionRandom(QuestionRandom questionRandom) {
        return questionMapper.questionRandom(questionRandom);
    }

    @Override
    public Long randomQuestionCount(QuestionRandom questionRandom) {
        return questionMapper.randomQuestionCount(questionRandom);
    }

    @Override
    public Long checkLikeQuestion(QuestionLike questionLike) {
        return questionMapper.checkLikeQuestion(questionLike);
    }

    @Override
    public QuestionJson getQuestionJsonById(String id) {
        return questionJsonMapper.selectById(id);
    }

    /**
     * 设置标答
     *
     * @param questionFrame
     * @param questionEditRequestVM
     */
    private void questionCorrectToVM(QuestionFrame questionFrame, QuestionEditRequestVM questionEditRequestVM) {
        QuestionTypeEnum questionTypeEnum = QuestionTypeEnum.fromCode(questionFrame.getQuestionType());
        switch (questionTypeEnum) {
            case SingleChoice:
            case TrueFalse:
                questionEditRequestVM.setCorrect(questionFrame.getCorrectPrefix());
                break;
            case UncertainMultipleChoice:
            case MultipleChoice:
                String[] correct = questionFrame.getCorrectPrefix().split(" ");
                questionEditRequestVM.setCorrectArray(Arrays.asList(correct));
                break;
            case ShortAnswer:
                questionEditRequestVM.setCorrect(questionFrame.getCorrect());
            case TrainOperate:
                questionEditRequestVM.setCorrect(questionFrame.getCorrect());
                questionEditRequestVM.setCommandType(questionFrame.getCommandType());
                questionEditRequestVM.setCommand(questionFrame.getCommand());
                break;
        }
    }


    /**
     * 处理题目正确答案
     *
     * @param questionEditRequestVM
     * @param questionFrame
     * @param questionItemFrameList
     */
    private void questionCorrectFromVM(QuestionEditRequestVM questionEditRequestVM, QuestionFrame questionFrame, List<QuestionItemFrame> questionItemFrameList) {
        QuestionTypeEnum questionTypeEnum = QuestionTypeEnum.fromCode(questionFrame.getQuestionType());
        switch (questionTypeEnum) {
            case SingleChoice:
                for (int i = 0; i < questionItemFrameList.size(); i++) {
                    QuestionItemFrame questionItemFrame = questionItemFrameList.get(i);
                    questionItemFrame.setKey(i);
                    if (questionEditRequestVM.getCorrect().equals(questionItemFrame.getPrefix())) {
                        questionFrame.setCorrectKey(i);
                        questionFrame.setCorrectPrefix(questionItemFrame.getPrefix());
                    }
                }
                break;
            case UncertainMultipleChoice:
            case MultipleChoice:
                List<Integer> correctArrayKey = new ArrayList<>(questionItemFrameList.size());
                List<String> correctPrefixList = new ArrayList<>(questionItemFrameList.size());
                for (int i = 0; i < questionItemFrameList.size(); i++) {
                    QuestionItemFrame questionItemFrame = questionItemFrameList.get(i);
                    questionItemFrame.setKey(i);
                    //判断是否包含为正确答案
                    if (questionEditRequestVM.getCorrectArray().contains(questionItemFrame.getPrefix())) {
                        correctArrayKey.add(i);
                        correctPrefixList.add(questionItemFrame.getPrefix());
                    }
                }
                questionFrame.setCorrectArrayKey(correctArrayKey);
                String correctPrefixStr = correctPrefixList.stream().collect(Collectors.joining(" "));
                questionFrame.setCorrectPrefix(correctPrefixStr);
                break;
            case TrueFalse:
                for (int i = 0; i < questionItemFrameList.size(); i++) {
                    QuestionItemFrame questionItemFrame = questionItemFrameList.get(i);
                    questionItemFrame.setKey(i);
                    if (questionEditRequestVM.getCorrect().equals(questionItemFrame.getPrefix())) {
                        questionFrame.setCorrectKey(i);
                        questionFrame.setCorrectPrefix(questionItemFrame.getPrefix());
                        questionFrame.setCorrectContent(questionItemFrame.getContent());
                    }
                }
                break;
            case ShortAnswer:
                questionFrame.setCorrect(questionEditRequestVM.getCorrect());
            case TrainOperate:
                questionFrame.setCorrect(questionEditRequestVM.getCorrect());
                questionFrame.setCommandType(questionEditRequestVM.getCommandType());
                questionFrame.setCommand(questionEditRequestVM.getCommand());
                break;
        }
    }


    /**
     * xss注入标签清理
     *
     * @param questionEditRequestVM
     */
    private void xssClear(QuestionEditRequestVM questionEditRequestVM) {
        questionEditRequestVM.setTitle(HtmlUtil.xssClear(questionEditRequestVM.getTitle()));
        questionEditRequestVM.getItems().forEach(item -> {
            item.setContent(HtmlUtil.xssClear(item.getContent()));
        });
        questionEditRequestVM.setAnalyze(HtmlUtil.xssClear(questionEditRequestVM.getAnalyze()));
        QuestionTypeEnum questionTypeEnum = QuestionTypeEnum.fromCode(questionEditRequestVM.getQuestionType());
        switch (questionTypeEnum) {
            case ShortAnswer:
                questionEditRequestVM.setCorrect(HtmlUtil.xssClear(questionEditRequestVM.getCorrect()));
                break;
        }
    }

}
