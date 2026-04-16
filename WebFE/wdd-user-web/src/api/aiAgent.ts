import axios from 'axios';

export type AgentType =
    | 'auto'
    | 'exam_analysis'
    | 'learning_profile'
    | 'learning_assessment'
    | 'teaching_assistant';

export interface AiAgentChatPayload {
    message: string;
    session_id: string;
    agent_type?: AgentType;
    use_local?: boolean;
    stream?: boolean;
    temperature?: number;
}

interface AiAgentChatResponse {
    success: boolean;
    message: string;
    agent_type?: AgentType;
    session_id?: string;
    timestamp?: string;
    data?: Record<string, unknown> | null;
}

function normalizeBaseUrl(baseUrl: string): string {
    return baseUrl.endsWith('/') ? baseUrl.slice(0, -1) : baseUrl;
}

const AI_AGENT_BASE_URL = normalizeBaseUrl(import.meta.env.VITE_APP_AI_AGENT_BASE_URL || '/ai-agent');

export async function chatWithAiAgent(payload: AiAgentChatPayload): Promise<AiAgentChatResponse> {
    const response = await axios.post(`${AI_AGENT_BASE_URL}/api/v1/chat`, {
        agent_type: 'auto',
        use_local: false,
        stream: false,
        ...payload
    });
    return response.data;
}

