package com.mindskip.wdd.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mindskip.wdd.domain.User;
import com.mindskip.wdd.viewmodel.user.ChangePasswordVM;
import com.mindskip.wdd.viewmodel.user.UpdateRequestVM;

import java.util.List;

/**
 * @version 1.7.0
 * @description: 用户
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
public interface UserService extends IService<User> {

    /**
     * 用户更新
     *
     * @param user
     * @return boolean
     */
    boolean updateById(User user);

    /**
     * 批量获取用户
     *
     * @param idList the id list
     * @return the user by id list
     */
    List<User> getUserByIdList(List<Integer> idList);


    /**
     * 根据用户名获取用户
     *
     * @param username the username
     * @return the user by user name
     */
    User getUserByUserName(String username);

    /**
     * 更新用户信息
     *
     * @param user            the user
     * @param updateRequestVM the update request vm
     */
    void updateUser(User user, UpdateRequestVM updateRequestVM);

    /**
     * 修改密码
     *
     * @param user             the user
     * @param changePasswordVM the change password vm
     */
    void changePassword(User user, ChangePasswordVM changePasswordVM);
}
