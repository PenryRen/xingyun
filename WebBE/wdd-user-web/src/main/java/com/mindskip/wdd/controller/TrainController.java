package com.mindskip.wdd.controller;

import com.github.pagehelper.PageInfo;
import com.mindskip.wdd.base.BaseApiController;
import com.mindskip.wdd.base.RestResponse;
import com.mindskip.wdd.domain.*;
import com.mindskip.wdd.domain.enums.ExamPaperAnswerStatusEnum;
import com.mindskip.wdd.domain.enums.TrainStatusEnum;
import com.mindskip.wdd.domain.enums.TrainTargetTypeEnum;
import com.mindskip.wdd.mapping.TrainMapping;
import com.mindskip.wdd.service.CredentialService;
import com.mindskip.wdd.service.TrainArchiveService;
import com.mindskip.wdd.service.TrainExamPaperService;
import com.mindskip.wdd.service.TrainService;
import com.mindskip.wdd.utility.DateTimeUtil;
import com.mindskip.wdd.utility.ExamUtil;
import com.mindskip.wdd.utility.PageInfoHelper;
import com.mindskip.wdd.viewmodel.train.*;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @version 9.0.0
 * @description: 培训中心
 * Copyright (C), 2025, 麟航团队
 * @date 2025/9/16 10:45
 */
@AllArgsConstructor
@RestController
@RequestMapping(value = "/api/train")
public class TrainController extends BaseApiController {

    private static final Logger logger = LoggerFactory.getLogger(TrainController.class);
    private final TrainArchiveService trainArchiveService;
    private final TrainService trainService;
    private final TrainExamPaperService trainExamPaperService;
    private final CredentialService credentialService;
    private final TrainMapping trainMapping;

    /**
     * 课程分类
     *
     * @return {@link RestResponse}<{@link List}<{@link TrainArchiveVM}>>
     */
    @PostMapping("/archive/list")
    public RestResponse<List<TrainArchiveVM>> list() {
        List<TrainArchive> rootTree = trainArchiveService.selectRootTree();
        List<TrainArchiveVM> rootVM = trainMapping.toTrainArchiveVMList(rootTree);
        return RestResponse.ok(rootVM);
    }

    /**
     * 课程分页
     *
     * @param model the model
     * @return the rest response
     */
    @PostMapping("/page")
    public RestResponse<PageInfo<TrainPageResponseVM>> page(@RequestBody TrainPageRequestVM model) {
        User user = getCurrentUser();
        model.setDepartmentId(user.getDepartmentId());
        model.setNow(new Date());
        if (model.getTrainArchiveId() != null) {
            TrainArchive rootTrainArchive = trainArchiveService.getTrainArchiveById(model.getTrainArchiveId());
            List<TrainArchive> trainArchiveList = trainArchiveService.getTrainArchiveByLevel(rootTrainArchive.getLevel());
            List<Integer> trainArchiveIdList = trainArchiveList.stream().map(item -> item.getId())
                    .collect(Collectors.toList());
            model.setTrainArchiveIdList(trainArchiveIdList);
        }
        PageInfo<Train> pageInfo = trainService.page(model);
        PageInfo<TrainPageResponseVM> page = PageInfoHelper.copyMap(pageInfo, d -> trainMapping.toTrainPageResponseVM(d));
        return RestResponse.ok(page);
    }


