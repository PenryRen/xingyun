package com.mindskip.wdd.viewmodel.ueit;

import lombok.Data;

/**
 * 实训/考试远程环境连接信息（无影云桌面 Web SDK）。
 *
 * @author libl
 * @date 2025-04-11
 */
@Data
public class VmUrl {

    /**
     * 兼容旧版 iframe；无影模式下可为空。
     */
    private String url;

    /**
     * 无影云电脑 ID（对应 ECD DesktopId）。
     */
    private String vmGuid;

    /**
     * 无影 Web SDK：userInfo.authCode
     */
    private String authCode;

    /**
     * 无影便捷用户 ID（与创建云桌面时 EndUserId 一致）。
     */
    private String endUserId;

    /**
     * 登录地域（desktopInfo.loginRegionId / sessionParam.regionId）。
     */
    private String loginRegionId;

    private String desktopName;

    private String realDesktopId;

    private String wuyingOpenType;

    private String wuyingResourceType;

    /**
     * 为 true 时前端应使用无影 Web SDK，而非 url iframe。
     */
    private Boolean useWuyingWebSdk;

    /**
     * 状态   00：Running 启动  01：shutdown 关机  02：Scheduled 启动中
     */
    private String status;

    /**
     * 消息
     */
    private String msg;
}
