package com.mindskip.wdd.domain.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * @version 1.7.0
 * @description: 答卷状态
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
 */
public enum ExamPaperAnswerStatusEnum {

    /**
     * Wait judge exam paper answer status enum.
     */
    WaitJudge(1, "待批改"),
    /**
     * Complete exam paper answer status enum.
     */
    Complete(2, "完成"),
    /**
     * Wait check exam paper answer status enum.
     */
    WaitCheck(3, "待核验"),
    /**
     * Check error exam paper answer status enum.
     */
    CheckError(4, "核验失败");

    /**
     * The Code.
     */
    int code;
    /**
     * The Name.
     */
    String name;

    ExamPaperAnswerStatusEnum(int code, String name) {
        this.code = code;
        this.name = name;
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


    private static final Map<Integer, ExamPaperAnswerStatusEnum> keyMap = new HashMap<>();

    static {
        for (ExamPaperAnswerStatusEnum item : ExamPaperAnswerStatusEnum.values()) {
            keyMap.put(item.getCode(), item);
        }
    }

    /**
     * From code exam paper answer status enum.
     *
     * @param code the code
     * @return the exam paper answer status enum
     */
    public static ExamPaperAnswerStatusEnum fromCode(Integer code) {
        return keyMap.get(code);
    }

}
