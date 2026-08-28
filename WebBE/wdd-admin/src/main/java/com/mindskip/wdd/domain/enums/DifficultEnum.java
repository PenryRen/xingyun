package com.mindskip.wdd.domain.enums;


import java.util.HashMap;
import java.util.Map;

/**
 * @version 1.7.0
 * @description: 题目难度
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
public enum DifficultEnum {

    /**
     * Easy difficult enum.
     */
    EASY(1, "简单"),
    /**
     * Simple difficult enum.
     */
    SIMPLE(2, "较难"),
    /**
     * Hard difficult enum.
     */
    HARD(3, "困难");

    /**
     * The Code.
     */
    int code;
    /**
     * The Name.
     */
    String name;

    DifficultEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    private static final Map<Integer, DifficultEnum> keyMap = new HashMap<>();

    static {
        for (DifficultEnum item : DifficultEnum.values()) {
            keyMap.put(item.getCode(), item);
        }
    }

    /**
     * From code difficult enum.
     *
     * @param code the code
     * @return the difficult enum
     */
    public static DifficultEnum fromCode(Integer code) {
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

}
