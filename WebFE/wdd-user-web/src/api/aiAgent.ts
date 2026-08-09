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

export async function askAgent(message: string, sessionId: string): Promise<AgentReply> {
    const response = await axios.post(`${BASE_URL}/api/v1/chat`, {
        session_id: sessionId,
        message
    }, {timeout: 120000});

    const content = response.data?.message;
    return {content: typeof content === 'string' ? content : '智能体已完成处理，但没有返回文本。'};
}
