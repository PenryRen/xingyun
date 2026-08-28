package com.mindskip.wdd.mapping;

import com.mindskip.wdd.domain.ueit.TrainItemUserQuestion;
import com.mindskip.wdd.viewmodel.ueit.TrainItemUserQuestionVM;
import javax.annotation.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-08-28T15:49:51+0800",
    comments = "version: 1.4.2.Final, compiler: Eclipse JDT (IDE) 3.46.100.v20260624-0231, environment: Java 21.0.11 (Eclipse Adoptium)"
)
@Component
public class TrainItemUserQuestionMappingImpl implements TrainItemUserQuestionMapping {

    @Override
    public TrainItemUserQuestionVM toTrainItemUserQuestionVM(TrainItemUserQuestion trainItemUserQuestion) {
        if ( trainItemUserQuestion == null ) {
            return null;
        }

        TrainItemUserQuestionVM trainItemUserQuestionVM = new TrainItemUserQuestionVM();

        trainItemUserQuestionVM.setCompletion( trainItemUserQuestion.getCompletion() );
        trainItemUserQuestionVM.setQuestionId( trainItemUserQuestion.getQuestionId() );

        return trainItemUserQuestionVM;
    }
}
