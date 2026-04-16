package com.mindskip.wdd.controller;

import com.mindskip.wdd.base.BaseApiController;
import com.mindskip.wdd.base.RestResponse;
import com.mindskip.wdd.configuration.spring.interceptor.PreAuthorize;
import com.mindskip.wdd.domain.AnnouncementArchive;
import com.mindskip.wdd.domain.User;
import com.mindskip.wdd.mapping.AnnouncementArchiveMapping;
import com.mindskip.wdd.service.AnnouncementArchiveService;
import com.mindskip.wdd.utility.ExamUtil;
import com.mindskip.wdd.viewmodel.announcement.AnnouncementArchiveEditRequestVM;
import com.mindskip.wdd.viewmodel.announcement.AnnouncementArchiveMoveRequestVM;
import com.mindskip.wdd.viewmodel.common.ArchiveVM;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @version 1.7.0
 * @description: 通知公告分类接口
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
@RestController
@AllArgsConstructor
@RequestMapping(value = "/api/announcement/archive")
public class AnnouncementArchiveController extends BaseApiController {

    private final AnnouncementArchiveService announcementArchiveService;
    private final AnnouncementArchiveMapping announcementArchiveMapping;


    /**
     * 通知公告分类树形
     *
     * @return the rest response
     */
    @PostMapping("/tree")
    public RestResponse<List<ArchiveVM>> tree() {
        List<AnnouncementArchive> announcementArchiveRoot = announcementArchiveService.getRootAnnouncementArchive();
        List<ArchiveVM> archiveVMList = new ArrayList<>();
        announcementArchiveRecursion(announcementArchiveRoot, archiveVMList);
        return RestResponse.ok(archiveVMList);
    }


    /**
     * 通知公告分类创建
     *
     * @param model the model
     * @return the rest response
     */
    @PostMapping("/create")
    @PreAuthorize("announcement:archive:create")
    public RestResponse create(@RequestBody @Valid AnnouncementArchiveEditRequestVM model) {
        User user = getCurrentUser();
        AnnouncementArchive newAnnouncementArchive = announcementArchiveMapping.toAnnouncementArchive(model);
        newAnnouncementArchive.setDeleted(false);
        newAnnouncementArchive.setCreateTime(new Date());
        newAnnouncementArchive.setCreateUser(user.getId());
        newAnnouncementArchive.setCreateDepartmentId(user.getDepartmentId());
        if (null == model.getParentId()) {
            newAnnouncementArchive.setLevel(String.format("/%s/", model.getName()));
        } else {
            AnnouncementArchive parentNode = announcementArchiveService.getById(model.getParentId());
            newAnnouncementArchive.setLevel(String.format("%s%s/", parentNode.getLevel(), model.getName()));
        }
        AnnouncementArchive exist = announcementArchiveService.getByLevel(newAnnouncementArchive.getLevel());
        if (null != exist) {
            return RestResponse.fail(2, "公告分类已存在");
        }
        announcementArchiveService.save(newAnnouncementArchive);
        newAnnouncementArchive.setItemOrder(newAnnouncementArchive.getId() * ExamUtil.ItemOrderInit);
        announcementArchiveService.updateById(newAnnouncementArchive);
        return RestResponse.ok();
    }


    /**
     * 通知公告分类更新
     *
     * @param model the model
     * @return the rest response
     */
    @PostMapping("/update")
    @PreAuthorize("announcement:archive:update")
    public RestResponse update(@RequestBody @Valid AnnouncementArchiveEditRequestVM model) {
        AnnouncementArchive oldAnnouncementArchive = announcementArchiveService.getById(model.getId());
        String newLevel;
        if (null == oldAnnouncementArchive.getParentId()) {
            newLevel = String.format("/%s/", model.getName());
        } else {
            AnnouncementArchive parentNode = announcementArchiveService.getById(oldAnnouncementArchive.getParentId());
            newLevel = String.format("%s%s/", parentNode.getLevel(), model.getName());
        }
        AnnouncementArchive exist = announcementArchiveService.getByLevel(newLevel);
        if (null != exist) {
            return RestResponse.fail(2, "公告分类已存在");
        }
        announcementArchiveService.updateLevel(oldAnnouncementArchive.getLevel(), newLevel);
        oldAnnouncementArchive.setLevel(newLevel);
        oldAnnouncementArchive.setName(model.getName());
        announcementArchiveService.updateById(oldAnnouncementArchive);
        return RestResponse.ok();
    }


    /**
     * 通知公告分类位置移动
     *
     * @param model 模型
     * @return {@link RestResponse}
     */
    @PostMapping("/move")
    @PreAuthorize("announcement:archive:move")
    public RestResponse move(@RequestBody @Valid AnnouncementArchiveMoveRequestVM model) {
        return announcementArchiveService.move(model);
    }

    /**
     * 通知公告分类删除
     *
     * @param id the id
     * @return the rest response
     */
    @PostMapping("/delete/{id}")
    @PreAuthorize("announcement:archive:delete")
    public RestResponse delete(@PathVariable Integer id) {
        AnnouncementArchive announcementArchive = announcementArchiveService.getById(id);
        announcementArchiveService.deleteByLevel(announcementArchive.getLevel());
        return RestResponse.ok();
    }

    /**
     * 分类模型转换
     *
     * @param announcementArchiveList
     * @param archiveVMList
     */
    private void announcementArchiveRecursion(List<AnnouncementArchive> announcementArchiveList, List<ArchiveVM> archiveVMList) {
        announcementArchiveList.forEach(item -> {
            ArchiveVM archiveVM = announcementArchiveMapping.toArchiveVM(item);
            archiveVMList.add(archiveVM);
            List<AnnouncementArchive> announcementArchiveChild = announcementArchiveService.getAnnouncementArchiveByParentId(item.getId());
            if (0 != announcementArchiveChild.size()) {
                announcementArchiveRecursion(announcementArchiveChild, archiveVM.getChildren());
            }
        });
    }

}
