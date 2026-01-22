package com.mindskip.wdd.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mindskip.wdd.domain.ExamPaperUserCamera;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @version 1.7.0
 * @description: 考试抓拍
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
 */
@Mapper
public interface ExamPaperUserCameraMapper extends BaseMapper<ExamPaperUserCamera> {


    /**
     * 获取用户抓拍图片
     *
     * @param userId  the user id
     * @param paperId the paper id
     * @return the camera image
     */
    List<String> getCameraImage(@Param("userId") Integer userId, @Param("examPaperId") Long paperId);
}