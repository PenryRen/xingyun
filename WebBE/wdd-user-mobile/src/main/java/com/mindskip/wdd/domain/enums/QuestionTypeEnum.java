package com.mindskip.wdd.domain.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * @version 1.7.0
 * @description: 题目类型
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
 */
public enum QuestionTypeEnum {

    /**
     * Single choice question type enum.
     */
    SingleChoice(1, "单选题"),
    /**
     * Multiple choice question type enum.
     */
    MultipleChoice(2, "多选题"),
    /**
     * True false question type enum.
     */
    TrueFalse(3, "判断题"),
    /**
     * Gap filling question type enum.
     */
    GapFilling(4, "填空题"),
    /**
     * Short answer question type enum.
     */
    ShortAnswer(5, "简答题"),
    /**
     * Uncertain multiple choice question type enum.
     */
    UncertainMultipleChoice(6, "不定项选择题"),
    /**
     * Train Operate question type enum.
     */
    TrainOperate(7, "实训题");

    /**
     * The Code.
     */
    int code;
    /**
     * The Name.
     */
    String name;

    QuestionTypeEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }


    private static final Map<Integer, QuestionTypeEnum> keyMap = new HashMap<>();
    private static final Map<String, QuestionTypeEnum> nameMap = new HashMap<>();

    static {
        for (QuestionTypeEnum item : QuestionTypeEnum.values()) {
            keyMap.put(item.getCode(), item);
            nameMap.put(item.getName(), item);
        }
    }

    /**
     * From code question type enum.
     *
     * @param code the code
     * @return the question type enum
     */
    public static QuestionTypeEnum fromCode(Integer code) {
        return keyMap.get(code);
    }

    /**
     * From name question type enum.
     *
     * @param name the name
     * @return the question type enum
     */
    public static QuestionTypeEnum fromName(String name) {
        return nameMap.get(name);
    }

    /**
     * Need save text content boolean.
     *
     * @param code the code
     * @return the boolean
     */
    public static boolean needSaveTextContent(Integer code) {
        QuestionTypeEnum questionTypeEnum = QuestionTypeEnum.fromCode(code);
        switch (questionTypeEnum) {
            case GapFilling:
            case ShortAnswer:
                return true;
            default:
                return false;
        }
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
