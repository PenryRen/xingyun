package com.mindskip.wdd.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import com.github.pagehelper.PageInfo;
import com.mindskip.wdd.base.BaseApiController;
import com.mindskip.wdd.base.RestResponse;
import com.mindskip.wdd.base.SystemCode;
import com.mindskip.wdd.configuration.spring.interceptor.PreAuthorize;
import com.mindskip.wdd.domain.*;
import com.mindskip.wdd.domain.enums.ExamPaperAnswerStatusEnum;
import com.mindskip.wdd.domain.enums.TrainStatusEnum;
import com.mindskip.wdd.domain.enums.TrainTargetTypeEnum;
import com.mindskip.wdd.mapping.TrainMapping;
import com.mindskip.wdd.service.*;
import com.mindskip.wdd.utility.DateTimeUtil;
import com.mindskip.wdd.utility.ExamUtil;
import com.mindskip.wdd.utility.PageInfoHelper;
import com.mindskip.wdd.viewmodel.train.course.*;
import com.mindskip.wdd.viewmodel.train.detail.TrainDetailVM;
import com.mindskip.wdd.viewmodel.train.detail.TrainPageRequestVM;
import com.mindskip.wdd.viewmodel.train.detail.TrainPageResponseVM;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @version 1.7.0
 * @description: 培训接口
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
@RestController
@RequestMapping(value = "/api/train/course")
@AllArgsConstructor
public class TrainController extends BaseApiController {

    private final TrainService trainService;
    private final TrainArchiveService trainArchiveService;
    private final CourseWareService courseWareService;
    private final CredentialService credentialService;
    private final TrainMapping trainMapping;
    private final UserService userService;
    private final TrainExamPaperService trainExamPaperService;
    private final DepartmentService departmentService;
    private final FileUploadService fileUploadService;
    private final static Integer MAXSIZE = 3000;

    /**
     * 课程分页
     *
     * @param model the model
     * @return the rest response
     */
    @PostMapping("/page")
    @PreAuthorize("train:course:page")
    public RestResponse<PageInfo<TrainCoursePageResponseVM>> page(@RequestBody TrainCoursePageRequestVM model) {
        initPermission(model);
        PageInfo<Train> pageInfo = trainService.page(model);
        PageInfo<TrainCoursePageResponseVM> page = PageInfoHelper.copyMap(pageInfo, d -> {
            TrainCoursePageResponseVM trainCoursePageResponseVM = trainMapping.toTrainResponseVM(d);
            if (d.getTrainArchiveId() != null) {
                TrainArchive trainArchive = trainArchiveService.getById(d.getTrainArchiveId());
                trainCoursePageResponseVM.setLevel(trainArchive.getLevel());
            }
            return trainCoursePageResponseVM;
        });
        return RestResponse.ok(page);
    }


