package com.mindskip.wdd.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

/**
 * @version 9.5.0
 * @description: 模拟练习发布部门
 * Copyright (C), 2024, 麒技团队
 * @date 2024/09/11 10:45
 */
@Getter
@Setter
@TableName("t_practice_build_department")
public class PracticeBuildDepartment {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 练习规则id
     */
    private Long practiceBuildId;

    /**
     * 部门Id
     */
    private Integer departmentId;

    /**
     * 是否删除
     */
    private Boolean deleted;

    /**
     * 创建人
     */
    private Integer createUserId;

    /**
     * 创建人部门
     */
    private Integer createDepartmentId;
}
