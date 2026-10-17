package com.mindskip.wdd.viewmodel.credential;

import com.mindskip.wdd.base.BasePage;
import lombok.Data;

import java.util.List;

/**
 * @version 7.1.0
 * @description: 证书列表
 * Copyright (C), 2025, 麟航团队
 * @date 2025/8/2 10:45
 */
@Data
public class CredentialUserPageRequestVM extends BasePage {
    /**
     * 证书编号
     */
    private String no;

    /**
     * 证书id
     */
    private Long id;

    /**
     * 部门
     */
    private List<Integer> departmentIdList;

    /**
     * 培训id
     */
    private Integer trainId;

    /**
     * 用户名
     */
    private String userName;
}
