package com.mindskip.wdd.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mindskip.wdd.domain.Department;
import com.mindskip.wdd.domain.User;
import com.mindskip.wdd.domain.enums.RoleEnum;
import com.mindskip.wdd.domain.enums.UserStatusEnum;
import com.mindskip.wdd.domain.frame.ExamPaperUserSelect;
import com.mindskip.wdd.domain.other.KeyValue;
import com.mindskip.wdd.mapping.UserMapping;
import com.mindskip.wdd.repository.DepartmentMapper;
import com.mindskip.wdd.repository.UserMapper;
import com.mindskip.wdd.service.SystemService;
import com.mindskip.wdd.service.UserService;
import com.mindskip.wdd.viewmodel.excel.UserVM;
import com.mindskip.wdd.viewmodel.profile.ChangePasswordVM;
import com.mindskip.wdd.viewmodel.user.UserEditRequestVM;
import com.mindskip.wdd.viewmodel.user.UserNameRequestVM;
import com.mindskip.wdd.viewmodel.user.UserPageRequestVM;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;

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
    private final SystemService systemService;
    private final UserMapping userMapping;
    private final DepartmentMapper departmentMapper;


    @Override
    @CacheEvict(value = CACHE_NAME, key = "#user.userName")
    public boolean updateById(User user) {
        return SqlHelper.retBool(userMapper.updateById(user));
    }

    @Override
    public User getUserById(Integer id) {
        return userMapper.getUserById(id);
    }

    @Override
    @Cacheable(value = CACHE_NAME, key = "#username", unless = "#result == null")
    public User getUserByUserName(String username) {
        return userMapper.getUserByUserName(username);
    }

    @Override
    public PageInfo<User> employeePage(UserPageRequestVM requestVM) {
        return PageHelper.startPage(requestVM.getPageIndex(), requestVM.getPageSize(), "id desc").doSelectPageInfo(() ->
                userMapper.employeePage(requestVM)
        );
    }

    @Override
    public PageInfo<User> adminPage(UserPageRequestVM requestVM) {
        return PageHelper.startPage(requestVM.getPageIndex(), requestVM.getPageSize(), "id desc").doSelectPageInfo(() ->
                userMapper.adminPage(requestVM)
        );
    }


    @Override
    @Transactional
    @CacheEvict(value = CACHE_NAME, key = "#userEditRequestVM.userName")
    public void createUser(UserEditRequestVM userEditRequestVM) {
        RoleEnum roleEnum = RoleEnum.fromCode(userEditRequestVM.getSystemRole());
        User user = userMapping.toUser(userEditRequestVM);
        String encodePwd = systemService.pwdEncode(userEditRequestVM.getPassword());
        user.setPassword(encodePwd);
        user.setUserUuid(UUID.randomUUID().toString());
        user.setSystemRole(roleEnum.getCode());
        user.setCreateTime(new Date());
        user.setLastActiveTime(new Date());
        user.setDeleted(false);
        user.setDepartmentId(userEditRequestVM.getDepartmentId());
        userMapper.insert(user);
    }


    @Override
    @Transactional
    @CacheEvict(value = CACHE_NAME, key = "#userEditRequestVM.userName")
    public void updateUser(UserEditRequestVM userEditRequestVM) {
        User user = userMapping.toUser(userEditRequestVM);
        if (!StringUtils.isBlank(userEditRequestVM.getPassword())) {
            String encodePwd = systemService.pwdEncode(userEditRequestVM.getPassword());
            user.setPassword(encodePwd);
        }
        String userName = userMapper.selectById(userEditRequestVM.getId()).getUserName();
        user.setUserName(userName);
        user.setModifyTime(new Date());
        user.setDepartmentId(userEditRequestVM.getDepartmentId());
        userMapper.updateById(user);
    }

    @Override
    public List<User> selectByIds(List<Integer> ids) {
        return userMapper.selectByIds(ids);
    }

    @Override
    public List<KeyValue> selectByUserName(UserNameRequestVM userNameRequestVM) {
        return userMapper.selectByUserName(userNameRequestVM);
    }

    @Override
    public List<KeyValue> selectMothCount(Date startTime, Date endTime) {
        return userMapper.selectMothCount(startTime, endTime);
    }


    @Override
    public List<KeyValue> selectByUserIds(List<Integer> idList) {
        return userMapper.selectByUserIds(idList);
    }


    @Override
    @Transactional
    public void userImport(UserVM userVM, User createUser, List<Integer> departmentFilter) {
        userVM.setSuccess(false);
        User user = userMapping.toUser(userVM);

        User existUser = getUserByUserName(userVM.getUserName());
        if (null != existUser) {
            userVM.setResult("用户名已存在");
            return;
        }

        //部门
        if (StringUtils.isNotBlank(userVM.getDepartmentLevel())) {
            Department department = departmentMapper.getByLevel(userVM.getDepartmentLevel());
            if (null == department) {
                userVM.setResult("班级未找到");
                return;
            } else {
                if (departmentFilter.size() == 0 || departmentFilter.stream().anyMatch(filter -> filter.equals(department.getId()))) {
                    user.setDepartmentId(department.getId());
                } else {
                    userVM.setResult("没有此班级权限");
                    return;
                }
            }
        } else {
            userVM.setResult("请填写班级");
            return;
        }

        Date now = new Date();
        String encodePwd = systemService.pwdEncode(userVM.getPassword());
        user.setUserUuid(UUID.randomUUID().toString());
        user.setPassword(encodePwd);
        user.setSystemRole(RoleEnum.EMPLOYEE.getCode());
        user.setStatus(UserStatusEnum.Enable.getCode());
        user.setLastActiveTime(now);
        user.setCreateTime(now);
        user.setDeleted(false);
        user.setCreateUser(createUser.getId());
        userMapper.insert(user);
        userVM.setSuccess(true);
        userVM.setId(user.getId());
        userVM.setDepartmentId(user.getDepartmentId());
    }

    @Override
    public List<Integer> getIdByDepartmentIdList(List<Integer> departmentIdList) {
        return userMapper.getIdByDepartmentIdList(departmentIdList);
    }

    @Override
    public Integer getUserCountByDepartmentIdList(List<Integer> departmentIdList) {
        return userMapper.getUserCountByDepartmentIdList(departmentIdList);
    }

    @Override
    public List<ExamPaperUserSelect> getUserByExcludeDepartment(List<Integer> userIdList, List<Integer> excludeDepartmentIdList) {
        return userMapper.getUserByExcludeDepartment(userIdList, excludeDepartmentIdList);
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