    /**
     * 课件查询
     *
     * @param id the id
     * @return the rest response
     */
    @PostMapping("/select/{id}")
    @PreAuthorize("train:course:update")
    public RestResponse<TrainEditRequestVM> select(@PathVariable Integer id) {
        Train train = trainService.getById(id);
        TrainEditRequestVM trainEditRequestVM = trainMapping.toTrainEditRequestVM(train);
        List<String> limitDateTime = Arrays.asList(DateTimeUtil.dateTimeFullFormat(train.getStartTime()), DateTimeUtil.dateTimeFullFormat(train.getEndTime()));
        trainEditRequestVM.setLimitDateTime(limitDateTime);
        List<TrainItem> trainItemList = trainService.getTrainItemList(id);

        List<TrainEditItem> courseWareItemList = trainItemList.stream().filter(item -> item.getTargetType().equals(TrainTargetTypeEnum.CourseWare.getCode())).map(item -> {
            TrainEditItem trainEditItem = trainMapping.toTrainEditItem(item);
            trainEditItem.setMaxNumberStr(DateTimeUtil.secondToChineseTime(trainEditItem.getMaxNumber()));
            trainEditItem.setPassNumberStr(DateTimeUtil.secondToTime(trainEditItem.getPassNumber()));
            CourseWare courseWare = courseWareService.getById(item.getTargetId());
            trainEditItem.setPreviewPath(courseWare.getPreviewPath());
            return trainEditItem;
        }).collect(Collectors.toList());
        trainEditRequestVM.setCourseWareItemList(courseWareItemList);

        TrainEditItem examPaperItem = trainItemList.stream().filter(item -> item.getTargetType().equals(TrainTargetTypeEnum.ExamPaper.getCode())).map(item -> {
            TrainEditItem trainEditItem = trainMapping.toTrainEditItem(item);
            trainEditItem.setMaxNumberStr(ExamUtil.scoreToVM(trainEditItem.getMaxNumber()));
            trainEditItem.setPassNumberStr(ExamUtil.scoreToVM(trainEditItem.getPassNumber()));
            return trainEditItem;
        }).findFirst().orElse(new TrainEditItem());
        trainEditRequestVM.setExamPaperItem(examPaperItem);

        TrainEditItem credentialItem = trainItemList.stream().filter(item -> item.getTargetType().equals(TrainTargetTypeEnum.Credential.getCode())).map(item -> {
            TrainEditItem trainEditItem = trainMapping.toTrainEditItem(item);
            CredentialTemplate credentialTemplate = credentialService.getCredentialTemplateById(item.getTargetId());
            trainEditItem.setTemplateImagePath(credentialTemplate.getTemplateImagePath());
            return trainEditItem;
        }).findFirst().orElse(new TrainEditItem());
        trainEditRequestVM.setCredentialItem(credentialItem);

        List<Integer> departmentIdList = trainService.getTrainDepartmentList(id).stream().map(p -> p.getDepartmentId()).collect(Collectors.toList());
        trainEditRequestVM.setDepartmentIdList(departmentIdList);

        return RestResponse.ok(trainEditRequestVM);
    }


    /**
     * 课件创建
     *
     * @param model the model
     * @return the rest response
     */
    @PostMapping("/create")
    @PreAuthorize("train:course:create")
    public RestResponse create(@RequestBody @Valid TrainEditRequestVM model) {
        Train newTrain = trainMapping.toTrain(model);
        RestResponse response = trainValid(model, newTrain);
        if (response.getCode() != SystemCode.OK.getCode()) {
            return response;
        }
        Date now = new Date();
        User user = getCurrentUser();
        newTrain.setDeleted(false);
        newTrain.setCreateTime(now);
        newTrain.setCreateUser(user.getId());
        newTrain.setCreateDepartmentId(user.getDepartmentId());
        trainService.save(newTrain);
        if (model.getExamPaperItem() != null) {
            model.getCourseWareItemList().add(model.getExamPaperItem());
        }
        if (model.getCredentialItem() != null) {
            model.getCourseWareItemList().add(model.getCredentialItem());
        }
        for (int itemOrder = 1; itemOrder <= model.getCourseWareItemList().size(); itemOrder++) {
            TrainEditItem trainEditItem = model.getCourseWareItemList().get(itemOrder - 1);
            TrainItem trainItem = trainMapping.toTrainItem(trainEditItem);
            trainItem.setTrainId(newTrain.getId());
            trainItem.setItemOrder(itemOrder);
            trainItem.setCreateUser(user.getId());
            trainItem.setCreateTime(now);
            trainItem.setDeleted(false);
            trainService.insertTrainItem(trainItem);
        }

        model.getDepartmentIdList().stream().forEach(item -> {
            TrainDepartment trainDepartment = new TrainDepartment();
            trainDepartment.setTrainId(newTrain.getId());
            trainDepartment.setDepartmentId(item);
            trainDepartment.setCreateUserId(user.getId());
            trainDepartment.setCreateDepartmentId(user.getDepartmentId());
            trainDepartment.setDeleted(false);
            trainService.insertTrainDepartment(trainDepartment);
        });
        return RestResponse.ok();
    }


