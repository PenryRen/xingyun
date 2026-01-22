package com.mindskip.wdd.domain.ueit;

import lombok.Data;

/**
 * 虚拟机
 *
 * @author libl
 * @date 2024-04-07
 */
@Data
public class VmWareClone {

    /**
     * 虚拟机账户名
     */
    private String vmUsername;

    /**
     * 虚拟机密码
     */
    private String vmPassword;

    /**
     * 虚拟机路径
     */
    private String url;
}