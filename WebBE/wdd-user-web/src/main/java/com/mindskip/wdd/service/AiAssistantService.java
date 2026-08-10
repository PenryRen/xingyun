package com.mindskip.wdd.service;

import com.mindskip.wdd.viewmodel.ai.AiAssistantReplyVM;
import com.mindskip.wdd.viewmodel.ai.AiLearningWorkspaceVM;

/** Authenticated bridge from WebBE to the internal AI Agent. */
public interface AiAssistantService {
    AiAssistantReplyVM ask(Integer userId, String message, AiLearningWorkspaceVM workspace);

    void resetSession(Integer userId);
}
