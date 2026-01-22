package com.mindskip.wdd.controller;

import com.github.pagehelper.PageInfo;
import com.mindskip.wdd.base.BaseApiController;
import com.mindskip.wdd.base.RestResponse;
import com.mindskip.wdd.domain.Apply;
import com.mindskip.wdd.domain.ApplyArchive;
import com.mindskip.wdd.domain.User;
import com.mindskip.wdd.domain.enums.ApplyStatusEnum;
import com.mindskip.wdd.mapping.ApplyMapping;
import com.mindskip.wdd.service.ApplyService;
import com.mindskip.wdd.utility.DateTimeUtil;
import com.mindskip.wdd.utility.PageInfoHelper;
import com.mindskip.wdd.viewmodel.apply.ApplyArchiveVM;
import com.mindskip.wdd.viewmodel.apply.ApplyPageRequestVM;
import com.mindskip.wdd.viewmodel.apply.ApplyPageResponseVM;
import com.mindskip.wdd.viewmodel.apply.UserApplyPageResponseVM;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @version 1.7.0
 * @description: 报名
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
 */
@RestController
@AllArgsConstructor
@RequestMapping(value = "/api/apply")
public class ApplyController extends BaseApiController {

    private final ApplyService applyService;
    private final ApplyMapping applyMapping;


    /**
     * 报名分类列表
     *
     * @return {@link RestResponse}<{@link List}<{@link ApplyArchiveVM}>>
     */
    @PostMapping("/archive/list")
    public RestResponse<List<ApplyArchiveVM>> archiveList() {
        List<ApplyArchive> rootTree = applyService.selectRootTree();
        List<ApplyArchiveVM> rootVM = applyMapping.toApplyArchiveVMList(rootTree);
        return RestResponse.ok(rootVM);
    }


    /**
     * 报名分页
     *
     * @param model the model
     * @return the rest response
     */
    @PostMapping("/page")
    public RestResponse<PageInfo<ApplyPageResponseVM>> page(@RequestBody ApplyPageRequestVM model) {
        if (model.getApplyArchiveId() != null) {
            ApplyArchive rootApplyArchive = applyService.getApplyArchiveById(model.getApplyArchiveId());
            List<ApplyArchive> applyArchiveList = applyService.getApplyArchiveByLevel(rootApplyArchive.getLevel());
            List<Integer> applyArchiveIdList = applyArchiveList.stream().map(item -> item.getId())
                    .collect(Collectors.toList());
            model.setApplyArchiveIdList(applyArchiveIdList);
        }

        User user = getCurrentUser();
        model.setDepartmentId(user.getDepartmentId());
        model.setNow(new Date());
        model.setUserId(user.getId());
        PageInfo<Apply> pageInfo = applyService.page(model);
        PageInfo<ApplyPageResponseVM> page = PageInfoHelper.copyMap(pageInfo, d -> {
            ApplyPageResponseVM applyPageResponseVM = applyMapping.toApplyPageResponseVM(d);
            applyPageResponseVM.setLimitCount(String.format("%s / %s", d.getAlreadyApplyCount(), d.getLimited() ? d.getCount().toString() : "无限"));
            return applyPageResponseVM;
        });
        return RestResponse.ok(page);
    }


    /**
     * 已报名记录
     *
     * @param model the model
     * @return the rest response
     */
    @PostMapping("/record")
    public RestResponse<PageInfo<UserApplyPageResponseVM>> record(@RequestBody ApplyPageRequestVM model) {
        model.setUserId(getCurrentUser().getId());
        PageInfo<UserApplyPageResponseVM> pageInfo = applyService.record(model);
        pageInfo.getList().forEach(d -> {
            d.setCreateTimeStr(DateTimeUtil.dateTimeFullFormat(d.getCreateTime()));
            d.setLimitStartTimeStr(DateTimeUtil.dateTimeFullFormat(d.getLimitStartTime()));
            d.setLimitEndTimeStr(DateTimeUtil.dateTimeFullFormat(d.getLimitEndTime()));
            d.setApplyEndTimeStr(DateTimeUtil.dateTimeFullFormat(d.getApplyEndTime()));
            d.setApplyDateTimeStr(DateTimeUtil.dateTimeFullFormat(d.getAppDateTime()));
            d.setLimitCount(String.format("%s / %s", d.getAlreadyApplyCount(), d.getLimited() ? d.getCount().toString() : "无限"));
            ApplyStatusEnum applyStatusEnum = ApplyStatusEnum.fromCode(d.getStatus());
            switch (applyStatusEnum) {
                case Publish:
                    if (new Date().before(d.getApplyEndTime())) {
                        d.setStatus(1);
                        d.setStatusStr("进行中");
                    } else {
                        d.setStatus(2);
                        d.setStatusStr("已结束");
                    }
                    break;
                case Close:
                    d.setStatus(2);
                    d.setStatusStr(ApplyStatusEnum.Close.getName());
                    break;
            }
        });
        return RestResponse.ok(pageInfo);
    }


    /**
     * 去报名
     *
     * @param id the id
     * @return the rest response
     */
    @PostMapping("/go/{id}")
    public RestResponse go(@PathVariable Integer id) {
        Apply apply = applyService.getById(id);
        try {
            applyService.goApply(id, getCurrentUser());
        } catch (Exception exception) {
            return RestResponse.fail(2, exception.getMessage());
        }
        if (apply.getNeedAudit()) {
            return RestResponse.ok("报名申请已提交，请等待审核");
        } else {
            return RestResponse.ok("报名成功");
        }
    }


    /**
     * 取消报名
     *
     * @param applyId     the apply id
     * @param userApplyId the user apply id
     * @return the rest response
     */
    @PostMapping("/cancel/{applyId}/{userApplyId}")
    public RestResponse cancel(@PathVariable Integer applyId, @PathVariable Long userApplyId) {
        try {
            applyService.cancelApply(applyId, userApplyId);
        } catch (Exception exception) {
            return RestResponse.fail(2, exception.getMessage());
        }
        return RestResponse.ok();
    }


}
