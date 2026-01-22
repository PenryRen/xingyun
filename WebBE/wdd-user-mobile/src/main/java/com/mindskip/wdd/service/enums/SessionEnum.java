package com.mindskip.wdd.service.enums;

/**
 * @version 1.7.0
 * @description: 操作结果
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
 */
public enum SessionEnum {

    /**
     * Success result enum.
     */
    Paper(1, "试卷"),
    /**
     * Fail result enum.
     */
    TrainPaper(2, "培训试卷");

    /**
     * The Code.
     */
    int code;
    /**
     * The Name.
     */
    String Name;

    SessionEnum(int code, String name) {
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
