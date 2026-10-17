package com.mindskip.wdd.viewmodel.announcement;

import com.mindskip.wdd.base.BasePage;
import lombok.Data;

import java.util.List;

/**
 * @version 1.7.0
 * @description: 公告分页查询
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
@Data
public class AnnouncementPageRequestVM extends BasePage {
    /**
     * 部门
     */
    private Integer departmentId;

    private Integer announcementArchiveId;

    private List<Integer> announcementArchiveIdList;


}
