package com.mindskip.wdd.viewmodel.exam.build;

import lombok.Data;

/**
 * @version 8.6.0
 * @description: 试卷用户导入
 * Copyright (C), 2024, 麒技团队
 * @date 2024/9/6 10:45
 */
@Data
public class PaperUserImportVM {
    /**
     * 用户Id
     */
    private Integer id;
    /**
     * 用户名
     */
    private String userName;
    /**
     * 真实姓名
     */
    private String realName;
    /**
     * 部门
     */
    private Integer departmentId;

    public PaperUserImportVM(Integer id, String userName, String realName, Integer departmentId) {
        this.id = id;
        this.userName = userName;
        this.realName = realName;
        this.departmentId = departmentId;
    }
}
