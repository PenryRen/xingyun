package com.mindskip.wdd.service.impl;

import cn.hutool.core.util.ReUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mindskip.wdd.base.RestResponse;
import com.mindskip.wdd.domain.CourseWareArchive;
import com.mindskip.wdd.domain.enums.TreeMoveEnum;
import com.mindskip.wdd.repository.CourseWareArchiveMapper;
import com.mindskip.wdd.service.CourseWareArchiveService;
import com.mindskip.wdd.utility.ExamUtil;
import com.mindskip.wdd.viewmodel.course.ware.CourseWareArchiveMoveRequestVM;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.IntStream;

/**
 * @version 6.0.0
 * @description: 课件分类
 * Copyright (C), 2025, 麟航团队
 * @date 2025/9/15 10:28
 */
@Service
@AllArgsConstructor
public class CourseWareArchiveServiceImpl extends ServiceImpl<CourseWareArchiveMapper, CourseWareArchive> implements CourseWareArchiveService {

    private final CourseWareArchiveMapper courseWareArchiveMapper;


    @Override
    public List<CourseWareArchive> getRootCourseWareArchive() {
        return courseWareArchiveMapper.getRootCourseWareArchive();
    }

    @Override
    public List<CourseWareArchive> getCourseWareArchiveByParentId(Integer id) {
        return courseWareArchiveMapper.getCourseWareArchiveByParentId(id);
    }

    @Override
    public int updateLevel(String originalLevel, String targetLevel) {
        String regexLevel = String.format("^%s", ReUtil.escape(originalLevel));
        return courseWareArchiveMapper.updateLevel(regexLevel, targetLevel);
    }

    @Override
    public CourseWareArchive getByLevel(String level) {
        return courseWareArchiveMapper.getByLevel(level);
    }

    @Override
    public int deleteByLevel(String level) {
        return courseWareArchiveMapper.deleteByLevel(level);
    }

    @Override
    public RestResponse move(CourseWareArchiveMoveRequestVM courseWareArchiveMoveRequestVM) {
        TreeMoveEnum treeMoveEnum = TreeMoveEnum.fromName(courseWareArchiveMoveRequestVM.getDropType());
        CourseWareArchive draggingNode = courseWareArchiveMapper.selectById(courseWareArchiveMoveRequestVM.getDraggingNodeId());
        CourseWareArchive dropNode = courseWareArchiveMapper.selectById(courseWareArchiveMoveRequestVM.getDropNodeId());
        if (draggingNode.getParentId() != dropNode.getParentId()) {
            String newLevel;
            if (treeMoveEnum == TreeMoveEnum.Inner) {
                newLevel = String.format("%s%s/", dropNode.getLevel(), draggingNode.getName());
            } else {
                if (null == dropNode.getParentId()) {
                    newLevel = String.format("/%s/", draggingNode.getName());
                } else {
                    CourseWareArchive parentNode = courseWareArchiveMapper.selectById(dropNode.getParentId());
                    newLevel = String.format("%s%s/", parentNode.getLevel(), draggingNode.getName());
                }
            }
            CourseWareArchive exist = getByLevel(newLevel);
            if (null != exist) {
                return RestResponse.fail(2, "课件分类已存在");
            }
        }
        switch (treeMoveEnum) {
            case Before:
                draggingNode.setParentId(dropNode.getParentId());
                break;
            case After:
                draggingNode.setParentId(dropNode.getParentId());
                break;
            case Inner:
                draggingNode.setItemOrder(ExamUtil.ItemOrderInit);
                draggingNode.setParentId(dropNode.getId());
                break;
        }
        updateFullCourseWareArchive(draggingNode);
        List<CourseWareArchive> rootCourseWareArchive = getRootCourseWareArchive();
        courseWareArchiveLevelUpdate(null, rootCourseWareArchive, treeMoveEnum, draggingNode.getId(), dropNode.getId());
        return RestResponse.ok();
    }


    /**
     * 课件分类位置调整
     *
     * @param parent
     * @param courseWareArchiveList
     * @param treeMoveEnum
     * @param draggingNodeId
     * @param dropNodeId
     */
    private void courseWareArchiveLevelUpdate(CourseWareArchive parent, List<CourseWareArchive> courseWareArchiveList, TreeMoveEnum treeMoveEnum, Integer draggingNodeId, Integer dropNodeId) {
        CourseWareArchive dropNode = courseWareArchiveList.stream()
                .filter(item -> item.getId().equals(dropNodeId))
                .findFirst().orElse(null);
        if (null != dropNode) {
            if (treeMoveEnum == TreeMoveEnum.Before || treeMoveEnum == TreeMoveEnum.After) {
                CourseWareArchive draggingNode = courseWareArchiveList.stream()
                        .filter(item -> item.getId().equals(draggingNodeId))
                        .findFirst().get();
                courseWareArchiveList.remove(draggingNode);
                int dropIndex = courseWareArchiveList.indexOf(dropNode);
                if (treeMoveEnum == TreeMoveEnum.Before) {
                    courseWareArchiveList.add(dropIndex, draggingNode);
                } else if (treeMoveEnum == TreeMoveEnum.After) {
                    courseWareArchiveList.add(dropIndex + 1, draggingNode);
                }
            }
        }

        IntStream.range(0, courseWareArchiveList.size()).forEach(i -> {
            CourseWareArchive item = courseWareArchiveList.get(i);
            item.setItemOrder((i + 1) * ExamUtil.ItemOrderInit);
            if (null == parent) {
                item.setLevel(String.format("/%s/", item.getName()));
            } else {
                item.setLevel(String.format("%s%s/", parent.getLevel(), item.getName()));
            }
            updateFullCourseWareArchive(item);
            List<CourseWareArchive> courseWareArchiveChild = getCourseWareArchiveByParentId(item.getId());
            if (courseWareArchiveChild.size() > 0) {
                courseWareArchiveLevelUpdate(item, courseWareArchiveChild, treeMoveEnum, draggingNodeId, dropNodeId);
            }
        });
    }


    /**
     * 课件分类更新
     *
     * @param courseWareArchive
     */
    private void updateFullCourseWareArchive(CourseWareArchive courseWareArchive) {
        LambdaUpdateWrapper<CourseWareArchive> updateWrapper = new LambdaUpdateWrapper<CourseWareArchive>()
                .eq(CourseWareArchive::getId, courseWareArchive.getId());
        if (null == courseWareArchive.getParentId()) {
            updateWrapper.set(CourseWareArchive::getParentId, null);
        }
        courseWareArchiveMapper.update(courseWareArchive, updateWrapper);
    }
}
