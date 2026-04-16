package com.mindskip.wdd.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mindskip.wdd.domain.CourseWareArchive;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @version 6.0.0
 * @description: 课件分类
 * Copyright (C), 2025, 麟航团队
 * @date 2025/9/15 10:28
 */
@Mapper
public interface CourseWareArchiveMapper extends BaseMapper<CourseWareArchive> {

    /**
     * 获取根节点课件分类
     *
     * @return the root apply archive
     */
    List<CourseWareArchive> getRootCourseWareArchive();

    /**
     * 获取课件分类，根据父节点
     *
     * @param id the id
     * @return the apply archive by parent id
     */
    List<CourseWareArchive> getCourseWareArchiveByParentId(Integer id);

    /**
     * 更新层级
     *
     * @param regexLevel  the regex level
     * @param targetLevel the target level
     * @return the int
     */
    int updateLevel(@Param("regexLevel") String regexLevel, @Param("targetLevel") String targetLevel);

    /**
     * 获取课件分类，根据层级
     *
     * @param level the level
     * @return the by level
     */
    CourseWareArchive getByLevel(String level);

    /**
     * 删除分类，根据层级
     *
     * @param level the level
     * @return the int
     */
    int deleteByLevel(String level);
}