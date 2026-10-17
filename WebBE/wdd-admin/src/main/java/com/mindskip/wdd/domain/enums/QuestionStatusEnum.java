package com.mindskip.wdd.domain.enums;

/**
 * @version 1.7.0
 * @description: 题目状态
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
public enum QuestionStatusEnum {

    /**
     * Ok question status enum.
     */
    OK(1, "正常"),
    /**
     * Publish question status enum.
     */
    Publish(2, "发布");

    /**
     * The Code.
     */
    int code;
    /**
     * The Name.
     */
    String name;

    QuestionStatusEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }


    /**
     * Gets code.
     *
     * @return the code
     */
    public int getCode() {
        return code;
    }

    /**
     * Sets code.
     *
     * @param code the code
     */
    public void setCode(int code) {
        this.code = code;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets name.
     *
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }


}
