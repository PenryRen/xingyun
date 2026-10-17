package com.mindskip.wdd.mapping;

import com.mindskip.wdd.domain.User;
import com.mindskip.wdd.utility.DateTimeUtil;
import com.mindskip.wdd.viewmodel.excel.UserVM;
import com.mindskip.wdd.viewmodel.login.LoginResponseVM;
import com.mindskip.wdd.viewmodel.profile.ProfileInfoVM;
import com.mindskip.wdd.viewmodel.user.UserEditRequestVM;
import com.mindskip.wdd.viewmodel.user.UserEditResponseVM;
import com.mindskip.wdd.viewmodel.user.UserPageResponseVM;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;


/**
 * @version 1.7.0
 * @description: UserMapping
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
@Mapper(componentModel = "spring", imports = DateTimeUtil.class)
public interface UserMapping {

    /**
     * To user page response vm user page response vm.
     *
     * @param user the user
     * @return the user page response vm
     */
    @Mappings({
            @Mapping(target = "birthDay", expression = "java(DateTimeUtil.dateTimeFullFormat(user.getBirthDay()))"),
            @Mapping(target = "lastActiveTime", expression = "java(DateTimeUtil.dateTimeFullFormat(user.getLastActiveTime()))"),
            @Mapping(target = "createTime", expression = "java(DateTimeUtil.dateTimeFullFormat(user.getCreateTime()))"),
            @Mapping(target = "modifyTime", expression = "java(DateTimeUtil.dateTimeFullFormat(user.getModifyTime()))")
    })
    UserPageResponseVM toUserPageResponseVM(User user);

    /**
     * To user user.
     *
     * @param userEditRequestVM the user edit request vm
     * @return the user
     */
    @Mappings({@Mapping(target = "birthDay", expression = "java(DateTimeUtil.toDate(userEditRequestVM.getBirthDay()))")})
    User toUser(UserEditRequestVM userEditRequestVM);


    /**
     * To user edit response vm user edit response vm.
     *
     * @param user the user
     * @return the user edit response vm
     */
    @Mappings({@Mapping(target = "birthDay", expression = "java(DateTimeUtil.dateTimeFullFormat(user.getBirthDay()))")})
    UserEditResponseVM toUserEditResponseVM(User user);

    /**
     * To user user.
     *
     * @param userVM the user vm
     * @return the user
     */
    User toUser(UserVM userVM);

    /**
     * To admin info vm profile info vm.
     *
     * @param user the user
     * @return the profile info vm
     */
    @Mappings({
            @Mapping(target = "lastActiveTime", expression = "java(DateTimeUtil.dateTimeFullFormat(user.getLastActiveTime()))"),
            @Mapping(target = "createTime", expression = "java(DateTimeUtil.dateTimeFullFormat(user.getCreateTime()))"),
    })
    ProfileInfoVM toAdminInfoVM(User user);

    /**
     * To login vm login response vm.
     *
     * @param user the user
     * @return the login response vm
     */
    LoginResponseVM toLoginVM(User user);
}
