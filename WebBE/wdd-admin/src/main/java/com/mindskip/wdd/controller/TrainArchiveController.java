package com.mindskip.wdd.controller;

import com.mindskip.wdd.base.BaseApiController;
import com.mindskip.wdd.base.RestResponse;
import com.mindskip.wdd.configuration.spring.interceptor.PreAuthorize;
import com.mindskip.wdd.domain.TrainArchive;
import com.mindskip.wdd.domain.User;
import com.mindskip.wdd.mapping.TrainArchiveMapping;
import com.mindskip.wdd.service.TrainArchiveService;
import com.mindskip.wdd.utility.ExamUtil;
import com.mindskip.wdd.viewmodel.common.ArchiveVM;
import com.mindskip.wdd.viewmodel.train.archive.TrainArchiveEditRequestVM;
import com.mindskip.wdd.viewmodel.train.archive.TrainArchiveMoveRequestVM;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @version 9.0.0
 * @description: 培训分类
 * Copyright (C), 2025, 麟航团队
 * @date 2025/11/16 10:45
 */
@RestController
@AllArgsConstructor
@RequestMapping(value = "/api/train/archive")
public class TrainArchiveController extends BaseApiController {

    private final TrainArchiveService trainArchiveService;
    private final TrainArchiveMapping trainArchiveMapping;


    /**
     * 培训分类树形
     *
     * @return the rest response
     */
    @PostMapping("/tree")
    public RestResponse<List<ArchiveVM>> tree() {
        List<TrainArchive> trainArchiveRoot = trainArchiveService.getRootTrainArchive();
        List<ArchiveVM> archiveVMList = new ArrayList<>();
        trainArchiveRecursion(trainArchiveRoot, archiveVMList);
        return RestResponse.ok(archiveVMList);
    }


    /**
     * 培训分类创建
     *
     * @param model the model
     * @return the rest response
     */
    @PostMapping("/create")
    @PreAuthorize("train:archive:create")
    public RestResponse create(@RequestBody @Valid TrainArchiveEditRequestVM model) {
        User user = getCurrentUser();
        TrainArchive newTrainArchive = trainArchiveMapping.toTrainArchive(model);
        newTrainArchive.setDeleted(false);
        newTrainArchive.setCreateTime(new Date());
        newTrainArchive.setCreateUser(user.getId());
        newTrainArchive.setCreateDepartmentId(user.getDepartmentId());
        if (null == model.getParentId()) {
            newTrainArchive.setLevel(String.format("/%s/", model.getName()));
        } else {
            TrainArchive parentNode = trainArchiveService.getById(model.getParentId());
            newTrainArchive.setLevel(String.format("%s%s/", parentNode.getLevel(), model.getName()));
        }
        TrainArchive exist = trainArchiveService.getByLevel(newTrainArchive.getLevel());
        if (null != exist) {
            return RestResponse.fail(2, "培训分类已存在");
        }
        trainArchiveService.save(newTrainArchive);
        newTrainArchive.setItemOrder(newTrainArchive.getId() * ExamUtil.ItemOrderInit);
        trainArchiveService.updateById(newTrainArchive);
        return RestResponse.ok();
    }


    /**
     * 培训分类更新
     *
     * @param model the model
     * @return the rest response
     */
    @PostMapping("/update")
    @PreAuthorize("train:archive:update")
    public RestResponse update(@RequestBody @Valid TrainArchiveEditRequestVM model) {
        TrainArchive oldTrainArchive = trainArchiveService.getById(model.getId());
        String newLevel;
        if (null == oldTrainArchive.getParentId()) {
            newLevel = String.format("/%s/", model.getName());
        } else {
            TrainArchive parentNode = trainArchiveService.getById(oldTrainArchive.getParentId());
            newLevel = String.format("%s%s/", parentNode.getLevel(), model.getName());
        }
        TrainArchive exist = trainArchiveService.getByLevel(newLevel);
        if (null != exist) {
            return RestResponse.fail(2, "培训分类已存在");
        }
        trainArchiveService.updateLevel(oldTrainArchive.getLevel(), newLevel);
        oldTrainArchive.setLevel(newLevel);
        oldTrainArchive.setName(model.getName());
        trainArchiveService.updateById(oldTrainArchive);
        return RestResponse.ok();
    }

    /**
     * 培训分类位置移动
     *
     * @param model 模型
     * @return {@link RestResponse}
     */
    @PostMapping("/move")
    @PreAuthorize("train:archive:move")
    public RestResponse move(@RequestBody @Valid TrainArchiveMoveRequestVM model) {
        return trainArchiveService.move(model);
    }


    /**
     * 培训分类删除
     *
     * @param id the id
     * @return the rest response
     */
    @PostMapping("/delete/{id}")
    @PreAuthorize("train:archive:delete")
    public RestResponse delete(@PathVariable Integer id) {
        TrainArchive trainArchive = trainArchiveService.getById(id);
        trainArchiveService.deleteByLevel(trainArchive.getLevel());
        return RestResponse.ok();
    }

    /**
     * 培训分类转化
     *
     * @param trainArchiveList
     * @param archiveVMList
     */
    private void trainArchiveRecursion(List<TrainArchive> trainArchiveList, List<ArchiveVM> archiveVMList) {
        trainArchiveList.forEach(item -> {
            ArchiveVM archiveVM = trainArchiveMapping.toArchiveVM(item);
            archiveVMList.add(archiveVM);
            List<TrainArchive> trainArchiveChild = trainArchiveService.getTrainArchiveByParentId(item.getId());
            if (0 != trainArchiveChild.size()) {
                trainArchiveRecursion(trainArchiveChild, archiveVM.getChildren());
            }
        });
    }

}
