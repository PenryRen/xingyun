package com.mindskip.wdd.viewmodel.credential;

import com.mindskip.wdd.base.BasePage;
import lombok.Data;

import java.util.List;

/**
 * @version 7.1.0
 * @description: 证书列表
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/2 10:45
 */
@Data
public class CredentialPageRequestVM extends BasePage {
    /**
     * 模板名称
     */
    private String name;


    /**
     * 已选中id
     */
    private List<Integer> selectIdList;
}
