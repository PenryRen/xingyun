package com.mindskip.wdd.domain.enums;


import java.util.HashMap;
import java.util.Map;

/**
 * @version 6.1.0
 * @description: 报名状态
 * Copyright (C), 2025, 麟航团队
 * @date 2025/9/12 13:50
 */
public enum TreeMoveEnum {

    Before(1, "before"),

    After(2, "after"),

    Inner(3, "inner");

    /**
     * The Code.
     */
    int code;
    /**
     * The Name.
     */
    String name;

    TreeMoveEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    private static final Map<Integer, TreeMoveEnum> keyMap = new HashMap<>();
    private static final Map<String, TreeMoveEnum> valueMap = new HashMap<>();

    static {
        for (TreeMoveEnum item : TreeMoveEnum.values()) {
            keyMap.put(item.getCode(), item);
            valueMap.put(item.getName(), item);
        }
    }

    /**
     * From code apply status enum.
     *
     * @param code the code
     * @return the apply status enum
     */
    public static TreeMoveEnum fromCode(Integer code) {
        return keyMap.get(code);
    }


    public static TreeMoveEnum fromName(String name) {
        return valueMap.get(name);
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
