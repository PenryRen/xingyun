package com.mindskip.wdd.controller;

import com.mindskip.wdd.base.BaseApiController;
import com.mindskip.wdd.base.RestResponse;
import com.mindskip.wdd.configuration.spring.interceptor.PreAuthorize;
import com.mindskip.wdd.domain.ForumArchive;
import com.mindskip.wdd.domain.User;
import com.mindskip.wdd.mapping.ForumArchiveMapping;
import com.mindskip.wdd.service.ForumArchiveService;
import com.mindskip.wdd.utility.ExamUtil;
import com.mindskip.wdd.viewmodel.common.ArchiveVM;
import com.mindskip.wdd.viewmodel.forum.ForumArchiveEditRequestVM;
import com.mindskip.wdd.viewmodel.forum.ForumArchiveMoveRequestVM;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @version 1.9.0
 * @description: 文章分类接口
 * Copyright (C), 2025, 麟航团队
 * @date 2025/7/1 10:45
 */
@RestController
@AllArgsConstructor
@RequestMapping(value = "/api/forum/archive")
public class ForumArchiveController extends BaseApiController {

    private final ForumArchiveService forumArchiveService;
    private final ForumArchiveMapping forumArchiveMapping;


    /**
     * 文章分类分页树形
     *
     * @return the rest response
     */
    @PostMapping("/tree")
    public RestResponse<List<ArchiveVM>> tree() {
        List<ForumArchive> forumArchiveRoot = forumArchiveService.getRootForumArchive();
        List<ArchiveVM> archiveVMList = new ArrayList<>();
        forumArchiveRecursion(forumArchiveRoot, archiveVMList);
        return RestResponse.ok(archiveVMList);
    }


    /**
     * 文章分类创建
     *
     * @param model the model
     * @return the rest response
     */
    @PostMapping("/create")
    @PreAuthorize("forum:archive:create")
    public RestResponse create(@RequestBody @Valid ForumArchiveEditRequestVM model) {
        User user = getCurrentUser();
        ForumArchive newForumArchive = forumArchiveMapping.toForumArchive(model);
        newForumArchive.setDeleted(false);
        newForumArchive.setCreateTime(new Date());
        newForumArchive.setCreateUser(user.getId());
        newForumArchive.setCreateDepartmentId(user.getDepartmentId());
        if (null == model.getParentId()) {
            newForumArchive.setLevel(String.format("/%s/", model.getName()));
        } else {
            ForumArchive parentNode = forumArchiveService.getById(model.getParentId());
            newForumArchive.setLevel(String.format("%s%s/", parentNode.getLevel(), model.getName()));
        }
        ForumArchive exist = forumArchiveService.getByLevel(newForumArchive.getLevel());
        if (null != exist) {
            return RestResponse.fail(2, "文章分类已存在");
        }
        forumArchiveService.save(newForumArchive);
        newForumArchive.setItemOrder(newForumArchive.getId() * ExamUtil.ItemOrderInit);
        forumArchiveService.updateById(newForumArchive);
        return RestResponse.ok();
    }


    /**
     * 文章分类更新
     *
     * @param model the model
     * @return the rest response
     */
    @PostMapping("/update")
    @PreAuthorize("forum:archive:update")
    public RestResponse update(@RequestBody @Valid ForumArchiveEditRequestVM model) {
        ForumArchive oldForumArchive = forumArchiveService.getById(model.getId());
        String newLevel;
        if (null == oldForumArchive.getParentId()) {
            newLevel = String.format("/%s/", model.getName());
        } else {
            ForumArchive parentNode = forumArchiveService.getById(oldForumArchive.getParentId());
            newLevel = String.format("%s%s/", parentNode.getLevel(), model.getName());
        }
        ForumArchive exist = forumArchiveService.getByLevel(newLevel);
        if (null != exist) {
            return RestResponse.fail(2, "文章分类已存在");
        }
        forumArchiveService.updateLevel(oldForumArchive.getLevel(), newLevel);
        oldForumArchive.setLevel(newLevel);
        oldForumArchive.setName(model.getName());
        forumArchiveService.updateById(oldForumArchive);
        return RestResponse.ok();
    }


    /**
     * 文章分类位置移动
     *
     * @param model 模型
     * @return {@link RestResponse}
     */
    @PostMapping("/move")
    @PreAuthorize("forum:archive:move")
    public RestResponse move(@RequestBody @Valid ForumArchiveMoveRequestVM model) {
        return forumArchiveService.move(model);
    }

    /**
     * 文章分类删除
     *
     * @param id the id
     * @return the rest response
     */
    @PostMapping("/delete/{id}")
    @PreAuthorize("forum:archive:delete")
    public RestResponse delete(@PathVariable Integer id) {
        ForumArchive forumArchive = forumArchiveService.getById(id);
        forumArchiveService.deleteByLevel(forumArchive.getLevel());
        return RestResponse.ok();
    }

    /**
     * 文章分类转换
     *
     * @param forumArchiveList
     */
    private void forumArchiveRecursion(List<ForumArchive> forumArchiveList, List<ArchiveVM> archiveVMList) {
        forumArchiveList.forEach(item -> {
            ArchiveVM archiveVM = forumArchiveMapping.toArchiveVM(item);
            archiveVMList.add(archiveVM);
            List<ForumArchive> forumArchiveChild = forumArchiveService.getForumArchiveByParentId(item.getId());
            if (0 != forumArchiveChild.size()) {
                forumArchiveRecursion(forumArchiveChild, archiveVM.getChildren());
            }
        });
    }

}
