package com.mindskip.wdd.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mindskip.wdd.domain.*;
import com.mindskip.wdd.domain.enums.TrainStatusEnum;
import com.mindskip.wdd.domain.enums.TrainTargetTypeEnum;
import com.mindskip.wdd.repository.*;
import com.mindskip.wdd.service.TrainService;
import com.mindskip.wdd.viewmodel.train.course.TrainCoursePageRequestVM;
import com.mindskip.wdd.viewmodel.train.course.TrainCourseWarePageResponseVM;
import com.mindskip.wdd.viewmodel.train.detail.TrainPageRequestVM;
import com.mindskip.wdd.viewmodel.train.detail.TrainPageResponseVM;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @version 1.7.0
 * @description: 课程
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
@Service
@AllArgsConstructor
public class TrainServiceImpl extends ServiceImpl<TrainMapper, Train> implements TrainService {

    private final TrainMapper trainMapper;
    private final TrainUserMapper trainUserMapper;
    private final TrainItemMapper trainItemMapper;
    private final TrainItemUserMapper trainItemUserMapper;
    private final TrainDepartmentMapper trainDepartmentMapper;
    private final TrainExamPaperAnswerMapper trainExamPaperAnswerMapper;
    private final UserCredentialMapper userCredentialMapper;

    @Override
    public PageInfo<Train> page(TrainCoursePageRequestVM requestVM) {
        return PageHelper.startPage(requestVM.getPageIndex(), requestVM.getPageSize(), "id desc").doSelectPageInfo(() ->
                trainMapper.page(requestVM)
        );
    }

    @Override
    public List<TrainItem> getTrainItemList(Integer trainId) {
        return trainItemMapper.getTrainItemList(trainId);
    }

    @Override
    public void clearTrainItem(Integer trainId) {
        trainItemMapper.clearTrainItem(trainId);
    }

    @Override
    public int insertTrainItem(TrainItem trainItem) {
        return trainItemMapper.insert(trainItem);
    }

    @Override
    public int updateTrainItem(TrainItem trainItem) {
        return trainItemMapper.updateById(trainItem);
    }

    @Override
    public List<TrainDepartment> getTrainDepartmentList(Integer trainId) {
        return trainDepartmentMapper.getTrainDepartmentList(trainId);
    }

    @Override
    public void clearTrainDepartment(Integer trainId) {
        trainDepartmentMapper.clearTrainDepartment(trainId);
    }

    @Override
    public int insertTrainDepartment(TrainDepartment trainDepartment) {
        return trainDepartmentMapper.insert(trainDepartment);
    }

    @Override
    public int trainUserStatusCount(Integer trainId, Integer status) {
        return trainUserMapper.trainStatusCount(trainId, status);
    }

    @Override
    public int trainItemUserStatusCount(Integer trainId, Integer targetType, Integer status) {
        return trainItemUserMapper.trainItemStatusCount(trainId, targetType, status);
    }


    @Override
    public PageInfo<TrainPageResponseVM> userPage(TrainPageRequestVM requestVM) {
        return PageHelper.startPage(requestVM.getPageIndex(), requestVM.getPageSize(), "id desc").doSelectPageInfo(() ->
                trainUserMapper.userPage(requestVM)
        );
    }

    @Override
    public PageInfo<TrainCourseWarePageResponseVM> userCourseWarePage(TrainPageRequestVM requestVM) {
        return PageHelper.startPage(requestVM.getPageIndex(), requestVM.getPageSize(), "id desc").doSelectPageInfo(() ->
                trainItemUserMapper.courseWarePage(requestVM)
        );
    }

    @Override
    public TrainItemUser trainItemUserById(Long id) {
        return trainItemUserMapper.selectById(id);
    }

