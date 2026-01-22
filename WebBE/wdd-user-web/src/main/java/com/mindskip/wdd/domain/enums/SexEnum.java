package com.mindskip.wdd.domain.enums;


import java.util.HashMap;
import java.util.Map;

/**
 * @version 1.7.0
 * @description: 性别
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
 */
public enum SexEnum {

    /**
     * Man sex enum.
     */
    Man(1, "男"),
    /**
     * Woman sex enum.
     */
    Woman(2, "女");

    /**
     * The Code.
     */
    int code;
    /**
     * The Name.
     */
    String name;

    SexEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    private static final Map<Integer, SexEnum> keyMap = new HashMap<>();

    static {
        for (SexEnum item : SexEnum.values()) {
            keyMap.put(item.getCode(), item);
        }
    }

    /**
     * From code sex enum.
     *
     * @param code the code
     * @return the sex enum
     */
    public static SexEnum fromCode(Integer code) {
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
