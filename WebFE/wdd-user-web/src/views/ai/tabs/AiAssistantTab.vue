<template>
  <section class="assistant-tab" aria-label="AI 助教">
    <div class="assistant-sheet">
      <main class="conversation-panel">
        <div class="model-status">
          <span><i :class="serviceReady ? 'ready' : 'error'"></i>{{ serviceReady ? 'Agent 在线' : 'Agent 未连接' }}</span>
          <span><i :class="modelConfigured ? 'ready' : 'muted'"></i>{{ modelConfigured ? '模型已配置' : '模型未配置' }}</span>
          <span><i :class="modelAvailable ? 'ready' : modelConfigured ? 'warning' : 'muted'"></i>{{ modelAvailable ? '模型调用可用' : '模型调用不可用' }}</span>
          <button type="button" :disabled="healthLoading" @click="$emit('refresh-health')">{{ healthLoading ? '检测中…' : '重新检测' }}</button>
        </div>

        <div v-if="healthChecked && (!serviceReady || !modelConfigured || !modelAvailable)" class="model-warning" role="status">
          <span>{{ statusMessage }}</span>
          <button type="button" :disabled="healthLoading" @click="$emit('refresh-health')">重新检测</button>
        </div>

        <div class="prompt-list">
          <button v-for="prompt in prompts" :key="prompt" type="button" :disabled="askLoading || !modelAvailable" @click="$emit('ask-prompt', prompt)">{{ prompt }}</button>
        </div>

        <div v-if="reply" class="assistant-reply">
          <span>AI</span>
          <p>{{ reply }}</p>
        </div>
        <div v-else class="assistant-empty">
          <strong>可以从考试数据开始提问</strong>
          <p>AI 会收到当前已加载的考试摘要；没有知识点数据时会明确说明。</p>
        </div>

        <div class="ask-row">
          <el-input
            :model-value="question"
            :disabled="askLoading || !modelAvailable"
            maxlength="1000"
            placeholder="输入你的问题…"
            @update:model-value="$emit('update:question', $event)"
            @keyup.enter="$emit('send')"
          />
          <el-button type="primary" :loading="askLoading" :disabled="!question.trim() || !modelAvailable" aria-label="发送问题" @click="$emit('send')">
            <el-icon v-if="!askLoading"><Promotion/></el-icon>
          </el-button>
        </div>
        <small>回答由 AI 生成，请结合考试记录与课程资料核验。</small>
      </main>

      <aside class="evidence-rail">
        <h2>本次对话依据</h2>
        <ul>
          <li><span>{{ examCount }} 场正式考试</span></li>
          <li><span>近 {{ trendCount }} 场趋势</span></li>
          <li><span>{{ accuracySampleCount }} 场题目汇总</span></li>
          <li><span>暂无知识点数据</span></li>
        </ul>
      </aside>
    </div>
  </section>
</template>

<script setup lang="ts">
import {Promotion} from '@element-plus/icons-vue';

defineProps<{
  serviceReady: boolean;
  modelConfigured: boolean;
  modelAvailable: boolean;
  healthChecked: boolean;
  healthLoading: boolean;
  statusMessage: string;
  question: string;
  reply: string;
  askLoading: boolean;
  prompts: string[];
  examCount: number;
  trendCount: number;
  accuracySampleCount: number;
}>();

defineEmits<{
  (event: 'refresh-health'): void;
  (event: 'ask-prompt', prompt: string): void;
  (event: 'update:question', value: string): void;
  (event: 'send'): void;
}>();
</script>

