package com.mindskip.wdd.service.enums;

/**
 * @version 1.7.0
 * @description: 操作结果
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
 */
public enum ResultEnum {

    /**
     * Success result enum.
     */
    SUCCESS(1, "成功"),
    /**
     * Fail result enum.
     */
    FAIL(2, "失败");

    /**
     * The Code.
     */
    int code;
    /**
     * The Name.
     */
    String Name;

    ResultEnum(int code, String name) {
        this.code = code;
        Name = name;
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
        return Name;
    }

    /**
     * Sets name.
     *
     * @param name the name
     */
    public void setName(String name) {
        Name = name;
    }


}
