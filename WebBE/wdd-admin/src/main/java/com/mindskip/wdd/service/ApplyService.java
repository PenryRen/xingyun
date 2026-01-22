package com.mindskip.wdd.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.mindskip.wdd.base.RestResponse;
import com.mindskip.wdd.domain.Apply;
import com.mindskip.wdd.domain.ApplyDepartment;
import com.mindskip.wdd.domain.other.KeyValue;
import com.mindskip.wdd.viewmodel.apply.ApplyPageRequestVM;
import com.mindskip.wdd.viewmodel.apply.ApplyUserPageRequestVM;
import com.mindskip.wdd.viewmodel.apply.ApplyUserPageResponseVM;

import java.util.List;

/**
 * @version 1.7.0
 * @description: 报名分类
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
 */
public interface ApplyService extends IService<Apply> {
    /**
     * 根据报名id查询报名分类
     *
     * @param applyId
     * @return
     */
    List<KeyValue> getDepartmentByApplyId(Integer applyId);

    /**
     * 根据报名id删除报名部门
     *
     * @param applyId
     */
    void deleteApplyDepartmentByApplyId(Integer applyId);

    /**
     * 插入报名部门
     *
     * @param applyDepartment
     */
    void insertApplyDepartment(ApplyDepartment applyDepartment);


    /**
     * 报名分页查询
     *
     * @param requestVM
     * @return
     */
    PageInfo<Apply> page(ApplyPageRequestVM requestVM);

    /**
     * 查询结束报名
     *
     * @param requestVM
     * @return
     */
    PageInfo<Apply> completePage(ApplyPageRequestVM requestVM);

    /**
     * 已参加报名用户
     *
     * @param applyUserPageRequestVM
     * @return
     */
    PageInfo<ApplyUserPageResponseVM> userApplyPage(ApplyUserPageRequestVM applyUserPageRequestVM);


    /**
     * 报名申请人员列表
     *
     * @param applyUserPageRequestVM
     * @return {@link PageInfo}<{@link ApplyUserPageResponseVM}>
     */
    PageInfo<ApplyUserPageResponseVM> userAuditPage(ApplyUserPageRequestVM applyUserPageRequestVM);


    /**
     * 报名取消
     *
     * @param id
     * @return {@link RestResponse}
     */
    RestResponse applyCancel(Integer id);


    /**
     * 报名申请操作
     *
     * @param auditId
     * @return {@link RestResponse}
     */
    RestResponse auditApply(Long auditId);
}