    /**
     * 课件更新
     *
     * @param model the model
     * @return the rest response
     */
    @PostMapping("/update")
    @PreAuthorize("train:course:update")
    public RestResponse update(@RequestBody @Valid TrainEditRequestVM model) {
        Train oldTrain = trainService.getById(model.getId());
        RestResponse response = trainValid(model, oldTrain);
        if (response.getCode() != SystemCode.OK.getCode()) {
            return response;
        }
        User user = getCurrentUser();
        Date now = new Date();
        trainMapping.mapTrain(model, oldTrain);
        trainService.updateById(oldTrain);

        trainService.clearTrainDepartment(oldTrain.getId());
        model.getDepartmentIdList().stream().forEach(item -> {
            TrainDepartment trainDepartment = new TrainDepartment();
            trainDepartment.setTrainId(oldTrain.getId());
            trainDepartment.setDepartmentId(item);
            trainDepartment.setCreateUserId(user.getId());
            trainDepartment.setCreateDepartmentId(user.getDepartmentId());
            trainDepartment.setDeleted(false);
            trainService.insertTrainDepartment(trainDepartment);
        });

        List<TrainEditItem> newTrainItemVmList = model.getCourseWareItemList();
        if (model.getExamPaperItem() != null) {
            newTrainItemVmList.add(model.getExamPaperItem());
        }
        if (model.getCredentialItem() != null) {
            newTrainItemVmList.add(model.getCredentialItem());
        }
        List<TrainItem> oldTrainItemList = trainService.getTrainItemList(oldTrain.getId());
        trainService.clearTrainItem(oldTrain.getId());
        Integer itemOrder = 1;
        for (; itemOrder <= newTrainItemVmList.size(); itemOrder++) {
            TrainEditItem trainEditItem = newTrainItemVmList.get(itemOrder - 1);
            TrainItem existTrainItem = oldTrainItemList.stream()
                    .filter(item -> item.getTargetType().equals(trainEditItem.getTargetType()) && item.getTargetId().equals(trainEditItem.getTargetId()))
                    .findFirst().orElse(null);
            if (null != existTrainItem) {  //更新旧的列表
                trainMapping.mapTrainItem(trainEditItem, existTrainItem);
                existTrainItem.setDeleted(false);
                existTrainItem.setItemOrder(itemOrder);
                trainService.updateTrainItem(existTrainItem);
            } else {  //插入新列表
                TrainItem trainItem = trainMapping.toTrainItem(trainEditItem);
                trainItem.setTrainId(oldTrain.getId());
                trainItem.setItemOrder(itemOrder);
                trainItem.setCreateUser(user.getId());
                trainItem.setCreateTime(now);
                trainItem.setDeleted(false);
                trainService.insertTrainItem(trainItem);
            }
        }
        return RestResponse.ok();
    }


    /**
     * 课件删除
     *
     * @param id the id
     * @return the rest response
     */
    @PostMapping("/delete/{id}")
    @PreAuthorize("train:course:delete")
    public RestResponse delete(@PathVariable Integer id) {
        Train train = trainService.getById(id);
        train.setDeleted(true);
        trainService.updateById(train);
        return RestResponse.ok();
    }


