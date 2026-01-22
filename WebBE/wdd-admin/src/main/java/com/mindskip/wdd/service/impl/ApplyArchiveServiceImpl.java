package com.mindskip.wdd.service.impl;

import cn.hutool.core.util.ReUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mindskip.wdd.base.RestResponse;
import com.mindskip.wdd.domain.ApplyArchive;
import com.mindskip.wdd.domain.enums.TreeMoveEnum;
import com.mindskip.wdd.repository.ApplyArchiveMapper;
import com.mindskip.wdd.service.ApplyArchiveService;
import com.mindskip.wdd.utility.ExamUtil;
import com.mindskip.wdd.viewmodel.apply.ApplyArchiveMoveRequestVM;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.IntStream;

/**
 * @version 1.7.0
 * @description: 报名分类
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
 */
@Service
@AllArgsConstructor
public class ApplyArchiveServiceImpl extends ServiceImpl<ApplyArchiveMapper, ApplyArchive> implements ApplyArchiveService {

    private final ApplyArchiveMapper applyArchiveMapper;


    @Override
    public List<ApplyArchive> getRootApplyArchive() {
        return applyArchiveMapper.getRootApplyArchive();
    }

    @Override
    public List<ApplyArchive> getApplyArchiveByParentId(Integer id) {
        return applyArchiveMapper.getApplyArchiveByParentId(id);
    }

    @Override
    public int updateLevel(String originalLevel, String targetLevel) {
        String regexLevel = String.format("^%s", ReUtil.escape(originalLevel));
        return applyArchiveMapper.updateLevel(regexLevel, targetLevel);
    }

    @Override
    public ApplyArchive getByLevel(String level) {
        return applyArchiveMapper.getByLevel(level);
    }

    @Override
    public int deleteByLevel(String level) {
        return applyArchiveMapper.deleteByLevel(level);
    }


    @Override
    @Transactional
    public RestResponse move(ApplyArchiveMoveRequestVM applyArchiveMoveRequestVM) {
        TreeMoveEnum treeMoveEnum = TreeMoveEnum.fromName(applyArchiveMoveRequestVM.getDropType());
        ApplyArchive draggingNode = applyArchiveMapper.selectById(applyArchiveMoveRequestVM.getDraggingNodeId());
        ApplyArchive dropNode = applyArchiveMapper.selectById(applyArchiveMoveRequestVM.getDropNodeId());
        if (draggingNode.getParentId() != dropNode.getParentId()) {
            String newLevel;
            if (treeMoveEnum == TreeMoveEnum.Inner) {
                newLevel = String.format("%s%s/", dropNode.getLevel(), draggingNode.getName());
            } else {
                if (null == dropNode.getParentId()) {
                    newLevel = String.format("/%s/", draggingNode.getName());
                } else {
                    ApplyArchive parentDepartment = applyArchiveMapper.selectById(dropNode.getParentId());
                    newLevel = String.format("%s%s/", parentDepartment.getLevel(), draggingNode.getName());
                }
            }
            ApplyArchive exist = getByLevel(newLevel);
            if (null != exist) {
                return RestResponse.fail(2, "报名分类已存在");
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
        applyArchiveMapper.updateById(draggingNode);
        List<ApplyArchive> rootApplyArchive = getRootApplyArchive();
        applyArchiveLevelUpdate(null, rootApplyArchive, treeMoveEnum, draggingNode.getId(), dropNode.getId());
        return RestResponse.ok();
    }


    /**
     * 报名分类位置调整
     *
     * @param parent
     * @param applyArchiveList
     * @param treeMoveEnum
     * @param draggingNodeId
     * @param dropNodeId
     */
    private void applyArchiveLevelUpdate(ApplyArchive parent, List<ApplyArchive> applyArchiveList, TreeMoveEnum treeMoveEnum, Integer draggingNodeId, Integer dropNodeId) {
        ApplyArchive dropNode = applyArchiveList.stream()
                .filter(item -> item.getId().equals(dropNodeId))
                .findFirst().orElse(null);
        if (null != dropNode) {
            if (treeMoveEnum == TreeMoveEnum.Before || treeMoveEnum == TreeMoveEnum.After) {
                ApplyArchive draggingNode = applyArchiveList.stream()
                        .filter(item -> item.getId().equals(draggingNodeId))
                        .findFirst().get();
                applyArchiveList.remove(draggingNode);
                int dropIndex = applyArchiveList.indexOf(dropNode);
                if (treeMoveEnum == TreeMoveEnum.Before) {
                    applyArchiveList.add(dropIndex, draggingNode);
                } else if (treeMoveEnum == TreeMoveEnum.After) {
                    applyArchiveList.add(dropIndex + 1, draggingNode);
                }
            }
        }

        IntStream.range(0, applyArchiveList.size()).forEach(i -> {
            ApplyArchive item = applyArchiveList.get(i);
            item.setItemOrder((i + 1) * ExamUtil.ItemOrderInit);
            if (null == parent) {
                item.setLevel(String.format("/%s/", item.getName()));
            } else {
                item.setLevel(String.format("%s%s/", parent.getLevel(), item.getName()));
            }
            applyArchiveMapper.updateById(item);
            List<ApplyArchive> applyArchiveChild = getApplyArchiveByParentId(item.getId());
            if (applyArchiveChild.size() > 0) {
                applyArchiveLevelUpdate(item, applyArchiveChild, treeMoveEnum, draggingNodeId, dropNodeId);
            }
        });
    }
}
