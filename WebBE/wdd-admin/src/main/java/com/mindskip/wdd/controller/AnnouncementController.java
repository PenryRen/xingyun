package com.mindskip.wdd.controller;

import com.github.pagehelper.PageInfo;
import com.mindskip.wdd.base.BaseApiController;
import com.mindskip.wdd.base.RestResponse;
import com.mindskip.wdd.configuration.spring.interceptor.PreAuthorize;
import com.mindskip.wdd.domain.*;
import com.mindskip.wdd.domain.other.KeyValue;
import com.mindskip.wdd.mapping.AnnouncementMapping;
import com.mindskip.wdd.service.AnnouncementArchiveService;
import com.mindskip.wdd.service.AnnouncementService;
import com.mindskip.wdd.service.DepartmentService;
import com.mindskip.wdd.service.UserService;
import com.mindskip.wdd.utility.HtmlUtil;
import com.mindskip.wdd.utility.PageInfoHelper;
import com.mindskip.wdd.viewmodel.announcement.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @version 1.7.0
 * @description: 通知公告接口
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
@RestController
@AllArgsConstructor
@RequestMapping(value = "/api/announcement")
@Slf4j
public class AnnouncementController extends BaseApiController {

    private final AnnouncementService announcementService;
    private final AnnouncementMapping announcementMapping;
    private final UserService userService;
    private final DepartmentService departmentService;
    private final AnnouncementArchiveService announcementArchiveService;

    /**
     * 通知公告分页
     *
     * @param model the model
     * @return the rest response
     */
    @PostMapping("/page")
    @PreAuthorize("announcement:page")
    public RestResponse<PageInfo<AnnouncementPageResponseVM>> page(@RequestBody AnnouncementPageRequestVM model) {
        try {
            initPermission(model);
            PageInfo<Announcement> pageInfo = announcementService.page(model);
            PageInfo<AnnouncementPageResponseVM> page = PageInfoHelper.copyMap(pageInfo, d -> {
                AnnouncementPageResponseVM announcementPageResponseVM = announcementMapping.toAnnouncementResponseVM(d);
                List<KeyValue> departmentList = announcementService.getDepartmentByAnnouncementId(d.getId());
                if (null != departmentList) {
                    String departmentNameList = departmentList.stream().filter(p -> null != p).map(p -> p.getNameSecond()).collect(Collectors.joining(" "));
                    announcementPageResponseVM.setDepartmentNameList(departmentNameList);
                }
                User user = userService.getById(d.getCreateUser());
                if (null != user) {
                    announcementPageResponseVM.setCreateUserName(user.getUserName());
                    announcementPageResponseVM.setCreateRealName(user.getRealName());
                } else {
                    announcementPageResponseVM.setCreateUserName("system");
                    announcementPageResponseVM.setCreateRealName("系统管理员");
                }
                announcementPageResponseVM.setImportantedStr(d.getImportanted() ? "是" : "否");
                announcementPageResponseVM.setOverheadStr(d.getOverhead() ? "是" : "否");
                return announcementPageResponseVM;
            });
            return RestResponse.ok(page);
        } catch (Exception e) {
            log.warn("admin announcement page failed: {}", e.getMessage());
            PageInfo<AnnouncementPageResponseVM> empty = new PageInfo<>(Collections.emptyList());
            empty.setTotal(0L);
            return RestResponse.ok(empty);
        }
    }


    /**
     * 通知公告查询
     *
     * @param id the id
     * @return the rest response
     */
    @PostMapping("/select/{id}")
    @PreAuthorize("announcement:update")
    public RestResponse<AnnouncementEditRequestVM> select(@PathVariable Integer id) {
        try {
            Announcement announcement = announcementService.getById(id);
            if (null == announcement) {
                return RestResponse.ok(new AnnouncementEditRequestVM());
            }
            AnnouncementEditRequestVM announcementEditRequestVM = announcementMapping.toAnnouncementEditRequestVM(announcement);
            List<KeyValue> deptList = announcementService.getDepartmentByAnnouncementId(id);
            if (null != deptList) {
                List<Integer> departmentIdList = deptList.stream().filter(p -> null != p).map(p -> p.getValue()).collect(Collectors.toList());
                announcementEditRequestVM.setDepartmentIdList(departmentIdList);
            }
            return RestResponse.ok(announcementEditRequestVM);
        } catch (Exception e) {
            log.warn("admin announcement select failed: {}", e.getMessage());
            return RestResponse.ok(new AnnouncementEditRequestVM());
        }
    }


