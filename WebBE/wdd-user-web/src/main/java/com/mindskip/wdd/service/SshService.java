package com.mindskip.wdd.service;


import com.mindskip.wdd.domain.ueit.SshParam;
import com.mindskip.wdd.domain.ueit.SshResult;

/**
 * ssh模块Service接口
 *
 * @author libl
 * @date 2024-04-07
 */
public interface SshService {


    /**
     * 向指定服务器执行命令
     *
     * @param sshParam ssh参数
     * @return 命令返回值
     */
    SshResult executeCommand(SshParam sshParam);
}
