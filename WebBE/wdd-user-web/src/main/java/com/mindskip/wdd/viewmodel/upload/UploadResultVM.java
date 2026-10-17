package com.mindskip.wdd.viewmodel.upload;

import lombok.Data;

import java.util.Arrays;

/**
 * @version 1.7.0
 * @description: 上传结果
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
@Data
public class UploadResultVM {

    public UploadResultVM() {
    }


    /**
     * Instantiates a new Upload result vm.
     *
     * @param url   the url
     * @param state the state
     */
    public UploadResultVM(String url, String state) {
        this.url = url;
        this.state = state;
    }


    /**
     * Instantiates a new Upload result vm.
     *
     * @param original the original
     * @param name     the name
     * @param url      the url
     * @param size     the size
     * @param type     the type
     * @param state    the state
     */
    public UploadResultVM(String original, String name, String url, Long size, String type, String state) {
        this.original = original;
        this.name = name;
        this.url = url;
        this.size = size;
        this.type = type;
        this.state = state;
    }

    private String original;
    /**
     * 文件名
     */
    private String name;
    /**
     * 文件地址
     */
    private String url;
    private Long size;
    private String type;
    private String state;
    private Integer errno = 0;
    private Object data;


    public static UploadResultVM ok(String url, String alt) {
        UploadResultVM uploadResultVM = new UploadResultVM();
        UploadItemVM UploadItemVM = new UploadItemVM();
        UploadItemVM.setUrl(url);
        UploadItemVM.setAlt(alt);
        uploadResultVM.setData(Arrays.asList(UploadItemVM));
        return uploadResultVM;
    }
}
