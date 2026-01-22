package com.mindskip.wdd.domain.enums;


import java.util.HashMap;
import java.util.Map;

/**
 * @version 9.0.0
 * @description: 培训列表类型
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
 */
public enum TrainTargetTypeEnum {

    CourseWare(1, "课件"),
    ExamPaper(2, "试卷"),
    Credential(3, "证书");

    /**
     * The Code.
     */
    int code;
    /**
     * The Name.
     */
    String name;

    TrainTargetTypeEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    private static final Map<Integer, TrainTargetTypeEnum> keyMap = new HashMap<>();

    static {
        for (TrainTargetTypeEnum item : TrainTargetTypeEnum.values()) {
            keyMap.put(item.getCode(), item);
        }
    }

    /**
     * From code range type enum.
     *
     * @param code the code
     * @return the range type enum
     */
    public static TrainTargetTypeEnum fromCode(Integer code) {
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
