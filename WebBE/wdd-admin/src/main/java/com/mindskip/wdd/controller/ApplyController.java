package com.mindskip.wdd.controller;

import com.github.pagehelper.PageInfo;
import com.mindskip.wdd.base.BaseApiController;
import com.mindskip.wdd.base.RestResponse;
import com.mindskip.wdd.base.SystemCode;
import com.mindskip.wdd.configuration.spring.interceptor.PreAuthorize;
import com.mindskip.wdd.domain.*;
import com.mindskip.wdd.domain.enums.ApplyStatusEnum;
import com.mindskip.wdd.domain.other.KeyValue;
import com.mindskip.wdd.mapping.ApplyMapping;
import com.mindskip.wdd.service.ApplyArchiveService;
import com.mindskip.wdd.service.ApplyService;
import com.mindskip.wdd.service.DepartmentService;
import com.mindskip.wdd.service.UserService;
import com.mindskip.wdd.utility.DateTimeUtil;
import com.mindskip.wdd.utility.PageInfoHelper;
import com.mindskip.wdd.viewmodel.apply.*;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @version 1.7.0
 * @description: 报名接口
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
@RestController
@AllArgsConstructor
@RequestMapping(value = "/api/apply")
public class ApplyController extends BaseApiController {

    private final ApplyService applyService;
    private final ApplyMapping applyMapping;
    private final UserService userService;
    private final DepartmentService departmentService;
    private final ApplyArchiveService applyArchiveService;

    /**
     * 报名分页查询
     *
     * @param model the model
     * @return the rest response
     */
    @PostMapping("/page")
    @PreAuthorize("apply:page")
    public RestResponse<PageInfo<ApplyPageResponseVM>> page(@RequestBody ApplyPageRequestVM model) {
        initPermission(model);
        PageInfo<Apply> pageInfo = applyService.page(model);
        PageInfo<ApplyPageResponseVM> page = PageInfoHelper.copyMap(pageInfo, d -> {
            ApplyPageResponseVM applyPageResponseVM = applyMapping.toApplyResponseVM(d);
            List<KeyValue> departmentList = applyService.getDepartmentByApplyId(d.getId());
            String departmentNameList = departmentList.stream().map(p -> p.getNameSecond()).collect(Collectors.joining(" "));
            List<Integer> departmentIdList = departmentList.stream().map(p -> p.getValue()).collect(Collectors.toList());
            if (null != d.getApplyArchiveId()) {
                ApplyArchive applyArchive = applyArchiveService.getById(d.getApplyArchiveId());
                applyPageResponseVM.setApplyArchive(applyArchive.getName());
            }
            if (d.getLimited()) {
                applyPageResponseVM.setApplyCount(String.format("%s / %s", d.getAlreadyApplyCount(), d.getCount()));
            } else {
                Integer departmentUserCount = departmentIdList.size() > 0 ? userService.getUserCountByDepartmentIdList(departmentIdList) : 0;
                applyPageResponseVM.setApplyCount(String.format("%s / %s", d.getAlreadyApplyCount(), departmentUserCount));
            }
            applyPageResponseVM.setStatusStr(ApplyStatusEnum.fromCode(d.getStatus()).getName());
            applyPageResponseVM.setDepartmentNameList(departmentNameList);
            return applyPageResponseVM;
        });
        return RestResponse.ok(page);
    }