    /**
     * 培训查询
     *
     * @param id
     * @return {@link RestResponse}
     */
    @PostMapping("/select/{id}")
    public RestResponse select(@PathVariable Integer id) {
        Train train = trainService.getById(id);
        if (null == train || train.getDeleted()) {
            return RestResponse.fail(2, "课程未找到！");
        }
        User user = getCurrentUser();
        TrainUser trainUser = trainService.selectTrainUser(id, user.getId());
        List<TrainItemUser> trainItemUserList = null == trainUser ? new ArrayList<>() : trainService.getTrainItemUserList(trainUser.getId());
        TrainDetailVM trainDetailVM = trainMapping.toTrainDetailVM(train);
        List<TrainItem> trainItemList = trainService.getTrainItemList(id);
        List<TrainDetailItem> courseWareList = trainItemList.stream()
                .filter(item -> item.getTargetType().equals(TrainTargetTypeEnum.CourseWare.getCode()))
                .map(item -> {
                    TrainDetailItem trainDetailItem = trainMapping.toTrainDetailItem(item);
                    trainDetailItem.setVmType(item.getVmType());
                    trainDetailItem.setCurrentNumber(0);
                    trainDetailItem.setPercentage(0);
                    trainDetailItem.setCurrentNumberStr("0秒");
                    trainDetailItem.setPassNumberStr(DateTimeUtil.secondToChineseTime(item.getPassNumber()));
                    trainItemUserList.stream()
                            .filter(uItem -> uItem.getTrainItemId().equals(item.getId()))
                            .findFirst().ifPresent(uItem -> {
                                trainDetailItem.setTrainUserItemId(uItem.getId());
                                trainDetailItem.setCurrentNumber(uItem.getCurrentNumber());
                                trainDetailItem.setCurrentNumberStr(DateTimeUtil.secondToChineseTime(uItem.getCurrentNumber()));
                                trainDetailItem.setStatus(uItem.getStatus());
                                if (null != uItem.getStatus() && uItem.getStatus().equals(TrainStatusEnum.Pass.getCode())) {
                                    trainDetailItem.setPercentage(100);
                                } else {
                                    trainDetailItem.setPercentage(uItem.getCurrentNumber() * 100 / uItem.getPassNumber());
                                }
                            });
                    return trainDetailItem;
                })
                .collect(Collectors.toList());
        trainDetailVM.setCourseWareList(courseWareList);

        TrainItem examPaperTrainItem = trainItemList.stream()
                .filter(item -> item.getTargetType().equals(TrainTargetTypeEnum.ExamPaper.getCode()))
                .findFirst().orElse(null);
        if (null != examPaperTrainItem) {
            TrainExamPaper trainExamPaper = trainExamPaperService.getById(examPaperTrainItem.getTargetId());
            TrainDetailItem examPaperItem = trainMapping.toTrainDetailItem(examPaperTrainItem);
            examPaperItem.setQuestionCount(trainExamPaper.getQuestionCount());
            examPaperItem.setSuggestTimeStr(ExamUtil.minToVM(trainExamPaper.getSuggestTime()));
            examPaperItem.setQuestionCount(trainExamPaper.getQuestionCount());
            examPaperItem.setPassNumberStr(ExamUtil.scoreToVM(examPaperTrainItem.getPassNumber()));
            examPaperItem.setMaxNumberStr(ExamUtil.scoreToVM(examPaperTrainItem.getMaxNumber()));
            trainItemUserList.stream()
                    .filter(uItem -> uItem.getTrainItemId().equals(examPaperTrainItem.getId()))
                    .findFirst().ifPresent(uItem -> {
                        examPaperItem.setTrainUserItemId(uItem.getId());
                        examPaperItem.setCurrentNumber(uItem.getCurrentNumber());
                        examPaperItem.setCurrentNumberStr(ExamUtil.scoreToVM(uItem.getCurrentNumber()));
                        examPaperItem.setStatus(uItem.getStatus());
                        examPaperItem.setStatusStr(TrainStatusEnum.fromCode(uItem.getStatus()).getName());
                        if (null != uItem.getCompleteTime()) {
                            examPaperItem.setCompleteTime(DateTimeUtil.dateTimeFullFormat(uItem.getCompleteTime()));
                        }

                        //考试记录列表
                        List<TrainExamPaperAnswer> trainAnswerList = trainExamPaperService.getPaperAnswer(uItem.getId());
                        List<TrainExamPaperAnswerVM> answerList = trainAnswerList.stream()
                                .map(pItem -> {
                                    TrainExamPaperAnswerVM trainExamPaperAnswerVM = trainMapping.toTrainExamPaperAnswerVM(pItem);
                                    if (null != pItem.getStatus()) {
                                        trainExamPaperAnswerVM.setStatusStr(ExamPaperAnswerStatusEnum.fromCode(pItem.getStatus()).getName());
                                    }
                                    if (null != pItem.getPassed()) {
                                        trainExamPaperAnswerVM.setPassedStr(pItem.getPassed() ? "是" : "否");
                                    }
                                    return trainExamPaperAnswerVM;
                                })
                                .collect(Collectors.toList());
                        examPaperItem.setAnswerList(answerList);
                    });
            trainDetailVM.setExamPaper(examPaperItem);
        }

        TrainItem credentialTrainItem = trainItemList.stream()
                .filter(item -> item.getTargetType().equals(TrainTargetTypeEnum.Credential.getCode()))
                .findFirst().orElse(null);
        if (null != credentialTrainItem) {
            trainItemUserList.stream()
                    .filter(uItem -> uItem.getTrainItemId().equals(credentialTrainItem.getId()) && uItem.getStatus().equals(TrainStatusEnum.Pass.getCode()))
                    .findFirst().ifPresent(uItem -> {
                        UserCredential userCredential = credentialService.trainCredential(trainUser.getId());
                        TrainDetailItem credentialItem = new TrainDetailItem();
                        credentialItem.setCredentialImagePath(userCredential.getCredentialImagePath());
                        if (null == userCredential.getCredentialImagePath()) {
                            try {
                                String credentialImagePath = credentialService.buildCredential(userCredential);
                                credentialItem.setCredentialImagePath(credentialImagePath);
                            } catch (IOException e) {
                                logger.error(e.getMessage(), e);
                            }
                        }
                        trainDetailVM.setCredential(credentialItem);
                    });
        }
        return RestResponse.ok(trainDetailVM);
    }


