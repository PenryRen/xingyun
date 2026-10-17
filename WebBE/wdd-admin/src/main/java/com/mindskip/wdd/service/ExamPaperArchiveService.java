package com.mindskip.wdd.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mindskip.wdd.base.RestResponse;
import com.mindskip.wdd.domain.ExamPaperArchive;
import com.mindskip.wdd.viewmodel.exam.paper.ExamPaperArchiveMoveRequestVM;

import java.util.List;

/**
 * @version 1.7.0
 * @description: 试卷分类
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
public interface ExamPaperArchiveService extends IService<ExamPaperArchive> {

    /**
     * 获取一级节点试卷分类
     *
     * @return
     */
    List<ExamPaperArchive> getRootExamPaperArchive();

    /**
     * 获取试卷分类， 根据父节点id
     *
     * @param id
     * @return
     */
    List<ExamPaperArchive> getExamPaperArchiveByParentId(Integer id);

    /**
     * 更新层级
     *
     * @param originalLevel
     * @param targetLevel
     * @return
     */
    int updateLevel(String originalLevel, String targetLevel);

    /**
     * 获取试卷分类，根据层级
     *
     * @param level
     * @return
     */
    ExamPaperArchive getByLevel(String level);

    /**
     * 根据层级删除试卷分类
     *
     * @param level
     * @return
     */
    int deleteByLevel(String level);

    /**
     * 试卷分类移动
     *
     * @param examPaperArchiveMoveRequestVM
     * @return {@link RestResponse}
     */
    RestResponse move(ExamPaperArchiveMoveRequestVM examPaperArchiveMoveRequestVM);
}
