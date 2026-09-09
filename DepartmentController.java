package com.mindskip.wdd.controller;

import com.mindskip.wdd.base.BaseApiController;
import com.mindskip.wdd.base.RestResponse;
import com.mindskip.wdd.configuration.spring.interceptor.PreAuthorize;
import com.mindskip.wdd.domain.Department;
import com.mindskip.wdd.domain.Role;
import com.mindskip.wdd.domain.User;
import com.mindskip.wdd.mapping.DepartmentMapping;
import com.mindskip.wdd.service.DepartmentService;
import com.mindskip.wdd.utility.ExamUtil;
import com.mindskip.wdd.viewmodel.department.DepartmentEditRequestVM;
import com.mindskip.wdd.viewmodel.department.DepartmentMoveRequestVM;
import com.mindskip.wdd.viewmodel.department.DepartmentTreeVM;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @version 1.7.0
 * @description: 部门接口
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
@RestController
@AllArgsConstructor
@RequestMapping(value = "/api/department")
public class DepartmentController extends BaseApiController {

    private final DepartmentService departmentService;
    private final DepartmentMapping departmentMapping;


    /**
     * 部门树形
     *
     * @return the rest response
     */
    @PostMapping("/all/tree")
    public RestResponse<List<DepartmentTreeVM>> allTree() {
        List<Department> departmentRoot = departmentService.getRootDepartment();
        List<DepartmentTreeVM> departmentTreeVMList = departmentMapping.toDepartmentTreeVMList(departmentRoot);
        departmentRecursion(departmentTreeVMList, new ArrayList<>());
        return RestResponse.ok(departmentTreeVMList);
    }

    /**
     * 部门树形，数据权限
     *
     * @return the rest response
     */
    @PostMapping("/tree")
    public RestResponse<List<DepartmentTreeVM>> tree() {
        Role role = getRole();
        List<Integer> departmentIdList = role.getDataFilter().getDepartmentIdList();
        List<DepartmentTreeVM> departmentTreeVMList;
        if (departmentIdList.size() > 0) {  //数据权限
            List<Department> dataFilterDepartment = departmentService.getDepartmentById(departmentIdList);
            List<Department> rootDepartment = departmentService.getRootDepartment();
            List<Department> departmentRoot = rootDepartment.stream()
                    .filter(root -> dataFilterDepartment.stream().anyMatch(filter -> filter.getLevel().startsWith(root.getLevel()))).collect(Collectors.toList());
            departmentTreeVMList = departmentMapping.toDepartmentTreeVMList(departmentRoot);
            departmentRecursion(departmentTreeVMList, dataFilterDepartment);
        } else {
            List<Department> departmentRoot = departmentService.getRootDepartment();
            departmentTreeVMList = departmentMapping.toDepartmentTreeVMList(departmentRoot);
            departmentRecursion(departmentTreeVMList, new ArrayList<>());
        }
        return RestResponse.ok(departmentTreeVMList);
    }


    /**
     * 创建部门
     *
     * @param model the model
     * @return the rest response
     */
    @PostMapping("/create")
    @PreAuthorize("department:create")
    public RestResponse create(@RequestBody @Valid DepartmentEditRequestVM model) {
        User user = getCurrentUser();
        Department newDepartment = departmentMapping.toDepartment(model);
        newDepartment.setDeleted(false);
        newDepartment.setCreateTime(new Date());
        newDepartment.setCreateUser(user.getId());
        newDepartment.setCreateDepartmentId(user.getDepartmentId());
        if (null == model.getParentId()) {
            newDepartment.setLevel(String.format("/%s/", model.getName()));
        } else {
            Department parentNode = departmentService.getById(model.getParentId());
            newDepartment.setLevel(String.format("%s%s/", parentNode.getLevel(), model.getName()));
        }
        Department exist = departmentService.getByLevel(newDepartment.getLevel());
        if (null != exist) {
            return RestResponse.fail(2, "部门已存在");
        }
        departmentService.save(newDepartment);
        newDepartment.setItemOrder(newDepartment.getId() * ExamUtil.ItemOrderInit);
        departmentService.updateById(newDepartment);
        return RestResponse.ok();
    }


    /**
     * 更新部门
     *
     * @param model the model
     * @return the rest response
     */
    @PostMapping("/update")
    @PreAuthorize("department:update")
    public RestResponse update(@RequestBody @Valid DepartmentEditRequestVM model) {
        Department oldDepartment = departmentService.getById(model.getId());
        String newLevel;
        if (null == oldDepartment.getParentId()) {
            newLevel = String.format("/%s/", model.getName());
        } else {
            Department parentNode = departmentService.getById(oldDepartment.getParentId());
            newLevel = String.format("%s%s/", parentNode.getLevel(), model.getName());
        }
        Department exist = departmentService.getByLevel(newLevel);
        if (null != exist) {
            return RestResponse.fail(2, "部门已存在");
        }
        departmentService.updateLevel(oldDepartment.getLevel(), newLevel);
        oldDepartment.setLevel(newLevel);
        oldDepartment.setName(model.getName());
        departmentService.updateById(oldDepartment);
        return RestResponse.ok();
    }


    /**
     * 部门位置移动
     *
     * @param model 模型
     * @return {@link RestResponse}
     */
    @PostMapping("/move")
    @PreAuthorize("department:move")
    public RestResponse move(@RequestBody @Valid DepartmentMoveRequestVM model) {
        return departmentService.move(model);
    }


    /**
     * 删除部门
     *
     * @param id the id
     * @return the rest response
     */
    @PostMapping("/delete/{id}")
    @PreAuthorize("department:delete")
    public RestResponse delete(@PathVariable Integer id) {
        Department department = departmentService.getById(id);
        departmentService.deleteByLevel(department.getLevel());
        return RestResponse.ok();
    }

    /**
     * 分类模型转换
     *
     * @param departmentTreeVMList
     * @param dataFilterDepartment
     */
    private void departmentRecursion(List<DepartmentTreeVM> departmentTreeVMList, List<Department> dataFilterDepartment) {
        departmentTreeVMList.forEach(item -> {
            List<Department> departmentChild = departmentService.getDepartmentByParentId(item.getId());
            if (dataFilterDepartment.size() > 0) {
                Boolean enable = departmentChild
                        .stream()
                        .allMatch(department -> dataFilterDepartment
                                .stream()
                                .anyMatch(filter -> department.getId().equals(filter))
                        );
                item.setDisabled(!enable);
                departmentChild = departmentChild.stream()
                        .filter(root -> dataFilterDepartment.stream().anyMatch(filter -> filter.getLevel().startsWith(root.getLevel()))).collect(Collectors.toList());
            }
            if (0 != departmentChild.size()) {
                List<DepartmentTreeVM> childDepartmentTreeVMList = departmentMapping.toDepartmentTreeVMList(departmentChild);
                item.setChildren(childDepartmentTreeVMList);
                departmentRecursion(childDepartmentTreeVMList, dataFilterDepartment);
            }
        });
    }

}
