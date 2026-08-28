package com.mindskip.wdd.controller;

import com.mindskip.wdd.base.BaseApiController;
import com.mindskip.wdd.base.RestResponse;
import com.mindskip.wdd.configuration.spring.interceptor.PreAuthorize;
import com.mindskip.wdd.domain.QuestionArchive;
import com.mindskip.wdd.domain.User;
import com.mindskip.wdd.mapping.QuestionArchiveMapping;
import com.mindskip.wdd.service.QuestionArchiveService;
import com.mindskip.wdd.utility.ExamUtil;
import com.mindskip.wdd.viewmodel.common.ArchiveVM;
import com.mindskip.wdd.viewmodel.question.QuestionArchiveEditRequestVM;
import com.mindskip.wdd.viewmodel.question.QuestionArchiveMoveRequestVM;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

/**
 * @version 1.7.0
 * @description: 题目分类接口
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
@RestController
@AllArgsConstructor
@RequestMapping(value = "/api/question/archive")
public class QuestionArchiveController extends BaseApiController {

    private final QuestionArchiveService questionArchiveService;
    private final QuestionArchiveMapping questionArchiveMapping;


    /**
     * 分类树形
     *
     * @return the rest response
     */
    @PostMapping("/vmTypeTree")
    public RestResponse<List<Map<String,Object>>> vmTypeTree() {
        List<Map<String,Object>> archiveVMList = new ArrayList<>();

        Map<String,Object> a1 = new HashMap<>();
        a1.put("value","11");
        a1.put("label","KylinV10_2025");
        archiveVMList.add(a1);

        Map<String,Object> a2 = new HashMap<>();
        a2.put("value","22");
        a2.put("label","KylinV10_2025_01");
        archiveVMList.add(a2);

        Map<String,Object> a3 = new HashMap<>();
        a3.put("value","229");
        a3.put("label","KylinV10_2025_09");
        archiveVMList.add(a3);

        return RestResponse.ok(archiveVMList);
    }

    /**
     * 分类树形
     *
     * @return the rest response
     */
    @PostMapping("/tree")
    public RestResponse<List<ArchiveVM>> tree() {
        List<QuestionArchive> questionArchiveRoot = questionArchiveService.getRootQuestionArchive();
        List<ArchiveVM> archiveVMList = new ArrayList<>();
        questionArchiveRecursion(questionArchiveRoot, archiveVMList);
        return RestResponse.ok(archiveVMList);
    }


    /**
     * 创建题目分类
     *
     * @param model the model
     * @return the rest response
     */
    @PostMapping("/create")
    @PreAuthorize("question:archive:create")
    public RestResponse create(@RequestBody @Valid QuestionArchiveEditRequestVM model) {
        User user = getCurrentUser();
        QuestionArchive newQuestionArchive = questionArchiveMapping.toQuestionArchive(model);
        newQuestionArchive.setDeleted(false);
        newQuestionArchive.setCreateTime(new Date());
        newQuestionArchive.setCreateUser(user.getId());
        newQuestionArchive.setCreateDepartmentId(user.getDepartmentId());
        if (null == model.getParentId()) {
            newQuestionArchive.setLevel(String.format("/%s/", model.getName()));
        } else {
            QuestionArchive parentNode = questionArchiveService.getById(model.getParentId());
            newQuestionArchive.setLevel(String.format("%s%s/", parentNode.getLevel(), model.getName()));
        }
        QuestionArchive exist = questionArchiveService.getByLevel(newQuestionArchive.getLevel());
        if (null != exist) {
            return RestResponse.fail(2, "题目分类已存在");
        }
        questionArchiveService.save(newQuestionArchive);
        newQuestionArchive.setItemOrder(newQuestionArchive.getId() * ExamUtil.ItemOrderInit);
        questionArchiveService.updateById(newQuestionArchive);
        return RestResponse.ok();
    }


    /**
     * 更新题目分类
     *
     * @param model the model
     * @return the rest response
     */
    @PostMapping("/update")
    @PreAuthorize("question:archive:update")
    public RestResponse update(@RequestBody @Valid QuestionArchiveEditRequestVM model) {
        QuestionArchive oldQuestionArchive = questionArchiveService.getById(model.getId());
        String newLevel;
        if (null == oldQuestionArchive.getParentId()) {
            newLevel = String.format("/%s/", model.getName());
        } else {
            QuestionArchive parentNode = questionArchiveService.getById(oldQuestionArchive.getParentId());
            newLevel = String.format("%s%s/", parentNode.getLevel(), model.getName());
        }
        QuestionArchive exist = questionArchiveService.getByLevel(newLevel);
        if (null != exist) {
            return RestResponse.fail(2, "题目分类已存在");
        }
        questionArchiveService.updateLevel(oldQuestionArchive.getLevel(), newLevel);
        oldQuestionArchive.setLevel(newLevel);
        oldQuestionArchive.setName(model.getName());
        questionArchiveService.updateById(oldQuestionArchive);
        return RestResponse.ok();
    }


    /**
     * 题目分类位置移动
     *
     * @param model 模型
     * @return {@link RestResponse}
     */
    @PostMapping("/move")
    @PreAuthorize("question:archive:move")
    public RestResponse move(@RequestBody @Valid QuestionArchiveMoveRequestVM model) {
        return questionArchiveService.move(model);
    }

    /**
     * 删除题目分类
     *
     * @param id the id
     * @return the rest response
     */
    @PostMapping("/delete/{id}")
    @PreAuthorize("question:archive:delete")
    public RestResponse delete(@PathVariable Integer id) {
        QuestionArchive questionArchive = questionArchiveService.getById(id);
        questionArchiveService.deleteByLevel(questionArchive.getLevel());
        return RestResponse.ok();
    }

    /**
     * 题目分类模型转化
     *
     * @param questionArchiveList
     * @param archiveVMList
     */
    private void questionArchiveRecursion(List<QuestionArchive> questionArchiveList, List<ArchiveVM> archiveVMList) {
        questionArchiveList.forEach(item -> {
            ArchiveVM archiveVM = questionArchiveMapping.toArchiveVM(item);
            archiveVMList.add(archiveVM);
            List<QuestionArchive> questionArchiveChild = questionArchiveService.getQuestionArchiveByParentId(item.getId());
            if (0 != questionArchiveChild.size()) {
                questionArchiveRecursion(questionArchiveChild, archiveVM.getChildren());
            }
        });
    }

}
