package com.mindskip.wdd.mapping;

import com.mindskip.wdd.domain.Feedback;
import com.mindskip.wdd.domain.User;
import com.mindskip.wdd.utility.DateTimeUtil;
import com.mindskip.wdd.viewmodel.user.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;


/**
 * @version 1.7.0
 * @description: The interface User mapping.
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
 */
@Mapper(componentModel = "spring", imports = DateTimeUtil.class)
public interface UserMapping {

    /**
     * To user user.
     *
     * @param registerRequestVM the register request vm
     * @return the user
     */
    User toUser(RegisterRequestVM registerRequestVM);

    /**
     * To current user info current user info vm.
     *
     * @param user the user
     * @return the current user info vm
     */
    @Mappings({
            @Mapping(target = "birthDay", expression = "java(DateTimeUtil.dateFormat(user.getBirthDay()))"),
            @Mapping(target = "createTime", expression = "java(DateTimeUtil.dateTimeFullFormat(user.getCreateTime()))"),
    })
    CurrentUserInfoVM toCurrentUserInfo(User user);


    /**
     * To update user user.
     *
     * @param updateRequestVM the update request vm
     * @return the user
     */
    @Mappings({@Mapping(target = "birthDay", expression = "java(DateTimeUtil.toDate(updateRequestVM.getBirthDay()))")})
    User toUpdateUser(UpdateRequestVM updateRequestVM);

    /**
     * To login vm login response vm.
     *
     * @param user the user
     * @return the login response vm
     */
    LoginResponseVM toLoginVM(User user);

    /**
     * To feed back feed back.
     *
     * @param feedbackRequestVM the feed back request vm
     * @return the feed back
     */
    Feedback toFeedback(FeedbackRequestVM feedbackRequestVM);
}
