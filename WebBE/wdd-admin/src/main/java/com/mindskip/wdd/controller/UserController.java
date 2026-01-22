package com.mindskip.wdd.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import com.github.pagehelper.PageInfo;
import com.mindskip.wdd.base.BaseApiController;
import com.mindskip.wdd.base.RestResponse;
import com.mindskip.wdd.configuration.spring.interceptor.PreAuthorize;
import com.mindskip.wdd.configuration.utility.BeanValidator;
import com.mindskip.wdd.domain.*;
import com.mindskip.wdd.domain.enums.RoleEnum;
import com.mindskip.wdd.domain.enums.SexEnum;
import com.mindskip.wdd.domain.enums.UserStatusEnum;
import com.mindskip.wdd.listener.UserVMListener;
import com.mindskip.wdd.mapping.UserMapping;
import com.mindskip.wdd.service.*;
import com.mindskip.wdd.utility.DateTimeUtil;
import com.mindskip.wdd.utility.JsonUtil;
import com.mindskip.wdd.utility.PageInfoHelper;
import com.mindskip.wdd.viewmodel.excel.UserVM;
import com.mindskip.wdd.viewmodel.login.LoginRequestVM;
import com.mindskip.wdd.viewmodel.login.LoginResponseVM;
import com.mindskip.wdd.viewmodel.login.WddToken;
import com.mindskip.wdd.viewmodel.user.UserEditRequestVM;
import com.mindskip.wdd.viewmodel.user.UserEditResponseVM;
import com.mindskip.wdd.viewmodel.user.UserPageRequestVM;
import com.mindskip.wdd.viewmodel.user.UserPageResponseVM;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


/**
 * @version 1.7.0
 * @description: 用户接口
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
 */
@RestController
@AllArgsConstructor
@RequestMapping(value = "/api/user")
public class UserController extends BaseApiController {

    private final UserService userService;
    private final UserMapping userMapping;
    private final BeanValidator beanValidator;
    private final DepartmentService departmentService;
    private final FileUploadService fileUploadService;
    private final RoleService roleService;
    private final UserEventLogService userEventLogService;
    private final SystemService systemService;
    private final UserTokenService userTokenService;


    /**
     * 用户登录接口
     *
     * @param loginRequestVM the login request vm
     * @return the rest response
     */
    @PostMapping("/login")
    public RestResponse login(@RequestBody @Valid LoginRequestVM loginRequestVM) {
        String password = systemService.pairOneDecode(loginRequestVM.getPassword());
        User user = userService.getUserByUserName(loginRequestVM.getUserName());
        if (user == null) {
            return RestResponse.fail(2, "用户名或密码错误");
        }

        boolean result = systemService.authUser(user, loginRequestVM.getUserName(), password);
        if (!result) {
            return RestResponse.fail(2, "用户名或密码错误");
        }

        RoleEnum roleEnum = RoleEnum.fromCode(user.getSystemRole());
        if (RoleEnum.ADMIN != roleEnum) {
            return RestResponse.fail(2, "该用户角色无法登录此系统");
        }

        if (null == user.getRoleId()) {
            return RestResponse.fail(2, "用户角色未设置");
        }

        UserStatusEnum userStatusEnum = UserStatusEnum.fromCode(user.getStatus());
        if (UserStatusEnum.Disable == userStatusEnum) {
            return RestResponse.fail(2, "用户被禁用");
        }

        UserToken userToken = userTokenService.insertUserToken(user);

        String logContent = String.format("%s 登录了%s", user.getUserName(), systemService.getName());
        UserEventLog userEventLog = new UserEventLog(user.getId(), user.getUserName(), logContent, new Date(), user.getDepartmentId());
        userEventLogService.save(userEventLog);

        LoginResponseVM loginResponseVM = userMapping.toLoginVM(user);
        WddToken wddToken = new WddToken(userToken.getToken(), userToken.getEndTime());
        String token = systemService.pairOneEncode(JsonUtil.toJsonStr(wddToken));
        loginResponseVM.setToken(token);
        loginResponseVM.setEndTime(userToken.getEndTime());
        return RestResponse.ok(loginResponseVM);

    }


    /**
     * 用户登出接口
     *
     * @return the rest response
     */
    @PostMapping("/logout")
    public RestResponse logout() {
        User user = getCurrentUser();
        UserToken userToken = webContext.getCurrentUserToken();
        String logContent = String.format("%s 登出了%s", user.getUserName(), systemService.getName());
        UserEventLog userEventLog = new UserEventLog(user.getId(), user.getUserName(), logContent, new Date(), user.getDepartmentId());
        userEventLogService.save(userEventLog);
        userTokenService.removeToken(userToken);
        return RestResponse.ok();
    }


