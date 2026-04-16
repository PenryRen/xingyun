package com.mindskip.wdd.domain.enums;


import java.util.HashMap;
import java.util.Map;

/**
 * @version 1.7.0
 * @description: 报名状态
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
public enum ApplyStatusEnum {

    /**
     * Wait publish apply status enum.
     */
    WaitPublish(1, "待发布"),
    /**
     * Publish apply status enum.
     */
    Publish(2, "已发布"),
    /**
     * Close apply status enum.
     */
    Close(3, "已关闭");

    /**
     * The Code.
     */
    int code;
    /**
     * The Name.
     */
    String name;

    ApplyStatusEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    private static final Map<Integer, ApplyStatusEnum> keyMap = new HashMap<>();

    static {
        for (ApplyStatusEnum item : ApplyStatusEnum.values()) {
            keyMap.put(item.getCode(), item);
        }
    }

    /**
     * From code apply status enum.
     *
     * @param code the code
     * @return the apply status enum
     */
    public static ApplyStatusEnum fromCode(Integer code) {
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
