package com.mindskip.wdd.domain.enums.ueit;

import java.util.HashMap;
import java.util.Map;

/**
 * 虚拟机 状态
 * 00：Running 启动  01：Shutdown 关机  02：Scheduled 启动中
 *
 * @author libl
 * @date 2024-04-10
 */
public enum VmStatusEnum {

    /**
     * Running enum.
     */
    Running("00", "正在运行", "Running"),
    /**
     * Shutdown enum.
     */
    Shutdown("01", "关机", "Shutdown"),
    /**
     * Scheduled enum.
     */
    Scheduled("02", "启动中", "Scheduled"),
    /**
     * Init enum.
     */
    Init("100", "正在初始化","Init"),
    /**
     * Init enum.
     */
    Restart("101", "重启中","Restart");

    /**
     * The Code.
     */
    String code;
    /**
     * The Name.
     */
    String name;
    /**
     * The English Name.
     */
    String enName;

    VmStatusEnum(String code, String name, String enName) {
        this.code = code;
        this.name = name;
        this.enName = enName;
    }

    private static final Map<String, VmStatusEnum> keyMap = new HashMap<>();

    private static final Map<String, VmStatusEnum> enKeyMap = new HashMap<>();

    static {
        for (VmStatusEnum item : VmStatusEnum.values()) {
            keyMap.put(item.getCode(), item);
            enKeyMap.put(item.getEnName(), item);
        }
    }

    /**
     * From code sex enum.
     *
     * @param code the code
     * @return the sex enum
     */
    public static VmStatusEnum fromCode(String code) {
        return keyMap.get(code);
    }

    public static VmStatusEnum fromEnName(String enName) {
        return enKeyMap.get(enName);
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

    /**
     * Gets english name.
     *
     * @return the name
     */
    public String getEnName() {
        return enName;
    }

    /**
     * Sets english name.
     *
     * @param enName the english name
     */
    public void setEnName(String enName) {
        this.enName = enName;
    }
}