<style scoped>
.assistant-tab { color: #4c596b; }
.assistant-sheet { display: grid; grid-template-columns: minmax(0, 1fr) 310px; border: 1px solid #ebeef5; border-radius: 4px; background: #fff; }
.conversation-panel { min-width: 0; padding: 24px 28px; }
.model-status { display: flex; align-items: center; gap: 26px; min-height: 32px; padding-bottom: 18px; border-bottom: 1px solid #ebeef5; }
.model-status span { display: inline-flex; align-items: center; gap: 8px; color: #606266; font-size: 14px; }
.model-status i { width: 8px; height: 8px; border-radius: 50%; background: #c0c4cc; }
.model-status i.ready { background: #28b487; }
.model-status i.warning { background: #e6a23c; }
.model-status i.error { background: #f56c6c; }
.model-status button, .model-warning button { margin-left: auto; padding: 0; border: 0; background: transparent; color: #409eff; font-size: 14px; cursor: pointer; }
.model-status button:disabled, .model-warning button:disabled { color: #c0c4cc; cursor: not-allowed; }
.model-warning { display: flex; align-items: center; gap: 20px; margin-top: 18px; padding: 13px 16px; border: 1px solid #faecd8; border-radius: 4px; background: #fdf6ec; color: #b88230; font-size: 14px; }
.prompt-list { display: flex; flex-wrap: wrap; gap: 12px; margin-top: 22px; }
.prompt-list button { min-height: 36px; padding: 0 16px; border: 1px solid #b3d8ff; border-radius: 4px; background: #fff; color: #337ecc; font-size: 14px; cursor: pointer; }
.prompt-list button:disabled { border-color: #e4e7ed; color: #c0c4cc; cursor: not-allowed; }
.assistant-reply, .assistant-empty { min-height: 260px; margin-top: 22px; padding: 22px; border: 1px solid #d9ecff; border-radius: 4px; background: #f5fbff; }
.assistant-reply { display: grid; grid-template-columns: 38px minmax(0, 1fr); gap: 16px; }
.assistant-reply > span { display: flex; align-items: center; justify-content: center; width: 38px; height: 38px; border-radius: 50%; background: #45aec8; color: #fff; font-size: 13px; font-weight: 600; }
.assistant-reply p { margin: 0; color: #4c596b; font-size: 14px; line-height: 1.85; white-space: pre-wrap; }
.assistant-empty { display: flex; align-items: center; justify-content: center; flex-direction: column; color: #909399; text-align: center; }
.assistant-empty strong { color: #606266; font-size: 16px; font-weight: 500; }
.assistant-empty p { margin: 10px 0 0; font-size: 14px; line-height: 1.7; }
.ask-row { display: grid; grid-template-columns: minmax(0, 1fr) 48px; gap: 10px; margin-top: 18px; }
.ask-row :deep(.el-input__wrapper) { min-height: 44px; border-radius: 4px; box-shadow: 0 0 0 1px #dcdfe6 inset; }
.ask-row :deep(.el-button) { width: 48px; height: 44px; border-radius: 4px; background: #68bfd6; border-color: #68bfd6; }
.conversation-panel > small { display: block; margin-top: 9px; color: #909399; font-size: 12px; }
.evidence-rail { padding: 26px 28px; border-left: 1px solid #ebeef5; }
.evidence-rail h2 { margin: 0; color: #303133; font-size: 18px; font-weight: 500; }
.evidence-rail ul { margin: 22px 0 0; padding: 0; list-style: none; }
.evidence-rail li { position: relative; padding: 20px 0 20px 24px; border-bottom: 1px solid #ebeef5; color: #606266; font-size: 14px; }
.evidence-rail li::before { position: absolute; top: 26px; left: 2px; width: 7px; height: 7px; border-radius: 50%; background: #a8b3bd; content: ''; }

@media (max-width: 900px) {
  .assistant-sheet { grid-template-columns: minmax(0, 1fr); }
  .evidence-rail { border-top: 1px solid #ebeef5; border-left: 0; }
  .evidence-rail ul { display: grid; grid-template-columns: 1fr 1fr; gap: 0 20px; }
}

@media (max-width: 560px) {
  .conversation-panel, .evidence-rail { padding: 18px 14px; }
  .model-status { align-items: flex-start; flex-direction: column; gap: 10px; }
  .model-status button { margin-left: 0; }
  .model-warning { align-items: flex-start; flex-direction: column; }
  .model-warning button { margin-left: 0; }
  .assistant-reply { grid-template-columns: minmax(0, 1fr); }
  .evidence-rail ul { grid-template-columns: minmax(0, 1fr); }
}
</style>
