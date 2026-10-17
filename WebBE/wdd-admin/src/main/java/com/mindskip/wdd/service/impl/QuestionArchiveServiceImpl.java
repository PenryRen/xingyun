package com.mindskip.wdd.service.impl;

import cn.hutool.core.util.ReUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mindskip.wdd.base.RestResponse;
import com.mindskip.wdd.domain.QuestionArchive;
import com.mindskip.wdd.domain.enums.TreeMoveEnum;
import com.mindskip.wdd.repository.QuestionArchiveMapper;
import com.mindskip.wdd.service.QuestionArchiveService;
import com.mindskip.wdd.utility.ExamUtil;
import com.mindskip.wdd.viewmodel.question.QuestionArchiveMoveRequestVM;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.IntStream;

/**
 * @version 1.7.0
 * @description: 题目分类
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
@Service
@AllArgsConstructor
public class QuestionArchiveServiceImpl extends ServiceImpl<QuestionArchiveMapper, QuestionArchive> implements QuestionArchiveService {

    private final QuestionArchiveMapper questionArchiveMapper;


    @Override
    public List<QuestionArchive> getRootQuestionArchive() {
        return questionArchiveMapper.getRootQuestionArchive();
    }

    @Override
    public List<QuestionArchive> getQuestionArchiveByParentId(Integer id) {
        return questionArchiveMapper.getQuestionArchiveByParentId(id);
    }

    @Override
    public int updateLevel(String originalLevel, String targetLevel) {
        String regexLevel = String.format("^%s", ReUtil.escape(originalLevel));
        return questionArchiveMapper.updateLevel(regexLevel, targetLevel);
    }

    @Override
    public QuestionArchive getByLevel(String level) {
        return questionArchiveMapper.getByLevel(level);
    }

    @Override
    public int deleteByLevel(String level) {
        return questionArchiveMapper.deleteByLevel(level);
    }


    @Override
    @Transactional
    public RestResponse move(QuestionArchiveMoveRequestVM questionArchiveMoveRequestVM) {
        TreeMoveEnum treeMoveEnum = TreeMoveEnum.fromName(questionArchiveMoveRequestVM.getDropType());
        QuestionArchive draggingNode = questionArchiveMapper.selectById(questionArchiveMoveRequestVM.getDraggingNodeId());
        QuestionArchive dropNode = questionArchiveMapper.selectById(questionArchiveMoveRequestVM.getDropNodeId());
        if (draggingNode.getParentId() != dropNode.getParentId()) {
            String newLevel;
            if (treeMoveEnum == TreeMoveEnum.Inner) {
                newLevel = String.format("%s%s/", dropNode.getLevel(), draggingNode.getName());
            } else {
                if (null == dropNode.getParentId()) {
                    newLevel = String.format("/%s/", draggingNode.getName());
                } else {
                    QuestionArchive parentNode = questionArchiveMapper.selectById(dropNode.getParentId());
                    newLevel = String.format("%s%s/", parentNode.getLevel(), draggingNode.getName());
                }
            }
            QuestionArchive exist = getByLevel(newLevel);
            if (null != exist) {
                return RestResponse.fail(2, "题目分类已存在");
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
        updateFullQuestionArchive(draggingNode);
        List<QuestionArchive> rootQuestionArchive = getRootQuestionArchive();
        questionArchiveLevelUpdate(null, rootQuestionArchive, treeMoveEnum, draggingNode.getId(), dropNode.getId());
        return RestResponse.ok();
    }


    /**
     * 题目分类位置调整
     *
     * @param parent
     * @param questionArchiveList
     * @param treeMoveEnum
     * @param draggingNodeId
     * @param dropNodeId
     */
    private void questionArchiveLevelUpdate(QuestionArchive parent, List<QuestionArchive> questionArchiveList, TreeMoveEnum treeMoveEnum, Integer draggingNodeId, Integer dropNodeId) {
        QuestionArchive dropNode = questionArchiveList.stream()
                .filter(item -> item.getId().equals(dropNodeId))
                .findFirst().orElse(null);
        if (null != dropNode) {
            if (treeMoveEnum == TreeMoveEnum.Before || treeMoveEnum == TreeMoveEnum.After) {
                QuestionArchive draggingNode = questionArchiveList.stream()
                        .filter(item -> item.getId().equals(draggingNodeId))
                        .findFirst().get();
                questionArchiveList.remove(draggingNode);
                int dropIndex = questionArchiveList.indexOf(dropNode);
                if (treeMoveEnum == TreeMoveEnum.Before) {
                    questionArchiveList.add(dropIndex, draggingNode);
                } else if (treeMoveEnum == TreeMoveEnum.After) {
                    questionArchiveList.add(dropIndex + 1, draggingNode);
                }
            }
        }

        IntStream.range(0, questionArchiveList.size()).forEach(i -> {
            QuestionArchive item = questionArchiveList.get(i);
            item.setItemOrder((i + 1) * ExamUtil.ItemOrderInit);
            if (null == parent) {
                item.setLevel(String.format("/%s/", item.getName()));
            } else {
                item.setLevel(String.format("%s%s/", parent.getLevel(), item.getName()));
            }
            updateFullQuestionArchive(item);
            List<QuestionArchive> questionArchiveChild = getQuestionArchiveByParentId(item.getId());
            if (questionArchiveChild.size() > 0) {
                questionArchiveLevelUpdate(item, questionArchiveChild, treeMoveEnum, draggingNodeId, dropNodeId);
            }
        });
    }


    /**
     * 题目分类更新
     *
     * @param questionArchive
     */
    private void updateFullQuestionArchive(QuestionArchive questionArchive) {
        LambdaUpdateWrapper<QuestionArchive> updateWrapper = new LambdaUpdateWrapper<QuestionArchive>()
                .eq(QuestionArchive::getId, questionArchive.getId());
        if (null == questionArchive.getParentId()) {
            updateWrapper.set(QuestionArchive::getParentId, null);
        }
        questionArchiveMapper.update(questionArchive, updateWrapper);
    }
}
