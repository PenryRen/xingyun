package com.mindskip.wdd.domain.enums.ueit;

public class WareEnum {

    /**
     * 虚拟机关机接口地址
     */
    public static final String ware_vm_shutdown = "http://192.168.1.211:32080/api/v1/vms/stop-virtual-machine";

    /**
     * 虚拟机开机接口地址
     */
    public static final String ware_vm_start = "http://192.168.1.211:32080/api/v1/vms/start-virtual-machine";

    /**
     * 虚拟机克隆接口地址
     */
    public static final String ware_vm_clone = "http://192.168.1.211:32080/api/v1/cloneLocalVM";

    /**
     * 虚拟机释放接口地址
     */
    public static final String ware_vm_release = "http://192.168.1.211:32080/api/v1/vms";
}