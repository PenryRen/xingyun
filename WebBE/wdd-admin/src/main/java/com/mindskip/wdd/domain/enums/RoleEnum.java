package com.mindskip.wdd.domain.enums;


import java.util.HashMap;
import java.util.Map;

/**
 * @version 1.7.0
 * @description: 角色类型
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
public enum RoleEnum {

    /**
     * Employee role enum.
     */
    EMPLOYEE(1, "员工"),
    /**
     * Admin role enum.
     */
    ADMIN(2, "管理员");

    /**
     * The Code.
     */
    int code;
    /**
     * The Name.
     */
    String name;

    RoleEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    private static final Map<Integer, RoleEnum> keyMap = new HashMap<>();

    static {
        for (RoleEnum item : RoleEnum.values()) {
            keyMap.put(item.getCode(), item);
        }
    }

    /**
     * From code role enum.
     *
     * @param code the code
     * @return the role enum
     */
    public static RoleEnum fromCode(Integer code) {
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
