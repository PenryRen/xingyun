package com.mindskip.wdd.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.mindskip.wdd.domain.User;
import com.mindskip.wdd.domain.frame.ExamPaperUserSelect;
import com.mindskip.wdd.domain.other.KeyValue;
import com.mindskip.wdd.viewmodel.excel.UserVM;
import com.mindskip.wdd.viewmodel.profile.ChangePasswordVM;
import com.mindskip.wdd.viewmodel.user.UserEditRequestVM;
import com.mindskip.wdd.viewmodel.user.UserNameRequestVM;
import com.mindskip.wdd.viewmodel.user.UserPageRequestVM;

import java.util.Date;
import java.util.List;

/**
 * @version 1.7.0
 * @description: 用户
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
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
     * 查询用户根据id
     *
     * @param id the id
     * @return the user by id
     */
    User getUserById(Integer id);

    /**
     * 根据用户名查询用户
     *
     * @param username the username
     * @return the user by user name
     */
    User getUserByUserName(String username);

    /**
     * 查询所有员工
     *
     * @param requestVM the request vm
     * @return the page info
     */
    PageInfo<User> employeePage(UserPageRequestVM requestVM);

    /**
     * 查询所有管理员
     *
     * @param requestVM the request vm
     * @return the page info
     */
    PageInfo<User> adminPage(UserPageRequestVM requestVM);

    /**
     * 创建用户
     *
     * @param userEditRequestVM the user edit request vm
     */
    void createUser(UserEditRequestVM userEditRequestVM);

    /**
     * 更新用户
     *
     * @param userEditRequestVM the user edit request vm
     */
    void updateUser(UserEditRequestVM userEditRequestVM);

    /**
     * 查询用户，根据所有id
     *
     * @param ids the ids
     * @return the list
     */
    List<User> selectByIds(List<Integer> ids);

    /**
     * 根据用户名查询用户
     *
     * @param userNameRequestVM the user name request vm
     * @return the list
     */
    List<KeyValue> selectByUserName(UserNameRequestVM userNameRequestVM);

    /**
     * 查询用户注册数量，根据时间
     *
     * @param startTime the start time
     * @param endTime   the end time
     * @return the list
     */
    List<KeyValue> selectMothCount(Date startTime, Date endTime);

    /**
     * 查询用户，根据用户id
     *
     * @param idList the id list
     * @return the list
     */
    List<KeyValue> selectByUserIds(List<Integer> idList);

    /**
     * excel导入用户
     *
     * @param userVM           the user vm
     * @param createUser       the create user
     * @param departmentFilter
     */
    void userImport(UserVM userVM, User createUser, List<Integer> departmentFilter);

    /**
     * 根据部门id，查询用户
     *
     * @param departmentIdList the department id list
     * @return the id by department id list
     */
    List<Integer> getIdByDepartmentIdList(List<Integer> departmentIdList);

    /**
     * 根据部门id,查询用户数量
     *
     * @param departmentIdList the department id list
     * @return the user count by department id list
     */
    Integer getUserCountByDepartmentIdList(List<Integer> departmentIdList);

    /**
     * 试卷自定义，员工查询
     *
     * @param userIdList              the user id list
     * @param excludeDepartmentIdList the exclude department id list
     * @return the user by exclude department
     */
    List<ExamPaperUserSelect> getUserByExcludeDepartment(List<Integer> userIdList, List<Integer> excludeDepartmentIdList);


    /**
     * 修改密码
     *
     * @param user             the user
     * @param changePasswordVM the change password vm
     */
    void changePassword(User user, ChangePasswordVM changePasswordVM);
}
