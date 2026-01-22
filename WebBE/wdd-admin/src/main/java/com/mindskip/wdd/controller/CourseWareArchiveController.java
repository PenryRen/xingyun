package com.mindskip.wdd.controller;

import com.mindskip.wdd.base.BaseApiController;
import com.mindskip.wdd.base.RestResponse;
import com.mindskip.wdd.configuration.spring.interceptor.PreAuthorize;
import com.mindskip.wdd.domain.CourseWareArchive;
import com.mindskip.wdd.domain.User;
import com.mindskip.wdd.mapping.CourseWareArchiveMapping;
import com.mindskip.wdd.service.CourseWareArchiveService;
import com.mindskip.wdd.utility.ExamUtil;
import com.mindskip.wdd.viewmodel.common.ArchiveVM;
import com.mindskip.wdd.viewmodel.course.ware.CourseWareArchiveEditRequestVM;
import com.mindskip.wdd.viewmodel.course.ware.CourseWareArchiveMoveRequestVM;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @version 6.0.0
 * @description: 课件分类
 * Copyright (C), 2024, 麒技团队
 * @date 2024/9/15 10:28
 */
@RestController
@AllArgsConstructor
@RequestMapping(value = "/api/course/ware/archive")
public class CourseWareArchiveController extends BaseApiController {

    private final CourseWareArchiveService courseWareArchiveService;
    private final CourseWareArchiveMapping courseWareArchiveMapping;


    /**
     * 课件分类树形
     *
     * @return the rest response
     */
    @PostMapping("/tree")
    public RestResponse<List<ArchiveVM>> tree() {
        List<CourseWareArchive> courseWareArchiveRoot = courseWareArchiveService.getRootCourseWareArchive();
        List<ArchiveVM> archiveVMList = new ArrayList<>();
        courseWareArchiveRecursion(courseWareArchiveRoot, archiveVMList);
        return RestResponse.ok(archiveVMList);
    }


    /**
     * 课件分类创建
     *
     * @param model the model
     * @return the rest response
     */
    @PostMapping("/create")
    @PreAuthorize("course:ware:archive:create")
    public RestResponse create(@RequestBody @Valid CourseWareArchiveEditRequestVM model) {
        User user = getCurrentUser();
        CourseWareArchive newCourseWareArchive = courseWareArchiveMapping.toCourseWareArchive(model);
        newCourseWareArchive.setDeleted(false);
        newCourseWareArchive.setCreateTime(new Date());
        newCourseWareArchive.setCreateUser(user.getId());
        newCourseWareArchive.setCreateDepartmentId(user.getDepartmentId());
        if (null == model.getParentId()) {
            newCourseWareArchive.setLevel(String.format("/%s/", model.getName()));
        } else {
            CourseWareArchive parentNode = courseWareArchiveService.getById(model.getParentId());
            newCourseWareArchive.setLevel(String.format("%s%s/", parentNode.getLevel(), model.getName()));
        }
        CourseWareArchive exist = courseWareArchiveService.getByLevel(newCourseWareArchive.getLevel());
        if (null != exist) {
            return RestResponse.fail(2, "课件分类已存在");
        }
        courseWareArchiveService.save(newCourseWareArchive);
        newCourseWareArchive.setItemOrder(newCourseWareArchive.getId() * ExamUtil.ItemOrderInit);
        courseWareArchiveService.updateById(newCourseWareArchive);
        return RestResponse.ok();
    }


    /**
     * 课件分类更新
     *
     * @param model the model
     * @return the rest response
     */
    @PostMapping("/update")
    @PreAuthorize("course:ware:archive:update")
    public RestResponse update(@RequestBody @Valid CourseWareArchiveEditRequestVM model) {
        CourseWareArchive oldCourseWareArchive = courseWareArchiveService.getById(model.getId());
        String newLevel;
        if (null == oldCourseWareArchive.getParentId()) {
            newLevel = String.format("/%s/", model.getName());
        } else {
            CourseWareArchive parentNode = courseWareArchiveService.getById(oldCourseWareArchive.getParentId());
            newLevel = String.format("%s%s/", parentNode.getLevel(), model.getName());
        }
        CourseWareArchive exist = courseWareArchiveService.getByLevel(newLevel);
        if (null != exist) {
            return RestResponse.fail(2, "课件分类已存在");
        }
        courseWareArchiveService.updateLevel(oldCourseWareArchive.getLevel(), newLevel);
        oldCourseWareArchive.setLevel(newLevel);
        oldCourseWareArchive.setName(model.getName());
        courseWareArchiveService.updateById(oldCourseWareArchive);
        return RestResponse.ok();
    }

    /**
     * 课件分类位置移动
     *
     * @param model 模型
     * @return {@link RestResponse}
     */
    @PostMapping("/move")
    @PreAuthorize("course:ware:archive:move")
    public RestResponse move(@RequestBody @Valid CourseWareArchiveMoveRequestVM model) {
        return courseWareArchiveService.move(model);
    }


    /**
     * 课件分类删除
     *
     * @param id the id
     * @return the rest response
     */
    @PostMapping("/delete/{id}")
    @PreAuthorize("course:ware:archive:delete")
    public RestResponse delete(@PathVariable Integer id) {
        CourseWareArchive courseWareArchive = courseWareArchiveService.getById(id);
        courseWareArchiveService.deleteByLevel(courseWareArchive.getLevel());
        return RestResponse.ok();
    }

    /**
     * 分类模型转换
     *
     * @param courseWareArchiveList
     * @param archiveVMList
     */
    private void courseWareArchiveRecursion(List<CourseWareArchive> courseWareArchiveList, List<ArchiveVM> archiveVMList) {
        courseWareArchiveList.forEach(item -> {
            ArchiveVM archiveVM = courseWareArchiveMapping.toArchiveVM(item);
            archiveVMList.add(archiveVM);
            List<CourseWareArchive> courseWareArchiveChild = courseWareArchiveService.getCourseWareArchiveByParentId(item.getId());
            if (0 != courseWareArchiveChild.size()) {
                courseWareArchiveRecursion(courseWareArchiveChild, archiveVM.getChildren());
            }
        });
    }

}
