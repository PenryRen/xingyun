package com.mindskip.wdd.domain.enums;


import java.util.HashMap;
import java.util.Map;

/**
 * @version 1.7.0
 * @description: 试卷发布范围
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
public enum RangeTypeEnum {

    /**
     * Apply range type enum.
     */
    Apply(1, "报名"),
    /**
     * Customize range type enum.
     */
    Customize(2, "自定义");

    /**
     * The Code.
     */
    int code;
    /**
     * The Name.
     */
    String name;

    RangeTypeEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    private static final Map<Integer, RangeTypeEnum> keyMap = new HashMap<>();

    static {
        for (RangeTypeEnum item : RangeTypeEnum.values()) {
            keyMap.put(item.getCode(), item);
        }
    }

    /**
     * From code range type enum.
     *
     * @param code the code
     * @return the range type enum
     */
    public static RangeTypeEnum fromCode(Integer code) {
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
