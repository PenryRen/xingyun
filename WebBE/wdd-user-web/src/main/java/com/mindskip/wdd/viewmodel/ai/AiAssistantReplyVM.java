package com.mindskip.wdd.viewmodel.ai;

import lombok.AllArgsConstructor;
import lombok.Data;

/** Text returned after WebBE authentication and the internal Agent call. */
@Data
@AllArgsConstructor
public class AiAssistantReplyVM {
    private String content;
}
