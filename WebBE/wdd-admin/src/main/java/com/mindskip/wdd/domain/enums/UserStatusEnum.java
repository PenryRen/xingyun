package com.mindskip.wdd.domain.enums;


import java.util.HashMap;
import java.util.Map;

/**
 * @version 1.7.0
 * @description: 用户状态
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
public enum UserStatusEnum {

    /**
     * Enable user status enum.
     */
    Enable(1, "启用"),
    /**
     * Disable user status enum.
     */
    Disable(2, "禁用");

    /**
     * The Code.
     */
    int code;
    /**
     * The Name.
     */
    String name;

    UserStatusEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    private static final Map<Integer, UserStatusEnum> keyMap = new HashMap<>();

    static {
        for (UserStatusEnum item : UserStatusEnum.values()) {
            keyMap.put(item.getCode(), item);
        }
    }

    /**
     * From code user status enum.
     *
     * @param code the code
     * @return the user status enum
     */
    public static UserStatusEnum fromCode(Integer code) {
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
