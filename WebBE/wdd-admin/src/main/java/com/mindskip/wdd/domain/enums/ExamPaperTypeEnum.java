package com.mindskip.wdd.domain.enums;


import java.util.HashMap;
import java.util.Map;

/**
 * @version 1.7.0
 * @description: 试卷类型
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
 */
public enum ExamPaperTypeEnum {

    /**
     * Manual exam paper type enum.
     */
    MANUAL(1, "人工试卷"),
    /**
     * Extract exam paper type enum.
     */
    EXTRACT(2, "抽题试卷"),
    /**
     * Random exam paper type enum.
     */
    RANDOM(3, "随机试卷"),
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

    ExamPaperTypeEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    private static final Map<Integer, ExamPaperTypeEnum> keyMap = new HashMap<>();

    static {
        for (ExamPaperTypeEnum item : ExamPaperTypeEnum.values()) {
            keyMap.put(item.getCode(), item);
        }
    }

    /**
     * From code exam paper type enum.
     *
     * @param code the code
     * @return the exam paper type enum
     */
    public static ExamPaperTypeEnum fromCode(Integer code) {
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
