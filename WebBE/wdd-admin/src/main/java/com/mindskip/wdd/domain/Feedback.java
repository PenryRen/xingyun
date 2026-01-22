package com.mindskip.wdd.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @version 9.5.0
 * @description: 意见反馈
 * Copyright (C), 2024, 麒技团队
 * @date 2024/9/10 10:45
 */
@Getter
@Setter
@TableName("t_feedback")
public class Feedback {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 联系方式
     */
    private String contact;

    /**
     * 反馈内容
     */
    private String content;

    /**
     * 创建人
     */
    private Integer createUser;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 部门Id
     */
    private Integer departmentId;
}