    /**
     * 已完成报名分页查询
     *
     * @param model the model
     * @return the rest response
     */
    @PostMapping("/complete/page")
    public RestResponse<PageInfo<ApplyPageResponseVM>> completePage(@RequestBody ApplyPageRequestVM model) {
        model.setNow(new Date());
        initPermission(model);
        PageInfo<Apply> pageInfo = applyService.completePage(model);
        PageInfo<ApplyPageResponseVM> page = PageInfoHelper.copyMap(pageInfo, d -> {
            ApplyPageResponseVM applyPageResponseVM = applyMapping.toApplyResponseVM(d);
            List<KeyValue> departmentList = applyService.getDepartmentByApplyId(d.getId());
            String departmentNameList = departmentList.stream().map(p -> String.format("[%s]", p.getNameSecond())).collect(Collectors.joining(" "));
            List<Integer> departmentIdList = departmentList.stream().map(p -> p.getValue()).collect(Collectors.toList());
            if (null != d.getApplyArchiveId()) {
                ApplyArchive applyArchive = applyArchiveService.getById(d.getApplyArchiveId());
                applyPageResponseVM.setApplyArchive(applyArchive.getName());
            }
            if (d.getLimited()) {
                applyPageResponseVM.setApplyCount(String.format("%s / %s", d.getAlreadyApplyCount(), d.getCount()));
            } else {
                Integer departmentUserCount = departmentIdList.size() > 0 ? userService.getUserCountByDepartmentIdList(departmentIdList) : 0;
                applyPageResponseVM.setApplyCount(String.format("%s / %s", d.getAlreadyApplyCount(), departmentUserCount));
            }
            applyPageResponseVM.setStatusStr(ApplyStatusEnum.fromCode(d.getStatus()).getName());
            applyPageResponseVM.setDepartmentNameList(departmentNameList);
            return applyPageResponseVM;
        });
        return RestResponse.ok(page);
    }


    /**
     * 报名查询
     *
     * @param id the id
     * @return the rest response
     */
    @PostMapping("/select/{id}")
    @PreAuthorize("apply:update")
    public RestResponse<ApplyEditRequestVM> select(@PathVariable Integer id) {
        Apply apply = applyService.getById(id);
        ApplyEditRequestVM applyEditRequestVM = applyMapping.toApplyEditRequestVM(apply);
        List<String> limitDateTime = Arrays.asList(DateTimeUtil.dateTimeFullFormat(apply.getLimitStartTime()), DateTimeUtil.dateTimeFullFormat(apply.getLimitEndTime()));
        applyEditRequestVM.setLimitDateTime(limitDateTime);
        List<Integer> departmentIdList = applyService.getDepartmentByApplyId(id).stream().map(p -> p.getValue()).collect(Collectors.toList());
        applyEditRequestVM.setDepartmentIdList(departmentIdList);
        return RestResponse.ok(applyEditRequestVM);
    }


    /**
     * 报名创建
     *
     * @param model the model
     * @return the rest response
     */
    @PostMapping("/create")
    @PreAuthorize("apply:create")
    public RestResponse create(@RequestBody @Valid ApplyEditRequestVM model) {
        model.setDepartmentIdList(selectDepartmentFilter(model.getDepartmentIdList()));
        User user = getCurrentUser();
        Apply newApply = applyMapping.toApply(model);
        newApply.setDeleted(false);
        newApply.setCreateTime(new Date());
        newApply.setCreateUser(user.getId());
        newApply.setCreateDepartmentId(user.getDepartmentId());
        newApply.setStatus(ApplyStatusEnum.WaitPublish.getCode());
        newApply.setAlreadyApplyCount(0);
        if (!model.getLimited()) {
            newApply.setCount(null);
        }
        RestResponse restResponse = applyValid(model, newApply);
        if (restResponse.getCode() != SystemCode.OK.getCode()) {
            return restResponse;
        }
        applyService.save(newApply);
        model.getDepartmentIdList().forEach(dId -> {
            ApplyDepartment applyDepartment = new ApplyDepartment();
            applyDepartment.setApplyId(newApply.getId());
            applyDepartment.setDepartmentId(dId);
            applyDepartment.setDeleted(false);
            applyDepartment.setCreateUserId(user.getId());
            applyDepartment.setCreateDepartmentId(user.getDepartmentId());
            applyService.insertApplyDepartment(applyDepartment);
        });
        return RestResponse.ok();
    }


