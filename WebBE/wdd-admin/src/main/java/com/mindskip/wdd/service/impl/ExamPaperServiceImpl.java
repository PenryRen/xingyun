package com.mindskip.wdd.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mindskip.wdd.configuration.spring.cache.CacheConfig;
import com.mindskip.wdd.domain.*;
import com.mindskip.wdd.domain.enums.ExamPaperTypeEnum;
import com.mindskip.wdd.domain.frame.ExamPaperBuildConfig;
import com.mindskip.wdd.domain.frame.ExamPaperFrame;
import com.mindskip.wdd.domain.frame.QuestionFrame;
import com.mindskip.wdd.mapping.ExamPaperMapping;
import com.mindskip.wdd.repository.*;
import com.mindskip.wdd.service.ExamPaperService;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @version 1.7.0
 * @description: 试卷
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
@Service
@AllArgsConstructor
public class ExamPaperServiceImpl extends ServiceImpl<ExamPaperMapper, ExamPaper> implements ExamPaperService {

    private final static String Session_CACHE_NAME = "ueit:paper:session";
    private final ExamPaperMapper examPaperMapper;
    private final ExamPaperChildMapper examPaperChildMapper;
    private final ExamPaperMapping examPaperMapping;
    private final ExamPaperJsonMapper examPaperJsonMapper;
    private final ExamPaperDepartmentMapper examPaperDepartmentMapper;
    private final ExamPaperUserMapper examPaperUserMapper;
    private final QuestionJsonMapper questionJsonMapper;
    private final RedisTemplate<String, Object> redisTemplate;
    private final CacheConfig cacheConfig;


    @Override
    public ExamPaper insertExamPaper(ExamPaperBuild examPaperBuild, ExamPaperFrame examPaperFrame, User createUser) {
        ExamPaperBuildConfig buildConfig = examPaperBuild.getBuildConfig();
        ExamPaper examPaper = examPaperMapping.toExamPaper(examPaperBuild);
        examPaper.setCheat(buildConfig.getCheat());
        examPaper.setMaxCheatCount(buildConfig.getMaxCheatCount());
        examPaper.setCapture(buildConfig.getCapture());
        examPaper.setFaceCheck(buildConfig.getFaceCheck());
        examPaper.setWatch(buildConfig.getWatch());
        examPaper.setQuestionMess(buildConfig.getQuestionMess());
        examPaper.setQuestionItemMess(buildConfig.getQuestionItemMess());
        examPaper.setCreateTime(new Date());
        examPaper.setCreateUser(createUser.getId());
        examPaper.setCreateDepartmentId(createUser.getDepartmentId());
        examPaper.setExamPaperBuildId(examPaperBuild.getId());
        if (null != examPaperFrame) {
            examPaperJsonMapper.insert(new ExamPaperJson(examPaperFrame));
            examPaper.setPaperFrameId(examPaperFrame.getId());
        }
        examPaper.setPaperType(examPaperBuild.getBuildType());
        examPaperMapper.insert(examPaper);

        //考试部门
        if (buildConfig.getDepartmentIdList().size() > 0) {
            examPaperDepartmentMapper.insertList(examPaper.getId(), buildConfig.getDepartmentIdList(), createUser.getId(), createUser.getDepartmentId());
        }

        //考试员工
        List<Integer> examPaperUserList = buildConfig.getExamPaperUserSelectList().stream().map(eu -> eu.getId()).collect(Collectors.toList());
        if (examPaperUserList.size() > 0) {
            examPaperUserMapper.insertList(examPaper.getId(), examPaperUserList, createUser.getId(), createUser.getDepartmentId());
        }
        examPaperBuild.setExamPaperId(examPaper.getId());
        return examPaper;
    }

    @Override
    public void insertExamPaperChild(ExamPaperChild examPaperChild, ExamPaperFrame examPaperFrame) {
        examPaperJsonMapper.insert(new ExamPaperJson(examPaperFrame));
        examPaperChild.setPaperFrameId(examPaperFrame.getId());
        examPaperChildMapper.insert(examPaperChild);
    }


    @Override
    public void deleteByExamPaperBuildId(Long examPaperBuildId) {
        examPaperMapper.deleteByExamPaperBuildId(examPaperBuildId);
    }


    @Override
    public void deleteChildByExamPaperBuildId(Long examPaperBuildId) {
        examPaperChildMapper.deleteByExamPaperBuildId(examPaperBuildId);
    }

    @Override
    public ExamPaperCache getExamPaperCache(Long paperId, Long childPaperId) {
        ExamPaper examPaper = examPaperMapper.selectById(paperId);
        if (null == examPaper) {
            return null;
        }
        String paperFrameId = null;
        ExamPaperCache examPaperCache = examPaperMapping.toExamPaperCache(examPaper);
        if (ExamPaperTypeEnum.RANDOM == ExamPaperTypeEnum.fromCode(examPaper.getPaperType())) {//随机组卷不需要缓存
            ExamPaperChild examPaperChild = examPaperChildMapper.selectById(childPaperId);
            paperFrameId = examPaperChild.getPaperFrameId();
            examPaperCache.setChildExamPaperId(examPaperChild.getId());
        } else {
            paperFrameId = examPaper.getPaperFrameId();
        }
        ExamPaperFrame examPaperFrame = examPaperJsonMapper.selectById(paperFrameId).getContent();
        examPaperCache.setExamPaperFrame(examPaperFrame);
        List<String> questionFrameIdList = examPaperFrame.getExamPaperItemFrames().stream()
                .flatMap(pt -> pt.getExamPaperItemQuestionFrames().stream()
                        .map(q -> q.getQuestionFrameId()))
                .collect(Collectors.toList());
        List<QuestionFrame> questionFrameList = questionFrameIdList.size() > 0
                ? questionJsonMapper.selectBatchIds(questionFrameIdList).stream().map(d -> d.getContent()).collect(Collectors.toList())
                : new ArrayList<>(0);
        examPaperCache.setQuestionFrameList(questionFrameList);
        examPaperCache.setAllowCache(false);
        return examPaperCache;
    }


    @Override
    public Integer paperAllUserCount(Long examPaperId) {
        return examPaperMapper.paperAllUserCount(examPaperId);
    }


    @Override
    public void clearPaperResit(Long examPaperId, Integer userId) {
        String key = cacheConfig.simpleKeyGenerator(Session_CACHE_NAME, String.format("%d_%d", examPaperId, userId));
        redisTemplate.delete(key);
        examPaperUserMapper.clearPaperResit(examPaperId, userId);
    }

    @Override
    public int insertExamPaperUser(ExamPaperUser examPaperUser) {
        return examPaperUserMapper.insert(examPaperUser);
    }
}
