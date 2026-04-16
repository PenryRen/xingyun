<template>
    <div v-if="enabled">
        <el-button class="ai-assistant-entry" type="primary" circle @click="visible = true">
            AI
        </el-button>

        <el-drawer
            v-model="visible"
            title="AI 学习助手"
            size="420px"
            :destroy-on-close="false"
        >
            <div class="chat-panel">
                <div class="chat-list">
                    <div
                        v-for="(item, index) in messages"
                        :key="`${item.role}-${index}`"
                        :class="['chat-item', item.role]"
                    >
                        <div class="bubble">{{ item.content }}</div>
                    </div>
                </div>

                <div class="chat-input">
                    <el-input
                        v-model="inputText"
                        type="textarea"
                        :rows="3"
                        :maxlength="2000"
                        show-word-limit
                        resize="none"
                        placeholder="请输入你的问题，例如：帮我分析最近考试薄弱点"
                        @keydown.enter.exact.prevent="sendMessage"
                    />
                    <div class="chat-actions">
                        <el-button :loading="loading" type="primary" @click="sendMessage">
                            发送
                        </el-button>
                    </div>
                </div>
            </div>
        </el-drawer>
    </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import { ElMessage } from 'element-plus';
import { chatWithAiAgent } from '@/api/aiAgent';

interface ChatMessage {
    role: 'assistant' | 'user';
    content: string;
}

const props = defineProps<{
    enabled: boolean;
    sessionId: string;
}>();

const visible = ref(false);
const loading = ref(false);
const inputText = ref('');
const messages = ref<ChatMessage[]>([
    {
        role: 'assistant',
        content: '你好，我是 AI 学习助手。你可以让我做学情分析、考试分析、组卷和答疑。'
    }
]);

const sendMessage = async () => {
    const text = inputText.value.trim();
    if (!text || loading.value) {
        return;
    }
    messages.value.push({ role: 'user', content: text });
    inputText.value = '';
    loading.value = true;

    try {
        const res = await chatWithAiAgent({
            message: text,
            session_id: props.sessionId || 'default',
            agent_type: 'auto'
        });
        if (!res?.success) {
            throw new Error('AI 服务返回失败');
        }
        messages.value.push({
            role: 'assistant',
            content: res.message || '已处理完成，但未返回文本内容。'
        });
    } catch (error: any) {
        ElMessage.error(error?.message || 'AI 服务调用失败，请稍后重试');
        messages.value.push({
            role: 'assistant',
            content: '当前无法连接 AI 服务，请稍后再试。'
        });
    } finally {
        loading.value = false;
    }
};
</script>

<style scoped>
.ai-assistant-entry {
    position: fixed;
    right: 24px;
    bottom: 24px;
    width: 52px;
    height: 52px;
    font-size: 16px;
    font-weight: 600;
    z-index: 2000;
}

.chat-panel {
    display: flex;
    flex-direction: column;
    height: calc(100vh - 120px);
}

.chat-list {
    flex: 1;
    overflow-y: auto;
    padding-right: 6px;
}

.chat-item {
    display: flex;
    margin-bottom: 12px;
}

.chat-item.user {
    justify-content: flex-end;
}

.chat-item.assistant {
    justify-content: flex-start;
}

.bubble {
    max-width: 90%;
    padding: 10px 12px;
    border-radius: 10px;
    line-height: 1.5;
    word-break: break-word;
    white-space: pre-wrap;
}

.chat-item.user .bubble {
    background: #409eff;
    color: #fff;
}

.chat-item.assistant .bubble {
    background: #f4f4f5;
    color: #303133;
}

.chat-input {
    margin-top: 8px;
}

.chat-actions {
    margin-top: 8px;
    text-align: right;
}
</style>

