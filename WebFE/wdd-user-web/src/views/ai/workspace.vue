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
          :waiting-review="waitingReviewCount"
          :waiting-verification="waitingVerificationCount"
          :verification-failed="verificationFailedCount"
          :service-ready="serviceReady"
          :model-configured="modelConfigured"
          :model-available="modelAvailable"
          @open-tab="openTab"
          @refresh-health="refreshAgentHealth(true)"
        />
      </el-tab-pane>

      <el-tab-pane label="考情分析" name="exam-analysis">
        <ExamAnalysisTab
          v-if="activeTab === 'exam-analysis'"
          :metrics="examMetrics"
          :trend-records="trendRecords"
          :recent-records="recentFormalRecords"
          :total-records="records.length"
          :status-items="examStatusSummary"
          :status-ring-style="statusRingStyle"
          :data-source-label="dataSourceLabel"
          :insights="insights"
          @open-report="reportVisible = true"
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
          :reply="assistantReply"
          :ask-loading="askLoading"
          :prompts="suggestionPrompts"
          :exam-count="records.length"
          :trend-count="trendRecords.length"
          :accuracy-sample-count="accuracySampleCount"
          @refresh-health="refreshAgentHealth(true)"
          @ask-prompt="askSuggested"
          @update:question="question = $event"
          @send="sendQuestion"
        />
      </el-tab-pane>
    </el-tabs>

    <el-dialog v-model="reportVisible" title="考试分析报告" width="760px" class="analysis-dialog">
      <div class="report-meta">
        <el-tag type="success">基于真实考试记录</el-tag>
        <span>趋势使用近 {{ trendRecords.length }} 场；复盘线索覆盖全部 {{ completedRecords.length }} 场有效已定稿记录</span>
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
        <el-button type="primary" @click="askAboutReport">向 AI 询问本报告</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import {computed, onBeforeUnmount, onMounted, ref, watch} from 'vue';
import {useRoute, useRouter} from 'vue-router';
import {ElMessage} from 'element-plus';
import {page as paperPage} from '@/api/examPaperAnswer';
import {askAgent, getAgentHealth} from '@/api/aiAgent';
import {getCurrentUser} from '@/api/user';
import useStore from '@/store';
import AiOverviewTab from './tabs/AiOverviewTab.vue';
import ExamAnalysisTab from './tabs/ExamAnalysisTab.vue';
import LearningProfileTab from './tabs/LearningProfileTab.vue';
import AiAssistantTab from './tabs/AiAssistantTab.vue';
import type {CSSProperties} from 'vue';
import type {AiTabName, ExamRecord, InsightItem, MetricItem, StatusItem, StudentProfile} from './types';

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
const records = ref<ExamRecord[]>([]);
const studentProfile = ref<StudentProfile>({});
const allFormalRecordsLoaded = ref(false);
const dataLoadError = ref(false);
const reportVisible = ref(false);
const question = ref('');
const askLoading = ref(false);
const assistantReply = ref('');

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

const finiteNumber = (value: unknown): number | null => {
  if (value === null || value === undefined || value === '') return null;
  const parsed = Number(value);
  return Number.isFinite(parsed) ? parsed : null;
};
const rawScoreOf = (record: ExamRecord): number | null => {
  const total = finiteNumber(record.paperScore);
  const score = finiteNumber(record.userScore);
  if (total === null || score === null || total <= 0 || score < 0 || score > total) return null;
  return score / total * 100;
};
const scoreOf = (record: ExamRecord) => Math.round(rawScoreOf(record) ?? 0);
const clamp = (value: number) => Math.max(0, Math.min(100, Math.round(Number.isFinite(value) ? value : 0)));
const timeOf = (record: ExamRecord) => {
  if (!record.createTime) return Number(record.id || 0);
  const parsed = new Date(record.createTime.replace(' ', 'T')).getTime();
  return Number.isFinite(parsed) ? parsed : Number(record.id || 0);
};

const completedRecords = computed(() => records.value
  .filter(item => item.status === 2 && rawScoreOf(item) !== null)
  .sort((a, b) => timeOf(a) - timeOf(b) || Number(a.id) - Number(b.id)));
const invalidCompletedCount = computed(() => records.value.filter(item => item.status === 2 && rawScoreOf(item) === null).length);
const trendRecords = computed(() => completedRecords.value.slice(-6));
const normalizedScores = computed(() => trendRecords.value.map(item => rawScoreOf(item) as number));
const overallScore = computed(() => normalizedScores.value.length
  ? clamp(normalizedScores.value.reduce((sum, score) => sum + score, 0) / normalizedScores.value.length)
  : 0);
