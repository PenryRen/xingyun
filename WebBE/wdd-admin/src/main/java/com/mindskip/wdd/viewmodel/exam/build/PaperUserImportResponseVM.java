package com.mindskip.wdd.viewmodel.exam.build;

import lombok.Data;

import java.util.List;

/**
 * @version 8.6.0
 * @description: 试卷用户导入
 * Copyright (C), 2024, 麒技团队
 * @date 2024/9/6 10:45
 */
@Data
public class PaperUserImportResponseVM {
    /**
     * 文件地址
     */
    private String filePath;
    /**
     * 用户列表
     */
    private List<PaperUserImportVM> userList;
}
