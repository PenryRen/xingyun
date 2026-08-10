package com.mindskip.wdd.controller;

import com.mindskip.wdd.base.BaseApiController;
import com.mindskip.wdd.base.RestResponse;
import com.mindskip.wdd.domain.Department;
import com.mindskip.wdd.domain.User;
import com.mindskip.wdd.service.AiAssistantService;
import com.mindskip.wdd.service.AiLearningService;
import com.mindskip.wdd.service.DepartmentService;
import com.mindskip.wdd.viewmodel.ai.AiAssistantReplyVM;
import com.mindskip.wdd.viewmodel.ai.AiAssistantRequestVM;
import com.mindskip.wdd.viewmodel.ai.AiLearningWorkspaceVM;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/** AI 学习中心结构化数据接口。 */
@AllArgsConstructor
@RestController
@RequestMapping(value = "/api/ai/learning")
public class AiLearningController extends BaseApiController {
    private final AiLearningService aiLearningService;
    private final AiAssistantService aiAssistantService;
    private final DepartmentService departmentService;

    @PostMapping("/workspace")
    public RestResponse<AiLearningWorkspaceVM> workspace() {
        User user = getCurrentUser();
        return RestResponse.ok(loadWorkspace(user));
    }

    private AiLearningWorkspaceVM loadWorkspace(User user) {
        String departmentName = null;
        if (user.getDepartmentId() != null) {
            Department department = departmentService.getById(user.getDepartmentId());
            if (department != null) departmentName = department.getLevel();
        }
        return aiLearningService.getWorkspace(user, departmentName);
    }

    @PostMapping("/assistant")
    public RestResponse<AiAssistantReplyVM> assistant(@RequestBody @Valid AiAssistantRequestVM requestVM) {
        User user = getCurrentUser();
        try {
            AiLearningWorkspaceVM workspace = loadWorkspace(user);
            return RestResponse.ok(aiAssistantService.ask(user.getId(), requestVM.getMessage(), workspace));
        } catch (IllegalStateException exception) {
            return new RestResponse<>(500, exception.getMessage());
        }
    }

    @PostMapping("/assistant/session/reset")
    public RestResponse<String> resetAssistantSession() {
        User user = getCurrentUser();
        aiAssistantService.resetSession(user.getId());
        return RestResponse.ok();
    }
}
