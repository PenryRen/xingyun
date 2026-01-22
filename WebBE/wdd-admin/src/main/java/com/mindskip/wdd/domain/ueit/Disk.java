package com.mindskip.wdd.domain.ueit;

import lombok.Data;

/**
 * 虚拟机硬盘信息
 *
 * @author libl
 * @date 2024-04-10
 */
@Data
public class Disk {

    private String name;

    private Integer boot_order;

    private String type;

    private String bus;

    private String size;

    private String mountType;

    private String storageclassName;

    private String accessMode;
}