const improvement = computed(() => normalizedScores.value.length > 1
  ? normalizedScores.value[normalizedScores.value.length - 1] - normalizedScores.value[0]
  : 0);
const scoreStandardDeviation = computed<number | null>(() => {
  if (normalizedScores.value.length < 3) return null;
  const mean = normalizedScores.value.reduce((sum, value) => sum + value, 0) / normalizedScores.value.length;
  const variance = normalizedScores.value.reduce((sum, value) => sum + Math.pow(value - mean, 2), 0) / normalizedScores.value.length;
  return Math.sqrt(variance);
});
const volatilityDisplay = computed(() => scoreStandardDeviation.value === null ? '--' : `${scoreStandardDeviation.value.toFixed(1)}pp`);

const accuracySummary = computed(() => completedRecords.value.reduce((acc, item) => {
  const correct = finiteNumber(item.questionCorrect);
  const count = finiteNumber(item.questionCount);
  if (correct !== null && count !== null && Number.isInteger(correct) && Number.isInteger(count) && count > 0 && correct >= 0 && correct <= count) {
    acc.correct += correct;
    acc.count += count;
    acc.samples += 1;
  }
  return acc;
}, {correct: 0, count: 0, samples: 0}));
const accuracyRate = computed(() => accuracySummary.value.count ? clamp(accuracySummary.value.correct / accuracySummary.value.count * 100) : 0);
const accuracySampleCount = computed(() => accuracySummary.value.samples);
const finalizedCount = computed(() => records.value.filter(item => item.status === 2).length);
const waitingReviewCount = computed(() => records.value.filter(item => item.status === 1).length);
const waitingVerificationCount = computed(() => records.value.filter(item => item.status === 3).length);
const verificationFailedCount = computed(() => records.value.filter(item => item.status === 4).length);
const completionRate = computed(() => records.value.length ? clamp(finalizedCount.value / records.value.length * 100) : 0);
const unknownStatusCount = computed(() => Math.max(0, records.value.length - finalizedCount.value - waitingReviewCount.value - waitingVerificationCount.value - verificationFailedCount.value));

const overviewMetrics = computed<MetricItem[]>(() => [
  {label: '正式考试', value: String(records.value.length), note: allFormalRecordsLoaded.value ? '已加载全部记录' : '已加载记录'},
  {label: `近 ${trendRecords.value.length} 场平均分`, value: trendRecords.value.length ? `${overallScore.value}/100` : '--'},
  {label: '题目正确率', value: accuracySampleCount.value ? `${accuracyRate.value}%` : '--', note: `${accuracySampleCount.value} 场含题目汇总`},
  {label: '成绩已定稿', value: records.value.length ? `${finalizedCount.value}/${records.value.length}` : '--', note: records.value.length ? `${completionRate.value}%` : ''}
]);
const examMetrics = computed<MetricItem[]>(() => [
  {label: `近 ${trendRecords.value.length} 场平均分`, value: trendRecords.value.length ? `${overallScore.value}/100` : '--'},
  {label: '较首场', value: trendRecords.value.length > 1 ? `${improvement.value >= 0 ? '+' : ''}${improvement.value.toFixed(1)}` : '--', note: '个百分点'},
  {label: '成绩波动', value: volatilityDisplay.value, note: '标准差'},
  {label: '已定稿率', value: records.value.length ? `${completionRate.value}%` : '--', note: `${finalizedCount.value}/${records.value.length} 条记录`}
]);
const profileMetrics = computed<MetricItem[]>(() => [
  {label: '近期平均分', value: trendRecords.value.length ? `${overallScore.value}/100` : '--', note: `最近 ${trendRecords.value.length} 场已定稿考试`},
  {label: '题目正确率', value: accuracySampleCount.value ? `${accuracyRate.value}%` : '--', note: `${accuracySampleCount.value} 场含题目汇总`},
  {label: '成绩已定稿率', value: records.value.length ? `${completionRate.value}%` : '--', note: `${finalizedCount.value}/${records.value.length} 条记录`},
  {label: '成绩波动', value: volatilityDisplay.value, note: scoreStandardDeviation.value === null ? '至少需要 3 场考试' : `最近 ${trendRecords.value.length} 场`}
]);

const studentDisplayName = computed(() => studentProfile.value.realName || user.realName || studentProfile.value.userName || user.userName || '学生');
const studentArchiveFields = computed(() => [
  {label: '用户名', value: studentProfile.value.userName || user.userName || '--'},
  {label: '工号', value: studentProfile.value.workNo || '--'},
  {label: '班级', value: studentProfile.value.departmentStr || '--'},
  {label: '身份', value: studentProfile.value.jobTitle || '学生'}
]);

