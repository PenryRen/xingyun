package com.mindskip.wdd.service;

import com.mindskip.wdd.domain.User;
import com.mindskip.wdd.viewmodel.ai.AiLearningWorkspaceVM;

/** AI 学习中心确定性数据服务。 */
public interface AiLearningService {
    AiLearningWorkspaceVM getWorkspace(User user, String departmentName);
}
