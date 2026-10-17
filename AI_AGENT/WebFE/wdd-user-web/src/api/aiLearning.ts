import {post} from '@/utils/request';
import type {AiLearningWorkspace} from '@/views/ai/types';

export interface AiLearningWorkspaceResponse {
    code: number;
    message: string;
    response: AiLearningWorkspace;
}

/**
 * 一次读取当前登录学生的结构化考试分析。
 * 该接口只访问 WebBE/MySQL，不触发模型调用。
 */
export function getAiLearningWorkspace(): Promise<AiLearningWorkspaceResponse> {
    return post('/api/ai/learning/workspace', undefined);
}
