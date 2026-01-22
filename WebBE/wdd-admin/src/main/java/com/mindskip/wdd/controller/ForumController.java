package com.mindskip.wdd.controller;

import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.PageInfo;
import com.mindskip.wdd.base.BaseApiController;
import com.mindskip.wdd.base.RestResponse;
import com.mindskip.wdd.configuration.spring.interceptor.PreAuthorize;
import com.mindskip.wdd.domain.Forum;
import com.mindskip.wdd.domain.ForumArchive;
import com.mindskip.wdd.domain.ForumComment;
import com.mindskip.wdd.domain.User;
import com.mindskip.wdd.mapping.ForumMapping;
import com.mindskip.wdd.service.ForumArchiveService;
import com.mindskip.wdd.service.ForumService;
import com.mindskip.wdd.service.UserService;
import com.mindskip.wdd.utility.HtmlUtil;
import com.mindskip.wdd.utility.PageInfoHelper;
import com.mindskip.wdd.viewmodel.forum.ForumEditRequestVM;
import com.mindskip.wdd.viewmodel.forum.ForumPageRequestVM;
import com.mindskip.wdd.viewmodel.forum.ForumPageResponseVM;
import com.mindskip.wdd.viewmodel.forum.comment.ForumCommentPageRequestVM;
import com.mindskip.wdd.viewmodel.forum.comment.ForumCommentPageResponseVM;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;

/**
 * @version 1.9.0
 * @description: 交流圈接口
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
 */
@RestController
@AllArgsConstructor
@RequestMapping(value = "/api/forum")
public class ForumController extends BaseApiController {

    private final ForumService forumService;
    private final ForumMapping forumMapping;
    private final UserService userService;
    private final ForumArchiveService forumArchiveService;

    /**
     * 文章分页
     *
     * @param model the model
     * @return the rest response
     */
    @PostMapping("/page")
    @PreAuthorize("forum:page")
    public RestResponse<PageInfo<ForumPageResponseVM>> page(@RequestBody ForumPageRequestVM model) {
        initPermission(model);
        PageInfo<Forum> pageInfo = forumService.forumPage(model);
        PageInfo<ForumPageResponseVM> page = PageInfoHelper.copyMap(pageInfo, d -> {
            ForumPageResponseVM forumPageResponseVM = forumMapping.toForumResponseVM(d);
            String content = StrUtil.sub(HtmlUtil.clear(d.getContent()), 0, 300);
            forumPageResponseVM.setContent(content);
            User user = userService.getById(d.getCreateUser());
            forumPageResponseVM.setCreateUserStr(String.format("%s - %s", user.getUserName(), user.getRealName()));
            if (null != d.getForumArchiveId()) {
                ForumArchive forumArchive = forumArchiveService.getById(d.getForumArchiveId());
                forumPageResponseVM.setForumArchiveStr(forumArchive.getLevel());
            }
            return forumPageResponseVM;
        });
        return RestResponse.ok(page);
    }


    /**
     * 文章查询
     *
     * @param id the id
     * @return the rest response
     */
    @PostMapping("/select/{id}")
    @PreAuthorize("forum:update")
    public RestResponse<ForumEditRequestVM> select(@PathVariable Integer id) {
        Forum forum = forumService.selectForum(id);
        ForumEditRequestVM forumEditRequestVM = forumMapping.toForumEditRequestVM(forum);
        return RestResponse.ok(forumEditRequestVM);
    }


    /**
     * 文章创建
     *
     * @param model the model
     * @return the rest response
     */
    @PostMapping("/create")
    @PreAuthorize("forum:create")
    public RestResponse create(@RequestBody @Valid ForumEditRequestVM model) {
        User user = getCurrentUser();
        Forum newForum = forumMapping.toForum(model);
        newForum.setDeleted(false);
        newForum.setCreateTime(new Date());
        newForum.setCreateUser(user.getId());
        newForum.setCreateDepartmentId(user.getDepartmentId());
        newForum.setContent(HtmlUtil.xssClear(newForum.getContent()));
        forumService.insertForum(newForum);
        return RestResponse.ok();
    }


    /**
     * 文章更新
     *
     * @param model the model
     * @return the rest response
     */
    @PostMapping("/update")
    @PreAuthorize("forum:update")
    public RestResponse update(@RequestBody @Valid ForumEditRequestVM model) {
        Forum oldForum = forumService.selectForum(model.getId());
        forumMapping.mapForum(model, oldForum);
        oldForum.setContent(HtmlUtil.xssClear(oldForum.getContent()));
        forumService.updateForum(oldForum);
        return RestResponse.ok();
    }


    /**
     * 文章删除
     *
     * @param id the id
     * @return the rest response
     */
    @PostMapping("/delete/{id}")
    @PreAuthorize("forum:delete")
    public RestResponse delete(@PathVariable Integer id) {
        Forum forum = forumService.selectForum(id);
        forum.setDeleted(true);
        forumService.updateForum(forum);
        return RestResponse.ok();
    }


    /**
     * 文章评论
     *
     * @param model
     * @return
     */
    @PostMapping("/comment/page")
    @PreAuthorize("forum:comment:page")
    public RestResponse<PageInfo<ForumCommentPageResponseVM>> page(@RequestBody ForumCommentPageRequestVM model) {
        initPermission(model);
        PageInfo<ForumComment> pageInfo = forumService.forumCommentPage(model);
        PageInfo<ForumCommentPageResponseVM> page = PageInfoHelper.copyMap(pageInfo, d -> {
            ForumCommentPageResponseVM forumCommentPageResponseVM = forumMapping.toForumCommentPageResponseVM(d);
            User user = userService.getById(d.getCreateUser());
            forumCommentPageResponseVM.setCreateUserStr(String.format("%s - %s", user.getUserName(), user.getRealName()));
            return forumCommentPageResponseVM;
        });
        return RestResponse.ok(page);
    }


    /**
     * 文章评论删除
     *
     * @param id
     * @return
     */
    @PostMapping("/comment/delete/{id}")
    @PreAuthorize("forum:comment:delete")
    public RestResponse delete(@PathVariable Long id) {
        ForumComment forumComment = forumService.selectForumComment(id);
        forumComment.setDeleted(true);
        forumService.updateForumComment(forumComment);
        return RestResponse.ok();
    }


}
