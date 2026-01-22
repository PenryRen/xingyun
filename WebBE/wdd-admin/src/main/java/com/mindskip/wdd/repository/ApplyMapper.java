package com.mindskip.wdd.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mindskip.wdd.domain.Apply;
import com.mindskip.wdd.viewmodel.apply.ApplyPageRequestVM;
import com.mindskip.wdd.viewmodel.apply.ApplyUserPageRequestVM;
import com.mindskip.wdd.viewmodel.apply.ApplyUserPageResponseVM;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @version 1.7.0
 * @description: 报名
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
 */
@Mapper
public interface ApplyMapper extends BaseMapper<Apply> {

    /**
     * 更新报名人数
     *
     * @param id            the id
     * @param mathCalculate the math calculate
     * @param limited       the limited
     * @return the int
     */
    int updateApply(Integer id, Integer mathCalculate, Boolean limited);

    /**
     * 报名分页
     *
     * @param requestVM the request vm
     * @return the list
     */
    List<Apply> page(ApplyPageRequestVM requestVM);

    /**
     * 已结束的报名分页
     *
     * @param requestVM the request vm
     * @return the list
     */
    List<Apply> completePage(ApplyPageRequestVM requestVM);

    /**
     * 已参加的报名用户
     *
     * @param applyUserPageRequestVM the apply user page request vm
     * @return the list
     */
    List<ApplyUserPageResponseVM> userApplyPage(ApplyUserPageRequestVM applyUserPageRequestVM);
}