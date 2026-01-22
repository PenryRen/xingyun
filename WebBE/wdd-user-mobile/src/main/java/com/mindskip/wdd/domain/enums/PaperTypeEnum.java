package com.mindskip.wdd.domain.enums;


import java.util.HashMap;
import java.util.Map;

/**
 * @version 4.1.0
 * @description: 考试类型
 * Copyright (C), 2024, 麒技团队
 * @date 2024/9/25 10:45
 */
public enum PaperTypeEnum {

    /**
     * Man sex enum.
     */
    Official(1, "正考"),
    /**
     * Woman sex enum.
     */
    Resit(2, "补考");

    /**
     * The Code.
     */
    int code;
    /**
     * The Name.
     */
    String name;

    PaperTypeEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    private static final Map<Integer, PaperTypeEnum> keyMap = new HashMap<>();

    static {
        for (PaperTypeEnum item : PaperTypeEnum.values()) {
            keyMap.put(item.getCode(), item);
        }
    }

    /**
     * From code sex enum.
     *
     * @param code the code
     * @return the sex enum
     */
    public static PaperTypeEnum fromCode(Integer code) {
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