    @Override
    public void paperTrainComplete(TrainItemUser trainItemUser) {
        TrainStatusEnum trainStatusEnum = TrainStatusEnum.fromCode(trainItemUser.getStatus());
        if (trainStatusEnum == TrainStatusEnum.Pass) {
            trainItemUser.setCompleteTime(new Date());
            trainItemUserMapper.updateById(trainItemUser);
            this.trainComplete(trainItemUser);
        } else {
            if (trainItemUser.getAllowCount() != null && trainItemUser.getAllowCount() > 0) {
                Integer doCount = trainExamPaperAnswerMapper.paperDoCount(trainItemUser.getId());
                if (doCount >= trainItemUser.getAllowCount()) {
                    trainItemUser.setCompleteTime(new Date());
                    trainItemUserMapper.updateById(trainItemUser);
                    this.trainComplete(trainItemUser);
                }
            }
        }
    }


    /**
     * 检测培训是否合格，并更新状态
     *
     * @param trainItemUser
     */
    private void trainComplete(TrainItemUser trainItemUser) {
        TrainUser trainUser = trainUserMapper.selectTrainUser(trainItemUser.getTrainId(), trainItemUser.getCreateUser());
        List<TrainItemUser> trainItemUserList = trainItemUserMapper.getTrainItemUserList(trainUser.getId());
        List<TrainItemUser> courseItemUserList = trainItemUserList.stream()
                .filter(item -> item.getTargetType().equals(TrainTargetTypeEnum.CourseWare.getCode()))
                .collect(Collectors.toList());
        for (TrainItemUser courseItem : courseItemUserList) {
            if (courseItem.getStatus().equals(TrainStatusEnum.Going.getCode())) {
                return;
            }
        }
        trainUser.setStatus(TrainStatusEnum.Pass.getCode());

        TrainItemUser paperUserItem = trainItemUserList.stream()
                .filter(item -> item.getTargetType().equals(TrainTargetTypeEnum.ExamPaper.getCode()))
                .findFirst().orElse(null);
        TrainItemUser credentialUserItem = trainItemUserList.stream()
                .filter(item -> item.getTargetType().equals(TrainTargetTypeEnum.Credential.getCode()))
                .findFirst().orElse(null);
        if (null != paperUserItem) {
            if (null != paperUserItem.getStatus()) {
                trainUser.setStatus(paperUserItem.getStatus());
                if (null != credentialUserItem && paperUserItem.getStatus().equals(TrainStatusEnum.Pass.getCode())) {
                    this.credentialTrainComplete(credentialUserItem);
                }
            } else {
                return;
            }
        } else {
            if (null != credentialUserItem) {
                this.credentialTrainComplete(credentialUserItem);
            }
        }

        trainUser.setCompleteTime(new Date());
        trainUserMapper.updateById(trainUser);
    }


    /**
     * 检测培训证书是否颁发
     *
     * @param credentialTrainItemUser
     */
    private void credentialTrainComplete(TrainItemUser credentialTrainItemUser) {
        Date now = new Date();
        Train train = trainMapper.selectById(credentialTrainItemUser.getTrainId());
        UserCredential userCredential = new UserCredential();
        userCredential.setTrainName(train.getName());
        userCredential.setDeleted(false);
        userCredential.setCreateTime(now);
        userCredential.setCredentialBuildTime(now);
        userCredential.setUserId(credentialTrainItemUser.getCreateUser());
        userCredential.setCreateDepartmentId(credentialTrainItemUser.getCreateDepartmentId());
        userCredential.setCredentialTemplateId(credentialTrainItemUser.getTargetId());
        userCredential.setTrainId(train.getId());
        userCredential.setTrainUserId(credentialTrainItemUser.getTrainUserId());
        userCredentialMapper.insert(userCredential);

        //更新证书培训
        credentialTrainItemUser.setUserTargetId(userCredential.getId());
        credentialTrainItemUser.setStatus(TrainStatusEnum.Pass.getCode());
        credentialTrainItemUser.setCompleteTime(now);
        trainItemUserMapper.updateById(credentialTrainItemUser);
    }

}
