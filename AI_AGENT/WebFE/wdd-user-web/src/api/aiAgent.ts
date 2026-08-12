import axios from 'axios';

export interface AgentHealth {
    status: string;
    message: string;
    service_ready?: boolean;
    model_configured?: boolean;
    model_available?: boolean;
    model_ready?: boolean;
    model_probe_code?: string;
    model_message?: string;
    data_source_ready?: boolean;
    data_source_mode?: string;
}

export interface AgentReply {
    content: string;
}

const BASE_URL = '/ai-agent';

export async function getAgentHealth(force = false): Promise<AgentHealth> {
    const response = await axios.get(`${BASE_URL}/health`, {
        timeout: 15000,
        params: force ? {force: true} : undefined
    });
    return response.data;
}

export async function askAgent(message: string): Promise<AgentReply> {
    const response = await axios.post('/api/ai/learning/assistant', {message}, {
        timeout: 130000,
        withCredentials: true
    });

    const content = response.data?.response?.content;
    if (typeof content !== 'string' || !content.trim()) {
        throw new Error(response.data?.message || 'AI 助教未返回有效内容。');
    }
    return {content};
}

export async function resetAgentSession(): Promise<void> {
    await axios.post('/api/ai/learning/assistant/session/reset', undefined, {
        timeout: 15000,
        withCredentials: true
    });
}
