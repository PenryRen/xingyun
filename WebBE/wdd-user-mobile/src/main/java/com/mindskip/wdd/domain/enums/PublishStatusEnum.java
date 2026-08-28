package com.mindskip.wdd.domain.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * @version 1.7.0
 * @description: 试卷发布状态
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
public enum PublishStatusEnum {

    /**
     * No publish publish status enum.
     */
    NoPublish(1, "未发布"),
    /**
     * Publish publish status enum.
     */
    Publish(2, "已发布"),
    /**
     * Recall publish status enum.
     */
    Recall(3, "撤回");

    /**
     * The Code.
     */
    int code;
    /**
     * The Name.
     */
    String name;

    PublishStatusEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }


    private static final Map<Integer, PublishStatusEnum> keyMap = new HashMap<>();

    static {
        for (PublishStatusEnum item : PublishStatusEnum.values()) {
            keyMap.put(item.getCode(), item);
        }
    }

    /**
     * From code publish status enum.
     *
     * @param code the code
     * @return the publish status enum
     */
    public static PublishStatusEnum fromCode(Integer code) {
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