    /**
     * 培训详情基本信息
     *
     * @param id
     * @return {@link RestResponse}<{@link TrainDetailVM}>
     */
    @PostMapping("/detail/{id}")
    @PreAuthorize("train:course:detail:select")
    public RestResponse detail(@PathVariable Integer id) {
        Train train = trainService.getById(id);
        if (null == train) {
            return RestResponse.fail(2, "未找到课程");
        }
        TrainDetailVM detailVM = trainMapping.toTrainDetailVM(train);
        User user = userService.getById(train.getCreateUser());
        detailVM.setCreateUserName(String.format("%s（%s）", user.getUserName(), user.getRealName()));

        List<TrainDepartment> trainDepartmentList = trainService.getTrainDepartmentList(train.getId());
        List<Integer> departmentIdList = trainDepartmentList.stream().map(p -> p.getDepartmentId()).collect(Collectors.toList());
        Integer userCount = userService.getUserCountByDepartmentIdList(departmentIdList);
        Integer passCount = trainService.trainUserStatusCount(train.getId(), TrainStatusEnum.Pass.getCode());
        Integer noPassCount = trainService.trainUserStatusCount(train.getId(), TrainStatusEnum.NoPass.getCode());
        Integer goingCount = trainService.trainUserStatusCount(train.getId(), TrainStatusEnum.Going.getCode());
        detailVM.setGoingCount(goingCount);
        detailVM.setNoStarCount(userCount - goingCount - noPassCount - passCount);
        detailVM.setNoPassCount(noPassCount);
        detailVM.setPassCount(passCount);
        detailVM.setAllUserCount(userCount);

        Integer judgeCount = trainExamPaperService.trainExamPaperCount(train.getId(), ExamPaperAnswerStatusEnum.WaitJudge.getCode());
        Integer paperCount = trainExamPaperService.trainExamPaperCount(train.getId(), null);
        Integer paperGoing = trainService.trainItemUserStatusCount(train.getId(), TrainTargetTypeEnum.ExamPaper.getCode(), TrainStatusEnum.Going.getCode());
        Integer paperNoPass = trainService.trainItemUserStatusCount(train.getId(), TrainTargetTypeEnum.ExamPaper.getCode(), TrainStatusEnum.NoPass.getCode());
        Integer paperPass = trainService.trainItemUserStatusCount(train.getId(), TrainTargetTypeEnum.ExamPaper.getCode(), TrainStatusEnum.Pass.getCode());
        detailVM.setJudgeCount(judgeCount);
        detailVM.setPaperCount(paperCount);
        detailVM.setPaperGoing(paperGoing);
        detailVM.setPaperNoPass(paperNoPass);
        detailVM.setPaperPass(paperPass);
        return RestResponse.ok(detailVM);
    }


    /**
     * 培训用户分页信息
     *
     * @param trainPageRequestVM
     * @return {@link RestResponse}
     */
    @PostMapping("/detail/page")
    @PreAuthorize("train:course:detail:select")
    public RestResponse detailPage(@RequestBody @Valid TrainPageRequestVM trainPageRequestVM) {
        Train train = trainService.getById(trainPageRequestVM.getTrainId());
        if (null == train) {
            return RestResponse.ok(new PageInfo());
        }

        initPermission(trainPageRequestVM);
        PageInfo<TrainPageResponseVM> pageInfo = trainService.userPage(trainPageRequestVM);
        pageInfo.getList().forEach(item -> {
            if (null != item.getStatus()) {
                item.setStatusStr(TrainStatusEnum.fromCode(item.getStatus()).getName());
            }
            if (null != item.getCreateTime()) {
                item.setCreateTimeStr(DateTimeUtil.dateTimeFullFormat(item.getCreateTime()));
            }
            if (null != item.getCompleteTime()) {
                item.setCompleteTimeStr(DateTimeUtil.dateTimeFullFormat(item.getCompleteTime()));
            }
            if (null != item.getDepartmentId()) {
                Department department = departmentService.getById(item.getDepartmentId());
                item.setDepartmentLevel(department.getLevel());
            }
        });
        return RestResponse.ok(pageInfo);
    }


