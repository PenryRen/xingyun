package com.mindskip.wdd.controller;

import com.github.pagehelper.PageInfo;
import com.mindskip.wdd.base.BaseApiController;
import com.mindskip.wdd.base.RestResponse;
import com.mindskip.wdd.domain.Announcement;
import com.mindskip.wdd.domain.AnnouncementArchive;
import com.mindskip.wdd.domain.AnnouncementRead;
import com.mindskip.wdd.domain.User;
import com.mindskip.wdd.mapping.AnnouncementMapping;
import com.mindskip.wdd.service.AnnouncementService;
import com.mindskip.wdd.service.UserService;
import com.mindskip.wdd.utility.PageInfoHelper;
import com.mindskip.wdd.viewmodel.announcement.AnnouncementArchiveVM;
import com.mindskip.wdd.viewmodel.announcement.AnnouncementDetailRequestVM;
import com.mindskip.wdd.viewmodel.announcement.AnnouncementPageRequestVM;
import com.mindskip.wdd.viewmodel.announcement.AnnouncementPageResponseVM;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * @version 1.7.0
 * @description: 通知公告接口
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
@RestController
@AllArgsConstructor
@RequestMapping(value = "/api/announcement")
public class AnnouncementController extends BaseApiController {

    private final AnnouncementService announcementService;
    private final AnnouncementMapping announcementMapping;
    private final UserService userService;

    /**
     * 公告分页树
     *
     * @return {@link RestResponse}<{@link List}<{@link AnnouncementArchiveVM}>>
     */
    @PostMapping("/tree")
    public RestResponse<List<AnnouncementArchiveVM>> tree() {
        List<AnnouncementArchive> rootTree = announcementService.selectRootTree();
        List<AnnouncementArchiveVM> rootVM = announcementMapping.toAnnouncementArchiveVMList(rootTree);
        archiveRecursion(rootVM);
        return RestResponse.ok(rootVM);
    }


    /**
     * 通知公告分页
     *
     * @param model the model
     * @return the rest response
     */
    @PostMapping("/page")
    public RestResponse<PageInfo<AnnouncementPageResponseVM>> page(@RequestBody AnnouncementPageRequestVM model) {
        model.setDepartmentId(getCurrentUser().getDepartmentId());
        PageInfo<Announcement> pageInfo = announcementService.page(model);
        PageInfo<AnnouncementPageResponseVM> page = PageInfoHelper.copyMap(pageInfo, d -> {
            AnnouncementPageResponseVM announcementPageResponseVM = announcementMapping.toAnnouncementResponseVM(d);
            User user = userService.getById(d.getCreateUser());
            announcementPageResponseVM.setCreateUserName(user.getUserName());
            announcementPageResponseVM.setCreateRealName(user.getRealName());
            return announcementPageResponseVM;
        });
        return RestResponse.ok(page);
    }


    /**
     * 公告详情
     *
     * @param id the id
     * @return the rest response
     */
    @PostMapping("/select/{id}")
    public RestResponse select(@PathVariable Integer id) {
        Announcement announcement = announcementService.getById(id);
        if (null == announcement || announcement.getDeleted()) {
            return RestResponse.fail(2, "通知公告未找到！");
        }
        AnnouncementDetailRequestVM announcementDetailRequestVM = announcementMapping.toAnnouncementEditRequestVM(announcement);
        User user = userService.getById(announcement.getCreateUser());
        announcementDetailRequestVM.setCreateUserName(user.getUserName());
        announcementDetailRequestVM.setCreateRealName(user.getRealName());
        return RestResponse.ok(announcementDetailRequestVM);
    }


    /**
     * 公告读取
     *
     * @param id the id
     * @return the rest response
     */
    @PostMapping("/read/{id}")
    public RestResponse read(@PathVariable Integer id) {
        User user = getCurrentUser();
        AnnouncementRead exitAnnouncementRead = announcementService.getAnnouncementReadByIds(id, user.getId());
        if (null == exitAnnouncementRead) {
            AnnouncementRead announcementRead = new AnnouncementRead();
            announcementRead.setAnnouncementId(id);
            announcementRead.setCreateTime(new Date());
            announcementRead.setCreateUser(user.getId());
            announcementRead.setCreateDepartmentId(user.getDepartmentId());
            announcementService.insertAnnouncementRead(announcementRead);
        }
        return RestResponse.ok();
    }

    /**
     * 分类模型转化
     *
     * @param announcementArchiveVMList
     */
    private void archiveRecursion(List<AnnouncementArchiveVM> announcementArchiveVMList) {
        announcementArchiveVMList.forEach(item -> {
            List<AnnouncementArchive> announcementArchiveList = announcementService.getByParentId(item.getId());
            List<AnnouncementArchiveVM> categoryVM = announcementMapping.toAnnouncementArchiveVMList(announcementArchiveList);
            if (0 != categoryVM.size()) {
                item.setChild(categoryVM);
                archiveRecursion(categoryVM);
            }
        });
    }


}
