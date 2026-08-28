package com.mindskip.wdd.mapping;

import com.mindskip.wdd.domain.frame.QuestionAnswerFrame;
import com.mindskip.wdd.domain.frame.QuestionFrame;
import com.mindskip.wdd.utility.DateTimeUtil;
import com.mindskip.wdd.utility.ExamUtil;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-08-28T15:49:46+0800",
    comments = "version: 1.4.2.Final, compiler: Eclipse JDT (IDE) 3.46.100.v20260624-0231, environment: Java 21.0.11 (Eclipse Adoptium)"
)
@Component
public class QuestionMappingImpl implements QuestionMapping {

    @Override
    public QuestionAnswerFrame toQuestionAnswerFrame(QuestionFrame questionFrame) {
        if ( questionFrame == null ) {
            return null;
        }

        QuestionAnswerFrame questionAnswerFrame = new QuestionAnswerFrame();

        questionAnswerFrame.setQuestionScoreVM( questionFrame.getTrickScore() );
        questionAnswerFrame.setCorrectContent( questionFrame.getCorrectContent() );
        questionAnswerFrame.setCorrectPrefix( questionFrame.getCorrectPrefix() );
        questionAnswerFrame.setItemOrder( questionFrame.getItemOrder() );
        questionAnswerFrame.setQuestionId( questionFrame.getQuestionId() );
        questionAnswerFrame.setQuestionType( questionFrame.getQuestionType() );

        questionAnswerFrame.setQuestionScore( ExamUtil.scoreFromVM(questionFrame.getTrickScore()) );

        return questionAnswerFrame;
    }
}
