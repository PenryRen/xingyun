package com.mindskip.wdd.service.impl;

import cn.hutool.core.util.ReUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mindskip.wdd.base.RestResponse;
import com.mindskip.wdd.domain.AnnouncementArchive;
import com.mindskip.wdd.domain.enums.TreeMoveEnum;
import com.mindskip.wdd.repository.AnnouncementArchiveMapper;
import com.mindskip.wdd.service.AnnouncementArchiveService;
import com.mindskip.wdd.utility.ExamUtil;
import com.mindskip.wdd.viewmodel.announcement.AnnouncementArchiveMoveRequestVM;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.IntStream;

/**
 * @version 1.7.0
 * @description: 通知公告分类
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
 */
@Service
@AllArgsConstructor
public class AnnouncementArchiveServiceImpl extends ServiceImpl<AnnouncementArchiveMapper, AnnouncementArchive> implements AnnouncementArchiveService {

    private final AnnouncementArchiveMapper announcementArchiveMapper;


    @Override
    public List<AnnouncementArchive> getRootAnnouncementArchive() {
        return announcementArchiveMapper.getRootAnnouncementArchive();
    }

    @Override
    public List<AnnouncementArchive> getAnnouncementArchiveByParentId(Integer id) {
        return announcementArchiveMapper.getAnnouncementArchiveByParentId(id);
    }

    @Override
    public int updateLevel(String originalLevel, String targetLevel) {
        String regexLevel = String.format("^%s", ReUtil.escape(originalLevel));
        return announcementArchiveMapper.updateLevel(regexLevel, targetLevel);
    }

    @Override
    public AnnouncementArchive getByLevel(String level) {
        return announcementArchiveMapper.getByLevel(level);
    }

    @Override
    public int deleteByLevel(String level) {
        return announcementArchiveMapper.deleteByLevel(level);
    }


    @Override
    @Transactional
    public RestResponse move(AnnouncementArchiveMoveRequestVM announcementArchiveMoveRequestVM) {
        TreeMoveEnum treeMoveEnum = TreeMoveEnum.fromName(announcementArchiveMoveRequestVM.getDropType());
        AnnouncementArchive draggingNode = announcementArchiveMapper.selectById(announcementArchiveMoveRequestVM.getDraggingNodeId());
        AnnouncementArchive dropNode = announcementArchiveMapper.selectById(announcementArchiveMoveRequestVM.getDropNodeId());
        if (draggingNode.getParentId() != dropNode.getParentId()) {
            String newLevel;
            if (treeMoveEnum == TreeMoveEnum.Inner) {
                newLevel = String.format("%s%s/", dropNode.getLevel(), draggingNode.getName());
            } else {
                if (null == dropNode.getParentId()) {
                    newLevel = String.format("/%s/", draggingNode.getName());
                } else {
                    AnnouncementArchive parentNode = announcementArchiveMapper.selectById(dropNode.getParentId());
                    newLevel = String.format("%s%s/", parentNode.getLevel(), draggingNode.getName());
                }
            }
            AnnouncementArchive exist = getByLevel(newLevel);
            if (null != exist) {
                return RestResponse.fail(2, "公告分类已存在");
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
        updateFullAnnouncementArchive(draggingNode);
        List<AnnouncementArchive> rootAnnouncementArchive = getRootAnnouncementArchive();
        announcementArchiveLevelUpdate(null, rootAnnouncementArchive, treeMoveEnum, draggingNode.getId(), dropNode.getId());
        return RestResponse.ok();
    }


    /**
     * 公告分页位置调整
     *
     * @param parent
     * @param announcementArchiveList
     * @param treeMoveEnum
     * @param draggingNodeId
     * @param dropNodeId
     */
    private void announcementArchiveLevelUpdate(AnnouncementArchive parent, List<AnnouncementArchive> announcementArchiveList, TreeMoveEnum treeMoveEnum, Integer draggingNodeId, Integer dropNodeId) {
        AnnouncementArchive dropNode = announcementArchiveList.stream()
                .filter(item -> item.getId().equals(dropNodeId))
                .findFirst().orElse(null);
        if (null != dropNode) {
            if (treeMoveEnum == TreeMoveEnum.Before || treeMoveEnum == TreeMoveEnum.After) {
                AnnouncementArchive draggingNode = announcementArchiveList.stream()
                        .filter(item -> item.getId().equals(draggingNodeId))
                        .findFirst().get();
                announcementArchiveList.remove(draggingNode);
                int dropIndex = announcementArchiveList.indexOf(dropNode);
                if (treeMoveEnum == TreeMoveEnum.Before) {
                    announcementArchiveList.add(dropIndex, draggingNode);
                } else if (treeMoveEnum == TreeMoveEnum.After) {
                    announcementArchiveList.add(dropIndex + 1, draggingNode);
                }
            }
        }

        IntStream.range(0, announcementArchiveList.size()).forEach(i -> {
            AnnouncementArchive item = announcementArchiveList.get(i);
            item.setItemOrder((i + 1) * ExamUtil.ItemOrderInit);
            if (null == parent) {
                item.setLevel(String.format("/%s/", item.getName()));
            } else {
                item.setLevel(String.format("%s%s/", parent.getLevel(), item.getName()));
            }
            updateFullAnnouncementArchive(item);
            List<AnnouncementArchive> announcementArchiveChild = getAnnouncementArchiveByParentId(item.getId());
            if (announcementArchiveChild.size() > 0) {
                announcementArchiveLevelUpdate(item, announcementArchiveChild, treeMoveEnum, draggingNodeId, dropNodeId);
            }
        });
    }


    /**
     * 公告分类更新
     *
     * @param announcementArchive
     */
    private void updateFullAnnouncementArchive(AnnouncementArchive announcementArchive) {
        LambdaUpdateWrapper<AnnouncementArchive> updateWrapper = new LambdaUpdateWrapper<AnnouncementArchive>()
                .eq(AnnouncementArchive::getId, announcementArchive.getId());
        if (null == announcementArchive.getParentId()) {
            updateWrapper.set(AnnouncementArchive::getParentId, null);
        }
        announcementArchiveMapper.update(announcementArchive, updateWrapper);
    }
}
