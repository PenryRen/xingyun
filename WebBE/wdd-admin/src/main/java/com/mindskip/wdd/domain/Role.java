package com.mindskip.wdd.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.mindskip.wdd.configuration.mybaties.RoleDataFilterFrameJsonTypeHandler;
import lombok.Getter;
import lombok.Setter;
import com.mindskip.wdd.domain.frame.RoleDataFilterFrame;

import java.util.Date;

/**
 * @version 1.7.0
 * @description: 角色
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
 */
@Getter
@Setter
@TableName(value = "t_role", autoResultMap = true)
public class Role {
    private Integer id;

    /**
     * 角色名称
     */
    private String name;

    /**
     * 创建者
     */
    private Integer createUser;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 是否删除
     */
    private Boolean deleted;

    /**
     * 创建者部门
     */
    private Integer createDepartmentId;

    /**
     * 数据权限
     */
    @TableField(typeHandler = RoleDataFilterFrameJsonTypeHandler.class)
    private RoleDataFilterFrame dataFilter;

}