const recentFormalRecords = computed(() => [...records.value]
  .sort((a, b) => timeOf(b) - timeOf(a) || Number(b.id) - Number(a.id))
  .slice(0, 6));
const examStatusSummary = computed<StatusItem[]>(() => [
  {label: '已定稿', count: finalizedCount.value, note: records.value.length ? `${Math.round(finalizedCount.value / records.value.length * 100)}%` : '--', color: '#20a6a8'},
  {label: '待批改', count: waitingReviewCount.value, note: '不计入趋势', color: '#409eff'},
  {label: '待核验', count: waitingVerificationCount.value, note: '成绩未定稿', color: '#e6a23c'},
  {label: '核验失败', count: verificationFailedCount.value, note: '需处理', color: '#f56c6c'},
  ...(unknownStatusCount.value ? [{label: '其他状态', count: unknownStatusCount.value, note: '待确认', color: '#c0c4cc'}] : [])
]);
const statusRingStyle = computed<CSSProperties>(() => {
  if (!records.value.length) return {background: '#e4e7ed'};
  let cursor = 0;
  const segments = examStatusSummary.value.filter(item => item.count > 0).map(item => {
    const start = cursor;
    cursor += item.count / records.value.length * 100;
    return `${item.color} ${start}% ${cursor}%`;
  });
  return {background: `conic-gradient(${segments.join(', ')})`};
});
const dataSourceLabel = computed(() => allFormalRecordsLoaded.value ? `全部正式考试 · ${records.value.length} 条` : `已加载正式考试 · ${records.value.length} 条`);
const lowestRecord = computed(() => completedRecords.value.length
  ? completedRecords.value.reduce((lowest, current) => scoreOf(current) < scoreOf(lowest) ? current : lowest)
  : null);
const latestRecord = computed(() => completedRecords.value[completedRecords.value.length - 1] || null);

const insights = computed<InsightItem[]>(() => {
  if (!completedRecords.value.length) {
    return [{title: '分析样本不足', description: dataLoadError.value ? '正式考试数据读取失败，请重试。' : '当前没有分数已定稿的正式考试。', evidence: '0 场'}];
  }
  const lowest = lowestRecord.value as ExamRecord;
  const latest = latestRecord.value as ExamRecord;
  const latestCorrect = finiteNumber(latest.questionCorrect);
  const latestCount = finiteNumber(latest.questionCount);
  const validSummary = latestCorrect !== null && latestCount !== null && latestCount > 0 && latestCorrect >= 0 && latestCorrect <= latestCount;
  const result: InsightItem[] = [
    {title: `最低得分出现在《${lowest.paperName || '未命名试卷'}》`, description: `该场得分率为 ${scoreOf(lowest)}%，建议优先复盘失分题目和作答过程。`, evidence: `失分 ${Math.max(0, Number(lowest.paperScore || 0) - Number(lowest.userScore || 0))} 分`},
    {title: '最近一次正式考试作答线索', description: validSummary ? `最近完成《${latest.paperName || '未命名试卷'}》，完全答对 ${latestCorrect} / ${latestCount} 题。` : `《${latest.paperName || '未命名试卷'}》缺少有效题目汇总。`, evidence: validSummary ? `${latestCount - latestCorrect} 题需复盘` : '无题目汇总'}
  ];
  if (verificationFailedCount.value) result.push({title: '存在核验失败记录', description: '建议在考试记录中检查并联系管理员处理。', evidence: `${verificationFailedCount.value} 条异常`});
  if (waitingReviewCount.value || waitingVerificationCount.value) result.push({title: '存在成绩尚未定稿的记录', description: `待批改 ${waitingReviewCount.value} 条，待核验 ${waitingVerificationCount.value} 条；定稿前不计入趋势。`, evidence: `${waitingReviewCount.value + waitingVerificationCount.value} 条待处理`});
  if (invalidCompletedCount.value) result.push({title: '存在分数字段异常的已完成记录', description: '这些记录不会作为 0 分进入趋势。', evidence: `${invalidCompletedCount.value} 条异常`});
  return result;
});

