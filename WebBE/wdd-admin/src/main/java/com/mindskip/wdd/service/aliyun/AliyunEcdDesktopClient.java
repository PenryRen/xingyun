package com.mindskip.wdd.service.aliyun;

import com.aliyun.appstream_center20210218.Client;
import com.aliyun.appstream_center20210218.models.GetAuthCodeRequest;
import com.aliyun.appstream_center20210218.models.GetAuthCodeResponse;
import com.aliyun.ecd20200930.models.*;
import com.aliyun.teaopenapi.models.Config;
import com.aliyun.teautil.models.RuntimeOptions;
import com.mindskip.wdd.configuration.aliyun.AliyunEcdProperties;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

/**
 * 封装无影云桌面 OpenAPI：创建/查询/电源管理/删除/获取 Web 授权码。
 */
@Service
@AllArgsConstructor
public class AliyunEcdDesktopClient {

    private static final Logger log = LoggerFactory.getLogger(AliyunEcdDesktopClient.class);

    private final AliyunEcdProperties properties;

    private com.aliyun.ecd20200930.Client ecdClient() throws Exception {
        Config config = new Config()
                .setAccessKeyId(properties.getAccessKeyId())
                .setAccessKeySecret(properties.getAccessKeySecret())
                .setRegionId(properties.getRegionId());
        if (properties.getEcdEndpoint() != null && !properties.getEcdEndpoint().isEmpty()) {
            config.setEndpoint(properties.getEcdEndpoint());
        }
        return new com.aliyun.ecd20200930.Client(config);
    }

    private Client appstreamClient() throws Exception {
        Config config = new Config()
                .setAccessKeyId(properties.getAccessKeyId())
                .setAccessKeySecret(properties.getAccessKeySecret())
                .setRegionId(properties.getRegionId());
        if (properties.getAppstreamEndpoint() != null && !properties.getAppstreamEndpoint().isEmpty()) {
            config.setEndpoint(properties.getAppstreamEndpoint());
        }
        return new Client(config);
    }

    public String createDesktop(String bundleId, String desktopName, String endUserId) throws Exception {
        CreateDesktopsRequest req = new CreateDesktopsRequest()
                .setRegionId(properties.getRegionId())
                .setOfficeSiteId(properties.getOfficeSiteId())
                .setPolicyGroupId(properties.getPolicyGroupId())
                .setBundleId(bundleId)
                .setAmount(1)
                .setDesktopName(desktopName)
                .setChargeType(properties.getChargeType())
                .setEndUserId(Collections.singletonList(endUserId));
        CreateDesktopsResponse resp = ecdClient().createDesktopsWithOptions(req, new RuntimeOptions());
        if (resp == null || resp.getBody() == null || resp.getBody().getDesktopId() == null
                || resp.getBody().getDesktopId().isEmpty()) {
            throw new IllegalStateException("CreateDesktops 未返回 DesktopId");
        }
        Object id = resp.getBody().getDesktopId().get(0);
        return id != null ? id.toString() : null;
    }

    public String describeDesktopStatus(String desktopId) throws Exception {
        DescribeDesktopsRequest req = new DescribeDesktopsRequest()
                .setRegionId(properties.getRegionId())
                .setDesktopId(Collections.singletonList(desktopId));
        DescribeDesktopsResponse resp = ecdClient().describeDesktopsWithOptions(req, new RuntimeOptions());
        if (resp == null || resp.getBody() == null || resp.getBody().getDesktops() == null
                || resp.getBody().getDesktops().isEmpty()) {
            return null;
        }
        DescribeDesktopsResponseBody.DescribeDesktopsResponseBodyDesktops row =
                resp.getBody().getDesktops().get(0);
        return row != null ? row.getDesktopStatus() : null;
    }

    public void deleteDesktops(List<String> desktopIds) throws Exception {
        if (desktopIds == null || desktopIds.isEmpty()) {
            return;
        }
        DeleteDesktopsRequest req = new DeleteDesktopsRequest()
                .setRegionId(properties.getRegionId())
                .setDesktopId(desktopIds);
        ecdClient().deleteDesktopsWithOptions(req, new RuntimeOptions());
    }

    public void stopDesktops(List<String> desktopIds) throws Exception {
        if (desktopIds == null || desktopIds.isEmpty()) {
            return;
        }
        StopDesktopsRequest req = new StopDesktopsRequest()
                .setRegionId(properties.getRegionId())
                .setDesktopId(desktopIds);
        ecdClient().stopDesktopsWithOptions(req, new RuntimeOptions());
    }

    public void startDesktops(List<String> desktopIds) throws Exception {
        if (desktopIds == null || desktopIds.isEmpty()) {
            return;
        }
        StartDesktopsRequest req = new StartDesktopsRequest()
                .setRegionId(properties.getRegionId())
                .setDesktopId(desktopIds);
        ecdClient().startDesktopsWithOptions(req, new RuntimeOptions());
    }

    public void rebootDesktops(List<String> desktopIds) throws Exception {
        if (desktopIds == null || desktopIds.isEmpty()) {
            return;
        }
        RebootDesktopsRequest req = new RebootDesktopsRequest()
                .setRegionId(properties.getRegionId())
                .setDesktopId(desktopIds);
        ecdClient().rebootDesktopsWithOptions(req, new RuntimeOptions());
    }

    public String getWebAuthCode(String endUserId) throws Exception {
        GetAuthCodeRequest req = new GetAuthCodeRequest().setEndUserId(endUserId);
        GetAuthCodeResponse resp = appstreamClient().getAuthCodeWithOptions(req, new RuntimeOptions());
        if (resp == null || resp.getBody() == null || resp.getBody().getAuthModel() == null) {
            log.warn("GetAuthCode 返回为空 endUserId={}", endUserId);
            return null;
        }
        return resp.getBody().getAuthModel().getAuthCode();
    }
}
