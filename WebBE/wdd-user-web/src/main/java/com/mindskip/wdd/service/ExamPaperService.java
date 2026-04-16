package com.mindskip.wdd.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.mindskip.wdd.base.RestResponse;
import com.mindskip.wdd.domain.*;
import com.mindskip.wdd.domain.enums.PaperTypeEnum;
import com.mindskip.wdd.domain.frame.ExamPaperBuildConfig;
import com.mindskip.wdd.domain.frame.ExamPaperFrame;
import com.mindskip.wdd.domain.frame.QuestionAnswerFrame;
import com.mindskip.wdd.domain.frame.QuestionFrame;
import com.mindskip.wdd.viewmodel.exam.paper.ExamPaperDoResponseVM;
import com.mindskip.wdd.viewmodel.exam.paper.ExamPaperPageRequestVM;

import java.util.List;

/**
 * @version 1.7.0
 * @description: 试卷
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
public interface ExamPaperService extends IService<ExamPaper> {


    /**
     * 查询一级节点分页
     *
     * @return the list
     */
    List<ExamPaperArchive> selectRootTree();

    /**
     * 根据父节点获取分类
     *
     * @param parentId the parent id
     * @return the by parent id
     */
    List<ExamPaperArchive> getByParentId(Integer parentId);


    /**
     * 获取试卷缓存
     *
     * @param paperId the paper id
     * @param userId
     * @return the exam paper cache
     */
    ExamPaperCache getExamPaperCache(Long paperId, Integer userId);


    /**
     * 根据题型随机组卷
     *
     * @param examPaperBuildConfig
     * @param examPaperFrame
     */
    void randomExamPaperBuild(ExamPaperBuildConfig examPaperBuildConfig, ExamPaperFrame examPaperFrame);

    /**
     * 组装好题目和题目空答案
     *
     * @param examPaperCache the exam paper cache
     * @param user           the user
     * @return exam paper do response vm
     */
    ExamPaperDoResponseVM toExamPaperDoResponseVM(ExamPaperCache examPaperCache, User user);


    /**
     * 正考试卷分页
     *
     * @param examPaperPageRequestVM the exam paper page request vm
     * @return the page info
     */
    PageInfo<ExamPaper> page(ExamPaperPageRequestVM examPaperPageRequestVM);


    /**
     * 补考试卷分页
     *
     * @param examPaperPageRequestVM the exam paper page request vm
     * @return the page info
     */
    PageInfo<ExamPaper> resitPage(ExamPaperPageRequestVM examPaperPageRequestVM);


    /**
     * 检查是否有查看权限
     *
     * @param paperTypeEnum
     * @param examPaperCache the exam paper cache
     * @param user           the user
     * @return the rest response
     */
    RestResponse paperPermissionCheck(PaperTypeEnum paperTypeEnum, ExamPaperCache examPaperCache, User user);


    /**
     * 抓拍
     *
     * @param examPaperUserCamera the exam paper user camera
     */
    void insertExamPaperUserCamera(ExamPaperUserCamera examPaperUserCamera);

    /**
     * 获取试卷分类
     *
     * @param id
     * @return {@link ExamPaperArchive}
     */
    ExamPaperArchive getExamPaperArchiveById(Integer id);

    /**
     * 获取试卷分类，根据层级
     *
     * @param level
     * @return {@link List}<{@link ExamPaperArchive}>
     */
    List<ExamPaperArchive> getExamPaperArchiveByLevel(String level);


    /**
     * 题目模型转化为答卷题目模型
     *
     * @param questionFrame
     * @param questionItemMess
     * @return {@link QuestionAnswerFrame}
     */
    QuestionAnswerFrame questionFrameToAnswer(QuestionFrame questionFrame, Boolean questionItemMess);


    /**
     * 获取题目内容
     *
     * @param id
     * @return {@link QuestionFrame}
     */
    QuestionFrame getQuestionFrame(String id);

}