    /**
     * 员工分页
     *
     * @param model the model
     * @return the rest response
     */
    @PostMapping("/employee/page")
    @PreAuthorize("user:employee:page")
    public RestResponse<PageInfo<UserPageResponseVM>> employeePage(@RequestBody UserPageRequestVM model) {
        initPermission(model);
        PageInfo<User> pageInfo = userService.employeePage(model);
        PageInfo<UserPageResponseVM> page = PageInfoHelper.copyMap(pageInfo, d -> {
            UserPageResponseVM itemVm = userMapping.toUserPageResponseVM(d);
            itemVm.setStatusStr(UserStatusEnum.fromCode(d.getStatus()).getName());
            if (null != d.getSex()) {
                itemVm.setSexStr(SexEnum.fromCode(d.getSex()).getName());
            }
            if (null != d.getDepartmentId()) {
                Department department = departmentService.getById(d.getDepartmentId());
                itemVm.setDepartmentLevel(department.getLevel());
            }
            return itemVm;
        });
        return RestResponse.ok(page);
    }


    /**
     * 员工查询
     *
     * @param id the id
     * @return the rest response
     */
    @PostMapping("/employee/select/{id}")
    @PreAuthorize("user:employee:update")
    public RestResponse<UserEditResponseVM> employeeSelect(@PathVariable Integer id) {
        return select(id);
    }


    /**
     * 员工创建
     *
     * @param model the model
     * @return the rest response
     */
    @PostMapping("/employee/create")
    @PreAuthorize("user:employee:create")
    public RestResponse employeeCreate(@RequestBody @Valid UserEditRequestVM model) {
        return create(model);
    }


    /**
     * 员工更新
     *
     * @param model the model
     * @return the rest response
     */
    @PostMapping("/employee/update")
    @PreAuthorize("user:employee:update")
    public RestResponse employeeUpdate(@RequestBody @Valid UserEditRequestVM model) {
        return update(model);
    }


    /**
     * 员工删除
     *
     * @param id the id
     * @return the rest response
     */
    @PostMapping("/employee/delete/{id}")
    @PreAuthorize("user:employee:delete")
    public RestResponse employeeDelete(@PathVariable Integer id) {
        return delete(id);
    }


    /**
     * 用户excel导入
     *
     * @param request the request
     * @return the rest response
     * @throws IOException the io exception
     */
    @RequestMapping("/employee/import")
    @ResponseBody
    @PreAuthorize("user:employee:import")
    public RestResponse userUploadAndReadExcel(HttpServletRequest request) throws IOException {
        MultipartFile file = ((MultipartHttpServletRequest) request).getFile("file");
        List<Integer> departmentFilter = getRole().getDataFilter().getDepartmentIdList();
        List<UserVM> userVMList = new ArrayList<>();
        EasyExcel.read(file.getInputStream(), UserVM.class, new UserVMListener(userVMList, beanValidator, userService, getCurrentUser(), departmentFilter)).sheet().doRead();
        List<UserVM> fairVMList = userVMList.stream().filter(u -> !u.getSuccess()).collect(Collectors.toList());
        File excelTemp = File.createTempFile(UUID.randomUUID().toString(), ".xlsx");
        EasyExcel.write(excelTemp, UserVM.class).sheet("员工").doWrite(fairVMList);
        String filePath = fileUploadService.fileUpload(excelTemp, String.format("员工导入结果 - %s.xlsx", DateTimeUtil.dateTimeFullNumberFormat(new Date())), "export/excel", true, true);
        return RestResponse.ok(filePath);
    }


