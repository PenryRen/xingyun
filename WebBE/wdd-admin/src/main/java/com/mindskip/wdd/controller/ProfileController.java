package com.mindskip.wdd.controller;

import com.mindskip.wdd.base.BaseApiController;
import com.mindskip.wdd.base.RestResponse;
import com.mindskip.wdd.domain.Department;
import com.mindskip.wdd.domain.Role;
import com.mindskip.wdd.domain.User;
import com.mindskip.wdd.domain.UserEventLog;
import com.mindskip.wdd.domain.enums.SexEnum;
import com.mindskip.wdd.mapping.UserEventLogMapping;
import com.mindskip.wdd.mapping.UserMapping;
import com.mindskip.wdd.service.*;
import com.mindskip.wdd.viewmodel.profile.ChangePasswordVM;
import com.mindskip.wdd.viewmodel.profile.ProfileInfoVM;
import com.mindskip.wdd.viewmodel.profile.ProfileVM;
import com.mindskip.wdd.viewmodel.userEventLog.UserEventLogPageResponseVM;
import com.mindskip.wdd.viewmodel.userEventLog.UserEventLogSelectRequestVM;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;


/**
 * @version 1.7.0
 * @description: 个人简介
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
@RestController
@AllArgsConstructor
@RequestMapping(value = "/api/profile")
public class ProfileController extends BaseApiController {

    private static final Logger logger = LoggerFactory.getLogger(ProfileController.class);
    private final UserMapping userMapping;
    private final UserEventLogService userEventLogService;
    private final UserEventLogMapping userEventLogMapping;
    private final UserService userService;
    private final FileUploadService fileUploadService;
    private final RoleService roleService;
    private final DepartmentService departmentService;
    private final SystemService systemService;


    /**
     * 获取当前用户信息
     *
     * @return the rest response
     */
    @PostMapping("/current")
    public RestResponse<ProfileVM> current() {
        User user = getCurrentUser();
        ProfileVM profileVM = new ProfileVM();
        ProfileInfoVM adminInfoVM = userMapping.toAdminInfoVM(user);
        if (null != user.getSex()) {
            adminInfoVM.setSexStr(SexEnum.fromCode(user.getSex()).getName());
        }

        if (null != user.getRoleId()) {
            Role permission = roleService.getById(user.getRoleId());
            adminInfoVM.setRoleStr(permission.getName());
        }

        if (null != user.getDepartmentId()) {
            Department department = departmentService.getById(user.getDepartmentId());
            adminInfoVM.setDepartmentStr(department.getLevel());
        }

        profileVM.setProfileInfoVM(adminInfoVM);
        UserEventLogSelectRequestVM requestVM = new UserEventLogSelectRequestVM();
        requestVM.setUserId(user.getId());
        List<UserEventLog> userEventLogList = userEventLogService.selectLogById(requestVM);
        List<UserEventLogPageResponseVM> userEventLogPageResponseVMList = userEventLogMapping.toUserEventLogPageResponseVMList(userEventLogList);
        profileVM.setUserEventLogList(userEventLogPageResponseVMList);

        return RestResponse.ok(profileVM);
    }


    /**
     * 个人信息修改
     *
     * @param profileInfoVM the profile info vm
     * @return the rest response
     */
    @PostMapping("/edit")
    public RestResponse edit(@RequestBody ProfileInfoVM profileInfoVM) {
        User currentUser = getCurrentUser();
        User user = new User();
        user.setId(currentUser.getId());
        user.setUserName(currentUser.getUserName());
        user.setRealName(profileInfoVM.getRealName());
        user.setPhone(profileInfoVM.getPhone());
        user.setEmail(profileInfoVM.getEmail());
        user.setAge(profileInfoVM.getAge());
        user.setSex(profileInfoVM.getSex());
        user.setModifyTime(new Date());
        userService.updateById(user);
        UserEventLog userEventLog = new UserEventLog(String.format("%s 更新了个人信息", currentUser.getUserName()), currentUser);
        userEventLogService.save(userEventLog);
        return RestResponse.ok();
    }


    /**
     * 修改密码
     *
     * @param changePasswordVM
     * @return {@link RestResponse}
     * @throws BindException
     */
    @PostMapping("/change/password")
    public RestResponse changePassword(@RequestBody @Valid ChangePasswordVM changePasswordVM) throws BindException {
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
        userEventLogService.save(userEventLog);
        return RestResponse.ok();
    }


    /**
     * 头像修改
     *
     * @param request the request
     * @return the rest response
     */
    @RequestMapping("/image")
    @ResponseBody
    public RestResponse image(HttpServletRequest request) {
        MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) request;
        MultipartFile multipartFile = multipartHttpServletRequest.getFile("file");
        String fileName = multipartFile.getOriginalFilename();
        long fileSize = multipartFile.getSize();
        String filePath = null;
        String folder = request.getParameter("folder");
        try (InputStream inputStream = multipartFile.getInputStream()) {
            filePath = fileUploadService.fileUpload(inputStream, fileSize, fileName, folder, true, true);
            User currentUser = getCurrentUser();
            User user = new User();
            user.setId(currentUser.getId());
            user.setUserName(currentUser.getUserName());
            user.setImagePath(filePath);
            userService.updateById(user);
            UserEventLog userEventLog = new UserEventLog(String.format("%s 更新了个人头像", currentUser.getUserName()), currentUser);
            userEventLogService.save(userEventLog);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            return RestResponse.fail(2, "文件上传失败");
        }
        return RestResponse.ok(filePath);
    }

}
