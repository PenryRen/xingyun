package com.mindskip.wdd.service.impl;

import cn.hutool.core.util.ReUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mindskip.wdd.base.RestResponse;
import com.mindskip.wdd.domain.TrainArchive;
import com.mindskip.wdd.domain.enums.TreeMoveEnum;
import com.mindskip.wdd.repository.TrainArchiveMapper;
import com.mindskip.wdd.service.TrainArchiveService;
import com.mindskip.wdd.utility.ExamUtil;
import com.mindskip.wdd.viewmodel.train.archive.TrainArchiveMoveRequestVM;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.IntStream;

/**
 * @version 9.0.0
 * @description: 培训分类
 * Copyright (C), 2025, 麟航团队
 * @date 2025/9/16 10:45
 */
@Service
@AllArgsConstructor
public class TrainArchiveServiceImpl extends ServiceImpl<TrainArchiveMapper, TrainArchive> implements TrainArchiveService {

    private final TrainArchiveMapper trainArchiveMapper;


    @Override
    public List<TrainArchive> getRootTrainArchive() {
        return trainArchiveMapper.getRootTrainArchive();
    }

    @Override
    public List<TrainArchive> getTrainArchiveByParentId(Integer id) {
        return trainArchiveMapper.getTrainArchiveByParentId(id);
    }

    @Override
    public int updateLevel(String originalLevel, String targetLevel) {
        String regexLevel = String.format("^%s", ReUtil.escape(originalLevel));
        return trainArchiveMapper.updateLevel(regexLevel, targetLevel);
    }

    @Override
    public TrainArchive getByLevel(String level) {
        return trainArchiveMapper.getByLevel(level);
    }

    @Override
    public int deleteByLevel(String level) {
        return trainArchiveMapper.deleteByLevel(level);
    }


    @Override
    public RestResponse move(TrainArchiveMoveRequestVM trainArchiveMoveRequestVM) {
        TreeMoveEnum treeMoveEnum = TreeMoveEnum.fromName(trainArchiveMoveRequestVM.getDropType());
        TrainArchive draggingNode = trainArchiveMapper.selectById(trainArchiveMoveRequestVM.getDraggingNodeId());
        TrainArchive dropNode = trainArchiveMapper.selectById(trainArchiveMoveRequestVM.getDropNodeId());
        if (draggingNode.getParentId() != dropNode.getParentId()) {
            String newLevel;
            if (treeMoveEnum == TreeMoveEnum.Inner) {
                newLevel = String.format("%s%s/", dropNode.getLevel(), draggingNode.getName());
            } else {
                if (null == dropNode.getParentId()) {
                    newLevel = String.format("/%s/", draggingNode.getName());
                } else {
                    TrainArchive parentNode = trainArchiveMapper.selectById(dropNode.getParentId());
                    newLevel = String.format("%s%s/", parentNode.getLevel(), draggingNode.getName());
                }
            }
            TrainArchive exist = getByLevel(newLevel);
            if (null != exist) {
                return RestResponse.fail(2, "培训分类已存在");
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
        updateFullTrainArchive(draggingNode);
        List<TrainArchive> rootTrainArchive = getRootTrainArchive();
        trainArchiveLevelUpdate(null, rootTrainArchive, treeMoveEnum, draggingNode.getId(), dropNode.getId());
        return RestResponse.ok();
    }


    /**
     * 培训分类位置调整
     *
     * @param parent
     * @param trainArchiveList
     * @param treeMoveEnum
     * @param draggingNodeId
     * @param dropNodeId
     */
    private void trainArchiveLevelUpdate(TrainArchive parent, List<TrainArchive> trainArchiveList, TreeMoveEnum treeMoveEnum, Integer draggingNodeId, Integer dropNodeId) {
        TrainArchive dropNode = trainArchiveList.stream()
                .filter(item -> item.getId().equals(dropNodeId))
                .findFirst().orElse(null);
        if (null != dropNode) {
            if (treeMoveEnum == TreeMoveEnum.Before || treeMoveEnum == TreeMoveEnum.After) {
                TrainArchive draggingNode = trainArchiveList.stream()
                        .filter(item -> item.getId().equals(draggingNodeId))
                        .findFirst().get();
                trainArchiveList.remove(draggingNode);
                int dropIndex = trainArchiveList.indexOf(dropNode);
                if (treeMoveEnum == TreeMoveEnum.Before) {
                    trainArchiveList.add(dropIndex, draggingNode);
                } else if (treeMoveEnum == TreeMoveEnum.After) {
                    trainArchiveList.add(dropIndex + 1, draggingNode);
                }
            }
        }

        IntStream.range(0, trainArchiveList.size()).forEach(i -> {
            TrainArchive item = trainArchiveList.get(i);
            item.setItemOrder((i + 1) * ExamUtil.ItemOrderInit);
            if (null == parent) {
                item.setLevel(String.format("/%s/", item.getName()));
            } else {
                item.setLevel(String.format("%s%s/", parent.getLevel(), item.getName()));
            }
            updateFullTrainArchive(item);
            List<TrainArchive> trainArchiveChild = getTrainArchiveByParentId(item.getId());
            if (trainArchiveChild.size() > 0) {
                trainArchiveLevelUpdate(item, trainArchiveChild, treeMoveEnum, draggingNodeId, dropNodeId);
            }
        });
    }


    /**
     * 培训分类更新
     *
     * @param trainArchive
     */
    private void updateFullTrainArchive(TrainArchive trainArchive) {
        LambdaUpdateWrapper<TrainArchive> updateWrapper = new LambdaUpdateWrapper<TrainArchive>()
                .eq(TrainArchive::getId, trainArchive.getId());
        if (null == trainArchive.getParentId()) {
            updateWrapper.set(TrainArchive::getParentId, null);
        }
        trainArchiveMapper.update(trainArchive, updateWrapper);
    }
}
