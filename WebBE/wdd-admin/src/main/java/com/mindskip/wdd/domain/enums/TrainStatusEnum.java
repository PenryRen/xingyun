package com.mindskip.wdd.domain.enums;


import java.util.HashMap;
import java.util.Map;

/**
 * @version 9.0.0
 * @description: 培训列表类型
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
 */
public enum TrainStatusEnum {

    Going(1, "进行中"),
    Pass(2, "合格"),
    NoPass(3, "不合格");

    /**
     * The Code.
     */
    int code;
    /**
     * The Name.
     */
    String name;

    TrainStatusEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    private static final Map<Integer, TrainStatusEnum> keyMap = new HashMap<>();

    static {
        for (TrainStatusEnum item : TrainStatusEnum.values()) {
            keyMap.put(item.getCode(), item);
        }
    }

    /**
     * From code range type enum.
     *
     * @param code the code
     * @return the range type enum
     */
    public static TrainStatusEnum fromCode(Integer code) {
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
