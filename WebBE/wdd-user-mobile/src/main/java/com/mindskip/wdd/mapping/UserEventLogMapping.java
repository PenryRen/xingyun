package com.mindskip.wdd.mapping;

import com.mindskip.wdd.domain.UserEventLog;
import com.mindskip.wdd.utility.DateTimeUtil;
import com.mindskip.wdd.viewmodel.user.event.UserEventPageResponseVM;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

/**
 * @version 1.7.0
 * @description: The interface User event log mapping.
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
 */
@Mapper(componentModel = "spring", imports = DateTimeUtil.class)
public interface UserEventLogMapping {

    /**
     * To user event log vm user event log vm.
     *
     * @param userEventLog the user event log
     * @return the user event log vm
     */
    @Mappings({@Mapping(target = "createTime", expression = "java(DateTimeUtil.dateFormat(userEventLog.getCreateTime()))"),})
    UserEventPageResponseVM toUserEventLogVM(UserEventLog userEventLog);
}
