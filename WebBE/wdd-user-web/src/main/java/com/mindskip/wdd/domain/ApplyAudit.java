package com.mindskip.wdd.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;

/**
 * @version 9.5.0
 * @description: 报名审核
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/28 10:45
 */
@Getter
@Setter
@TableName("t_apply_audit")
public class ApplyAudit {

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
     * 创建人部门
     */
    private Integer createDepartmentId;
}
