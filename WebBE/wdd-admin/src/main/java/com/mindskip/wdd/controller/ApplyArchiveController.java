package com.mindskip.wdd.controller;

import com.mindskip.wdd.base.BaseApiController;
import com.mindskip.wdd.base.RestResponse;
import com.mindskip.wdd.configuration.spring.interceptor.PreAuthorize;
import com.mindskip.wdd.domain.ApplyArchive;
import com.mindskip.wdd.domain.User;
import com.mindskip.wdd.mapping.ApplyArchiveMapping;
import com.mindskip.wdd.service.ApplyArchiveService;
import com.mindskip.wdd.utility.ExamUtil;
import com.mindskip.wdd.viewmodel.apply.ApplyArchiveEditRequestVM;
import com.mindskip.wdd.viewmodel.apply.ApplyArchiveMoveRequestVM;
import com.mindskip.wdd.viewmodel.common.ArchiveVM;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @version 1.7.0
 * @description: 报名分类接口
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
 */
@RestController
@AllArgsConstructor
@RequestMapping(value = "/api/apply/archive")
public class ApplyArchiveController extends BaseApiController {

    private final ApplyArchiveService applyArchiveService;
    private final ApplyArchiveMapping applyArchiveMapping;


    /**
     * 报名分类树形
     *
     * @return the rest response
     */
    @PostMapping("/tree")
    public RestResponse<List<ArchiveVM>> tree() {
        List<ApplyArchive> applyArchiveRoot = applyArchiveService.getRootApplyArchive();
        List<ArchiveVM> archiveVMList = new ArrayList<>();
        applyArchiveRecursion(applyArchiveRoot, archiveVMList);
        return RestResponse.ok(archiveVMList);
    }


    /**
     * 报名分类创建
     *
     * @param model the model
     * @return the rest response
     */
    @PostMapping("/create")
    @PreAuthorize("apply:archive:create")
    public RestResponse create(@RequestBody @Valid ApplyArchiveEditRequestVM model) {
        User user = getCurrentUser();
        ApplyArchive newApplyArchive = applyArchiveMapping.toApplyArchive(model);
        newApplyArchive.setDeleted(false);
        newApplyArchive.setCreateTime(new Date());
        newApplyArchive.setCreateUser(user.getId());
        newApplyArchive.setCreateDepartmentId(user.getDepartmentId());
        if (null == model.getParentId()) {
            newApplyArchive.setLevel(String.format("/%s/", model.getName()));
        } else {
            ApplyArchive parentNode = applyArchiveService.getById(model.getParentId());
            newApplyArchive.setLevel(String.format("%s%s/", parentNode.getLevel(), model.getName()));
        }
        ApplyArchive exist = applyArchiveService.getByLevel(newApplyArchive.getLevel());
        if (null != exist) {
            return RestResponse.fail(2, "报名分类已存在");
        }
        applyArchiveService.save(newApplyArchive);
        newApplyArchive.setItemOrder(newApplyArchive.getId() * ExamUtil.ItemOrderInit);
        applyArchiveService.updateById(newApplyArchive);
        return RestResponse.ok();
    }


    /**
     * 报名分类更新
     *
     * @param model the model
     * @return the rest response
     */
    @PostMapping("/update")
    @PreAuthorize("apply:archive:update")
    public RestResponse update(@RequestBody @Valid ApplyArchiveEditRequestVM model) {
        ApplyArchive oldApplyArchive = applyArchiveService.getById(model.getId());
        String newLevel;
        if (null == oldApplyArchive.getParentId()) {
            newLevel = String.format("/%s/", model.getName());
        } else {
            ApplyArchive parentNode = applyArchiveService.getById(oldApplyArchive.getParentId());
            newLevel = String.format("%s%s/", parentNode.getLevel(), model.getName());
        }
        ApplyArchive exist = applyArchiveService.getByLevel(newLevel);
        if (null != exist) {
            return RestResponse.fail(2, "报名分类已存在");
        }
        applyArchiveService.updateLevel(oldApplyArchive.getLevel(), newLevel);
        oldApplyArchive.setLevel(newLevel);
        oldApplyArchive.setName(model.getName());
        applyArchiveService.updateById(oldApplyArchive);
        return RestResponse.ok();
    }


    /**
     * 报名分类位置移动
     *
     * @param model 模型
     * @return {@link RestResponse}
     */
    @PostMapping("/move")
    @PreAuthorize("apply:archive:move")
    public RestResponse move(@RequestBody @Valid ApplyArchiveMoveRequestVM model) {
        return applyArchiveService.move(model);
    }

    /**
     * 报名分类删除
     *
     * @param id the id
     * @return the rest response
     */
    @PostMapping("/delete/{id}")
    @PreAuthorize("apply:archive:delete")
    public RestResponse delete(@PathVariable Integer id) {
        ApplyArchive applyArchive = applyArchiveService.getById(id);
        applyArchiveService.deleteByLevel(applyArchive.getLevel());
        return RestResponse.ok();
    }

    /**
     * 分类模型转换
     *
     * @param applyArchiveList
     * @param archiveVMList
     */
    private void applyArchiveRecursion(List<ApplyArchive> applyArchiveList, List<ArchiveVM> archiveVMList) {
        applyArchiveList.forEach(item -> {
            ArchiveVM archiveVM = applyArchiveMapping.toArchiveVM(item);
            archiveVMList.add(archiveVM);
            List<ApplyArchive> applyArchiveChild = applyArchiveService.getApplyArchiveByParentId(item.getId());
            if (0 != applyArchiveChild.size()) {
                applyArchiveRecursion(applyArchiveChild, archiveVM.getChildren());
            }
        });
    }

}
