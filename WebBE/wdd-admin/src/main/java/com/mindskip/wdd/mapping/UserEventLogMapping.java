package com.mindskip.wdd.mapping;

import com.mindskip.wdd.domain.Feedback;
import com.mindskip.wdd.domain.UserEventLog;
import com.mindskip.wdd.utility.DateTimeUtil;
import com.mindskip.wdd.viewmodel.user.FeedbackPageResponseVM;
import com.mindskip.wdd.viewmodel.userEventLog.UserEventLogPageResponseVM;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;


/**
 * @version 1.7.0
 * @description: UserEventLogMapping
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
 */
@Mapper(componentModel = "spring", imports = DateTimeUtil.class)
public interface UserEventLogMapping {

    /**
     * To user event log response vm user event log page response vm.
     *
     * @param userEventLog the user event log
     * @return the user event log page response vm
     */
    @Mappings({@Mapping(target = "createTime", expression = "java(DateTimeUtil.dateTimeFullFormat(userEventLog.getCreateTime()))")})
    UserEventLogPageResponseVM toUserEventLogResponseVM(UserEventLog userEventLog);

    /**
     * To user event log page response vm list list.
     *
     * @param userEventLogList the user event log list
     * @return the list
     */
    List<UserEventLogPageResponseVM> toUserEventLogPageResponseVMList(List<UserEventLog> userEventLogList);


    @Mappings({@Mapping(target = "createTime", expression = "java(DateTimeUtil.dateTimeFullFormat(feedback.getCreateTime()))")})
    FeedbackPageResponseVM toFeedbackPageResponseVM(Feedback feedback);
}
