package com.mindskip.wdd.configuration.aliyun;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 阿里云无影云桌面（ECD）接入配置。模板行 {@code t_vmware} 中 {@code url} 存放 BundleId。
 */
@Configuration
@ConfigurationProperties(prefix = "aliyun.ecd")
@Data
public class AliyunEcdProperties {

    private String accessKeyId = "";

    private String accessKeySecret = "";

    /** 地域，如 cn-hangzhou */
    private String regionId = "cn-hangzhou";

    private String officeSiteId = "";

    private String policyGroupId = "";

    /** 计费方式：PostPaid / PrePaid */
    private String chargeType = "PostPaid";

    /** ECD API Endpoint，默认按地域拼接 */
    private String ecdEndpoint = "";

    /** 获取 Web 授权码（GetAuthCode）使用的 Endpoint */
    private String appstreamEndpoint = "";

    /** 无影 Web SDK：sessionParam.openType */
    private String wuyingOpenType = "inline";

    /**
     * 无影 Web SDK：sessionParam.resourceType，需与控制台/SDK 说明一致；空则前端不传该字段。
     */
    private String wuyingResourceType = "";

    public boolean isConfigured() {
        return accessKeyId != null && !accessKeyId.isEmpty()
                && accessKeySecret != null && !accessKeySecret.isEmpty()
                && officeSiteId != null && !officeSiteId.isEmpty()
                && policyGroupId != null && !policyGroupId.isEmpty();
    }
}