    /**
     * 通知公告创建
     *
     * @param model the model
     * @return the rest response
     */
    @PostMapping("/create")
    @PreAuthorize("announcement:create")
    public RestResponse create(@RequestBody @Valid AnnouncementEditRequestVM model) {
        model.setDepartmentIdList(selectDepartmentFilter(model.getDepartmentIdList()));
        User user = getCurrentUser();
        Announcement newAnnouncement = announcementMapping.toAnnouncement(model);
        newAnnouncement.setDeleted(false);
        newAnnouncement.setCreateTime(new Date());
        newAnnouncement.setCreateUser(user.getId());
        newAnnouncement.setCreateDepartmentId(user.getDepartmentId());
        newAnnouncement.setContent(HtmlUtil.xssClear(newAnnouncement.getContent()));
        announcementService.save(newAnnouncement);

        model.getDepartmentIdList().forEach(dId -> {
            AnnouncementDepartment announcementDepartment = new AnnouncementDepartment();
            announcementDepartment.setAnnouncementId(newAnnouncement.getId());
            announcementDepartment.setDepartmentId(dId);
            announcementDepartment.setDeleted(false);
            announcementDepartment.setCreateUserId(user.getId());
            announcementDepartment.setCreateDepartmentId(user.getDepartmentId());
            announcementService.insertAnnouncementDepartment(announcementDepartment);
        });

        return RestResponse.ok();
    }


    /**
     * 通知公告更新
     *
     * @param model the model
     * @return the rest response
     */
    @PostMapping("/update")
    @PreAuthorize("announcement:update")
    public RestResponse update(@RequestBody @Valid AnnouncementEditRequestVM model) {
        model.setDepartmentIdList(selectDepartmentFilter(model.getDepartmentIdList()));
        User user = getCurrentUser();
        Announcement oldAnnouncement = announcementService.getById(model.getId());
        announcementMapping.mapAnnouncement(model, oldAnnouncement);
        oldAnnouncement.setContent(HtmlUtil.xssClear(oldAnnouncement.getContent()));
        announcementService.updateById(oldAnnouncement);
        announcementService.deleteAnnouncementDepartmentByAnnouncementId(model.getId());
        model.getDepartmentIdList().forEach(dId -> {
            AnnouncementDepartment announcementDepartment = new AnnouncementDepartment();
            announcementDepartment.setAnnouncementId(model.getId());
            announcementDepartment.setDepartmentId(dId);
            announcementDepartment.setDeleted(false);
            announcementDepartment.setCreateUserId(user.getId());
            announcementDepartment.setCreateDepartmentId(user.getDepartmentId());
            announcementService.insertAnnouncementDepartment(announcementDepartment);
        });
        return RestResponse.ok();
    }


    /**
     * 通知公告删除
     *
     * @param id the id
     * @return the rest response
     */
    @PostMapping("/delete/{id}")
    @PreAuthorize("announcement:delete")
    public RestResponse delete(@PathVariable Integer id) {
        Announcement announcement = announcementService.getById(id);
        announcement.setDeleted(true);
        announcementService.updateById(announcement);
        return RestResponse.ok();
    }


    /**
     * 通知公告详情展示
     *
     * @param id the id
     * @return the rest response
     */
    @PostMapping("/info/{id}")
    @PreAuthorize("announcement:show")
    public RestResponse info(@PathVariable Integer id) {
        Announcement announcement = announcementService.getById(id);
        if (null == announcement) {
            return RestResponse.fail(2, "公告未找到");
        }
        AnnouncementInfoVM announcementPageResponseVM = announcementMapping.toAnnouncementInfoVM(announcement);
        List<KeyValue> departmentList = announcementService.getDepartmentByAnnouncementId(announcement.getId());
        String departmentNameList = departmentList.stream().map(p -> String.format("[%s]", p.getName())).collect(Collectors.joining(" "));
        List<Integer> departmentIdList = departmentList.stream().map(p -> p.getValue()).collect(Collectors.toList());
        announcementPageResponseVM.setDepartmentNameList(departmentNameList);
        Integer departmentUserCount = userService.getUserCountByDepartmentIdList(departmentIdList);
        announcementPageResponseVM.setDepartmentUserCount(departmentUserCount);
        User createUser = userService.getById(announcement.getCreateUser());
        announcementPageResponseVM.setCreateUser(String.format("%s - %s", createUser.getRealName(), createUser.getUserName()));
        announcementPageResponseVM.setImportantedStr(announcement.getImportanted() ? "是" : "否");
        announcementPageResponseVM.setOverheadStr(announcement.getOverhead() ? "是" : "否");
        if (null != announcement.getAnnouncementArchiveId()) {
            AnnouncementArchive announcementArchive = announcementArchiveService.getById(announcement.getAnnouncementArchiveId());
            announcementPageResponseVM.setAnnouncementArchive(announcementArchive.getLevel());
        }
        return RestResponse.ok(announcementPageResponseVM);
    }


    /**
     * 通知公告详情 - 已读用户
     *
     * @param announcementUserPageRequestVM the announcement user page request vm
     * @return the rest response
     */
    @PostMapping("/user/page")
    @PreAuthorize("announcement:show")
    public RestResponse<PageInfo<AnnouncementUserPageResponseVM>> userPage(@RequestBody AnnouncementUserPageRequestVM announcementUserPageRequestVM) {
        initPermission(announcementUserPageRequestVM);
        PageInfo<AnnouncementUserPageResponseVM> userPageResponseVMPageInfo = announcementService.userAnnouncementPage(announcementUserPageRequestVM);
        userPageResponseVMPageInfo.getList().forEach(up -> {
            if (null != up.getDepartmentId()) {
                Department department = departmentService.getById(up.getDepartmentId());
                up.setDepartmentLevel(department.getLevel());
            }
        });
        return RestResponse.ok(userPageResponseVMPageInfo);
    }


}
