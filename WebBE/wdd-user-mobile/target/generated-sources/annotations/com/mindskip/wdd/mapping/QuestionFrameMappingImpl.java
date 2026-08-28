package com.mindskip.wdd.mapping;

import com.mindskip.wdd.domain.frame.QuestionFrame;
import com.mindskip.wdd.domain.frame.QuestionItemFrame;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-08-26T21:28:30+0800",
    comments = "version: 1.4.2.Final, compiler: Eclipse JDT (IDE) 3.46.100.v20260624-0231, environment: Java 21.0.11 (Eclipse Adoptium)"
)
@Component
public class QuestionFrameMappingImpl implements QuestionFrameMapping {

    @Override
    public QuestionFrame copyQuestionFrame(QuestionFrame questionFrame) {
        if ( questionFrame == null ) {
            return null;
        }

        QuestionFrame questionFrame1 = new QuestionFrame();

        questionFrame1.setAnalyze( questionFrame.getAnalyze() );
        questionFrame1.setCorrect( questionFrame.getCorrect() );
        List<Integer> list = questionFrame.getCorrectArrayKey();
        if ( list != null ) {
            questionFrame1.setCorrectArrayKey( new ArrayList<Integer>( list ) );
        }
        questionFrame1.setCorrectContent( questionFrame.getCorrectContent() );
        questionFrame1.setCorrectKey( questionFrame.getCorrectKey() );
        questionFrame1.setCorrectPrefix( questionFrame.getCorrectPrefix() );
        questionFrame1.setDifficult( questionFrame.getDifficult() );
        questionFrame1.setId( questionFrame.getId() );
        questionFrame1.setItemOrder( questionFrame.getItemOrder() );
        questionFrame1.setQuestionArchiveId( questionFrame.getQuestionArchiveId() );
        questionFrame1.setQuestionId( questionFrame.getQuestionId() );
        questionFrame1.setQuestionItemFrames( toQuestionItemFrameList( questionFrame.getQuestionItemFrames() ) );
        questionFrame1.setQuestionType( questionFrame.getQuestionType() );
        questionFrame1.setScore( questionFrame.getScore() );
        questionFrame1.setTitle( questionFrame.getTitle() );
        questionFrame1.setTrickScore( questionFrame.getTrickScore() );

        return questionFrame1;
    }

    @Override
    public QuestionItemFrame copyQuestionItemFrame(QuestionItemFrame questionItemFrame) {
        if ( questionItemFrame == null ) {
            return null;
        }

        QuestionItemFrame questionItemFrame1 = new QuestionItemFrame();

        questionItemFrame1.setContent( questionItemFrame.getContent() );
        questionItemFrame1.setItemUuid( questionItemFrame.getItemUuid() );
        questionItemFrame1.setKey( questionItemFrame.getKey() );
        questionItemFrame1.setPrefix( questionItemFrame.getPrefix() );
        questionItemFrame1.setScore( questionItemFrame.getScore() );

        return questionItemFrame1;
    }

    @Override
    public List<QuestionItemFrame> toQuestionItemFrameList(List<QuestionItemFrame> questionItemFrameList) {
        if ( questionItemFrameList == null ) {
            return null;
        }

        List<QuestionItemFrame> list = new ArrayList<QuestionItemFrame>( questionItemFrameList.size() );
        for ( QuestionItemFrame questionItemFrame : questionItemFrameList ) {
            list.add( copyQuestionItemFrame( questionItemFrame ) );
        }

        return list;
    }
}
