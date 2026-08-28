package com.mindskip.wdd.controller;

import cn.hutool.core.util.StrUtil;
import com.github.pagehelper.PageInfo;
import com.mindskip.wdd.base.BaseApiController;
import com.mindskip.wdd.base.RestResponse;
import com.mindskip.wdd.domain.Forum;
import com.mindskip.wdd.domain.ForumArchive;
import com.mindskip.wdd.domain.ForumComment;
import com.mindskip.wdd.domain.User;
import com.mindskip.wdd.mapping.ForumMapping;
import com.mindskip.wdd.service.ForumService;
import com.mindskip.wdd.service.UserService;
import com.mindskip.wdd.utility.HtmlUtil;
import com.mindskip.wdd.utility.PageInfoHelper;
import com.mindskip.wdd.viewmodel.forum.*;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @version 1.9.0
 * @description: 交流圈
 * Copyright (C), 2025, 麟航团队
 * @date 2025/7/1 10:45
 */
@RestController
@AllArgsConstructor
@RequestMapping(value = "/api/forum")
public class ForumController extends BaseApiController {

    private final ForumService forumService;
    private final ForumMapping forumMapping;
    private final UserService userService;


    /**
     * 文章查询
     *
     * @param id
     * @return
     */
    @PostMapping("/select/{id}")
    public RestResponse<ForumPageResponseVM> select(@PathVariable Integer id) {
        Forum forum = forumService.getById(id);
        ForumPageResponseVM forumPageResponseVM = forumMapping.toForumResponseVM(forum);
        User user = userService.getById(forum.getCreateUser());
        forumPageResponseVM.setUserName(user.getUserName());
        forumPageResponseVM.setRealName(user.getRealName());
        forumPageResponseVM.setImagePath(user.getImagePath());
        if (null != forum.getForumArchiveId()) {
            ForumArchive forumArchive = forumService.getForumArchiveById(forum.getForumArchiveId());
            forumPageResponseVM.setForumArchive(forumArchive.getLevel());
        }
        return RestResponse.ok(forumPageResponseVM);
    }


    /**
     * 新增文章
     *
     * @param forumRequestVM
     * @return
     */
    @PostMapping("/add")
    public RestResponse add(@RequestBody @Valid ForumRequestVM forumRequestVM) {
        User user = getCurrentUser();
        Forum forum = forumMapping.toForum(forumRequestVM);
        forum.setCreateUser(user.getId());
        forum.setCreateTime(new Date());
        forum.setDeleted(false);
        forum.setContent(HtmlUtil.xssClear(forum.getContent()));
        forum.setCreateDepartmentId(user.getDepartmentId());
        forumService.insertForum(forum);
        return RestResponse.ok();
    }


    /**
     * 文章分类树形
     *
     * @return the rest response
     */
    @PostMapping("/tree")
    public RestResponse<List<ForumArchiveVM>> tree() {
        List<ForumArchive> rootTree = forumService.selectRootTree();
        List<ForumArchiveVM> rootVM = forumMapping.toForumArchiveVMList(rootTree);
        archiveRecursion(rootVM);
        return RestResponse.ok(rootVM);
    }


    /**
     * 文章分页
     *
     * @param model the model
     * @return the rest response
     */
    @PostMapping("/page")
    public RestResponse<PageInfo<ForumPageResponseVM>> page(@RequestBody ForumPageRequestVM model) {
        PageInfo<Forum> pageInfo = forumService.page(model);
        //批量搜索分类
        List<Integer> forumArchiveIdList = pageInfo.getList().stream().map(d -> d.getForumArchiveId()).collect(Collectors.toList());
        List<ForumArchive> forumArchiveList = forumArchiveIdList.size() > 0 ? forumService.getForumArchiveList(forumArchiveIdList) : new ArrayList<>(0);

        //批量搜索用户
        List<Integer> userIdList = pageInfo.getList().stream().map(d -> d.getCreateUser()).collect(Collectors.toList());
        List<User> userList = userService.getUserByIdList(userIdList);

        PageInfo<ForumPageResponseVM> page = PageInfoHelper.copyMap(pageInfo, d -> {
            ForumPageResponseVM forumPageResponseVM = forumMapping.toForumResponseVM(d);

            userList.stream()
                    .filter(u -> u.getId().equals(d.getCreateUser()))
                    .findFirst()
                    .ifPresent(u -> {
                        forumPageResponseVM.setUserName(u.getUserName());
                        forumPageResponseVM.setRealName(u.getRealName());
                        forumPageResponseVM.setImagePath(u.getImagePath());
                    });

            String content = StrUtil.sub(HtmlUtil.clear(d.getContent()), 0, 300);
            forumPageResponseVM.setContent(content);
            if (null != d.getForumArchiveId()) {
                forumArchiveList.stream()
                        .filter(c -> c.getId().equals(d.getForumArchiveId()))
                        .findFirst()
                        .ifPresent(c -> {
                            forumPageResponseVM.setForumArchive(c.getLevel());
                        });
            }
            return forumPageResponseVM;
        });
        return RestResponse.ok(page);
    }


