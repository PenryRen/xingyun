<template>
  <div class="app-item-contain ai-learning-contain" v-loading="pageLoading">
    <el-tabs v-model="activeTab" type="border-card" class="ai-content-tabs" @tab-change="handleTabChange">
      <el-tab-pane label="总览" name="overview">
        <AiOverviewTab
          v-if="activeTab === 'overview'"
          :metrics="overviewMetrics"
          :trend-records="trendRecords"
          :student-name="studentDisplayName"
          :work-no="studentProfile.workNo || '--'"
          :volatility="volatilityDisplay"
          :waiting-review="counts.waitingReview"
          :waiting-verification="counts.waitingVerification"
          :verification-failed="counts.verificationFailed"
          :service-ready="serviceReady"
          :model-configured="modelConfigured"
          :model-available="modelAvailable"
          @open-tab="openTab"
          @ask-trend="askAboutTrend"
          @refresh-health="refreshAgentHealth(true)"
        />
      </el-tab-pane>

      <el-tab-pane label="考情分析" name="exam-analysis">
        <ExamAnalysisTab
          v-if="activeTab === 'exam-analysis'"
          :metrics="examMetrics"
          :trend-records="trendRecords"
          :recent-records="recentFormalRecords"
          :total-records="counts.total"
          :status-items="examStatusSummary"
          :status-ring-style="statusRingStyle"
          :data-source-label="dataSourceLabel"
          :insights="insights"
          @open-report="reportVisible = true"
          @ask-trend="askAboutTrend"
          @view-record="viewRecord"
        />
      </el-tab-pane>

      <el-tab-pane label="学情档案" name="profile">
        <LearningProfileTab
          v-if="activeTab === 'profile'"
          :student-name="studentDisplayName"
          :archive-fields="studentArchiveFields"
          :metrics="profileMetrics"
          :trend-records="trendRecords"
          @ask-profile="askAboutProfile"
        />
      </el-tab-pane>

      <el-tab-pane label="AI 助教" name="assistant">
        <AiAssistantTab
          v-if="activeTab === 'assistant'"
          :service-ready="serviceReady"
          :model-configured="modelConfigured"
          :model-available="modelAvailable"
          :health-checked="healthChecked"
          :health-loading="healthLoading"
          :status-message="modelStatusMessage"
          :question="question"
          :messages="conversationMessages"
          :ask-loading="askLoading"
          :prompts="suggestionPrompts"
          @refresh-health="refreshAgentHealth(true)"
          @ask-prompt="askSuggested"
          @clear="clearConversation"
          @update:question="question = $event"
          @send="sendQuestion"
        />
      </el-tab-pane>
    </el-tabs>

    <el-dialog v-model="reportVisible" title="考试分析报告" width="760px" class="analysis-dialog">
      <div class="report-meta">
        <el-tag type="success">基于真实考试记录</el-tag>
        <span>趋势使用近 {{ trendRecords.length }} 场；复盘线索覆盖全部 {{ counts.validFinalized }} 场有效已定稿记录</span>
      </div>
      <div class="report-score">
        <strong>{{ trendRecords.length ? overallScore : '--' }}</strong>
        <div><span>近场平均分</span><p>{{ reportSummary }}</p></div>
      </div>
      <section class="report-section">
        <h3>数据结论</h3>
        <ul><li v-for="item in insights" :key="item.title"><strong>{{ item.title }}：</strong>{{ item.description }}</li></ul>
      </section>
      <template #footer>
        <el-button @click="reportVisible = false">关闭</el-button>
        <el-button type="primary" @click="askAboutReport">让 AI 解读报告</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import {computed, onBeforeUnmount, onMounted, ref, watch} from 'vue';
