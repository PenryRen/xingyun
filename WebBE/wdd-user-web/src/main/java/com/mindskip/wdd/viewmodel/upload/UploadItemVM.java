package com.mindskip.wdd.viewmodel.upload;

import lombok.Data;

/**
 * @version 1.7.0
 * @description: 上传返回文件
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
@Data
public class UploadItemVM {

    /**
     * 图片地址
     */
    private String url;
    private String alt;
    private String href;

    public UploadItemVM() {
    }

    public UploadItemVM(String url) {
        this.url = url;
    }


    public UploadItemVM(String url, String alt) {
        this.url = url;
        this.alt = alt;
    }

    public UploadItemVM(String url, String alt, String href) {
        this.url = url;
        this.alt = alt;
        this.href = href;
    }
}
