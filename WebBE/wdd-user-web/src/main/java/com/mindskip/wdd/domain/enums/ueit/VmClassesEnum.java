package com.mindskip.wdd.domain.enums.ueit;

import java.util.HashMap;
import java.util.Map;

/**
 * 虚拟机 分类
 * 00：实训   01：考试
 *
 * @author libl
 * @date 2024-04-10
 */
public enum VmClassesEnum {

    /**
     * Train enum.
     */
    Train("00", "实训"),
    /**
     * Exam enum.
     */
    Exam("01", "考试");

    /**
     * The Code.
     */
    String code;
    /**
     * The Name.
     */
    String name;

    VmClassesEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    private static final Map<String, VmClassesEnum> keyMap = new HashMap<>();

    static {
        for (VmClassesEnum item : VmClassesEnum.values()) {
            keyMap.put(item.getCode(), item);
        }
    }

    /**
     * From code sex enum.
     *
     * @param code the code
     * @return the sex enum
     */
    public static VmClassesEnum fromCode(String code) {
        return keyMap.get(code);
    }

    /**
     * Gets code.
     *
     * @return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * Sets code.
     *
     * @param code the code
     */
    public void setCode(String code) {
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