    /**
     * 培训用户分页导出
     *
     * @param trainPageRequestVM
     * @return {@link RestResponse}
     * @throws IOException
     */
    @PostMapping("/detail/export")
    @PreAuthorize("train:course:detail:export")
    public RestResponse detailExport(@RequestBody @Valid TrainPageRequestVM trainPageRequestVM) throws IOException {
        trainPageRequestVM.setPageSize(MAXSIZE);
        RestResponse restResponse = detailPage(trainPageRequestVM);
        if (restResponse.getCode() == SystemCode.OK.getCode()) {
            @SuppressWarnings("unchecked")
            List<TrainPageResponseVM> exportList = ((RestResponse<PageInfo<TrainPageResponseVM>>) restResponse).getResponse().getList();
            Train train = trainService.getById(trainPageRequestVM.getTrainId());
            //结果回写
            File excelTemp = File.createTempFile(UUID.randomUUID().toString(), ".xlsx");
            WriteCellStyle headWriteCellStyle = new WriteCellStyle();
            WriteCellStyle contentWriteCellStyle = new WriteCellStyle();
            contentWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.LEFT);
            HorizontalCellStyleStrategy horizontalCellStyleStrategy = new HorizontalCellStyleStrategy(headWriteCellStyle, contentWriteCellStyle);
            EasyExcel.write(excelTemp, TrainPageResponseVM.class).sheet(train.getName()).registerWriteHandler(horizontalCellStyleStrategy).doWrite(exportList);
            String filePath = fileUploadService.fileUpload(excelTemp, String.format("%s - 培训结果 - %s.xlsx", train.getName(), DateTimeUtil.dateTimeFullNumberFormat(new Date())), "export/excel", true, true);
            return RestResponse.ok(filePath);
        } else {
            return restResponse;
        }
    }


    /**
     * 培训课件用户分页
     *
     * @param trainPageRequestVM
     * @return {@link RestResponse}
     */
    @PostMapping("/detail/course/ware/page")
    @PreAuthorize("train:course:detail:select")
    public RestResponse detailCourseWarePage(@RequestBody @Valid TrainPageRequestVM trainPageRequestVM) {
        Train train = trainService.getById(trainPageRequestVM.getTrainId());
        if (null == train) {
            return RestResponse.ok(new PageInfo());
        }

        initPermission(trainPageRequestVM);
        PageInfo<TrainCourseWarePageResponseVM> pageInfo = trainService.userCourseWarePage(trainPageRequestVM);
        pageInfo.getList().forEach(item -> {
            if (null != item.getCurrentNumber()) {
                item.setCurrentNumberStr(DateTimeUtil.secondToChineseTime(item.getCurrentNumber()));
            }
            if (null != item.getPassNumber()) {
                item.setPassNumberStr(DateTimeUtil.secondToChineseTime(item.getPassNumber()));
            }
            if (null != item.getStatus()) {
                item.setStatusStr(TrainStatusEnum.fromCode(item.getStatus()).getName());
            }
            if (null != item.getCompleteTime()) {
                item.setCompleteTimeStr(DateTimeUtil.dateTimeFullFormat(item.getCompleteTime()));
            }
        });
        return RestResponse.ok(pageInfo);
    }


    /**
     * 课程参数校验
     *
     * @param model
     * @param train
     * @return {@link RestResponse}
     */
    private RestResponse trainValid(TrainEditRequestVM model, Train train) {
        train.setStartTime(DateTimeUtil.parse(model.getLimitDateTime().get(0)));
        train.setEndTime(DateTimeUtil.parse(model.getLimitDateTime().get(1)));
        if (model.getExamPaperItem().getTargetId() == null) {
            model.setExamPaperItem(null);
        }
        if (model.getCredentialItem().getTargetId() == null) {
            model.setCredentialItem(null);
        }
        for (TrainEditItem item : model.getCourseWareItemList()) {
            if (StringUtils.isEmpty(item.getPassNumberStr())) {
                return RestResponse.fail(2, "请输入课件合格时长");
            }
            item.setPassNumber(DateTimeUtil.timeToSecond(item.getPassNumberStr()));
            if (item.getPassNumber() > item.getMaxNumber()) {
                return RestResponse.fail(2, "课件合格时长不能大于课件总时长");
            }
        }
        train.setItemCount(model.getCourseWareItemList().size());
        Integer studyTime = model.getCourseWareItemList().stream().mapToInt(item -> item.getPassNumber()).sum();
        train.setStudyTime(studyTime);
        TrainEditItem examPaperItem = model.getExamPaperItem();
        if (examPaperItem != null) {
            if (StringUtils.isEmpty(examPaperItem.getPassNumberStr())) {
                return RestResponse.fail(2, "请输入试卷合格分");
            }
            examPaperItem.setPassNumber(ExamUtil.scoreFromVM(examPaperItem.getPassNumberStr()));
            examPaperItem.setMaxNumber(ExamUtil.scoreFromVM(examPaperItem.getMaxNumberStr()));
        }
        return RestResponse.ok();
    }
}
