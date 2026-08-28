package com.mindskip.wdd.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mindskip.wdd.domain.User;
import com.mindskip.wdd.domain.frame.ExamPaperUserSelect;
import com.mindskip.wdd.domain.other.KeyValue;
import com.mindskip.wdd.viewmodel.user.UserNameRequestVM;
import com.mindskip.wdd.viewmodel.user.UserPageRequestVM;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * @version 1.7.0
 * @description: 用户
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

    /**
     * 根据id获取用户
     *
     * @param id the id
     * @return the user by id
     */
    User getUserById(Integer id);

    /**
     * 根据用户名获取用户
     *
     * @param username the username
     * @return the user by user name
     */
    User getUserByUserName(String username);

    /**
     * 员工分页
     *
     * @param requestVM the request vm
     * @return the list
     */
    List<User> employeePage(UserPageRequestVM requestVM);

    /**
     * 管理员分页
     *
     * @param requestVM the request vm
     * @return the list
     */
    List<User> adminPage(UserPageRequestVM requestVM);

    /**
     * 根据id查找用户
     *
     * @param ids the ids
     * @return the list
     */
    List<User> selectByIds(@Param("ids") List<Integer> ids);

    /**
     * 根据用户名查询用户
     *
     * @param userNameRequestVM the user name request vm
     * @return the list
     */
    List<KeyValue> selectByUserName(UserNameRequestVM userNameRequestVM);

    /**
     * 用户月注册量
     *
     * @param startTime the start time
     * @param endTime   the end time
     * @return the list
     */
    List<KeyValue> selectMothCount(@Param("startTime") Date startTime, @Param("endTime") Date endTime);

    /**
     * 根据用户id查询用户
     *
     * @param idList the id list
     * @return the list
     */
    List<KeyValue> selectByUserIds(List<Integer> idList);

    /**
     * 根据部门查询用户
     *
     * @param departmentIdList the department id list
     * @return the id by department id list
     */
    List<Integer> getIdByDepartmentIdList(@Param("departmentIdList") List<Integer> departmentIdList);

    /**
     * 统计部门下的所有用户
     *
     * @param departmentIdList the department id list
     * @return the user count by department id list
     */
    Integer getUserCountByDepartmentIdList(@Param("departmentIdList") List<Integer> departmentIdList);

    /**
     * 查询用户，排除部门
     *
     * @param userIdList              the user id list
     * @param excludeDepartmentIdList the exclude department id list
     * @return the user by exclude department
     */
    List<ExamPaperUserSelect> getUserByExcludeDepartment(@Param("userIdList") List<Integer> userIdList, @Param("excludeDepartmentIdList") List<Integer> excludeDepartmentIdList);
}
