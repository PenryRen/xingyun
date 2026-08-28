package com.mindskip.wdd.controller;

import com.github.pagehelper.PageInfo;
import com.mindskip.wdd.base.BaseApiController;
import com.mindskip.wdd.base.RestResponse;
import com.mindskip.wdd.configuration.utility.BeanValidator;
import com.mindskip.wdd.domain.*;
import com.mindskip.wdd.domain.enums.RoleEnum;
import com.mindskip.wdd.domain.enums.TrainStatusEnum;
import com.mindskip.wdd.domain.enums.UserStatusEnum;
import com.mindskip.wdd.mapping.ForumMapping;
import com.mindskip.wdd.mapping.UserEventLogMapping;
import com.mindskip.wdd.mapping.UserMapping;
import com.mindskip.wdd.service.*;
import com.mindskip.wdd.utility.DateTimeUtil;
import com.mindskip.wdd.utility.JsonUtil;
import com.mindskip.wdd.utility.PageInfoHelper;
import com.mindskip.wdd.viewmodel.common.DepartmentVM;
import com.mindskip.wdd.viewmodel.user.*;
import com.mindskip.wdd.viewmodel.user.event.UserEventPageRequestVM;
import com.mindskip.wdd.viewmodel.user.event.UserEventPageResponseVM;
import com.mindskip.wdd.viewmodel.user.train.UserTrainPageRequestVM;
import com.mindskip.wdd.viewmodel.user.train.UserTrainPageResponseVM;
import lombok.AllArgsConstructor;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


