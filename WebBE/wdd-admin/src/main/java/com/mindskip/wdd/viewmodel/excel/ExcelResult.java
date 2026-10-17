package com.mindskip.wdd.viewmodel.excel;

import lombok.Data;

/**
 * @version 1.7.0
 * @description: excel导入结果
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
@Data
public class ExcelResult {

    /**
     * 是否成功
     */
    private Boolean success;
    /**
     * 失败原因
     */
    private String message;

    /**
     * Instantiates a new Excel result.
     */
    public ExcelResult() {
    }

    /**
     * Instantiates a new Excel result.
     *
     * @param success the success
     * @param message the message
     */
    public ExcelResult(Boolean success, String message) {
        this.success = success;
        this.message = message;
    }
}
