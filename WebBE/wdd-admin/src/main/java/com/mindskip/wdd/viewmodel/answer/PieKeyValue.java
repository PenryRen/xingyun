package com.mindskip.wdd.viewmodel.answer;

import lombok.Data;

/**
 * @version 7.3.0
 * @description: 答卷分布
 * Copyright (C), 2024, 麒技团队
 * @date 2000/9/19 15:45
 */
@Data
public class PieKeyValue {

    public PieKeyValue(String name, Integer value) {
        this.name = name;
        this.value = value;
    }

    public String name;
    private Integer value;

}
