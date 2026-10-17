package com.mindskip.wdd.configuration.vnc;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 第三方VNC虚拟机平台对接配置。
 * <p>
 * 对接文档：财大实训平台VNC对接
 * 平台地址：http://tadsxpt.ueweixin.com
 * 流程：登录获取token → 调用thirdPartyGetVnc获取VNC地址
 */
@Configuration
@ConfigurationProperties(prefix = "vmware.third-party")
@Data
public class ThirdPartyVncProperties {

    /** 是否启用第三方VNC模式（启用后替代阿里云无影） */
    private boolean enabled = false;

    /** 第三方平台基础URL */
    private String baseUrl = "";

    /** 登录用户名 */
    private String loginUser = "";

    /** 登录密码（RSA加密后的Base64字符串） */
    private String loginPassword = "";

    /**
     * 是否已配置
     */
    public boolean isConfigured() {
        return enabled
                && baseUrl != null && !baseUrl.isEmpty()
                && loginUser != null && !loginUser.isEmpty()
                && loginPassword != null && !loginPassword.isEmpty();
    }
}
