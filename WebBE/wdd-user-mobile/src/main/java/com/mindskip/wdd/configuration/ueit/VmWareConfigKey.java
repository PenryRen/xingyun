package com.mindskip.wdd.configuration.ueit;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * 虚拟机缓存键 配置类
 *
 * @author libl
 * @date 2025-04-15
 */
@ConfigurationProperties(prefix = "vm-ware.config-key")
@Configuration
@Data
@Component
public class VmWareConfigKey {

    /**
     * 授权
     */
    private String authConfigKey = "sys.vm.auth";

    /**
     * 授权过期时间
     */
    private String expirationConfigKey = "sys.vm.expiration";

    /**
     * 虚拟机基础信息
     */
    private String rootConfigKey = "sys.vm.root";

    /**
     * 虚拟机硬盘信息
     */
    private String diskConfigKey = "sys.vm.disk";

    /**
     * 虚拟机上限个数
     */
    private String maxCountConfigKey = "sys.vm.maxCount";

}