/**
 * @version 1.7.0
 * @description: 用户信息
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
@AllArgsConstructor
@RestController
@RequestMapping(value = "/api/user")
public class UserController extends BaseApiController {


    private final UserService userService;
    private final SystemService systemService;
    private final UserMapping userMapping;
    private final UserEventLogService userEventLogService;
    private final UserEventLogMapping userEventLogMapping;
    private final BeanValidator beanValidator;
    private final MessageQueueService messageQueueService;
    private final DepartmentService departmentService;
    private final FeedbackService feedbackService;
    private final UserTokenService userTokenService;
    private final ForumService forumService;
    private final ForumMapping forumMapping;
    private final TrainService trainService;


    /**
     * 登录
     *
     * @param encryptV the encrypt v
     * @return the rest response
     * @throws BindException the bind exception
     */
    @PostMapping("/login")
    public RestResponse login(@RequestBody String encryptV) throws BindException {
        LoginRequestVM model = systemService.aesDecrypt(encryptV, LoginRequestVM.class);
        beanValidator.validate(model);

        User user = userService.getUserByUserName(model.getUserName());
        if (user == null) {
            return new RestResponse<>(2, "用户名或密码错误");
        }

        boolean result = systemService.authUser(user, model.getUserName(), model.getPassword());
        if (!result) {
            return new RestResponse<>(3, "用户名或密码错误");
        }

        RoleEnum roleEnum = RoleEnum.fromCode(user.getSystemRole());
        if (RoleEnum.EMPLOYEE != roleEnum) {
            return new RestResponse<>(4, "没有权限登录系统");
        }

        UserStatusEnum userStatusEnum = UserStatusEnum.fromCode(user.getStatus());
        if (UserStatusEnum.Disable == userStatusEnum) {
            return new RestResponse<>(5, "用户被禁用");
        }

        UserToken userToken = userTokenService.insertUserToken(user);

        String logContent = String.format("%s 登录了%s", user.getUserName(), systemService.getName());
        UserEventLog userEventLog = new UserEventLog(user.getId(), user.getUserName(), logContent, new Date(), user.getDepartmentId());
        userEventLogService.save(userEventLog);

        LoginResponseVM loginResponseVM = userMapping.toLoginVM(user);
        WddToken wddToken = new WddToken(userToken.getToken(), userToken.getEndTime());
        String token = systemService.pairOneEncode(JsonUtil.toJsonStr(wddToken));
        loginResponseVM.setToken(token);
        return RestResponse.ok(loginResponseVM);
    }


    /**
     * 登出
     *
     * @return the rest response
     */
    @PostMapping("/logout")
    public RestResponse logout() {
        User user = getCurrentUser();
        UserToken userToken = apiContext.getCurrentUserToken();
        String logContent = String.format("%s 登出了%s", user.getUserName(), systemService.getName());
        UserEventLog userEventLog = new UserEventLog(user.getId(), user.getUserName(), logContent, new Date(), user.getDepartmentId());
        userEventLogService.save(userEventLog);
        userTokenService.removeToken(userToken);
        return RestResponse.ok();
    }


    /**
     * 注册
     *
     * @param encryptV the encrypt v
     * @return the rest response
     * @throws BindException the bind exception
     */
    @PostMapping("/register")
    public RestResponse register(@RequestBody String encryptV) throws BindException {
        RegisterRequestVM model = systemService.aesDecrypt(encryptV, RegisterRequestVM.class);
        beanValidator.validate(model);

        if (!model.getPassword().equals(model.getConfirmPassword())) {
            return new RestResponse<>(2, "两次密码不一致");
        }

        User existUser = userService.getUserByUserName(model.getUserName());
        if (null != existUser) {
            return new RestResponse<>(3, "用户已存在");
        }

        User user = userMapping.toUser(model);
        String encodePwd = systemService.pwdEncode(model.getPassword());
        user.setUserUuid(UUID.randomUUID().toString());
        user.setPassword(encodePwd);
        user.setDepartmentId(model.getDepartmentId());
        user.setSystemRole(RoleEnum.EMPLOYEE.getCode());
        user.setStatus(UserStatusEnum.Enable.getCode());
        user.setLastActiveTime(new Date());
        user.setCreateTime(new Date());
        user.setDeleted(false);
        userService.save(user);


        UserEventLog userEventLog = new UserEventLog(user, String.format("欢迎 %s 注册来到 %s", user.getUserName(), systemService.getName()));
        messageQueueService.userEventSend(userEventLog);
        return RestResponse.ok("注册成功");
    }


    /**
     * 部门结构
     *
     * @return the rest response
     */
    @PostMapping("/department/tree")
    public RestResponse<List<DepartmentVM>> tree() {
        List<DepartmentVM> departmentTree = departmentService.getDepartmentByCache();
        return RestResponse.ok(departmentTree);
    }

    /**
     * 当前用户信息
     *
     * @return the rest response
     */
    @PostMapping("/current")
    public RestResponse<CurrentUserInfoVM> current() {
        User user = getCurrentUser();
        CurrentUserInfoVM currentUserInfoVM = userMapping.toCurrentUserInfo(user);
        if (null != user.getDepartmentId()) {
            Department department = departmentService.getById(user.getDepartmentId());
            currentUserInfoVM.setDepartmentStr(department.getLevel());
        }
        return RestResponse.ok(currentUserInfoVM);
    }


    /**
     * 修改个人信息
     *
     * @param updateRequestVM the update request vm
     * @return the rest response
     */
    @PostMapping("/update")
    public RestResponse update(@RequestBody @Valid UpdateRequestVM updateRequestVM) {
        User user = getCurrentUser();
        userService.updateUser(user, updateRequestVM);
        UserEventLog userEventLog = new UserEventLog(user, String.format("%s 更新了个人信息", user.getUserName()));
        messageQueueService.userEventSend(userEventLog);
        return RestResponse.ok();
    }


    /**
     * 修改密码
     *
     * @param encryptV
     * @return the rest response
     * @throws BindException the bind exception
     */
    @PostMapping("/changePassword")
    public RestResponse changePassword(@RequestBody String encryptV) throws BindException {
        ChangePasswordVM changePasswordVM = systemService.aesDecrypt(encryptV, ChangePasswordVM.class);
        beanValidator.validate(changePasswordVM);
        if (!changePasswordVM.getNewPassword().equals(changePasswordVM.getConfirmPassword())) {
            return new RestResponse<>(2, "两次密码不一致");
        }
        User user = getCurrentUser();
        Boolean authPassword = systemService.authUser(user, user.getUserName(), changePasswordVM.getOldPassword());
        if (!authPassword) {
            return new RestResponse<>(3, "原始密码不正确");
        }
        userService.changePassword(user, changePasswordVM);
        UserEventLog userEventLog = new UserEventLog(user, String.format("%s 修改了密码", user.getUserName()));
        messageQueueService.userEventSend(userEventLog);
        return RestResponse.ok();
    }


    /**
     * 用户动态
     *
     * @return the rest response
     */
    @PostMapping("/event")
    public RestResponse<PageInfo<UserEventPageResponseVM>> event(@RequestBody UserEventPageRequestVM model) {
        User user = getCurrentUser();
        model.setUserId(user.getId());
        PageInfo<UserEventLog> pageInfo = userEventLogService.page(model);
        PageInfo<UserEventPageResponseVM> page = PageInfoHelper.copyMap(pageInfo, item -> userEventLogMapping.toUserEventLogVM(item));
        return RestResponse.ok(page);
    }


    @PostMapping("/comment")
    public RestResponse<PageInfo<CommentPageResponseVM>> commentPage(@RequestBody @Valid CommentPageRequestVM model) {
        model.setUserId(getCurrentUser().getId());
        PageInfo<ForumComment> pageInfo = forumService.userCommentPage(model);

        List<Integer> forumIdList = pageInfo.getList().stream()
                .map(c -> c.getForumId()).distinct().collect(Collectors.toList());
        List<Forum> forumList = forumIdList.size() > 0
                ? forumService.getForumByIdList(forumIdList)
                : new ArrayList<>(0);
        List<Long> replyCommentIdList = pageInfo.getList().stream()
                .filter(c -> null != c.getParentId())
                .map(c -> c.getParentId()).collect(Collectors.toList());
        List<ForumComment> replyCommentList = replyCommentIdList.size() > 0
                ? forumService.getCommentByIdList(replyCommentIdList)
                : new ArrayList<>(0);
        List<Integer> replyUserIdList = pageInfo.getList().stream().map(c -> c.getReplyUser()).collect(Collectors.toList());
        List<User> replyUserList = replyUserIdList.size() > 0
                ? userService.getUserByIdList(replyUserIdList)
                : new ArrayList<>(0);


        PageInfo<CommentPageResponseVM> page = PageInfoHelper.copyMap(pageInfo, c -> {
            CommentPageResponseVM commentPageResponseVM = forumMapping.toCommentPageResponseVM(c);
            forumList.stream().filter(r -> r.getId().equals(c.getForumId()))
                    .findFirst().ifPresent(f -> {
                commentPageResponseVM.setTitle(f.getTitle());
            });

            if (null != c.getParentId()) {
                ForumComment forumComment = replyCommentList.stream().filter(r -> r.getId().equals(c.getParentId()))
                        .findFirst().orElse(null);
                String forumCommentContent = null == forumComment ? "该评论已删除" : forumComment.getContent();
                replyUserList.stream().filter(u -> u.getId().equals(c.getReplyUser()))
                        .findFirst().ifPresent(u -> {
                    commentPageResponseVM.setReplyContent(String.format("@%s：%s", u.getRealName(), forumCommentContent));
                });
            }
            return commentPageResponseVM;
        });
        return RestResponse.ok(page);
    }


    /**
     * 用户反馈
     *
     * @param feedbackRequestVM the feedback request vm
     * @return the rest response
     */
    @PostMapping("/feedback")
    public RestResponse feedback(@RequestBody @Valid FeedbackRequestVM feedbackRequestVM) {
        User user = getCurrentUser();
        Feedback feedback = userMapping.toFeedback(feedbackRequestVM);
        feedback.setCreateUser(user.getId());
        feedback.setCreateTime(new Date());
        feedback.setDepartmentId(user.getDepartmentId());
        feedbackService.save(feedback);
        return RestResponse.ok();
    }



    /**
     * 培训记录
     *
     * @param model
     * @return {@link RestResponse}<{@link PageInfo}<{@link UserTrainPageResponseVM}>>
     */
    @PostMapping("/train")
    public RestResponse<PageInfo<UserTrainPageResponseVM>> train(@RequestBody UserTrainPageRequestVM model) {
        User user = getCurrentUser();
        model.setUserId(user.getId());
        PageInfo<UserTrainPageResponseVM> pageInfo = trainService.userTrainPage(model);
        pageInfo.getList().forEach(item -> {
            item.setCreateTimeStr(DateTimeUtil.dateTimeFullFormat(item.getCreateTime()));
            item.setStatusStr(TrainStatusEnum.fromCode(item.getStatus()).getName());
        });
        return RestResponse.ok(pageInfo);
    }

}
