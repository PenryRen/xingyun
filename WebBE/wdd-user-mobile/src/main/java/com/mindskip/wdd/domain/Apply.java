package com.mindskip.wdd.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @version 1.7.0
 * @description: 报名
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
 */
@Getter
@Setter
@TableName("t_apply")
public class Apply {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 报名名称
     */
    private String name;

    /**
     * 考试结束时间
     */
    private Date limitEndTime;

    /**
     * 考试开始时间
     */
    private Date limitStartTime;

    /**
     * 创建人
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
     * 是否限制人数
     */
    private Boolean limited;

    /**
     * 限制报名人数量
     */
    private Integer count;

    /**
     * 报名状态：1.未发布  2.已发布  3.已关闭
     */
    private Integer status;

    /**
     * 报名截止时间
     */
    private Date applyEndTime;

    /**
     * 已报名人数
     */
    @Deprecated
    private Integer alreadyApplyCount;

    /**
     * 报名分类
     */
    private Integer applyArchiveId;

    /**
     * 创建者部门id
     */
    private Integer createDepartmentId;

    /**
     * 是否需要审核
     */
    private Boolean needAudit;
}
