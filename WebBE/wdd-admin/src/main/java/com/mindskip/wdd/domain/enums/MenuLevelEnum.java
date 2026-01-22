package com.mindskip.wdd.domain.enums;


import java.util.HashMap;
import java.util.Map;

/**
 * @version 1.7.0
 * @description: 菜单层级
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
 */
public enum MenuLevelEnum {

    /**
     * One menu level enum.
     */
    ONE(1, "一级菜单"),
    /**
     * Two menu level enum.
     */
    TWO(2, "二级菜单");

    /**
     * The Code.
     */
    int code;
    /**
     * The Name.
     */
    String name;

    MenuLevelEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    private static final Map<Integer, MenuLevelEnum> keyMap = new HashMap<>();

    static {
        for (MenuLevelEnum item : MenuLevelEnum.values()) {
            keyMap.put(item.getCode(), item);
        }
    }

    /**
     * From code menu level enum.
     *
     * @param code the code
     * @return the menu level enum
     */
    public static MenuLevelEnum fromCode(Integer code) {
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
