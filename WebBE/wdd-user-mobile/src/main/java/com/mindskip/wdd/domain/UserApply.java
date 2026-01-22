package com.mindskip.wdd.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @version 1.7.0
 * @description: 用户报名
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
 */
@Getter
@Setter
@TableName("t_user_apply")
public class UserApply {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 报名id
     */
    private Integer applyId;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 是否删除
     */
    private Boolean deleted;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建部门
     */
    private Integer createDepartmentId;
}
