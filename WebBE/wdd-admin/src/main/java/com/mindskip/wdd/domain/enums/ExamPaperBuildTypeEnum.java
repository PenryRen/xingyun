package com.mindskip.wdd.domain.enums;


import java.util.HashMap;
import java.util.Map;

/**
 * @version 1.7.0
 * @description: 组卷类型
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
 */
public enum ExamPaperBuildTypeEnum {

    /**
     * Manual exam paper build type enum.
     */
    MANUAL(1, "人工组卷"),
    /**
     * Extract exam paper build type enum.
     */
    EXTRACT(2, "抽题组卷"),
    /**
     * Random exam paper build type enum.
     */
    RANDOM(3, "随机组卷"),
    /**
     * Operate exam paper build type enum.
     */
    OPERATE(4, "实训组卷");

    /**
     * The Code.
     */
    int code;
    /**
     * The Name.
     */
    String name;

    ExamPaperBuildTypeEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    private static final Map<Integer, ExamPaperBuildTypeEnum> keyMap = new HashMap<>();

    static {
        for (ExamPaperBuildTypeEnum item : ExamPaperBuildTypeEnum.values()) {
            keyMap.put(item.getCode(), item);
        }
    }

    /**
     * From code exam paper build type enum.
     *
     * @param code the code
     * @return the exam paper build type enum
     */
    public static ExamPaperBuildTypeEnum fromCode(Integer code) {
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
