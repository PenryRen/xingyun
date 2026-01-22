package com.mindskip.wdd.service.impl;

import com.mindskip.wdd.domain.enums.QuestionTypeEnum;
import com.mindskip.wdd.domain.frame.QuestionAnswerFrame;
import com.mindskip.wdd.domain.frame.QuestionFrame;
import com.mindskip.wdd.domain.frame.QuestionItemFrame;
import com.mindskip.wdd.service.ExamPaperQuestionAnswerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @version 1.7.0
 * @description: 答卷题目
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
 */
@Service
public class ExamPaperQuestionAnswerServiceImpl implements ExamPaperQuestionAnswerService {

    private static final Logger logger = LoggerFactory.getLogger(ExamPaperQuestionAnswerServiceImpl.class);

    @Override
    public void questionItemMessRestore(QuestionFrame questionFrame, QuestionAnswerFrame questionAnswerFrame) {
        try {
            QuestionTypeEnum questionTypeEnum = QuestionTypeEnum.fromCode(questionFrame.getQuestionType());
            if (questionTypeEnum == QuestionTypeEnum.SingleChoice || questionTypeEnum == QuestionTypeEnum.MultipleChoice || questionTypeEnum == QuestionTypeEnum.UncertainMultipleChoice || questionTypeEnum == QuestionTypeEnum.TrueFalse) {
                List<QuestionItemFrame> questionItemFrames = questionFrame.getQuestionItemFrames();
                if (questionItemFrames.size() >= 2) {
                    Integer size = questionItemFrames.size();
                    List<QuestionItemFrame> userQuestionItemFrame = new ArrayList<>(size);
                    List<String> preFixList = questionItemFrames.stream().map(d -> d.getPrefix()).collect(Collectors.toList());
                    List<Integer> keyOrderList = questionAnswerFrame.getQuestionItemKeyOrder();
                    for (int i = 0; i < size; i++) {
                        Integer key = keyOrderList.get(i);
                        QuestionItemFrame questionItemFrame = questionItemFrames.stream().filter(qi -> qi.getKey().equals(key)).findFirst().get();
                        questionItemFrame.setPrefix(preFixList.get(i));
                        userQuestionItemFrame.add(questionItemFrame);
                    }
                    questionFrame.setQuestionItemFrames(userQuestionItemFrame);
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

}
