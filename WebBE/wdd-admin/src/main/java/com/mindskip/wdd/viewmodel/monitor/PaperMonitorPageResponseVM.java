package com.mindskip.wdd.viewmodel.monitor;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @version 6.5.0
 * @description: 答卷监考返回
 * Copyright (C), 2025, 麟航团队
 * @date 2025/9/25 10:45
 */
@Data
public class PaperMonitorPageResponseVM implements Serializable {

    /**
     * 答卷Id
     */
    private Long id;

    /**
     * 试卷名称
     */
    private String paperName;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 真实姓名
     */
    private String realName;


    private String imagePath;

    /**
     * 工号
     */
    private String workNo;

    /**
     * 身份证号
     */
    private String idCard;

    /**
     * 职位
     */
    private String jobTitle;

    /**
     * 作弊次数
     */
    private Integer cheatCount;

    /**
     * 部门
     */
    private String departmentLevel;

    /**
     * 部门
     */
    private Integer departmentId;


    /**
     * 最终得分
     */
    private String userScore;

    /**
     * 试卷总分
     */
    private String paperScore;


    /**
     * 正确题数
     */
    private Integer questionCorrect;

    /**
     * 总题数
     */
    private Integer questionCount;

    /**
     * 耗时
     */
    private Integer doTime;

    /**
     * 耗时
     */
    private String doTimeStr;

    /**
     * 创建人
     */
    private Integer createUser;

    /**
     * 更新时间
     */
    private String updateTime;

    /**
     * 是否抓拍
     */
    private Boolean capture;

}
