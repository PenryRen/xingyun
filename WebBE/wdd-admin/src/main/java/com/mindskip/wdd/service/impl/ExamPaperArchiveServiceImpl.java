package com.mindskip.wdd.service.impl;

import cn.hutool.core.util.ReUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mindskip.wdd.base.RestResponse;
import com.mindskip.wdd.domain.ExamPaperArchive;
import com.mindskip.wdd.domain.enums.TreeMoveEnum;
import com.mindskip.wdd.repository.ExamPaperArchiveMapper;
import com.mindskip.wdd.service.ExamPaperArchiveService;
import com.mindskip.wdd.utility.ExamUtil;
import com.mindskip.wdd.viewmodel.exam.paper.ExamPaperArchiveMoveRequestVM;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.IntStream;

/**
 * @version 1.7.0
 * @description: 试卷分类
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
 */
@Service
@AllArgsConstructor
public class ExamPaperArchiveServiceImpl extends ServiceImpl<ExamPaperArchiveMapper, ExamPaperArchive> implements ExamPaperArchiveService {

    private final ExamPaperArchiveMapper examPaperArchiveMapper;


    @Override
    public List<ExamPaperArchive> getRootExamPaperArchive() {
        return examPaperArchiveMapper.getRootExamPaperArchive();
    }

    @Override
    public List<ExamPaperArchive> getExamPaperArchiveByParentId(Integer id) {
        return examPaperArchiveMapper.getExamPaperArchiveByParentId(id);
    }

    @Override
    public int updateLevel(String originalLevel, String targetLevel) {
        String regexLevel = String.format("^%s", ReUtil.escape(originalLevel));
        return examPaperArchiveMapper.updateLevel(regexLevel, targetLevel);
    }

    @Override
    public ExamPaperArchive getByLevel(String level) {
        return examPaperArchiveMapper.getByLevel(level);
    }

    @Override
    public int deleteByLevel(String level) {
        return examPaperArchiveMapper.deleteByLevel(level);
    }

    @Override
    @Transactional
    public RestResponse move(ExamPaperArchiveMoveRequestVM examPaperArchiveMoveRequestVM) {
        TreeMoveEnum treeMoveEnum = TreeMoveEnum.fromName(examPaperArchiveMoveRequestVM.getDropType());
        ExamPaperArchive draggingNode = examPaperArchiveMapper.selectById(examPaperArchiveMoveRequestVM.getDraggingNodeId());
        ExamPaperArchive dropNode = examPaperArchiveMapper.selectById(examPaperArchiveMoveRequestVM.getDropNodeId());
        if (draggingNode.getParentId() != dropNode.getParentId()) {
            String newLevel;
            if (treeMoveEnum == TreeMoveEnum.Inner) {
                newLevel = String.format("%s%s/", dropNode.getLevel(), draggingNode.getName());
            } else {
                if (null == dropNode.getParentId()) {
                    newLevel = String.format("/%s/", draggingNode.getName());
                } else {
                    ExamPaperArchive parentNode = examPaperArchiveMapper.selectById(dropNode.getParentId());
                    newLevel = String.format("%s%s/", parentNode.getLevel(), draggingNode.getName());
                }
            }
            ExamPaperArchive exist = getByLevel(newLevel);
            if (null != exist) {
                return RestResponse.fail(2, "试卷分类已存在");
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
        updateFullExamPaperArchive(draggingNode);
        List<ExamPaperArchive> rootExamPaperArchive = getRootExamPaperArchive();
        examPaperArchiveLevelUpdate(null, rootExamPaperArchive, treeMoveEnum, draggingNode.getId(), dropNode.getId());
        return RestResponse.ok();
    }


    /**
     * 试卷分类位置调整
     *
     * @param parent
     * @param examPaperArchiveList
     * @param treeMoveEnum
     * @param draggingNodeId
     * @param dropNodeId
     */
    private void examPaperArchiveLevelUpdate(ExamPaperArchive parent, List<ExamPaperArchive> examPaperArchiveList, TreeMoveEnum treeMoveEnum, Integer draggingNodeId, Integer dropNodeId) {
        ExamPaperArchive dropNode = examPaperArchiveList.stream()
                .filter(item -> item.getId().equals(dropNodeId))
                .findFirst().orElse(null);
        if (null != dropNode) {
            if (treeMoveEnum == TreeMoveEnum.Before || treeMoveEnum == TreeMoveEnum.After) {
                ExamPaperArchive draggingNode = examPaperArchiveList.stream()
                        .filter(item -> item.getId().equals(draggingNodeId))
                        .findFirst().get();
                examPaperArchiveList.remove(draggingNode);
                int dropIndex = examPaperArchiveList.indexOf(dropNode);
                if (treeMoveEnum == TreeMoveEnum.Before) {
                    examPaperArchiveList.add(dropIndex, draggingNode);
                } else if (treeMoveEnum == TreeMoveEnum.After) {
                    examPaperArchiveList.add(dropIndex + 1, draggingNode);
                }
            }
        }

        IntStream.range(0, examPaperArchiveList.size()).forEach(i -> {
            ExamPaperArchive item = examPaperArchiveList.get(i);
            item.setItemOrder((i + 1) * ExamUtil.ItemOrderInit);
            if (null == parent) {
                item.setLevel(String.format("/%s/", item.getName()));
            } else {
                item.setLevel(String.format("%s%s/", parent.getLevel(), item.getName()));
            }
            updateFullExamPaperArchive(item);
            List<ExamPaperArchive> examPaperArchiveChild = getExamPaperArchiveByParentId(item.getId());
            if (examPaperArchiveChild.size() > 0) {
                examPaperArchiveLevelUpdate(item, examPaperArchiveChild, treeMoveEnum, draggingNodeId, dropNodeId);
            }
        });
    }


    /**
     * 试卷分类更新
     *
     * @param examPaperArchive
     */
    private void updateFullExamPaperArchive(ExamPaperArchive examPaperArchive) {
        LambdaUpdateWrapper<ExamPaperArchive> updateWrapper = new LambdaUpdateWrapper<ExamPaperArchive>()
                .eq(ExamPaperArchive::getId, examPaperArchive.getId());
        if (null == examPaperArchive.getParentId()) {
            updateWrapper.set(ExamPaperArchive::getParentId, null);
        }
        examPaperArchiveMapper.update(examPaperArchive, updateWrapper);
    }
}
