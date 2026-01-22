package com.mindskip.wdd.viewmodel.course.ware;

import com.mindskip.wdd.base.BasePage;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;


/**
 * @version 9.0.0
 * @description: 课件观看
 * Copyright (C), 2024, 麒技团队
 * @date 2024/9/16 10:45
 */
@Data
public class WatchRequestVM {
    /**
     * 用户培训项
     */
    @NotNull
    private Long trainItemUserId;
    /**
     * 课件id
     */
    @NotNull
    private Integer courseWareId;
    /**
     * 当前观看时间
     */
    @NotNull
    private Integer watchTime;
    /**
     * 是否观看完
     */
    @NotNull
    private Boolean watchEnd;
}
