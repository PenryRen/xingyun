package com.mindskip.wdd.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mindskip.wdd.domain.*;
import com.mindskip.wdd.domain.enums.MathCalculateEnum;
import com.mindskip.wdd.repository.ApplyArchiveMapper;
import com.mindskip.wdd.repository.ApplyAuditMapper;
import com.mindskip.wdd.repository.ApplyMapper;
import com.mindskip.wdd.repository.UserApplyMapper;
import com.mindskip.wdd.service.ApplyService;
import com.mindskip.wdd.viewmodel.apply.ApplyPageRequestVM;
import com.mindskip.wdd.viewmodel.apply.UserApplyPageResponseVM;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @version 1.7.0
 * @description: 报名
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
@Service
@AllArgsConstructor
public class ApplyServiceImpl extends ServiceImpl<ApplyMapper, Apply> implements ApplyService {


    private final ApplyMapper applyMapper;
    private final UserApplyMapper userApplyMapper;
    private final ApplyAuditMapper applyAuditMapper;
    private final ApplyArchiveMapper applyArchiveMapper;


    @Override
    public PageInfo<Apply> page(ApplyPageRequestVM requestVM) {
        return PageHelper.startPage(requestVM.getPageIndex(), requestVM.getPageSize(), "id desc").doSelectPageInfo(() ->
                applyMapper.page(requestVM)
        );
    }

    @Override
    public PageInfo<UserApplyPageResponseVM> record(ApplyPageRequestVM requestVM) {
        return PageHelper.startPage(requestVM.getPageIndex(), requestVM.getPageSize(), "id desc").doSelectPageInfo(() ->
                userApplyMapper.page(requestVM)
        );
    }

    @Override
    public UserApply getUserApply(Integer userId, Integer applyId) {
        return userApplyMapper.getUserApply(userId, applyId);
    }

    @Override
    public UserApply getUserApply(Long id) {
        return userApplyMapper.selectById(id);
    }

    @Override
    public int updateUserApply(UserApply userApply) {
        return userApplyMapper.updateById(userApply);
    }

    @Override
    @Transactional
    public Boolean goApply(Integer applyId, User user) {
        UserApply existUserApply = getUserApply(user.getId(), applyId);
        if (null != existUserApply) {
            throw new RuntimeException("已报名");
        }
        Apply apply = applyMapper.selectById(applyId);
        if (apply.getNeedAudit()) {
            ApplyAudit existApplyAudit = applyAuditMapper.getApplyAuditByUser(applyId, user.getId());
            if (null == existApplyAudit) {
                ApplyAudit applyAudit = new ApplyAudit();
                applyAudit.setDeleted(false);
                applyAudit.setApplyId(applyId);
                applyAudit.setUserId(user.getId());
                applyAudit.setCreateDepartmentId(user.getDepartmentId());
                applyAudit.setCreateTime(new Date());
                applyAuditMapper.insert(applyAudit);
            }
        } else {
            UserApply userApply = new UserApply();
            userApply.setDeleted(false);
            userApply.setApplyId(applyId);
            userApply.setUserId(user.getId());
            userApply.setCreateDepartmentId(user.getDepartmentId());
            userApply.setCreateTime(new Date());
            if (apply.getLimited()) {
                if (apply.getAlreadyApplyCount() < apply.getCount()) {
                    userApplyMapper.insert(userApply);
                    Integer result = applyMapper.updateApply(applyId, MathCalculateEnum.Add.getCode(), true);
                    if (result != 1) {
                        throw new RuntimeException("报名失败");
                    }
                } else {
                    throw new RuntimeException("报名人数已达上限");
                }
            } else {
                userApplyMapper.insert(userApply);
                Integer result = applyMapper.updateApply(applyId, MathCalculateEnum.Add.getCode(), false);
                if (result != 1) {
                    throw new RuntimeException("报名失败");
                }
            }
        }
        return true;
    }


    @Override
    @Transactional
    public void cancelApply(Integer applyId, Long userApplyId) {
        UserApply userApply = getUserApply(userApplyId);
        if (userApply.getDeleted()) {
            throw new RuntimeException("报名已取消");
        }
        userApply.setDeleted(true);
        updateUserApply(userApply);
        Integer result = applyMapper.updateApply(applyId, MathCalculateEnum.Sub.getCode(), null);
        if (result != 1) {
            throw new RuntimeException("取消报名失败");
        }
    }

    @Override
    public List<ApplyArchive> selectRootTree() {
        return applyArchiveMapper.selectRootTree();
    }

    @Override
    public ApplyArchive getApplyArchiveById(Integer id) {
        return applyArchiveMapper.selectById(id);
    }

    @Override
    public List<ApplyArchive> getApplyArchiveByLevel(String level) {
        return applyArchiveMapper.getApplyArchiveByLevel(level);
    }

}