    /**
     * 报名更新
     *
     * @param model the model
     * @return the rest response
     */
    @PostMapping("/update")
    @PreAuthorize("apply:update")
    public RestResponse update(@RequestBody @Valid ApplyEditRequestVM model) {
        model.setDepartmentIdList(selectDepartmentFilter(model.getDepartmentIdList()));
        User user = getCurrentUser();
        Apply oldApply = applyService.getById(model.getId());
        applyMapping.mapApply(model, oldApply);
        RestResponse restResponse = applyValid(model, oldApply);
        if (restResponse.getCode() != SystemCode.OK.getCode()) {
            return restResponse;
        }
        if (!model.getLimited()) {
            oldApply.setCount(null);
        }
        applyService.updateById(oldApply);
        applyService.deleteApplyDepartmentByApplyId(model.getId());
        model.getDepartmentIdList().forEach(dId -> {
            ApplyDepartment applyDepartment = new ApplyDepartment();
            applyDepartment.setApplyId(model.getId());
            applyDepartment.setDepartmentId(dId);
            applyDepartment.setDeleted(false);
            applyDepartment.setCreateUserId(user.getId());
            applyDepartment.setCreateDepartmentId(user.getDepartmentId());
            applyService.insertApplyDepartment(applyDepartment);
        });
        return RestResponse.ok();
    }


    /**
     * 报名发布
     *
     * @param id the id
     * @return the rest response
     */
    @PostMapping("/publish/{id}")
    @PreAuthorize("apply:publish")
    public RestResponse publish(@PathVariable Integer id) {
        Apply apply = applyService.getById(id);
        apply.setStatus(ApplyStatusEnum.Publish.getCode());
        applyService.updateById(apply);
        return RestResponse.ok();
    }


    /**
     * 报名关闭
     *
     * @param id the id
     * @return the rest response
     */
    @PostMapping("/close/{id}")
    @PreAuthorize("apply:close")
    public RestResponse close(@PathVariable Integer id) {
        Apply apply = applyService.getById(id);
        apply.setStatus(ApplyStatusEnum.Close.getCode());
        applyService.updateById(apply);
        return RestResponse.ok();
    }

    /**
     * 报名删除
     *
     * @param id the id
     * @return the rest response
     */
    @PostMapping("/delete/{id}")
    @PreAuthorize("apply:delete")
    public RestResponse delete(@PathVariable Integer id) {
        Apply apply = applyService.getById(id);
        apply.setDeleted(true);
        applyService.updateById(apply);
        return RestResponse.ok();
    }


    /**
     * 报名详情
     *
     * @param id the id
     * @return the rest response
     */
    @PostMapping("/info/{id}")
    @PreAuthorize("apply:show")
    public RestResponse info(@PathVariable Integer id) {
        Apply apply = applyService.getById(id);
        if (null == apply) {
            return RestResponse.fail(2, "报名未找到");
        }
        ApplyInfoVM applyPageResponseVM = applyMapping.toApplyInfoVM(apply);
        applyPageResponseVM.setStatusStr(ApplyStatusEnum.fromCode(apply.getStatus()).getName());
        List<KeyValue> departmentList = applyService.getDepartmentByApplyId(apply.getId());
        String departmentNameList = departmentList.stream().map(p -> String.format("[%s]", p.getName())).collect(Collectors.joining(" "));
        List<Integer> departmentIdList = departmentList.stream().map(p -> p.getValue()).collect(Collectors.toList());
        applyPageResponseVM.setDepartmentNameList(departmentNameList);
        Integer departmentUserCount = userService.getUserCountByDepartmentIdList(departmentIdList);
        applyPageResponseVM.setDepartmentUserCount(departmentUserCount);
        User createUser = userService.getById(apply.getCreateUser());
        applyPageResponseVM.setCreateUser(String.format("%s - %s", createUser.getRealName(), createUser.getUserName()));
        if (apply.getLimited()) {
            applyPageResponseVM.setLimitedStr("是");
            applyPageResponseVM.setApplyCount(String.format("%s / %s", apply.getAlreadyApplyCount(), apply.getCount()));
        } else {
            applyPageResponseVM.setLimitedStr("否");
            applyPageResponseVM.setApplyCount(String.format("%s / %s", apply.getAlreadyApplyCount(), departmentUserCount));
        }
        if (null != apply.getApplyArchiveId()) {
            ApplyArchive applyArchive = applyArchiveService.getById(apply.getApplyArchiveId());
            applyPageResponseVM.setApplyArchive(applyArchive.getLevel());
        }
        return RestResponse.ok(applyPageResponseVM);
    }


