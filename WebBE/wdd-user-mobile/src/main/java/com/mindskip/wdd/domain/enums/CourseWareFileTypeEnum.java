package com.mindskip.wdd.domain.enums;


import java.util.HashMap;
import java.util.Map;

/**
 * @version 1.7.0
 * @description: 课件类型
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
public enum CourseWareFileTypeEnum {

    /**
     * Video course ware file type enum.
     */
    VIDEO(1, "视频"),
    /**
     * Document course ware file type enum.
     */
    DOCUMENT(2, "文档");

    /**
     * The Code.
     */
    int code;
    /**
     * The Name.
     */
    String name;

    CourseWareFileTypeEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    private static final Map<Integer, CourseWareFileTypeEnum> keyMap = new HashMap<>();

    static {
        for (CourseWareFileTypeEnum item : CourseWareFileTypeEnum.values()) {
            keyMap.put(item.getCode(), item);
        }
    }

    /**
     * From code course ware file type enum.
     *
     * @param code the code
     * @return the course ware file type enum
     */
    public static CourseWareFileTypeEnum fromCode(Integer code) {
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
