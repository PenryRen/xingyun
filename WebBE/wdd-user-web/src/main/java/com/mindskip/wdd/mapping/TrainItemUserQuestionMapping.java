package com.mindskip.wdd.mapping;

import com.mindskip.wdd.domain.ueit.TrainItemUserQuestion;
import com.mindskip.wdd.viewmodel.ueit.TrainItemUserQuestionVM;
import org.mapstruct.Mapper;

/**
 * 用户课件功能点对象 实体映射
 *
 * @author libl
 * @date 2024-04-18
 */
@Mapper(componentModel = "spring")
public interface TrainItemUserQuestionMapping {

    TrainItemUserQuestionVM toTrainItemUserQuestionVM(TrainItemUserQuestion trainItemUserQuestion);
}