import {useRoute, useRouter} from 'vue-router';
import {ElMessage} from 'element-plus';
import {askAgent, getAgentHealth, resetAgentSession} from '@/api/aiAgent';
import {getAiLearningWorkspace} from '@/api/aiLearning';
import useStore from '@/store';
import AiOverviewTab from './tabs/AiOverviewTab.vue';
import ExamAnalysisTab from './tabs/ExamAnalysisTab.vue';
import LearningProfileTab from './tabs/LearningProfileTab.vue';
import AiAssistantTab from './tabs/AiAssistantTab.vue';
import type {CSSProperties} from 'vue';
import type {AiConversationMessage, AiLearningWorkspace, AiTabName, ExamRecord, MetricItem, StatusItem} from './types';

const route = useRoute();
const router = useRouter();
const {user} = useStore();
const validTabs: AiTabName[] = ['overview', 'exam-analysis', 'profile', 'assistant'];
const normalizeTab = (value: unknown): AiTabName => validTabs.includes(String(value) as AiTabName) ? String(value) as AiTabName : 'overview';

const activeTab = ref<AiTabName>(normalizeTab(route.query.tab));
const pageLoading = ref(false);
const healthLoading = ref(false);
const healthChecked = ref(false);
const serviceReady = ref(false);
const modelConfigured = ref(false);
const modelAvailable = ref(false);
const modelProbeCode = ref('NOT_CHECKED');
const modelProbeMessage = ref('尚未检测模型连接');
const workspaceData = ref<AiLearningWorkspace>({
  student: {},
  counts: {total: 0, finalized: 0, validFinalized: 0, invalidFinalized: 0, waitingReview: 0, waitingVerification: 0, verificationFailed: 0, other: 0},
  summary: {trendSampleCount: 0, recentAveragePercent: null, changeFromFirstPercentPoints: null, volatilityPercentPoints: null, questionAccuracyPercent: null, accuracySampleCount: 0, finalizedPercent: null},
  trend: [],
  recentExams: [],
  insights: [],
  report: {summary: '需要先完成考试，才能形成可验证的学习结论。', evidenceExamCount: 0},
  knowledgePoints: {available: false, reasonCode: 'QUESTION_TAGS_NOT_AVAILABLE', message: '暂无知识点级数据'},
  meta: {source: 'LOCAL_MYSQL', scope: 'CURRENT_USER_FORMAL_EXAMS', complete: false, generatedAt: ''}
});
const dataLoadError = ref(false);
const reportVisible = ref(false);
const question = ref('');
const askLoading = ref(false);
const conversationMessages = ref<AiConversationMessage[]>([]);
let messageSequence = 0;

watch(() => route.query.tab, value => {
  const next = normalizeTab(value);
  if (next !== activeTab.value) activeTab.value = next;
});

const syncTabRoute = (tab: AiTabName) => {
  const current = normalizeTab(route.query.tab);
  if (current === tab && route.query.tab) return;
  router.push({query: {...route.query, tab}});
};
const handleTabChange = (name: string | number) => syncTabRoute(normalizeTab(name));
const openTab = (tab: AiTabName) => {
  activeTab.value = tab;
  syncTabRoute(tab);
  window.scrollTo({top: 0, behavior: 'smooth'});
};

const studentProfile = computed(() => workspaceData.value.student);
const counts = computed(() => workspaceData.value.counts);
const summary = computed(() => workspaceData.value.summary);
const trendRecords = computed(() => workspaceData.value.trend);
const recentFormalRecords = computed(() => workspaceData.value.recentExams);
const insights = computed(() => dataLoadError.value
  ? [{title: '数据读取失败', description: '正式考试数据读取失败，请重试。', evidence: '后端接口异常'}]
  : workspaceData.value.insights);
const overallScore = computed(() => summary.value.recentAveragePercent ?? 0);
const improvement = computed(() => summary.value.changeFromFirstPercentPoints ?? 0);
const accuracyRate = computed(() => summary.value.questionAccuracyPercent ?? 0);
const accuracySampleCount = computed(() => summary.value.accuracySampleCount);
const volatilityDisplay = computed(() => summary.value.volatilityPercentPoints === null ? '--' : `${summary.value.volatilityPercentPoints.toFixed(1)}pp`);

