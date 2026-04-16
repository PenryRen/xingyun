package com.mindskip.wdd.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mindskip.wdd.base.RestResponse;
import com.mindskip.wdd.domain.Apply;
import com.mindskip.wdd.domain.ApplyAudit;
import com.mindskip.wdd.domain.ApplyDepartment;
import com.mindskip.wdd.domain.UserApply;
import com.mindskip.wdd.domain.enums.MathCalculateEnum;
import com.mindskip.wdd.domain.other.KeyValue;
import com.mindskip.wdd.repository.ApplyAuditMapper;
import com.mindskip.wdd.repository.ApplyDepartmentMapper;
import com.mindskip.wdd.repository.ApplyMapper;
import com.mindskip.wdd.repository.UserApplyMapper;
import com.mindskip.wdd.service.ApplyService;
import com.mindskip.wdd.viewmodel.apply.ApplyPageRequestVM;
import com.mindskip.wdd.viewmodel.apply.ApplyUserPageRequestVM;
import com.mindskip.wdd.viewmodel.apply.ApplyUserPageResponseVM;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @version 1.7.0
 * @description: 报名分类
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
@Service
@AllArgsConstructor
public class ApplyServiceImpl extends ServiceImpl<ApplyMapper, Apply> implements ApplyService {


    private final ApplyMapper applyMapper;
    private final ApplyDepartmentMapper applyDepartmentMapper;
    private final UserApplyMapper userApplyMapper;
    private final ApplyAuditMapper applyAuditMapper;


    @Override
    public List<KeyValue> getDepartmentByApplyId(Integer applyId) {
        return applyDepartmentMapper.getDepartmentByApplyId(applyId);
    }

    @Override
    public void deleteApplyDepartmentByApplyId(Integer applyId) {
        applyDepartmentMapper.updateDeleteByApplyId(applyId);
    }

    @Override
    public void insertApplyDepartment(ApplyDepartment applyDepartment) {
        applyDepartmentMapper.insert(applyDepartment);
    }

    @Override
    public PageInfo<Apply> page(ApplyPageRequestVM requestVM) {
        return PageHelper.startPage(requestVM.getPageIndex(), requestVM.getPageSize(), "id desc").doSelectPageInfo(() ->
                applyMapper.page(requestVM)
        );
    }

    @Override
    public PageInfo<Apply> completePage(ApplyPageRequestVM requestVM) {
        return PageHelper.startPage(requestVM.getPageIndex(), requestVM.getPageSize(), "id desc").doSelectPageInfo(() ->
                applyMapper.completePage(requestVM)
        );
    }

    @Override
    public PageInfo<ApplyUserPageResponseVM> userApplyPage(ApplyUserPageRequestVM applyUserPageRequestVM) {
        return PageHelper.startPage(applyUserPageRequestVM.getPageIndex(), applyUserPageRequestVM.getPageSize(), "createTime desc").doSelectPageInfo(() ->
                applyMapper.userApplyPage(applyUserPageRequestVM)
        );
    }


    @Override
    public PageInfo<ApplyUserPageResponseVM> userAuditPage(ApplyUserPageRequestVM applyUserPageRequestVM) {
        return PageHelper.startPage(applyUserPageRequestVM.getPageIndex(), applyUserPageRequestVM.getPageSize(), "createTime desc").doSelectPageInfo(() ->
                applyAuditMapper.userAuditPage(applyUserPageRequestVM)
        );
    }

    @Override
    @Transactional
    public RestResponse applyCancel(Integer id) {
        try {
            UserApply userApply = userApplyMapper.selectById(id);
            if (userApply.getDeleted()) {
                throw new RuntimeException("报名已取消");
            }
            Integer result = applyMapper.updateApply(userApply.getApplyId(), MathCalculateEnum.Sub.getCode(), null);
            if (result != 1) {
                throw new RuntimeException("取消报名失败");
            }
            userApply.setDeleted(true);
            userApplyMapper.updateById(userApply);
        } catch (Exception exception) {
            return RestResponse.fail(2, exception.getMessage());
        }
        return RestResponse.ok();
    }

    @Override
    @Transactional
    public RestResponse auditApply(Long auditId) {
        try {
            ApplyAudit applyAudit = applyAuditMapper.selectById(auditId);
            Apply apply = applyMapper.selectById(applyAudit.getApplyId());
            UserApply existUserApply = userApplyMapper.getUserApply(applyAudit.getUserId(), apply.getId());
            if (null != existUserApply) {
                throw new RuntimeException("已报名");
            }
            UserApply userApply = new UserApply();
            userApply.setDeleted(false);
            userApply.setApplyId(apply.getId());
            userApply.setUserId(applyAudit.getUserId());
            userApply.setCreateDepartmentId(applyAudit.getCreateDepartmentId());
            userApply.setCreateTime(new Date());
            if (apply.getLimited()) {
                if (apply.getAlreadyApplyCount() < apply.getCount()) {
                    userApplyMapper.insert(userApply);
                    Integer result = applyMapper.updateApply(apply.getId(), MathCalculateEnum.Add.getCode(), true);
                    if (result != 1) {
                        throw new RuntimeException("报名失败");
                    }
                } else {
                    throw new RuntimeException("报名人数已达上限");
                }
            } else {
                userApplyMapper.insert(userApply);
                Integer result = applyMapper.updateApply(apply.getId(), MathCalculateEnum.Add.getCode(), false);
                if (result != 1) {
                    throw new RuntimeException("报名失败");
                }
            }
            applyAudit.setDeleted(true);
            applyAuditMapper.updateById(applyAudit);
        } catch (Exception exception) {
            return RestResponse.fail(2, exception.getMessage());
        }
        return RestResponse.ok();
    }


}
