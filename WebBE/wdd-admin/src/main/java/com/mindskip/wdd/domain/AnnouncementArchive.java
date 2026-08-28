package com.mindskip.wdd.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * @version 1.7.0
 * @description: 通知公告分类
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
@Getter
@Setter
public class AnnouncementArchive {
    private Integer id;

    /**
     * 分类名称
     */
    private String name;

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
     * 父节点id
     */
    private Integer parentId;

    /**
     * 公告层级
     */
    private String level;

    /**
     * 创建者部门
     */
    private Integer createDepartmentId;


    /**
     * 排序
     */
    private Integer itemOrder;
}