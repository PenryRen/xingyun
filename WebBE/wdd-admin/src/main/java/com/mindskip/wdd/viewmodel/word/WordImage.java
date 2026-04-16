package com.mindskip.wdd.viewmodel.word;

import lombok.Data;

/**
 * @version 1.7.0
 * @description: word图片
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
@Data
public class WordImage {

    /**
     * 图片id
     */
    private String imageId;
    /**
     * 图片地址
     */
    private String path;
    /**
     * 图片宽度
     */
    private Integer width;
    /**
     * 图片高度
     */
    private Integer height;
    /**
     * 图片长度
     */
    private Long length;
    /**
     * 文件名
     */
    private String fileName;


    /**
     * Instantiates a new Word image.
     */
    public WordImage() {
    }

    /**
     * Instantiates a new Word image.
     *
     * @param imageId the image id
     */
    public WordImage(String imageId) {
        this.imageId = imageId;
    }

    /**
     * Instantiates a new Word image.
     *
     * @param imageId the image id
     * @param width   the width
     * @param height  the height
     */
    public WordImage(String imageId, Integer width, Integer height) {
        this.imageId = imageId;
        this.width = width;
        this.height = height;
    }
}
