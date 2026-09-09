package com.mindskip.wdd.controller;

import com.github.pagehelper.PageInfo;
import com.mindskip.wdd.base.BaseApiController;
import com.mindskip.wdd.base.RestResponse;
import com.mindskip.wdd.configuration.spring.interceptor.PreAuthorize;
import com.mindskip.wdd.domain.Department;
import com.mindskip.wdd.domain.Feedback;
import com.mindskip.wdd.domain.User;
import com.mindskip.wdd.domain.UserEventLog;
import com.mindskip.wdd.mapping.UserEventLogMapping;
import com.mindskip.wdd.service.DepartmentService;
import com.mindskip.wdd.service.FeedbackService;
import com.mindskip.wdd.service.UserEventLogService;
import com.mindskip.wdd.service.UserService;
import com.mindskip.wdd.utility.PageInfoHelper;
import com.mindskip.wdd.viewmodel.user.FeedbackPageRequestVM;
import com.mindskip.wdd.viewmodel.user.FeedbackPageResponseVM;
import com.mindskip.wdd.viewmodel.userEventLog.UserEventLogPageRequestVM;
import com.mindskip.wdd.viewmodel.userEventLog.UserEventLogPageResponseVM;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户日志
 */
@RestController
@AllArgsConstructor
@RequestMapping(value = "/api/user/log")
public class UserEventLogController extends BaseApiController {

    private final UserService userService;
    private final UserEventLogService userEventLogService;
    private final UserEventLogMapping userEventLogMapping;
    private final DepartmentService departmentService;
    private final FeedbackService feedbackService;

    /**
     * 用户事件日志分页
     *
     * @param model the model
     * @return the rest response
     */
    @PostMapping("/event/page")
    @PreAuthorize("user:log:event:page")
    public RestResponse<PageInfo<UserEventLogPageResponseVM>> eventPage(@RequestBody UserEventLogPageRequestVM model) {
        initPermission(model);
        PageInfo<UserEventLog> pageInfo = userEventLogService.page(model);
        PageInfo<UserEventLogPageResponseVM> page = PageInfoHelper.copyMap(pageInfo, d -> {
            UserEventLogPageResponseVM userEventLogPageResponseVM = userEventLogMapping.toUserEventLogResponseVM(d);
            if (null != d.getDepartmentId()) {
                Department department = departmentService.getById(d.getDepartmentId());
                if (null != department) {
                    userEventLogPageResponseVM.setDepartmentLevel(department.getLevel());
                }
            }
            return userEventLogPageResponseVM;
        });
        return RestResponse.ok(page);
    }


    /**
     * 用户意见反馈分页
     *
     * @param model the model
     * @return the rest response
     */
    @PostMapping("/feedback/page")
    @PreAuthorize("user:log:feedback:page")
    public RestResponse<PageInfo<FeedbackPageResponseVM>> feedbackPage(@RequestBody FeedbackPageRequestVM model) {
        initPermission(model);
        PageInfo<Feedback> pageInfo = feedbackService.page(model);
        PageInfo<FeedbackPageResponseVM> page = PageInfoHelper.copyMap(pageInfo, d -> {
            FeedbackPageResponseVM feedbackPageResponseVM = userEventLogMapping.toFeedbackPageResponseVM(d);
            User user = userService.getById(d.getCreateUser());
            if (null != user) {
                feedbackPageResponseVM.setUserName(user.getUserName());
                feedbackPageResponseVM.setRealName(user.getRealName());
            }
            if (null != d.getDepartmentId()) {
                Department department = departmentService.getById(d.getDepartmentId());
                if (null != department) {
                    feedbackPageResponseVM.setDepartmentLevel(department.getLevel());
                }
            }
            return feedbackPageResponseVM;
        });
        return RestResponse.ok(page);
    }

}
