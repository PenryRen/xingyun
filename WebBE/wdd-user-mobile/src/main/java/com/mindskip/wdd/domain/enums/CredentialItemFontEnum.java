package com.mindskip.wdd.domain.enums;


import java.util.HashMap;
import java.util.Map;

/**
 * @version 7.1.0
 * @description: 证书节点类型
 * Copyright (C), 2025, 麟航团队
 * @date 2025/7/29 10:45
 */
public enum CredentialItemFontEnum {
    /**
     * 试卷名称
     */
    PAPER_NAME(1, "试卷名称"),

    /**
     * 学员姓名
     */
    REAL_NAME(2, "学员姓名"),

    /**
     * 证书编号
     */
    NO(3, "证书编号"),
    /**
     * 创建时间
     */
    CREATE_TIME(4, "创建时间"),
    /**
     * 有效期
     */
    VALIDITY(5, "有效期"),
    /**
     * 电子印章
     */
    SEAL(6, "电子印章"),
    /**
     * 自定义
     */
    CUSTOMS(7, "自定义"),
    /**
     * 课程名称
     */
    COURSE(8, "课程名称");

    /**
     * The Code.
     */
    int code;
    /**
     * The Name.
     */
    String name;

    CredentialItemFontEnum(int code, String name) {
        this.code = code;
        this.name = name;
    }

    private static final Map<Integer, CredentialItemFontEnum> keyMap = new HashMap<>();

    static {
        for (CredentialItemFontEnum item : CredentialItemFontEnum.values()) {
            keyMap.put(item.getCode(), item);
        }
    }

    /**
     * From code apply status enum.
     *
     * @param code the code
     * @return the apply status enum
     */
    public static CredentialItemFontEnum fromCode(Integer code) {
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

    /**
     * Gets role name.
     *
     * @return the role name
     */
    public String getRoleName() {
        return "ROLE_" + name;
    }

}