    /**
     * 新增文章评论
     *
     * @param forumCommentAdd
     * @return
     */
    @PostMapping("/comment/add")
    public RestResponse<ForumCommentPageResponseVM> commentAdd(@RequestBody @Valid ForumCommentAdd forumCommentAdd) {
        User user = getCurrentUser();
        ForumComment forumComment = forumMapping.toForumComment(forumCommentAdd);
        forumComment.setCreateUser(user.getId());
        forumComment.setCreateTime(new Date());
        forumComment.setCreateDepartmentId(user.getDepartmentId());
        forumComment.setDeleted(false);
        forumComment.setContent(HtmlUtil.xssClear(forumComment.getContent()));
        forumService.insertForumComment(forumComment);
        if (null == forumCommentAdd.getParentId()) {
            forumComment.setIdLevel(String.format("/%s/", forumComment.getId()));
            forumService.updateForumComment(forumComment);
        } else {
            ForumComment parentForumComment = forumService.getForumCommentById(forumCommentAdd.getParentId());
            forumComment.setIdLevel(String.format("%s%s/", parentForumComment.getIdLevel(), forumComment.getId()));
            forumComment.setReplyUser(parentForumComment.getCreateUser());
            forumComment.setReplyDepartmentId(parentForumComment.getCreateDepartmentId());
            forumService.updateForumComment(forumComment);
        }

        ForumCommentPageResponseVM forumPageResponseVM = forumMapping.toForumCommentPageResponseVM(forumComment);
        forumPageResponseVM.setUserName(user.getUserName());
        forumPageResponseVM.setRealName(user.getRealName());
        forumPageResponseVM.setImagePath(user.getImagePath());
        return RestResponse.ok(forumPageResponseVM);
    }


    /**
     * 文章评论分页
     *
     * @param model
     * @return
     */
    @PostMapping("/comment/page")
    public RestResponse<PageInfo<ForumCommentPageResponseVM>> commentPage(@RequestBody @Valid ForumCommentPageRequestVM model) {
        PageInfo<ForumComment> pageInfo = forumService.commentPage(model);
        List<String> idLevelList = pageInfo.getList().stream().map(d -> d.getIdLevel()).collect(Collectors.toList());
        List<ForumComment> childForumCommentList = idLevelList.size() > 0 ? forumService.getForumCommentByLevel(model.getForumId(), idLevelList) : new ArrayList<>(0);

        List<Integer> userIdList = pageInfo.getList().stream().map(d -> d.getCreateUser()).collect(Collectors.toList());
        List<Integer> allChildUserIdList = childForumCommentList.stream().map(d -> d.getCreateUser()).collect(Collectors.toList());
        userIdList.addAll(allChildUserIdList);
        List<User> userList = userService.getUserByIdList(userIdList);

        PageInfo<ForumCommentPageResponseVM> page = PageInfoHelper.copyMap(pageInfo, d -> commentRecursion(d, userList, childForumCommentList));
        return RestResponse.ok(page);
    }


    /**
     * 子评论处理
     *
     * @param forumComment
     * @param userList
     * @param allChildForumCommentList
     * @return
     */
    private ForumCommentPageResponseVM commentRecursion(ForumComment forumComment, List<User> userList, List<ForumComment> allChildForumCommentList) {
        ForumCommentPageResponseVM forumPageResponseVM = forumMapping.toForumCommentPageResponseVM(forumComment);
        forumPageResponseVM.setCreateByMe(getCurrentUser().getId().equals(forumComment.getCreateUser()));
        userList.stream()
                .filter(u -> u.getId().equals(forumComment.getCreateUser()))
                .findFirst()
                .ifPresent(u -> {
                    forumPageResponseVM.setUserName(u.getUserName());
                    forumPageResponseVM.setRealName(u.getRealName());
                    forumPageResponseVM.setImagePath(u.getImagePath());
                });

        List<ForumCommentPageResponseVM> childResponse = allChildForumCommentList.stream()
                .filter(child -> forumComment.getId().equals(child.getParentId()))
                .map(childItem -> commentRecursion(childItem, userList, allChildForumCommentList))
                .collect(Collectors.toList());

        forumPageResponseVM.setChild(childResponse);
        return forumPageResponseVM;
    }


    /**
     * 文章删除
     *
     * @param id
     * @return
     */
    @PostMapping("/comment/delete/{id}")
    public RestResponse commentDelete(@PathVariable Long id) {
        ForumComment forumComment = forumService.getForumCommentById(id);
        if (forumComment.getCreateUser().equals(getCurrentUser().getId())) {
            forumComment.setDeleted(true);
            forumService.updateForumComment(forumComment);
            return RestResponse.ok();
        } else {
            return RestResponse.fail(2, "没有权限删除");
        }
    }

    /**
     * 文章树形查询
     *
     * @param forumArchiveVMList
     */
    private void archiveRecursion(List<ForumArchiveVM> forumArchiveVMList) {
        forumArchiveVMList.forEach(item -> {
            List<ForumArchive> forumArchiveList = forumService.getByParentId(item.getId());
            List<ForumArchiveVM> categoryVM = forumMapping.toForumArchiveVMList(forumArchiveList);
            if (0 != categoryVM.size()) {
                item.setChild(categoryVM);
                archiveRecursion(categoryVM);
            }
        });
    }


}
