package com.mindskip.wdd.domain.ueit;

import lombok.Data;

/**
 * ssh执行命令参数
 *
 * @author libl
 * @date 2025-04-07
 */
@Data
public class SshParam {

    /**
     * 服务器地址
     */
    private String host;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 执行的命令
     */
    private String command;
}