const overviewMetrics = computed<MetricItem[]>(() => [
  {label: '正式考试', value: String(counts.value.total), note: workspaceData.value.meta.complete ? '已加载全部记录' : '已加载记录'},
  {label: `近 ${trendRecords.value.length} 场平均分`, value: summary.value.recentAveragePercent === null ? '--' : `${overallScore.value}/100`},
  {label: '题目正确率', value: accuracySampleCount.value ? `${accuracyRate.value}%` : '--', note: `${accuracySampleCount.value} 场含题目汇总`},
  {label: '成绩已定稿', value: counts.value.total ? `${counts.value.finalized}/${counts.value.total}` : '--', note: summary.value.finalizedPercent === null ? '' : `${summary.value.finalizedPercent}%`}
]);
const examMetrics = computed<MetricItem[]>(() => [
  {label: `近 ${trendRecords.value.length} 场平均分`, value: summary.value.recentAveragePercent === null ? '--' : `${overallScore.value}/100`},
  {label: '较首场', value: summary.value.changeFromFirstPercentPoints === null ? '--' : `${improvement.value >= 0 ? '+' : ''}${improvement.value.toFixed(1)}`, note: '个百分点'},
  {label: '成绩波动', value: volatilityDisplay.value, note: '标准差'},
  {label: '已定稿率', value: summary.value.finalizedPercent === null ? '--' : `${summary.value.finalizedPercent}%`, note: `${counts.value.finalized}/${counts.value.total} 条记录`}
]);
const profileMetrics = computed<MetricItem[]>(() => [
  {label: '近期平均分', value: summary.value.recentAveragePercent === null ? '--' : `${overallScore.value}/100`, note: `最近 ${trendRecords.value.length} 场已定稿考试`},
  {label: '题目正确率', value: accuracySampleCount.value ? `${accuracyRate.value}%` : '--', note: `${accuracySampleCount.value} 场含题目汇总`},
  {label: '成绩已定稿率', value: summary.value.finalizedPercent === null ? '--' : `${summary.value.finalizedPercent}%`, note: `${counts.value.finalized}/${counts.value.total} 条记录`},
  {label: '成绩波动', value: volatilityDisplay.value, note: summary.value.volatilityPercentPoints === null ? '至少需要 3 场考试' : `最近 ${trendRecords.value.length} 场`}
]);

const studentDisplayName = computed(() => studentProfile.value.realName || user.realName || studentProfile.value.userName || user.userName || '学生');
const studentArchiveFields = computed(() => [
  {label: '用户名', value: studentProfile.value.userName || user.userName || '--'},
  {label: '工号', value: studentProfile.value.workNo || '--'},
  {label: '班级', value: studentProfile.value.departmentName || '--'},
  {label: '身份', value: studentProfile.value.jobTitle || '学生'}
]);

const examStatusSummary = computed<StatusItem[]>(() => [
  {label: '已定稿', count: counts.value.finalized, note: summary.value.finalizedPercent === null ? '--' : `${summary.value.finalizedPercent}%`, color: '#20a6a8'},
  {label: '待批改', count: counts.value.waitingReview, note: '不计入趋势', color: '#409eff'},
  {label: '待核验', count: counts.value.waitingVerification, note: '成绩未定稿', color: '#e6a23c'},
  {label: '核验失败', count: counts.value.verificationFailed, note: '需处理', color: '#f56c6c'},
  ...(counts.value.other ? [{label: '其他状态', count: counts.value.other, note: '待确认', color: '#c0c4cc'}] : [])
]);
const statusRingStyle = computed<CSSProperties>(() => {
  if (!counts.value.total) return {background: '#e4e7ed'};
  let cursor = 0;
  const segments = examStatusSummary.value.filter(item => item.count > 0).map(item => {
    const start = cursor;
    cursor += item.count / counts.value.total * 100;
    return `${item.color} ${start}% ${cursor}%`;
  });
  return {background: `conic-gradient(${segments.join(', ')})`};
});
const dataSourceLabel = computed(() => workspaceData.value.meta.complete
  ? `全部正式考试 · ${counts.value.total} 条`
  : `已加载正式考试 · ${counts.value.total} 条`);