const reportSummary = computed(() => {
  if (!trendRecords.value.length) return '需要先完成考试，才能形成可验证的学习结论。';
  if (trendRecords.value.length < 2) return `当前只有 1 场已定稿正式考试，得分率为 ${overallScore.value}%，样本不足以判断趋势。`;
  const direction = improvement.value > 0
    ? `末场较本窗口首场高 ${Math.abs(improvement.value).toFixed(1)} 个百分点`
    : improvement.value < 0
      ? `末场较本窗口首场低 ${Math.abs(improvement.value).toFixed(1)} 个百分点`
      : '末场与本窗口首场得分率相同';
  return `最近 ${trendRecords.value.length} 场平均得分率为 ${overallScore.value}%，${direction}。中间场次存在波动，应结合具体答卷复盘。`;
});

const modelStatusMessage = computed(() => {
  if (!healthChecked.value) return '正在检测模型连接…';
  if (!serviceReady.value) return 'Agent 服务未连接，请确认本地 agent 容器已启动。';
  if (!modelConfigured.value) return 'Agent 在线，但模型 API 尚未配置。';
  if (!modelAvailable.value) return modelProbeMessage.value || 'Agent 在线，但模型网络不可用，请检查本地网络代理后重试。';
  return '模型调用可用。';
});

const loadStudentProfile = async () => {
  try {
    const result = await getCurrentUser();
    studentProfile.value = result?.response || {};
  } catch {
    studentProfile.value = {userName: user.userName, realName: user.realName};
  }
};

const loadExamData = async () => {
  pageLoading.value = true;
  dataLoadError.value = false;
  try {
    const pageSize = 100;
    const query = {paperType: 1, passed: null, examPaperArchiveId: null, pageIndex: 1, pageSize};
    const firstResult = await paperPage(query);
    const firstPage = firstResult?.response;
    const firstList = Array.isArray(firstPage?.list) ? firstPage.list : [];
    const total = Number(firstPage?.total || firstList.length);
    const totalPages = Math.max(1, Math.ceil(total / pageSize));
    const remainingResults = totalPages > 1
      ? await Promise.all(Array.from({length: totalPages - 1}, (_, index) => paperPage({...query, pageIndex: index + 2})))
      : [];
    records.value = [...firstList, ...remainingResults.flatMap(result => Array.isArray(result?.response?.list) ? result.response.list : [])];
    allFormalRecordsLoaded.value = records.value.length >= total;
  } catch (error: any) {
    dataLoadError.value = true;
    ElMessage.error(error?.message || '考试数据加载失败');
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

const buildLearningContext = () => {
  const summary = trendRecords.value.map(item => `${item.paperName || '未命名试卷'}：得分率${scoreOf(item)}%，完全答对${finiteNumber(item.questionCorrect) ?? '未知'}/${finiteNumber(item.questionCount) ?? '未知'}题`).join('；');
  return `学生${user.userName || ''}最近已定稿正式考试数据：${summary || '暂无'}。近场平均得分率${overallScore.value}%，已加载正式考试正确率${accuracyRate.value}%。当前没有知识点级数据，不得推断具体薄弱知识点。`;
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
  return knownMessages[code] || detail?.message || '模型调用失败，请稍后重试或重新检测连接。';
};
const sendQuestion = async () => {
  const text = question.value.trim();
  if (!text || askLoading.value) return;
  if (!modelAvailable.value) {
    assistantReply.value = modelStatusMessage.value;
    ElMessage.warning(modelStatusMessage.value);
    return;
  }
  askLoading.value = true;
  assistantReply.value = '';
  try {
    const result = await askAgent(`${buildLearningContext()}\n用户问题：${text}\n请只基于给出的真实数据回答；数据不足时明确说明。`, `student-${user.userName || 'local'}`);
    assistantReply.value = result.content;
    question.value = '';
    modelAvailable.value = true;
  } catch (error: any) {
    const message = safeAgentError(error);
    assistantReply.value = message;
    modelAvailable.value = false;
    modelProbeCode.value = error?.response?.data?.detail?.code || 'MODEL_CALL_FAILED';
    modelProbeMessage.value = message;
    ElMessage.error(message);
  } finally {
    askLoading.value = false;
  }
};

const suggestionPrompts = ['我该先复习什么？', '解释最近成绩趋势', '给我一份学习计划'];
const askSuggested = (prompt: string) => {
  question.value = prompt;
  sendQuestion();
};
const askAboutProfile = () => {
  reportVisible.value = false;
  question.value = '请解释我的学情档案，并根据现有考试证据给出下一步建议；没有知识点级数据时请明确说明。';
  openTab('assistant');
};
const askAboutReport = () => {
  reportVisible.value = false;
  question.value = '请解释这份考试分析报告，并根据现有考试证据给出下一步复盘建议；不要推断不存在的知识点数据。';
  openTab('assistant');
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
  loadExamData();
  loadStudentProfile();
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
