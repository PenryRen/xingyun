package com.mindskip.wdd.service.impl;

import cn.hutool.core.util.ReUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mindskip.wdd.base.RestResponse;
import com.mindskip.wdd.domain.ForumArchive;
import com.mindskip.wdd.domain.enums.TreeMoveEnum;
import com.mindskip.wdd.repository.ForumArchiveMapper;
import com.mindskip.wdd.service.ForumArchiveService;
import com.mindskip.wdd.utility.ExamUtil;
import com.mindskip.wdd.viewmodel.forum.ForumArchiveMoveRequestVM;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.IntStream;

/**
 * @version 1.9.0
 * @description: 文章分类
 * Copyright (C), 2024, 麒技团队
 * @date 2024/7/1 10:45
 */
@Service
@AllArgsConstructor
public class ForumArchiveServiceImpl extends ServiceImpl<ForumArchiveMapper, ForumArchive> implements ForumArchiveService {

    private final ForumArchiveMapper forumArchiveMapper;


    @Override
    public List<ForumArchive> getRootForumArchive() {
        return forumArchiveMapper.getRootForumArchive();
    }

    @Override
    public List<ForumArchive> getForumArchiveByParentId(Integer id) {
        return forumArchiveMapper.getForumArchiveByParentId(id);
    }

    @Override
    public int updateLevel(String originalLevel, String targetLevel) {
        String regexLevel = String.format("^%s", ReUtil.escape(originalLevel));
        return forumArchiveMapper.updateLevel(regexLevel, targetLevel);
    }

    @Override
    public ForumArchive getByLevel(String level) {
        return forumArchiveMapper.getByLevel(level);
    }

    @Override
    public int deleteByLevel(String level) {
        return forumArchiveMapper.deleteByLevel(level);
    }


    @Override
    @Transactional
    public RestResponse move(ForumArchiveMoveRequestVM forumArchiveMoveRequestVM) {
        TreeMoveEnum treeMoveEnum = TreeMoveEnum.fromName(forumArchiveMoveRequestVM.getDropType());
        ForumArchive draggingNode = forumArchiveMapper.selectById(forumArchiveMoveRequestVM.getDraggingNodeId());
        ForumArchive dropNode = forumArchiveMapper.selectById(forumArchiveMoveRequestVM.getDropNodeId());
        if (draggingNode.getParentId() != dropNode.getParentId()) {
            String newLevel;
            if (treeMoveEnum == TreeMoveEnum.Inner) {
                newLevel = String.format("%s%s/", dropNode.getLevel(), draggingNode.getName());
            } else {
                if (null == dropNode.getParentId()) {
                    newLevel = String.format("/%s/", draggingNode.getName());
                } else {
                    ForumArchive parentNode = forumArchiveMapper.selectById(dropNode.getParentId());
                    newLevel = String.format("%s%s/", parentNode.getLevel(), draggingNode.getName());
                }
            }
            ForumArchive exist = getByLevel(newLevel);
            if (null != exist) {
                return RestResponse.fail(2, "文章分类已存在");
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
        updateFullForumArchive(draggingNode);
        List<ForumArchive> rootForumArchive = getRootForumArchive();
        forumArchiveLevelUpdate(null, rootForumArchive, treeMoveEnum, draggingNode.getId(), dropNode.getId());
        return RestResponse.ok();
    }


    /**
     * 文章分类位置调整
     *
     * @param parent
     * @param forumArchiveList
     * @param treeMoveEnum
     * @param draggingNodeId
     * @param dropNodeId
     */
    private void forumArchiveLevelUpdate(ForumArchive parent, List<ForumArchive> forumArchiveList, TreeMoveEnum treeMoveEnum, Integer draggingNodeId, Integer dropNodeId) {
        ForumArchive dropNode = forumArchiveList.stream()
                .filter(item -> item.getId().equals(dropNodeId))
                .findFirst().orElse(null);
        if (null != dropNode) {
            if (treeMoveEnum == TreeMoveEnum.Before || treeMoveEnum == TreeMoveEnum.After) {
                ForumArchive draggingNode = forumArchiveList.stream()
                        .filter(item -> item.getId().equals(draggingNodeId))
                        .findFirst().get();
                forumArchiveList.remove(draggingNode);
                int dropIndex = forumArchiveList.indexOf(dropNode);
                if (treeMoveEnum == TreeMoveEnum.Before) {
                    forumArchiveList.add(dropIndex, draggingNode);
                } else if (treeMoveEnum == TreeMoveEnum.After) {
                    forumArchiveList.add(dropIndex + 1, draggingNode);
                }
            }
        }

        IntStream.range(0, forumArchiveList.size()).forEach(i -> {
            ForumArchive item = forumArchiveList.get(i);
            item.setItemOrder((i + 1) * ExamUtil.ItemOrderInit);
            if (null == parent) {
                item.setLevel(String.format("/%s/", item.getName()));
            } else {
                item.setLevel(String.format("%s%s/", parent.getLevel(), item.getName()));
            }
            updateFullForumArchive(item);
            List<ForumArchive> forumArchiveChild = getForumArchiveByParentId(item.getId());
            if (forumArchiveChild.size() > 0) {
                forumArchiveLevelUpdate(item, forumArchiveChild, treeMoveEnum, draggingNodeId, dropNodeId);
            }
        });
    }


    /**
     * 文章分类更新
     *
     * @param forumArchive
     */
    private void updateFullForumArchive(ForumArchive forumArchive) {
        LambdaUpdateWrapper<ForumArchive> updateWrapper = new LambdaUpdateWrapper<ForumArchive>()
                .eq(ForumArchive::getId, forumArchive.getId());
        if (null == forumArchive.getParentId()) {
            updateWrapper.set(ForumArchive::getParentId, null);
        }
        forumArchiveMapper.update(forumArchive, updateWrapper);
    }
}
