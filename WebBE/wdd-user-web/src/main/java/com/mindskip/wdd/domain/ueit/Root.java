package com.mindskip.wdd.domain.ueit;

import lombok.Data;

import java.util.List;

/**
 * 虚拟机基础信息
 *
 * @author libl
 * @date 2024-04-10
 */
@Data
public class Root {

    private String name;

    private String osType;

    private Integer cpu;

    private String mem;

    private String dataSource;

    private List<Disk> disk;
}
