package com.mindskip.wdd.domain.frame;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @version 1.7.0
 * @description: 角色数据权限
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/25 10:45
 */
@Data
public class RoleDataFilterFrame implements Serializable {

    /**
     * 数据权限部门
     */
    private List<Integer> departmentIdList;

}
