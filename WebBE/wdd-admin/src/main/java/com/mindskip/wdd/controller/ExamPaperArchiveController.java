package com.mindskip.wdd.controller;

import com.mindskip.wdd.base.BaseApiController;
import com.mindskip.wdd.base.RestResponse;
import com.mindskip.wdd.configuration.spring.interceptor.PreAuthorize;
import com.mindskip.wdd.domain.ExamPaperArchive;
import com.mindskip.wdd.domain.User;
import com.mindskip.wdd.mapping.ExamPaperArchiveMapping;
import com.mindskip.wdd.service.ExamPaperArchiveService;
import com.mindskip.wdd.utility.ExamUtil;
import com.mindskip.wdd.viewmodel.common.ArchiveVM;
import com.mindskip.wdd.viewmodel.exam.paper.ExamPaperArchiveEditRequestVM;
import com.mindskip.wdd.viewmodel.exam.paper.ExamPaperArchiveMoveRequestVM;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @version 1.7.0
 * @description: 试卷分类接口
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
 */
@RestController
@AllArgsConstructor
@RequestMapping(value = "/api/exam/paper/archive")
public class ExamPaperArchiveController extends BaseApiController {

    private final ExamPaperArchiveService examPaperArchiveService;
    private final ExamPaperArchiveMapping examPaperArchiveMapping;


    /**
     * 试卷分类树形
     *
     * @return the rest response
     */
    @PostMapping("/tree")
    public RestResponse<List<ArchiveVM>> tree() {
        List<ExamPaperArchive> examPaperArchiveRoot = examPaperArchiveService.getRootExamPaperArchive();
        List<ArchiveVM> archiveVMList = new ArrayList<>();
        examPaperArchiveRecursion(examPaperArchiveRoot, archiveVMList);
        return RestResponse.ok(archiveVMList);
    }


    /**
     * 试卷分类创建
     *
     * @param model the model
     * @return the rest response
     */
    @PostMapping("/create")
    @PreAuthorize("exam:paper:archive:create")
    public RestResponse create(@RequestBody @Valid ExamPaperArchiveEditRequestVM model) {
        User user = getCurrentUser();
        ExamPaperArchive newExamPaperArchive = examPaperArchiveMapping.toExamPaperArchive(model);
        newExamPaperArchive.setDeleted(false);
        newExamPaperArchive.setCreateTime(new Date());
        newExamPaperArchive.setCreateUser(user.getId());
        newExamPaperArchive.setCreateDepartmentId(user.getDepartmentId());
        if (null == model.getParentId()) {
            newExamPaperArchive.setLevel(String.format("/%s/", model.getName()));
        } else {
            ExamPaperArchive parentNode = examPaperArchiveService.getById(model.getParentId());
            newExamPaperArchive.setLevel(String.format("%s%s/", parentNode.getLevel(), model.getName()));
        }
        ExamPaperArchive exist = examPaperArchiveService.getByLevel(newExamPaperArchive.getLevel());
        if (null != exist) {
            return RestResponse.fail(2, "试卷分类已存在");
        }
        examPaperArchiveService.save(newExamPaperArchive);
        newExamPaperArchive.setItemOrder(newExamPaperArchive.getId() * ExamUtil.ItemOrderInit);
        examPaperArchiveService.updateById(newExamPaperArchive);
        return RestResponse.ok();
    }


    /**
     * 更新试卷分类
     *
     * @param model the model
     * @return the rest response
     */
    @PostMapping("/update")
    @PreAuthorize("exam:paper:archive:update")
    public RestResponse update(@RequestBody @Valid ExamPaperArchiveEditRequestVM model) {
        ExamPaperArchive oldExamPaperArchive = examPaperArchiveService.getById(model.getId());
        String newLevel;
        if (null == oldExamPaperArchive.getParentId()) {
            newLevel = String.format("/%s/", model.getName());
        } else {
            ExamPaperArchive parentNode = examPaperArchiveService.getById(oldExamPaperArchive.getParentId());
            newLevel = String.format("%s%s/", parentNode.getLevel(), model.getName());
        }
        ExamPaperArchive exist = examPaperArchiveService.getByLevel(newLevel);
        if (null != exist) {
            return RestResponse.fail(2, "试卷分类已存在");
        }
        examPaperArchiveService.updateLevel(oldExamPaperArchive.getLevel(), newLevel);
        oldExamPaperArchive.setLevel(newLevel);
        oldExamPaperArchive.setName(model.getName());
        examPaperArchiveService.updateById(oldExamPaperArchive);
        return RestResponse.ok();
    }


    /**
     * 试卷分类位置移动
     *
     * @param model 模型
     * @return {@link RestResponse}
     */
    @PostMapping("/move")
    @PreAuthorize("exam:paper:archive:move")
    public RestResponse move(@RequestBody @Valid ExamPaperArchiveMoveRequestVM model) {
        return examPaperArchiveService.move(model);
    }

    /**
     * 删除试卷分类
     *
     * @param id the id
     * @return the rest response
     */
    @PostMapping("/delete/{id}")
    @PreAuthorize("exam:paper:archive:delete")
    public RestResponse delete(@PathVariable Integer id) {
        ExamPaperArchive examPaperArchive = examPaperArchiveService.getById(id);
        examPaperArchiveService.deleteByLevel(examPaperArchive.getLevel());
        return RestResponse.ok();
    }

    /**
     * 模型分类转化
     *
     * @param examPaperArchiveList
     * @param archiveVMList
     */
    private void examPaperArchiveRecursion(List<ExamPaperArchive> examPaperArchiveList, List<ArchiveVM> archiveVMList) {
        examPaperArchiveList.forEach(item -> {
            ArchiveVM archiveVM = examPaperArchiveMapping.toArchiveVM(item);
            archiveVMList.add(archiveVM);
            List<ExamPaperArchive> examPaperArchiveChild = examPaperArchiveService.getExamPaperArchiveByParentId(item.getId());
            if (0 != examPaperArchiveChild.size()) {
                examPaperArchiveRecursion(examPaperArchiveChild, archiveVM.getChildren());
            }
        });
    }

}