    /**
     * 报名详情 - 已报名用户列表
     *
     * @param applyUserPageRequestVM the apply user page request vm
     * @return the rest response
     */
    @PostMapping("/user/page")
    @PreAuthorize("apply:show")
    public RestResponse<PageInfo<ApplyUserPageResponseVM>> userPage(@RequestBody ApplyUserPageRequestVM applyUserPageRequestVM) {
        initPermission(applyUserPageRequestVM);
        PageInfo<ApplyUserPageResponseVM> userPageResponseVMPageInfo = applyService.userApplyPage(applyUserPageRequestVM);
        userPageResponseVMPageInfo.getList().forEach(up -> {
            if (null != up.getDepartmentId()) {
                Department department = departmentService.getById(up.getDepartmentId());
                up.setDepartmentLevel(department.getLevel());
            }
        });
        return RestResponse.ok(userPageResponseVMPageInfo);
    }


    /**
     * 报名用户移除
     *
     * @param id
     * @return {@link RestResponse}
     */
    @PostMapping("/user/remove/{id}")
    @PreAuthorize("apply:user:remove")
    public RestResponse userRemove(@PathVariable Integer id) {
        return applyService.applyCancel(id);
    }


    /**
     * 报名申请人员列表
     *
     * @param applyUserPageRequestVM
     * @return {@link RestResponse}<{@link PageInfo}<{@link ApplyUserPageResponseVM}>>
     */
    @PostMapping("/audit/user/page")
    @PreAuthorize("apply:show")
    public RestResponse<PageInfo<ApplyUserPageResponseVM>> auditUserPage(@RequestBody ApplyUserPageRequestVM applyUserPageRequestVM) {
        initPermission(applyUserPageRequestVM);
        PageInfo<ApplyUserPageResponseVM> userPageResponseVMPageInfo = applyService.userAuditPage(applyUserPageRequestVM);
        userPageResponseVMPageInfo.getList().forEach(up -> {
            if (null != up.getDepartmentId()) {
                Department department = departmentService.getById(up.getDepartmentId());
                up.setDepartmentLevel(department.getLevel());
            }
        });
        return RestResponse.ok(userPageResponseVMPageInfo);
    }


    /**
     * 报名确认
     *
     * @param id
     * @return {@link RestResponse}
     */
    @PostMapping("/audit/confirm/{id}")
    @PreAuthorize("apply:audit:confirm")
    public RestResponse auditConfirm(@PathVariable Long id) {
        return applyService.auditApply(id);
    }


    /**
     * 报名时间校验
     *
     * @param model
     * @param apply
     * @return
     */
    private RestResponse applyValid(ApplyEditRequestVM model, Apply apply) {
        if (model.getLimited()) {
            if (null == model.getCount() || model.getCount() == 0) {
                return RestResponse.fail(2, "人数限制输入不正确");
            }
        }
        apply.setLimitStartTime(DateTimeUtil.parse(model.getLimitDateTime().get(0)));
        apply.setLimitEndTime(DateTimeUtil.parse(model.getLimitDateTime().get(1)));
        if (new Date().before(apply.getApplyEndTime())) {
            if (apply.getApplyEndTime().before(apply.getLimitStartTime())) {
                if (!apply.getLimitStartTime().before(apply.getLimitEndTime())) {
                    return RestResponse.fail(2, "考试开始时间应小于结束时间");
                }
            } else {
                return RestResponse.fail(2, "报名截止时间应小于考试开始时间");
            }
        } else {
            return RestResponse.fail(2, "报名截止时间应大于当前时间");
        }
        return RestResponse.ok();
    }

}
