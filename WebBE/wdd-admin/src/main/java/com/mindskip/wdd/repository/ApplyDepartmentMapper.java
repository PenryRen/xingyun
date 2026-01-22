package com.mindskip.wdd.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mindskip.wdd.domain.ApplyDepartment;
import com.mindskip.wdd.domain.other.KeyValue;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @version 1.7.0
 * @description: 报名发布部门表
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
 */
@Mapper
public interface ApplyDepartmentMapper extends BaseMapper<ApplyDepartment> {


    /**
     * 删除发布部门，根据报名id
     *
     * @param applyId the apply id
     * @return the int
     */
    int updateDeleteByApplyId(Integer applyId);

    /**
     * 根据报名id获取发布部门
     *
     * @param applyId the apply id
     * @return the department by apply id
     */
    List<KeyValue> getDepartmentByApplyId(Integer applyId);

    /**
     * 根据报名id获取报名部门
     *
     * @param applyId the apply id
     * @return the apply department by apply id
     */
    List<ApplyDepartment> getApplyDepartmentByApplyId(Integer applyId);
}