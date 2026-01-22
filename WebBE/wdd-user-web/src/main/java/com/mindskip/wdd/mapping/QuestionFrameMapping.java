package com.mindskip.wdd.mapping;


import com.mindskip.wdd.domain.frame.QuestionFrame;
import com.mindskip.wdd.domain.frame.QuestionItemFrame;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * @version 1.7.0
 * @description: The interface Question frame mapping.
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
 */
@Mapper(componentModel = "spring")
public interface QuestionFrameMapping {

    /**
     * Copy question frame question frame.
     *
     * @param questionFrame the question frame
     * @return the question frame
     */
    QuestionFrame copyQuestionFrame(QuestionFrame questionFrame);

    /**
     * Copy question item frame question item frame.
     *
     * @param questionItemFrame the question item frame
     * @return the question item frame
     */
    QuestionItemFrame copyQuestionItemFrame(QuestionItemFrame questionItemFrame);

    /**
     * To question item frame list list.
     *
     * @param questionItemFrameList the question item frame list
     * @return the list
     */
    List<QuestionItemFrame> toQuestionItemFrameList(List<QuestionItemFrame> questionItemFrameList);

}