const reportSummary = computed(() => workspaceData.value.report.summary);

const modelStatusMessage = computed(() => {
  if (!healthChecked.value) return '正在检测模型连接…';
  if (!serviceReady.value) return 'Agent 服务未连接，请确认本地 agent 容器已启动。';
  if (!modelConfigured.value) return 'Agent 在线，但模型 API 尚未配置。';
  if (!modelAvailable.value) return modelProbeMessage.value || 'Agent 在线，但模型网络不可用，请检查本地网络代理后重试。';
  return '模型调用可用。';
});

const loadWorkspaceData = async () => {
  pageLoading.value = true;
  dataLoadError.value = false;
  try {
    const result = await getAiLearningWorkspace();
    if (!result?.response) throw new Error('聚合接口未返回学习数据');
    workspaceData.value = result.response;
  } catch (error: any) {
    dataLoadError.value = true;
    ElMessage.error(error?.message || '正式考试数据读取失败，请重试');
  } finally {
    pageLoading.value = false;
  }
};

const refreshAgentHealth = async (force = false) => {
  if (healthLoading.value) return;
  healthLoading.value = true;
  try {
    const health = await getAgentHealth(force);
    serviceReady.value = health.service_ready !== false;
    modelConfigured.value = health.model_configured ?? health.model_ready === true;
    modelAvailable.value = health.model_available ?? health.model_ready === true;
    modelProbeCode.value = health.model_probe_code || (modelAvailable.value ? 'OK' : 'MODEL_UNAVAILABLE');
    modelProbeMessage.value = health.model_message || health.message || '';
  } catch {
    serviceReady.value = false;
    modelAvailable.value = false;
    modelProbeCode.value = 'AGENT_UNAVAILABLE';
    modelProbeMessage.value = 'Agent 服务未连接，请确认本地 agent 容器已启动。';
  } finally {
    healthChecked.value = true;
    healthLoading.value = false;
  }
};

const safeAgentError = (error: any) => {
  const detail = error?.response?.data?.detail;
  const code = detail?.code || detail?.error_code || error?.response?.data?.error?.code;
  const knownMessages: Record<string, string> = {
    MODEL_NETWORK_UNAVAILABLE: 'Agent 在线，但模型网络不可用，请检查本地网络代理后重试。',
    MODEL_AUTHENTICATION_FAILED: '模型鉴权失败，请检查本地 API 配置。',
    MODEL_RATE_LIMITED: '模型请求过于频繁，请稍后重试。',
    MODEL_NOT_FOUND: '已配置的模型不可用，请检查模型名称。'
  };
  return knownMessages[code] || detail?.message || error?.response?.data?.message || error?.message || '模型调用失败，请稍后重试或重新检测连接。';
};
const nextMessageId = (role: 'user' | 'assistant') => `${role}-${Date.now()}-${++messageSequence}`;
const sendQuestion = async () => {
  const text = question.value.trim();
  if (!text || askLoading.value) return;
  if (!serviceReady.value || !modelConfigured.value) {
    ElMessage.warning(modelStatusMessage.value);
    return;
  }
  conversationMessages.value.push({id: nextMessageId('user'), role: 'user', content: text});
  question.value = '';
  askLoading.value = true;
  try {
    const result = await askAgent(text);
    conversationMessages.value.push({id: nextMessageId('assistant'), role: 'assistant', content: result.content});
    modelAvailable.value = true;
  } catch (error: any) {
    const message = safeAgentError(error);
    conversationMessages.value.push({id: nextMessageId('assistant'), role: 'assistant', content: message, error: true});
    modelAvailable.value = false;
    modelProbeCode.value = error?.response?.data?.detail?.code || 'MODEL_CALL_FAILED';
    modelProbeMessage.value = message;
    ElMessage.error(message);
  } finally {
    askLoading.value = false;
  }
};

