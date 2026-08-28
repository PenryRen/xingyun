package com.mindskip.wdd.service.impl;


import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.mindskip.wdd.configuration.spring.cache.CacheConfig;
import com.mindskip.wdd.domain.User;
import com.mindskip.wdd.domain.other.PaperSession;
import com.mindskip.wdd.service.PaperSessionService;
import com.mindskip.wdd.service.enums.SessionEnum;
import com.mindskip.wdd.viewmodel.exam.paper.ExamPaperDoPaperVM;
import com.mindskip.wdd.viewmodel.exam.paper.ExamPaperDoResponseVM;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @version 1.7.0
 * @description: 试卷缓存信息
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
@Service
@RequiredArgsConstructor
public class PaperSessionServiceImpl implements PaperSessionService {

    private final static Integer DELAY = 3;
    private final static String Face_CACHE_NAME = "ueit:paper:face";
    private final static String Session_CACHE_NAME = "ueit:paper:session";
    private final static String Train_Session_CACHE_NAME = "ueit:train:paper:session";
    private final RedisTemplate<String, Object> redisTemplate;
    private final CacheConfig cacheConfig;


    @Override
    public void initPaperSession(ExamPaperDoResponseVM examPaperDoResponseVM, User user, SessionEnum sessionEnum) {
        String cacheKey = cacheKeyByEnum(sessionEnum);
        ExamPaperDoPaperVM examPaperDoPaperVM = examPaperDoResponseVM.getPaper();
        String key = cacheConfig.simpleKeyGenerator(cacheKey, String.format("%d_%d", examPaperDoPaperVM.getId(), user.getId()));
        PaperSession paperSession = (PaperSession) redisTemplate.opsForValue().get(key);
        Integer doSecond = 0;
        Integer totalSecond = examPaperDoPaperVM.getSuggestTime() * 60;
        if (null == paperSession) {
            paperSession = new PaperSession();
            paperSession.setCheatCount(0);
            paperSession.setRemainTime(totalSecond);
            paperSession.setTotalTime(totalSecond);
            paperSession.setStartTime(new Date());

            redisTemplate.opsForValue().setIfAbsent(key, paperSession, paperSession.getRemainTime() + DELAY, TimeUnit.SECONDS);
        } else {
            doSecond = (int) DateUtil.between(paperSession.getStartTime(), new Date(), DateUnit.SECOND);
            int newRemainTime = totalSecond - doSecond;
            paperSession.setRemainTime(newRemainTime < 0 ? 0 : newRemainTime);
            if (paperSession.getRemainTime() > 0) {
                redisTemplate.opsForValue().setIfPresent(key, paperSession, paperSession.getRemainTime() + DELAY, TimeUnit.SECONDS);
            }
        }
        examPaperDoResponseVM.getAnswer().setDoTime(doSecond);
        examPaperDoResponseVM.setCheatCount(paperSession.getCheatCount());
        examPaperDoResponseVM.setRemainTime(paperSession.getRemainTime());
    }

    @Override
    public void clearPaperSession(Long examPaperId, User user, SessionEnum sessionEnum) {
        String cacheKey = cacheKeyByEnum(sessionEnum);
        String key = cacheConfig.simpleKeyGenerator(cacheKey, String.format("%d_%d", examPaperId, user.getId()));
        redisTemplate.delete(key);
    }


    @Override
    public void incrementCheat(Long examPaperId, User user, SessionEnum sessionEnum) {
        String cacheKey = cacheKeyByEnum(sessionEnum);
        String key = cacheConfig.simpleKeyGenerator(cacheKey, String.format("%d_%d", examPaperId, user.getId()));
        PaperSession paperSession = (PaperSession) redisTemplate.opsForValue().get(key);
        if (null != paperSession) {
            Integer totalSecond = paperSession.getTotalTime();
            int doSecond = (int) DateUtil.between(paperSession.getStartTime(), new Date(), DateUnit.SECOND);
            int newRemainTime = totalSecond - doSecond;
            paperSession.setCheatCount(paperSession.getCheatCount() + 1);
            paperSession.setRemainTime(newRemainTime < 0 ? 0 : newRemainTime);
            if (paperSession.getRemainTime() > 0) {
                //新增5秒，防止网络延迟
                redisTemplate.opsForValue().setIfPresent(key, paperSession, paperSession.getRemainTime() + DELAY, TimeUnit.SECONDS);
            }
        }
    }

    @Override
    public PaperSession getPaperSession(Long examPaperId, User user, SessionEnum sessionEnum) {
        String cacheKey = cacheKeyByEnum(sessionEnum);
        String key = cacheConfig.simpleKeyGenerator(cacheKey, String.format("%d_%d", examPaperId, user.getId()));
        return (PaperSession) redisTemplate.opsForValue().get(key);
    }

    @Override
    public void paperFace(Long examPaperId, Integer userId) {
        String key = cacheConfig.simpleKeyGenerator(Face_CACHE_NAME, String.format("%d_%d", examPaperId, userId));
        redisTemplate.opsForValue().setIfAbsent(key, true, 10, TimeUnit.MINUTES);
    }

    @Override
    public Boolean getPaperFace(Long examPaperId, Integer userId) {
        String key = cacheConfig.simpleKeyGenerator(Face_CACHE_NAME, String.format("%d_%d", examPaperId, userId));
        return (Boolean) redisTemplate.opsForValue().get(key);
    }


    /**
     * 根据枚举获取缓存key
     *
     * @param sessionEnum
     * @return {@link String}
     */
    private String cacheKeyByEnum(SessionEnum sessionEnum) {
        switch (sessionEnum) {
            case Paper:
                return Session_CACHE_NAME;
            case TrainPaper:
                return Train_Session_CACHE_NAME;

        }
        return null;
    }

}
