package com.mindskip.wdd.service.impl;

import cn.hutool.http.ContentType;
import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson2.JSONObject;
import com.mindskip.wdd.service.AiAssistantService;
import com.mindskip.wdd.utility.JsonUtil;
import com.mindskip.wdd.viewmodel.ai.AiAssistantReplyVM;
import com.mindskip.wdd.viewmodel.ai.AiLearningWorkspaceVM;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * The browser reaches the Agent only through this authenticated WebBE endpoint.
 * Only an anonymous numeric learning summary is added to the signed-in user's
 * question. Identity, answer sheets, cookies, tokens and database credentials
 * never cross this boundary.
 */
@Service
public class AiAssistantServiceImpl implements AiAssistantService {
    private static final Logger logger = LoggerFactory.getLogger(AiAssistantServiceImpl.class);
    private static final int REQUEST_TIMEOUT_MILLIS = 125_000;

    private final String agentBaseUrl;
    private final ConcurrentMap<Integer, String> anonymousSessions = new ConcurrentHashMap<>();

    public AiAssistantServiceImpl(@Value("${ai-agent.base-url:http://agent:8000}") String agentBaseUrl) {
        this.agentBaseUrl = agentBaseUrl == null || agentBaseUrl.trim().isEmpty()
                ? "http://agent:8000"
                : agentBaseUrl.replaceAll("/+$", "");
    }

    @Override
    public AiAssistantReplyVM ask(Integer userId, String message, AiLearningWorkspaceVM workspace) {
        if (userId == null || workspace == null || !workspace.getMeta().isComplete()) {
            throw new IllegalStateException("学习数据尚未完整加载，请刷新后重试");
        }
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("session_id", anonymousSessions.computeIfAbsent(userId, ignored -> UUID.randomUUID().toString()));
        payload.put("message", message.trim());
        payload.put("learning_context", buildLearningContext(workspace));

        try (HttpResponse response = HttpRequest.post(agentBaseUrl + "/api/v1/chat")
                .header(Header.CONTENT_TYPE, ContentType.JSON.getValue())
                .timeout(REQUEST_TIMEOUT_MILLIS)
                .body(JsonUtil.toJsonStr(payload))
                .execute()) {
            if (!response.isOk()) {
                logger.warn("Internal AI Agent returned HTTP {}", response.getStatus());
                throw new IllegalStateException("AI 助教暂时不可用，请稍后重试");
            }
            JSONObject body = JSONObject.parseObject(response.body());
            String content = body == null ? null : body.getString("message");
            if (content == null || content.trim().isEmpty()) {
                throw new IllegalStateException("AI 助教未返回有效内容，请稍后重试");
            }
            return new AiAssistantReplyVM(content);
        } catch (IllegalStateException exception) {
            throw exception;
        } catch (Exception exception) {
            logger.warn("Internal AI Agent request failed: {}", exception.getClass().getSimpleName());
            throw new IllegalStateException("AI 助教暂时不可用，请检查模型连接后重试");
        }
    }

    @Override
    public void resetSession(Integer userId) {
        if (userId != null) anonymousSessions.remove(userId);
    }

    private Map<String, Object> buildLearningContext(AiLearningWorkspaceVM workspace) {
        Map<String, Object> context = new LinkedHashMap<>();
        context.put("source", "LOCAL_MYSQL");

        AiLearningWorkspaceVM.CountsVM sourceCounts = workspace.getCounts();
        Map<String, Object> counts = new LinkedHashMap<>();
        counts.put("total", sourceCounts.getTotal());
        counts.put("finalized", sourceCounts.getFinalized());
        counts.put("validFinalized", sourceCounts.getValidFinalized());
        counts.put("invalidFinalized", sourceCounts.getInvalidFinalized());
        counts.put("waitingReview", sourceCounts.getWaitingReview());
        counts.put("waitingVerification", sourceCounts.getWaitingVerification());
        counts.put("verificationFailed", sourceCounts.getVerificationFailed());
        counts.put("other", sourceCounts.getOther());
        context.put("counts", counts);

        AiLearningWorkspaceVM.SummaryVM sourceSummary = workspace.getSummary();
        Map<String, Object> summary = new LinkedHashMap<>();
        summary.put("trendSampleCount", sourceSummary.getTrendSampleCount());
        summary.put("recentAveragePercent", sourceSummary.getRecentAveragePercent());
        summary.put("changeFromFirstPercentPoints", sourceSummary.getChangeFromFirstPercentPoints());
        summary.put("volatilityPercentPoints", sourceSummary.getVolatilityPercentPoints());
        summary.put("questionAccuracyPercent", sourceSummary.getQuestionAccuracyPercent());
        summary.put("accuracySampleCount", sourceSummary.getAccuracySampleCount());
        summary.put("finalizedPercent", sourceSummary.getFinalizedPercent());
        context.put("summary", summary);

        List<Map<String, Object>> trend = new ArrayList<>();
        for (AiLearningWorkspaceVM.ExamEvidenceVM evidence : workspace.getTrend()) {
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("scoreRatePercent", evidence.getScoreRatePercent());
            item.put("questionCorrect", evidence.getQuestionCorrect());
            item.put("questionCount", evidence.getQuestionCount());
            trend.add(item);
        }
        context.put("trend", trend);

        Map<String, Object> knowledgePoints = new LinkedHashMap<>();
        knowledgePoints.put("available", workspace.getKnowledgePoints().isAvailable());
        knowledgePoints.put("reasonCode", workspace.getKnowledgePoints().getReasonCode());
        context.put("knowledgePoints", knowledgePoints);
        return context;
    }
}
