package com.mindskip.wdd.viewmodel.ueit;

import com.mindskip.wdd.base.BasePage;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class VmWarePageRequestVM extends BasePage {

    /**
     * 虚拟机类型 主机 子机
     */
    private String vmType;

    /**
     * 虚拟机名称
     */
    private String vmName;

    /**
     * 状态   00：Running 启动  01：shutdown 关机  02：Scheduled 启动中
     */
    private String status;

    /**
     * 分类   00：实训   01：考试
     */
    private String classes;
}
