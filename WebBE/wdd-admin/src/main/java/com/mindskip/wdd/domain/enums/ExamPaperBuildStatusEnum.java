package com.mindskip.wdd.domain.enums;


import java.util.HashMap;
import java.util.Map;

/**
 * @version 1.7.0
 * @description: 组卷规则状态
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
 */
public enum ExamPaperBuildStatusEnum {

    /**
     * Wait publish exam paper build status enum.
     */
    WaitPublish(1, "待发布"),
    /**
     * Publish exam paper build status enum.
     */
    Publish(2, "已发布"),
    /**
     * Recall exam paper build status enum.
     */
    Recall(3, "撤回");

    /**
     * The Code.
     */
    int code;
    /**
     * The Name.
     */
    String name;

    ExamPaperBuildStatusEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    private static final Map<Integer, ExamPaperBuildStatusEnum> keyMap = new HashMap<>();

    static {
        for (ExamPaperBuildStatusEnum item : ExamPaperBuildStatusEnum.values()) {
            keyMap.put(item.getCode(), item);
        }
    }

    /**
     * From code exam paper build status enum.
     *
     * @param code the code
     * @return the exam paper build status enum
     */
    public static ExamPaperBuildStatusEnum fromCode(Integer code) {
        return keyMap.get(code);
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

    /**
     * Gets role name.
     *
     * @return the role name
     */
    public String getRoleName() {
        return "ROLE_" + name;
    }

}
