package com.mindskip.wdd.domain.enums;


import java.util.HashMap;
import java.util.Map;

/**
 * @version 1.7.0
 * @description: 数据计算
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
 */
public enum MathCalculateEnum {

    /**
     * Add math calculate enum.
     */
    Add(1, "加法"),
    /**
     * Sub math calculate enum.
     */
    Sub(2, "减法");

    /**
     * The Code.
     */
    int code;
    /**
     * The Name.
     */
    String name;

    MathCalculateEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    private static final Map<Integer, MathCalculateEnum> keyMap = new HashMap<>();
    private static final Map<String, MathCalculateEnum> nameMap = new HashMap<>();

    static {
        for (MathCalculateEnum item : MathCalculateEnum.values()) {
            keyMap.put(item.getCode(), item);
            nameMap.put(item.getName(), item);
        }
    }

    /**
     * From code math calculate enum.
     *
     * @param code the code
     * @return the math calculate enum
     */
    public static MathCalculateEnum fromCode(Integer code) {
        return keyMap.get(code);
    }

    /**
     * From name math calculate enum.
     *
     * @param name the name
     * @return the math calculate enum
     */
    public static MathCalculateEnum fromName(String name) {
        return nameMap.get(name);
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
