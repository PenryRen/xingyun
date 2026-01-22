package com.mindskip.wdd.viewmodel.announcement;

import com.mindskip.wdd.base.BasePage;
import lombok.Data;

import java.util.List;

/**
 * @version 1.7.0
 * @description: 公告已读人员列表过滤
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
 */
@Data
public class AnnouncementUserPageRequestVM extends BasePage {
    private Integer id;
    /**
     * 用户名
     */
    private String userName;
    /**
     * 部门id
     */
    private List<Integer> departmentIdList;
}