    /**
     * 培训开始
     *
     * @param id
     * @return {@link RestResponse}
     */
    @PostMapping("/start/{id}")
    public RestResponse start(@PathVariable Integer id) {
        Train train = trainService.getById(id);
        if (null == train || train.getDeleted()) {
            return RestResponse.fail(2, "课程未找到！");
        }
        User user = getCurrentUser();
        Date now = new Date();
        List<TrainItem> trainItemList = trainService.getTrainItemList(id);
        TrainUser existTrainUser = trainService.selectTrainUser(id, user.getId());
        if (null == existTrainUser) {  //未领取培训列表
            TrainUser trainUser = new TrainUser();
            trainUser.setTrainId(train.getId());
            trainUser.setPassCount(0);
            trainUser.setItemCount(train.getItemCount());
            trainUser.setCreateUser(user.getId());
            trainUser.setCreateTime(now);
            trainUser.setCreateDepartmentId(user.getDepartmentId());
            trainUser.setStatus(TrainStatusEnum.Going.getCode());
            trainUser.setDeleted(false);
            trainService.insertTrainUser(trainUser);
            List<TrainItemUser> trainItemUserAddList = trainItemList.stream().map(item -> {
                TrainItemUser trainItemUser = new TrainItemUser();
                trainItemUser.setTrainUserId(trainUser.getId());
                trainItemUser.setTrainId(train.getId());
                trainItemUser.setTrainItemId(item.getId());
                trainItemUser.setTargetId(item.getTargetId());
                trainItemUser.setTargetType(item.getTargetType());
                trainItemUser.setCurrentNumber(0);
                trainItemUser.setPassNumber(item.getPassNumber());
                trainItemUser.setMaxNumber(item.getMaxNumber());
                trainItemUser.setAllowCount(item.getAllowCount());
                trainItemUser.setItemOrder(item.getItemOrder());
                trainItemUser.setCreateUser(user.getId());
                trainItemUser.setCreateDepartmentId(user.getDepartmentId());
                trainItemUser.setCreateTime(now);
                trainItemUser.setStatus(TrainStatusEnum.Going.getCode());
                trainItemUser.setDeleted(false);
                return trainItemUser;
            }).collect(Collectors.toList());
            trainService.insertTrainItemUserList(trainItemUserAddList);
        } else {   //培训列表更新
            List<TrainItemUser> oldTrainItemUserList = trainService.getTrainItemUserList(existTrainUser.getId());
            trainService.clearTrainItemUser(existTrainUser.getId());
            List<TrainItemUser> addList = new ArrayList<>();
            List<TrainItemUser> updateList = new ArrayList<>();
            trainItemList.stream().forEach(trainItem -> {
                TrainItemUser existTrainItemUser = oldTrainItemUserList.stream()
                        .filter(trainItemUser -> trainItemUser.getTrainItemId().equals(trainItem.getId()))
                        .findFirst().orElse(null);
                if (null != existTrainItemUser) {
                    existTrainItemUser.setItemOrder(trainItem.getItemOrder());
                    existTrainItemUser.setDeleted(false);
                    updateList.add(existTrainItemUser);
                } else {
                    TrainItemUser newTrainItemUser = new TrainItemUser();
                    newTrainItemUser.setTrainUserId(existTrainUser.getId());
                    newTrainItemUser.setTrainId(train.getId());
                    newTrainItemUser.setTrainItemId(trainItem.getId());
                    newTrainItemUser.setTargetId(trainItem.getTargetId());
                    newTrainItemUser.setTargetType(trainItem.getTargetType());
                    newTrainItemUser.setCurrentNumber(0);
                    newTrainItemUser.setPassNumber(trainItem.getPassNumber());
                    newTrainItemUser.setMaxNumber(trainItem.getMaxNumber());
                    newTrainItemUser.setAllowCount(trainItem.getAllowCount());
                    newTrainItemUser.setItemOrder(trainItem.getItemOrder());
                    newTrainItemUser.setCreateUser(user.getId());
                    newTrainItemUser.setCreateDepartmentId(user.getDepartmentId());
                    newTrainItemUser.setCreateTime(now);
                    newTrainItemUser.setStatus(TrainStatusEnum.Going.getCode());
                    newTrainItemUser.setDeleted(false);
                    addList.add(newTrainItemUser);
                }
            });
            if (addList.size() > 0) {
                trainService.insertTrainItemUserList(addList);
            }
            if (updateList.size() > 0) {
                trainService.updateTrainItemUserList(updateList);
            }
        }
        return select(id);
    }
}
