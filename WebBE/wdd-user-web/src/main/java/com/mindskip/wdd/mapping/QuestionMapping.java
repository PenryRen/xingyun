package com.mindskip.wdd.mapping;

import com.mindskip.wdd.domain.frame.QuestionAnswerFrame;
import com.mindskip.wdd.domain.frame.QuestionFrame;
import com.mindskip.wdd.utility.DateTimeUtil;
import com.mindskip.wdd.utility.ExamUtil;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

/**
 * @version 1.7.0
 * @description: The interface Question mapping.
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
@Mapper(componentModel = "spring", imports = {DateTimeUtil.class, ExamUtil.class})
public interface QuestionMapping {

    /**
     * To question answer frame question answer frame.
     *
     * @param questionFrame the question frame
     * @return the question answer frame
     */
    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "questionScoreVM", source = "trickScore"),
            @Mapping(target = "questionScore", expression = "java(ExamUtil.scoreFromVM(questionFrame.getTrickScore()))"),
    })
    QuestionAnswerFrame toQuestionAnswerFrame(QuestionFrame questionFrame);

}
