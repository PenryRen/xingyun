package com.mindskip.wdd.configuration.ueit;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * 虚拟机接口 配置类
 *
 * @author libl
 * @date 2024-04-15
 */
@ConfigurationProperties(prefix = "vm-ware.api")
@Configuration
@Data
@Component
public class VmWareApiConfig {

    /**
     * 接口前缀
     */
    private String urlPrefix = "http://192.168.1.210:32080";

    /**
     * 授权时间接口地址 Get
     */
    private String expiration = "/api/v1/getProtected";

    /**
     * 获取虚拟化列表 Get
     */
    private String list = "/api/v1/vms";

    /**
     * 虚拟机关机接口地址 Put
     */
    private String shutdown = "/api/v1/vms/stop-virtual-machine";

    /**
     * 虚拟机开机接口地址 Put
     */
    private String start = "/api/v1/vms/start-virtual-machine";

    /**
     * 虚拟机重启接口地址 Put
     */
    private String reStart = "/api/v1/vms/restart-virtual-machine";

    /**
     * 虚拟机克隆接口地址 Post
     */
    private String clone = "/api/v1/cloneLocalVM";

    /**
     * 虚拟机删除接口地址 Delete
     */
    private String delete = "/api/v1/vms";

    public String getUrlPrefix() {
        return urlPrefix;
    }

    public String getExpiration() {
        return urlPrefix + expiration;
    }

    public String getList() {
        return urlPrefix + list;
    }

    public String getShutdown() {
        return urlPrefix + shutdown;
    }

    public String getStart() {
        return urlPrefix + start;
    }

    public String getReStart() {
        return urlPrefix + reStart;
    }

    public String getClone() {
        return urlPrefix + clone;
    }

    public String getDelete() {
        return urlPrefix + delete;
    }
}