const suggestionPrompts = [
  '结合我的成绩趋势，分析最近为什么不稳定。',
  '根据正确率和平均分，给我制定一周复习计划。',
  '结合当前数据，告诉我下一阶段优先提升什么。'
];
const askSuggested = (prompt: string) => {
  question.value = prompt;
  sendQuestion();
};
const askAboutProfile = () => {
  reportVisible.value = false;
  question.value = '请结合系统提供的脱敏学情摘要，根据我的平均分、正确率和成绩波动，制定一周复习计划。';
  openTab('assistant');
};
const askAboutReport = () => {
  reportVisible.value = false;
  question.value = '请结合系统提供的脱敏考试摘要，对当前考试分析报告做简明总结，并给出下一步建议。';
  openTab('assistant');
};
const askAboutTrend = () => {
  question.value = '请结合系统提供的最近六场脱敏成绩趋势，分析我的表现是否稳定、可能需要关注什么，并给出下一步复习建议。';
  openTab('assistant');
};
const clearConversation = async () => {
  if (askLoading.value) return;
  try {
    await resetAgentSession();
    conversationMessages.value = [];
    question.value = '';
    ElMessage.success('已开始新的对话');
  } catch (error: any) {
    ElMessage.error(error?.message || '新建对话失败，请稍后重试');
  }
};
const viewRecord = (record: ExamRecord) => {
  if (record.watch) {
    router.push({path: '/read', query: {id: record.id}});
  } else {
    router.push('/record/index');
  }
};

onMounted(() => {
  document.getElementById('app')?.classList.add('ai-learning-app');
  loadWorkspaceData();
  refreshAgentHealth();
});

onBeforeUnmount(() => {
  document.getElementById('app')?.classList.remove('ai-learning-app');
});
</script>

<style scoped lang="scss">
.ai-learning-contain { min-height: 560px; color: #4c596b; }
:global(#app.ai-learning-app) { min-width: 0; }
.ai-content-tabs { border-radius: 4px; box-shadow: none; }
.ai-content-tabs :deep(.el-tabs__header) { margin: 0; background: #fff; }
.ai-content-tabs :deep(.el-tabs__item) { width: 140px; height: 52px; color: #606266; font-size: 16px; font-weight: 400; }
.ai-content-tabs :deep(.el-tabs__item:hover),
.ai-content-tabs :deep(.el-tabs__item.is-active) { color: #68bfd6; }
.ai-content-tabs :deep(.el-tabs__content) { padding: 20px; background: #f8f8f8; }
.report-meta { display: flex; align-items: center; gap: 12px; color: #606266; font-size: 13px; }
.report-score { display: grid; grid-template-columns: 100px minmax(0, 1fr); gap: 22px; margin-top: 20px; padding: 20px; border: 1px solid #ebeef5; border-radius: 4px; background: #fafafa; }
.report-score > strong { color: #45aec8; font-size: 46px; font-weight: 500; text-align: center; }
.report-score span { color: #303133; font-size: 15px; }
.report-score p { margin: 8px 0 0; color: #606266; font-size: 14px; line-height: 1.7; }
.report-section { margin-top: 22px; }
.report-section h3 { margin: 0; color: #303133; font-size: 16px; font-weight: 500; }
.report-section ul { margin: 12px 0 0; padding-left: 20px; }
.report-section li { margin-bottom: 10px; color: #606266; font-size: 14px; line-height: 1.6; }
:global(.analysis-dialog) { max-width: calc(100vw - 32px); }

@media (max-width: 768px) {
  .ai-learning-contain { padding-top: 18px; }
  .ai-content-tabs :deep(.el-tabs__nav-wrap) { overflow-x: auto; }
  .ai-content-tabs :deep(.el-tabs__item) { width: auto; min-width: 92px; padding: 0 16px; font-size: 14px; }
  .ai-content-tabs :deep(.el-tabs__content) { padding: 12px; }
}

@media (max-width: 480px) {
  .report-score { grid-template-columns: minmax(0, 1fr); }
}
</style>
