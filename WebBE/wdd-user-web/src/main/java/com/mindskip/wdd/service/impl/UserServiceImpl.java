package com.mindskip.wdd.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.mindskip.wdd.domain.User;
import com.mindskip.wdd.mapping.UserMapping;
import com.mindskip.wdd.repository.UserMapper;
import com.mindskip.wdd.service.SystemService;
import com.mindskip.wdd.service.UserService;
import com.mindskip.wdd.viewmodel.user.ChangePasswordVM;
import com.mindskip.wdd.viewmodel.user.UpdateRequestVM;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @version 1.7.0
 * @description: 用户
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
@Service
@AllArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private final static String CACHE_NAME = "ueit:user";
    private final UserMapper userMapper;
    private final UserMapping userMapping;
    private final SystemService systemService;


    @Override
    @CacheEvict(value = CACHE_NAME, key = "#user.userName")
    public boolean updateById(User user) {
        return SqlHelper.retBool(userMapper.updateById(user));
    }

    @Override
    public List<User> getUserByIdList(List<Integer> idList) {
        if (idList.size() == 0) {
            return new ArrayList<>(0);
        }
        return userMapper.getUserByIdList(idList);
    }


    @Override
    @Cacheable(value = CACHE_NAME, key = "#username", unless = "#result == null")
    public User getUserByUserName(String username) {
        return userMapper.getUserByUserName(username);
    }

    @Override
    @CacheEvict(value = CACHE_NAME, key = "#user.userName")
    @Transactional
    public void updateUser(User user, UpdateRequestVM updateRequestVM) {
        User updateUser = userMapping.toUpdateUser(updateRequestVM);
        updateUser.setId(user.getId());
        updateUser.setModifyTime(new Date());
        userMapper.updateById(updateUser);
    }

    @Override
    @CacheEvict(value = CACHE_NAME, key = "#user.userName")
    @Transactional
    public void changePassword(User user, ChangePasswordVM changePasswordVM) {
        String encodePwd = systemService.pwdEncode(changePasswordVM.getNewPassword());
        User changePassword = new User();
        changePassword.setId(user.getId());
        changePassword.setPassword(encodePwd);
        changePassword.setModifyTime(new Date());
        userMapper.updateById(changePassword);
    }

}
