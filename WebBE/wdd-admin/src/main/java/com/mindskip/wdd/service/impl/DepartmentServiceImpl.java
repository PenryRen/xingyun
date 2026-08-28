package com.mindskip.wdd.service.impl;

import cn.hutool.core.util.ReUtil;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.baomidou.mybatisplus.extension.toolkit.SqlHelper;
import com.mindskip.wdd.base.RestResponse;
import com.mindskip.wdd.domain.Department;
import com.mindskip.wdd.domain.enums.TreeMoveEnum;
import com.mindskip.wdd.repository.DepartmentMapper;
import com.mindskip.wdd.service.DepartmentService;
import com.mindskip.wdd.utility.ExamUtil;
import com.mindskip.wdd.viewmodel.department.DepartmentMoveRequestVM;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.IntStream;

/**
 * @version 1.7.0
 * @description: 部门
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
@Service
@AllArgsConstructor
public class DepartmentServiceImpl extends ServiceImpl<DepartmentMapper, Department> implements DepartmentService {

    private final DepartmentMapper departmentMapper;
    private final static String CACHE_NAME = "ueit:department";


    @Override
    @CacheEvict(value = CACHE_NAME, key = "'unique'")
    public boolean save(Department department) {
        return SqlHelper.retBool(departmentMapper.insert(department));
    }

    @Override
    @CacheEvict(value = CACHE_NAME, key = "'unique'")
    public boolean updateById(Department department) {
        return SqlHelper.retBool(departmentMapper.updateById(department));
    }

    @Override
    public List<Department> getRootDepartment() {
        return departmentMapper.getRootDepartment();
    }

    @Override
    public List<Department> getDepartmentByParentId(Integer id) {
        return departmentMapper.getDepartmentByParentId(id);
    }

    @Override
    public int updateLevel(String originalLevel, String targetLevel) {
        String regexLevel = String.format("^%s", ReUtil.escape(originalLevel));
        return departmentMapper.updateLevel(regexLevel, targetLevel);
    }

    @Override
    public Department getByLevel(String level) {
        return departmentMapper.getByLevel(level);
    }

    @Override
    @CacheEvict(value = CACHE_NAME, key = "'unique'")
    public int deleteByLevel(String level) {
        return departmentMapper.deleteByLevel(level);
    }

    @Override
    public List<Department> getDepartmentById(List<Integer> idList) {
        return departmentMapper.getDepartmentById(idList);
    }

    @Override
    @Transactional
    @CacheEvict(value = CACHE_NAME, key = "'unique'")
    public RestResponse move(DepartmentMoveRequestVM departmentMoveRequestVM) {
        TreeMoveEnum treeMoveEnum = TreeMoveEnum.fromName(departmentMoveRequestVM.getDropType());
        Department draggingNode = departmentMapper.selectById(departmentMoveRequestVM.getDraggingNodeId());
        Department dropNode = departmentMapper.selectById(departmentMoveRequestVM.getDropNodeId());
        if (draggingNode.getParentId() != dropNode.getParentId()) {
            String newLevel;
            if (treeMoveEnum == TreeMoveEnum.Inner) {
                newLevel = String.format("%s%s/", dropNode.getLevel(), draggingNode.getName());
            } else {
                if (null == dropNode.getParentId()) {
                    newLevel = String.format("/%s/", draggingNode.getName());
                } else {
                    Department parentDepartment = departmentMapper.selectById(dropNode.getParentId());
                    newLevel = String.format("%s%s/", parentDepartment.getLevel(), draggingNode.getName());
                }
            }
            Department exist = getByLevel(newLevel);
            if (null != exist) {
                return RestResponse.fail(2, "班级已存在");
            }
        }
        switch (treeMoveEnum) {
            case Before:
                draggingNode.setParentId(dropNode.getParentId());
                break;
            case After:
                draggingNode.setParentId(dropNode.getParentId());
                break;
            case Inner:
                draggingNode.setItemOrder(ExamUtil.ItemOrderInit);
                draggingNode.setParentId(dropNode.getId());
                break;
        }
        updateDepartment(draggingNode);
        List<Department> rootDepartment = getRootDepartment();
        departmentLevelUpdate(null, rootDepartment, treeMoveEnum, draggingNode.getId(), dropNode.getId());
        return RestResponse.ok();
    }


    /**
     * 部门位置调整
     *
     * @param parent
     * @param departmentList
     * @param treeMoveEnum
     * @param draggingNodeId
     * @param dropNodeId
     */
    private void departmentLevelUpdate(Department parent, List<Department> departmentList, TreeMoveEnum treeMoveEnum, Integer draggingNodeId, Integer dropNodeId) {
        Department dropNode = departmentList.stream()
                .filter(item -> item.getId().equals(dropNodeId))
                .findFirst().orElse(null);
        if (null != dropNode) {
            if (treeMoveEnum == TreeMoveEnum.Before || treeMoveEnum == TreeMoveEnum.After) {
                Department draggingNode = departmentList.stream()
                        .filter(item -> item.getId().equals(draggingNodeId))
                        .findFirst().get();
                departmentList.remove(draggingNode);
                int dropIndex = departmentList.indexOf(dropNode);
                if (treeMoveEnum == TreeMoveEnum.Before) {
                    departmentList.add(dropIndex, draggingNode);
                } else if (treeMoveEnum == TreeMoveEnum.After) {
                    departmentList.add(dropIndex + 1, draggingNode);
                }
            }
        }

        IntStream.range(0, departmentList.size()).forEach(i -> {
            Department item = departmentList.get(i);
            item.setItemOrder((i + 1) * ExamUtil.ItemOrderInit);
            if (null == parent) {
                item.setLevel(String.format("/%s/", item.getName()));
            } else {
                item.setLevel(String.format("%s%s/", parent.getLevel(), item.getName()));
            }
            updateDepartment(item);
            List<Department> departmentChild = getDepartmentByParentId(item.getId());
            if (departmentChild.size() > 0) {
                departmentLevelUpdate(item, departmentChild, treeMoveEnum, draggingNodeId, dropNodeId);
            }
        });
    }

    /**
     * 部门更新
     *
     * @param department
     */
    private void updateDepartment(Department department) {
        LambdaUpdateWrapper<Department> updateWrapper = new LambdaUpdateWrapper<Department>()
                .eq(Department::getId, department.getId());
        if (null == department.getParentId()) {
            updateWrapper.set(Department::getParentId, null);
        }
        departmentMapper.update(department, updateWrapper);
    }

}
