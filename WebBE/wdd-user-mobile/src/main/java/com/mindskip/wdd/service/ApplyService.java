package com.mindskip.wdd.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.mindskip.wdd.domain.Apply;
import com.mindskip.wdd.domain.ApplyArchive;
import com.mindskip.wdd.domain.User;
import com.mindskip.wdd.domain.UserApply;
import com.mindskip.wdd.viewmodel.apply.ApplyPageRequestVM;
import com.mindskip.wdd.viewmodel.apply.UserApplyPageResponseVM;

import java.util.List;

/**
 * @version 1.7.0
 * @description: 报名
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
public interface ApplyService extends IService<Apply> {
    /**
     * 报名分页
     *
     * @param requestVM the request vm
     * @return the page info
     */
    PageInfo<Apply> page(ApplyPageRequestVM requestVM);

    /**
     * 用户报名记录分页
     *
     * @param requestVM the request vm
     * @return the page info
     */
    PageInfo<UserApplyPageResponseVM> record(ApplyPageRequestVM requestVM);

    /**
     * 获取用户报名记录
     *
     * @param userId  the user id
     * @param applyId the apply id
     * @return the user apply
     */
    UserApply getUserApply(Integer userId, Integer applyId);

    /**
     * 获取用户报名
     *
     * @param id the id
     * @return the user apply
     */
    UserApply getUserApply(Long id);

    /**
     * 更新用户报名
     *
     * @param userApply the user apply
     * @return the int
     */
    int updateUserApply(UserApply userApply);

    /**
     * 去报名
     *
     * @param id   the id
     * @param user the user
     * @return the int
     */
    Boolean goApply(Integer id, User user);

    /**
     * 取消报名
     *
     * @param applyId     the apply id
     * @param userApplyId the user apply id
     */
    void cancelApply(Integer applyId, Long userApplyId);


    /**
     * 获取一级分类
     *
     * @return {@link List}<{@link ApplyArchive}>
     */
    List<ApplyArchive> selectRootTree();

    /**
     * 根据id获取分类
     *
     * @param id
     * @return {@link ApplyArchive}
     */
    ApplyArchive getApplyArchiveById(Integer id);

    /**
     * 根据层级获取分类
     *
     * @param level
     * @return {@link List}<{@link ApplyArchive}>
     */
    List<ApplyArchive> getApplyArchiveByLevel(String level);
}
