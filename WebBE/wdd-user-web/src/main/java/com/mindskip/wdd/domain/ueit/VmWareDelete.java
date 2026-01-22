package com.mindskip.wdd.domain.ueit;

import lombok.Data;

import java.util.List;

/**
 * 虚拟机删除 请求参数
 *
 * @author libl
 * @date 2024-04-18
 */
@Data
public class VmWareDelete {

    private String vmName;

    private List<String> volumes;
}
