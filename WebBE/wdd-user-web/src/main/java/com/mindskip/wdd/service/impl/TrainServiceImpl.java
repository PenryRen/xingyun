package com.mindskip.wdd.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mindskip.wdd.domain.*;
import com.mindskip.wdd.domain.enums.TrainStatusEnum;
import com.mindskip.wdd.domain.enums.TrainTargetTypeEnum;
import com.mindskip.wdd.repository.*;
import com.mindskip.wdd.service.TrainService;
import com.mindskip.wdd.viewmodel.train.TrainPageRequestVM;
import com.mindskip.wdd.viewmodel.user.train.UserTrainPageRequestVM;
import com.mindskip.wdd.viewmodel.user.train.UserTrainPageResponseVM;
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
    private final TrainItemMapper trainItemMapper;

    private final TrainUserMapper trainUserMapper;

    private final TrainItemUserMapper trainItemUserMapper;

    private final UserCredentialMapper userCredentialMapper;

    private final TrainExamPaperAnswerMapper trainExamPaperAnswerMapper;


    @Override
    public PageInfo<Train> page(TrainPageRequestVM requestVM) {
        return PageHelper.startPage(requestVM.getPageIndex(), requestVM.getPageSize(), "item_order desc , id desc").doSelectPageInfo(() ->
                trainMapper.page(requestVM)
        );
    }

    @Override
    public List<TrainItem> getTrainItemList(Integer id) {
        return trainItemMapper.getTrainItemList(id);
    }

    @Override
    public TrainUser selectTrainUser(Integer trainId, Integer userId) {
        return trainUserMapper.selectTrainUser(trainId, userId);
    }

    @Override
    public List<TrainItemUser> getTrainItemUserList(Long trainUserId) {
        return trainItemUserMapper.getTrainItemUserList(trainUserId);
    }

    @Override
    public TrainItemUser getTrainItemUser(Long trainUserId, Integer userId, Integer status) {
        return trainItemUserMapper.getTrainItemUser(trainUserId, userId, status);
    }


    @Override
    public int trainCourseWareGoingCount(Integer trainId, Integer userId) {
        return trainItemUserMapper.trainCourseWareGoingCount(trainId, userId);
    }

    @Override
    public int updateTrainItemUser(TrainItemUser trainItemUser) {
        return trainItemUserMapper.updateById(trainItemUser);
    }

    @Override
    public int insertTrainUser(TrainUser trainUser) {
        return trainUserMapper.insert(trainUser);
    }

    @Override
    public int insertTrainItemUserList(List<TrainItemUser> trainItemUserList) {
        return trainItemUserMapper.insertList(trainItemUserList);
    }

    @Override
    public int updateTrainItemUserList(List<TrainItemUser> trainItemUserList) {
        return trainItemUserMapper.updateList(trainItemUserList);
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


    @Override
    public void trainComplete(TrainItemUser trainItemUser) {
        TrainUser trainUser = selectTrainUser(trainItemUser.getTrainId(), trainItemUser.getCreateUser());
        List<TrainItemUser> trainItemUserList = getTrainItemUserList(trainUser.getId());
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


    @Override
    public PageInfo<UserTrainPageResponseVM> userTrainPage(UserTrainPageRequestVM requestVM) {
        return PageHelper.startPage(requestVM.getPageIndex(), requestVM.getPageSize(), "id desc").doSelectPageInfo(() ->
                trainUserMapper.userTrainPage(requestVM)
        );
    }

    @Override
    public void clearTrainItemUser(Long trainUserId) {
        trainItemUserMapper.clearTrainItemUser(trainUserId);
    }



    /**
     * 培训证书完成更新
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