    /**
     * 用户导出
     *
     * @param model
     * @return
     * @throws IOException
     */
    @PostMapping("/employee/export")
    @ResponseBody
    @PreAuthorize("user:employee:export")
    public RestResponse export(@RequestBody UserPageRequestVM model) throws IOException {
        model.setPageIndex(1);
        model.setPageSize(Integer.MAX_VALUE);
        RestResponse<PageInfo<UserPageResponseVM>> userPage = employeePage(model);
        File excelTemp = File.createTempFile(UUID.randomUUID().toString(), ".xlsx");
        WriteCellStyle headWriteCellStyle = new WriteCellStyle();
        WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
        contentWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.LEFT);
        HorizontalCellStyleStrategy horizontalCellStyleStrategy = new HorizontalCellStyleStrategy(headWriteCellStyle, contentWriteCellStyle);
        EasyExcel.write(excelTemp, UserPageResponseVM.class).sheet("员工列表").registerWriteHandler(horizontalCellStyleStrategy).doWrite(userPage.getResponse().getList());
        String filePath = fileUploadService.fileUpload(excelTemp, String.format("员工列表导出 - %s.xlsx", DateTimeUtil.dateTimeFullNumberFormat(new Date())), "export/excel", true, true);
        return RestResponse.ok(filePath);
    }


    /**
     * 管理员分页
     *
     * @param model the model
     * @return the rest response
     */
    @PostMapping("/admin/page")
    @PreAuthorize("user:admin:page")
    public RestResponse<PageInfo<UserPageResponseVM>> adminPage(@RequestBody UserPageRequestVM model) {
        initPermission(model);
        PageInfo<User> pageInfo = userService.adminPage(model);
        PageInfo<UserPageResponseVM> page = PageInfoHelper.copyMap(pageInfo, d -> {
            UserPageResponseVM itemVm = userMapping.toUserPageResponseVM(d);
            itemVm.setStatusStr(UserStatusEnum.fromCode(d.getStatus()).getName());
            Role role = roleService.getById(d.getRoleId());
            itemVm.setRoleStr(role.getName());
            if (null != d.getSex()) {
                itemVm.setSexStr(SexEnum.fromCode(d.getSex()).getName());
            }
            if (null != d.getDepartmentId()) {
                Department department = departmentService.getById(d.getDepartmentId());
                itemVm.setDepartmentLevel(department.getLevel());
            }
            return itemVm;
        });
        return RestResponse.ok(page);
    }


    /**
     * 管理员查询
     *
     * @param id the id
     * @return the rest response
     */
    @PostMapping("/admin/select/{id}")
    @PreAuthorize("user:admin:update")
    public RestResponse<UserEditResponseVM> adminSelect(@PathVariable Integer id) {
        return select(id);
    }

    /**
     * 管理员创建
     *
     * @param model the model
     * @return the rest response
     */
    @PostMapping("/admin/create")
    @PreAuthorize("user:admin:create")
    public RestResponse adminCreate(@RequestBody @Valid UserEditRequestVM model) {
        return create(model);
    }

    /**
     * 管理员更新
     *
     * @param model the model
     * @return the rest response
     */
    @PostMapping("/admin/update")
    @PreAuthorize("user:admin:update")
    public RestResponse adminUpdate(@RequestBody @Valid UserEditRequestVM model) {
        return update(model);
    }

    /**
     * 管理员删除
     *
     * @param id the id
     * @return the rest response
     */
    @PostMapping("/admin/delete/{id}")
    @PreAuthorize("user:admin:delete")
    public RestResponse adminDelete(@PathVariable Integer id) {
        return delete(id);
    }


    /**
     * 用户查询
     *
     * @param id
     * @return
     */
    private RestResponse<UserEditResponseVM> select(Integer id) {
        User user = userService.getUserById(id);
        UserEditResponseVM userVm = userMapping.toUserEditResponseVM(user);
        return RestResponse.ok(userVm);
    }


    /**
     * 用户创建
     *
     * @param model
     * @return
     */
    private RestResponse create(UserEditRequestVM model) {
        List<Integer> filterDepartmentIdList = getRole().getDataFilter().getDepartmentIdList();
        if (filterDepartmentIdList.size() > 0) {
            if (!filterDepartmentIdList.stream().anyMatch(filter -> filter.equals(model.getDepartmentId()))) {
                return new RestResponse<>(2, "没有此部门权限，请选择下级部门");
            }
        }

        User existUser = userService.getUserByUserName(model.getUserName());
        if (null != existUser) {
            return new RestResponse<>(2, "用户已存在");
        }

        if (StringUtils.isBlank(model.getPassword())) {
            return new RestResponse<>(3, "密码不能为空");
        } else if (model.getPassword().length() < 5 || model.getPassword().length() > 24) {
            return new RestResponse<>(4, "密码长度在5到24个字符之间");
        }

        model.setCreateUser(getCurrentUser().getId());
        userService.createUser(model);
        return RestResponse.ok();
    }


    /**
     * 用户更新
     *
     * @param model
     * @return
     */
    private RestResponse update(UserEditRequestVM model) {
        List<Integer> filterDepartmentIdList = getRole().getDataFilter().getDepartmentIdList();
        if (filterDepartmentIdList.size() > 0) {
            if (!filterDepartmentIdList.stream().anyMatch(filter -> filter.equals(model.getDepartmentId()))) {
                return new RestResponse<>(2, "没有此部门权限，请选择下级部门");
            }
        }
        userService.updateUser(model);
        return RestResponse.ok();
    }


    /**
     * 用户删除
     *
     * @param id
     * @return
     */
    private RestResponse delete(Integer id) {
        User user = userService.getUserById(id);
        user.setDeleted(true);
        userService.updateById(user);
        return RestResponse.ok();
    }

}
