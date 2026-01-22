package com.mindskip.wdd.viewmodel.answer;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * @version 4.1
 * @description: 补考
 * Copyright (C), 2024, 麒技团队
 * @date 2024-09-23 9:23
 */
@Data
public class ExamResitVM {

    @NotNull
    private Long paperAnswerId;

    @Size(min = 2, max = 2)
    private List<String> limitDateTime;
}
