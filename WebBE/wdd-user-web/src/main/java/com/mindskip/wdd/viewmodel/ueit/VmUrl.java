package com.mindskip.wdd.viewmodel.ueit;

import lombok.Data;

/**
 * 虚拟机链接 VM
 *
 * @author libl
 * @date 2024-04-11
 */
@Data
public class VmUrl {

    /**
     * 虚拟机链接
     */
    private String url;

    /**
     * 虚拟机Guid
     */
    private String vmGuid;

    /**
     * 状态   00：Running 启动  01：shutdown 关机  02：Scheduled 启动中
     */
    private String status;

    /**
     * 消息
     */
    private String msg;
}
