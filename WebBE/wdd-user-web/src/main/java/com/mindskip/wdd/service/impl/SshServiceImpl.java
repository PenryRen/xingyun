package com.mindskip.wdd.service.impl;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import com.mindskip.wdd.domain.ueit.SshParam;
import com.mindskip.wdd.domain.ueit.SshResult;
import com.mindskip.wdd.service.SshService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.Properties;

/**
 * ssh模块Service实现类
 *
 * @author libl
 * @date 2025-04-07
 */
@Service
@AllArgsConstructor
public class SshServiceImpl implements SshService {

    private final static Logger logger = LoggerFactory.getLogger(SshServiceImpl.class);

    /**
     * 向指定服务器执行命令
     *
     * @param sshParam ssh参数
     * @return 命令返回值
     */
    @Override
    public SshResult executeCommand(SshParam sshParam) {
        int port = 22;

        // 创建JSch对象
        JSch jSch = new JSch();
        Session jSchSession = null;
        Channel jschChannel = null;

        // 存放执行命令结果
        StringBuffer result = new StringBuffer();
        int exitStatus = 0;
        String errmsg = "";

        try {
            // 根据主机账号、ip、端口获取一个Session对象
            jSchSession = jSch.getSession(sshParam.getUsername(), sshParam.getHost(), port);

            // 存放主机密码
            jSchSession.setPassword(sshParam.getPassword());

            // 去掉首次连接确认
            Properties config = new Properties();
            config.put("StrictHostKeyChecking", "no");
            jSchSession.setConfig(config);

            // 超时连接时间为3秒
            jSchSession.setTimeout(3000);

            // 进行连接
            jSchSession.connect();

            jschChannel = jSchSession.openChannel("exec");
            ((ChannelExec) jschChannel).setCommand(sshParam.getCommand());

            jschChannel.setInputStream(null);
            // 错误信息输出流，用于输出错误的信息，当exitstatus<0的时候
            ((ChannelExec) jschChannel).setErrStream(System.err);

            // 执行命令，等待执行结果
            jschChannel.connect();

            // 获取命令执行结果
            InputStream in = jschChannel.getInputStream();
            /**
             * 通过channel获取信息的方式，采用官方Demo代码
             */
            byte[] tmp = new byte[1024];
            while (true) {
                while (in.available() > 0) {
                    int i = in.read(tmp, 0, 1024);
                    if (i < 0) {
                        break;
                    }
                    result.append(new String(tmp, 0, i));
                }
                // 从channel获取全部信息之后，channel会自动关闭
                if (jschChannel.isClosed()) {
                    if (in.available() > 0) {
                        continue;
                    }
                    exitStatus = jschChannel.getExitStatus();
                    break;
                }
                try {
                    Thread.sleep(1000);
                } catch (Exception ee) {
                }
            }

        } catch (Exception e) {
            exitStatus = 500;
            errmsg = e.getMessage();
            logger.error("错误信息：" + e.getMessage());
        } finally {
            // 关闭sftpChannel
            if (jschChannel != null && jschChannel.isConnected()) {
                jschChannel.disconnect();
            }

            // 关闭jschSesson流
            if (jSchSession != null && jSchSession.isConnected()) {
                jSchSession.disconnect();
            }

        }

        logger.info("退出码为：" + exitStatus + "；获取执行命令的结果结果：" + result);

        SshResult sshResult = new SshResult(exitStatus, result, errmsg);
        return sshResult;

    }
}
