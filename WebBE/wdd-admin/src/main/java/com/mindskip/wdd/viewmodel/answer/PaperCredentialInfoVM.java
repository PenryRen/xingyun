package com.mindskip.wdd.viewmodel.answer;

import lombok.Data;

/**
 * @version 1.7.0
 * @description: 试卷证书生成信息
 * Copyright (C), 2024, 麒技团队
 * @date 2024/8/25 10:45
 */
@Data
public class PaperCredentialInfoVM {
    /**
     * 所有证书数量
     */
    private Integer allCredential;

    /**
     * 已生成数量
     */
    private Integer buildCredential;

    /**
     * 待生成数量
     */
    private Integer waitCredential;
}
