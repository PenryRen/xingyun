package com.mindskip.wdd.viewmodel.profile;

import com.mindskip.wdd.viewmodel.userEventLog.UserEventLogPageResponseVM;
import lombok.Data;

import java.util.List;

/**
 * @version 1.7.0
 * @description: 个人信息
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
 */
@Data
public class ProfileVM {
    /**
     * 个人信息
     */
    private ProfileInfoVM profileInfoVM;
    /**
     * 用户动态
     */
    private List<UserEventLogPageResponseVM> userEventLogList;
}
