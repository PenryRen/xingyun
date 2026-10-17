package com.mindskip.wdd.domain.enums.ueit;

import java.util.HashMap;
import java.util.Map;

/**
 * 虚拟机 类型
 * 00：主机  01：子机
 *
 * @author libl
 * @date 2025-04-08
 */
public enum VmTypeEnum {

    /**
     * Train enum.
     */
    Main("00", "主机"),
    /**
     * Exam enum.
     */
    Child("01", "子机");

    /**
     * The Code.
     */
    String code;
    /**
     * The Name.
     */
    String name;

    VmTypeEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

    private static final Map<String, VmTypeEnum> keyMap = new HashMap<>();

    static {
        for (VmTypeEnum item : VmTypeEnum.values()) {
            keyMap.put(item.getCode(), item);
        }
    }

    /**
     * From code sex enum.
     *
     * @param code the code
     * @return the sex enum
     */
    public static VmTypeEnum fromCode(String code) {
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
