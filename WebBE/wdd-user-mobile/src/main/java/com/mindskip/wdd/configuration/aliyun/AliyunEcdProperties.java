package com.mindskip.wdd.configuration.aliyun;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "aliyun.ecd")
@Data
public class AliyunEcdProperties {

    private String accessKeyId = "";
    private String accessKeySecret = "";
    private String regionId = "cn-hangzhou";
    private String officeSiteId = "";
    private String policyGroupId = "";
    private String chargeType = "PostPaid";
    private String ecdEndpoint = "";
    private String appstreamEndpoint = "";
    private String wuyingOpenType = "inline";
    private String wuyingResourceType = "";
}
