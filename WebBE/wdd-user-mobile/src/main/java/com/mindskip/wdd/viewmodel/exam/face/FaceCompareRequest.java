package com.mindskip.wdd.viewmodel.exam.face;

import lombok.Data;

/**
 * @version 1.0.0
 * @description: 比对图片地址
 * Copyright (C), 2024, 麒技团队
 * @date 2024/10/31 10:45
 */
@Data
public class FaceCompareRequest {

    public FaceCompareRequest(String url1, String url2) {
        this.url1 = url1;
        this.url2 = url2;
    }

    /**
     * 图片1
     */
    private String url1;
    /**
     * 图片2
     */
    private String url2;

}
