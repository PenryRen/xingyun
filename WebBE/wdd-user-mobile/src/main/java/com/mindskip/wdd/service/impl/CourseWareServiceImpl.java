package com.mindskip.wdd.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mindskip.wdd.domain.CourseWare;
import com.mindskip.wdd.domain.CourseWareQuestion;
import com.mindskip.wdd.domain.CourseWareWatch;
import com.mindskip.wdd.domain.CourseWareWatchDetail;
import com.mindskip.wdd.domain.frame.QuestionAnswerFrame;
import com.mindskip.wdd.domain.frame.QuestionFrame;
import com.mindskip.wdd.repository.*;
import com.mindskip.wdd.service.CourseWareService;
import com.mindskip.wdd.service.ExamPaperService;
import com.mindskip.wdd.utility.ExamUtil;
import com.mindskip.wdd.viewmodel.course.ware.CourseWareQuestionVM;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @version 1.7.0
 * @description: 课件
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
 */
@Service
@AllArgsConstructor
public class CourseWareServiceImpl extends ServiceImpl<CourseWareMapper, CourseWare> implements CourseWareService {

    private final Integer watchInterval = 60;
    private final CourseWareWatchMapper courseWareWatchMapper;
    private final CourseWareWatchDetailMapper courseWareWatchDetailMapper;
    private final CourseWareQuestionMapper courseWareQuestionMapper;
    private final QuestionJsonMapper questionJsonMapper;
    private final ExamPaperService examPaperService;


    @Override
    public void courseWareWatchInsert(CourseWareWatch courseWareWatch) {
        courseWareWatchMapper.insert(courseWareWatch);
    }

    @Override
    public void courseWareWatchUpdate(CourseWareWatch courseWareWatch) {
        courseWareWatchMapper.updateById(courseWareWatch);
    }

    @Override
    public CourseWareWatch getCourseWareWatch(Integer userId, Integer courseWareId) {
        return courseWareWatchMapper.getCourseWareWatch(userId, courseWareId);
    }

    @Override
    public void courseWareWatchDetailInsert(CourseWareWatchDetail courseWareWatchDetail) {
        courseWareWatchDetailMapper.insert(courseWareWatchDetail);
    }

    @Override
    public CourseWareWatchDetail getLastItem(Integer userId, Integer courseWareId) {
        return courseWareWatchDetailMapper.getLastItem(userId, courseWareId);
    }

    @Override
    public Integer getWatchInterval() {
        return watchInterval;
    }


    @Override
    public List<CourseWareQuestionVM> getCourseWareQuestion(Integer courseWareId) {
        List<CourseWareQuestion> courseWareQuestionList = courseWareQuestionMapper.getCourseWareQuestion(courseWareId);
        List<String> questionFrameIdList = courseWareQuestionList.stream()
                .map(cq -> cq.getQuestionFrameId())
                .collect(Collectors.toList());
        List<QuestionFrame> questionFrameList = questionFrameIdList.size() > 0
                ? questionJsonMapper.selectBatchIds(questionFrameIdList).stream().map(d -> d.getContent()).collect(Collectors.toList())
                : new ArrayList<>(0);
        List<CourseWareQuestionVM> courseWareQuestionVMList = new ArrayList<>(courseWareQuestionList.size());
        for (int i = 1; i <= courseWareQuestionList.size(); i++) {
            CourseWareQuestion item = courseWareQuestionList.get(i - 1);
            CourseWareQuestionVM courseWareQuestionVM = new CourseWareQuestionVM();
            courseWareQuestionVM.setAnchorSecond(item.getAnchorSecond());
            QuestionFrame questionFrame = questionFrameList.stream()
                    .filter(qf -> qf.getQuestionId().equals(item.getQuestionId()))
                    .findFirst().get();
            questionFrame.setTrickScore(ExamUtil.scoreToVM(questionFrame.getScore()));
            questionFrame.setQuestionId(item.getQuestionId());
            questionFrame.setItemOrder(i);
            QuestionAnswerFrame questionAnswerFrame = examPaperService.questionFrameToAnswer(questionFrame, true);
            courseWareQuestionVM.setQuestionFrame(questionFrame);
            courseWareQuestionVM.setQuestionAnswerFrame(questionAnswerFrame);
            courseWareQuestionVMList.add(courseWareQuestionVM);
        }
        return courseWareQuestionVMList;
    }

    @Override
    public Integer watchUserSum(Integer courseWareId, Integer userId, Date startTime) {
        return courseWareWatchDetailMapper.watchUserSum(courseWareId, userId, startTime);
    }
}
