package com.mindskip.wdd.viewmodel.ai;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/** Browser input for the authenticated AI assistant endpoint. */
@Data
public class AiAssistantRequestVM {
    @NotBlank(message = "问题不能为空")
    @Size(max = 1000, message = "问题不能超过 1000 个字符")
    private String message;
}
