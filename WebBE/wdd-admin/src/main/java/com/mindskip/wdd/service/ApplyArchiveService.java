package com.mindskip.wdd.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mindskip.wdd.base.RestResponse;
import com.mindskip.wdd.domain.ApplyArchive;
import com.mindskip.wdd.viewmodel.apply.ApplyArchiveMoveRequestVM;

import java.util.List;


/**
 * @version 1.7.0
 * @description: 报名分类
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
public interface ApplyArchiveService extends IService<ApplyArchive> {

    /**
     * 获取一级节点报名分类
     *
     * @return
     */
    List<ApplyArchive> getRootApplyArchive();

    /**
     * 根据父节点id,获取报名分类
     *
     * @param id
     * @return
     */
    List<ApplyArchive> getApplyArchiveByParentId(Integer id);

    /**
     * 更新报名分类层级
     *
     * @param originalLevel
     * @param targetLevel
     * @return
     */
    int updateLevel(String originalLevel, String targetLevel);

    /**
     * 根据层级获取报名分类
     *
     * @param level
     * @return
     */
    ApplyArchive getByLevel(String level);

    /**
     * 根据层级删除报名分类
     *
     * @param level
     * @return
     */
    int deleteByLevel(String level);


    /**
     * 报名分页位置移动
     *
     * @param applyArchiveMoveRequestVM
     * @return {@link RestResponse}
     */
    RestResponse move(ApplyArchiveMoveRequestVM applyArchiveMoveRequestVM);
}
