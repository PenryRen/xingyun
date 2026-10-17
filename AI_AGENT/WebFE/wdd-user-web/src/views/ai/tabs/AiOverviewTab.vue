<template>
  <section class="overview-tab" aria-label="AI 学习中心总览">
    <div class="metric-strip">
      <div v-for="item in metrics" :key="item.label" class="metric-cell">
        <span>{{ item.label }}</span>
        <strong>{{ item.value }}</strong>
        <small v-if="item.note">{{ item.note }}</small>
      </div>
    </div>

    <div class="overview-sheet">
      <section class="trend-summary">
        <header class="section-head">
          <h2>近期表现</h2>
          <div class="head-actions">
            <button type="button" @click="$emit('ask-trend')">询问 AI</button>
            <button type="button" @click="$emit('open-tab', 'exam-analysis')">进入考情分析</button>
          </div>
        </header>
        <ExamTrendChart :records="trendRecords"/>
      </section>

      <section class="profile-summary">
        <header class="section-head">
          <h2>学习档案摘要</h2>
          <button type="button" @click="$emit('open-tab', 'profile')">查看学情档案</button>
        </header>
        <div class="student-line">
          <strong>{{ studentName }}</strong>
          <span>工号 {{ workNo }}</span>
        </div>
        <dl>
          <div><dt>近期平均分</dt><dd>{{ metrics[1]?.value || '--' }}</dd></div>
          <div><dt>题目正确率</dt><dd>{{ metrics[2]?.value || '--' }}</dd></div>
          <div><dt>成绩波动（标准差）</dt><dd>{{ volatility }}</dd></div>
        </dl>
        <p>知识点数据尚未接入，当前档案仅来自正式考试记录。</p>
      </section>
    </div>

    <footer class="overview-footer">
      <div class="pending-summary">
        <strong>待处理：</strong>
        <span>待批改 {{ waitingReview }}</span>
        <i>·</i>
        <span>待核验 {{ waitingVerification }}</span>
        <i>·</i>
        <span>核验失败 {{ verificationFailed }}</span>
      </div>
      <div class="service-summary">
        <span><i :class="serviceReady ? 'ready' : 'error'"></i>{{ serviceReady ? 'Agent 在线' : 'Agent 未连接' }}</span>
        <span><i :class="modelAvailable ? 'ready' : modelConfigured ? 'warning' : 'muted'"></i>{{ modelAvailable ? '模型调用可用' : modelConfigured ? '模型调用不可用' : '模型未配置' }}</span>
        <button type="button" @click="$emit('refresh-health')">重新检测</button>
      </div>
    </footer>
  </section>
</template>

<script setup lang="ts">
import ExamTrendChart from '../components/ExamTrendChart.vue';
import type {AiTabName, ExamRecord, MetricItem} from '../types';

defineProps<{
  metrics: MetricItem[];
  trendRecords: ExamRecord[];
  studentName: string;
  workNo: string;
  volatility: string;
  waitingReview: number;
  waitingVerification: number;
  verificationFailed: number;
  serviceReady: boolean;
  modelConfigured: boolean;
  modelAvailable: boolean;
}>();

defineEmits<{
  (event: 'open-tab', tab: AiTabName): void;
  (event: 'ask-trend'): void;
  (event: 'refresh-health'): void;
}>();
</script>

<style scoped>
.overview-tab { color: #4c596b; }
.metric-strip { display: grid; grid-template-columns: repeat(4, minmax(0, 1fr)); border: 1px solid #ebeef5; border-radius: 4px; background: #fff; }
.metric-cell { min-width: 0; padding: 22px 28px; border-right: 1px solid #ebeef5; }
.metric-cell:last-child { border-right: 0; }
.metric-cell span { display: block; color: #606266; font-size: 14px; }
.metric-cell strong { display: inline-block; margin-top: 8px; color: #303133; font-size: 26px; font-weight: 500; line-height: 1.2; }
.metric-cell small { display: block; margin-top: 5px; color: #909399; font-size: 12px; }
.overview-sheet { display: grid; grid-template-columns: minmax(0, 1fr) minmax(0, 1fr); margin-top: 20px; border: 1px solid #ebeef5; border-radius: 4px; background: #fff; }
.overview-sheet > section { min-width: 0; padding: 26px 28px 24px; }
.overview-sheet > section + section { border-left: 1px solid #ebeef5; }
.section-head { display: flex; align-items: center; justify-content: space-between; gap: 20px; margin-bottom: 18px; }
.section-head h2 { margin: 0; color: #303133; font-size: 18px; font-weight: 500; }
.head-actions { display: flex; align-items: center; gap: 18px; }
.section-head button, .service-summary button { padding: 0; border: 0; background: transparent; color: #409eff; font-size: 14px; cursor: pointer; }
.student-line { display: flex; align-items: baseline; gap: 22px; padding-bottom: 18px; border-bottom: 1px solid #ebeef5; }
.student-line strong { color: #303133; font-size: 18px; font-weight: 500; }
.student-line span { color: #909399; font-size: 14px; }
.profile-summary dl { margin: 18px 0 0; }
.profile-summary dl > div { display: flex; align-items: center; justify-content: space-between; padding: 14px 0; border-bottom: 1px solid #f2f3f5; }
.profile-summary dt { color: #606266; font-size: 14px; }
.profile-summary dd { margin: 0; color: #303133; font-size: 18px; }
.profile-summary p { margin: 18px 0 0; color: #909399; font-size: 13px; line-height: 1.7; }
.overview-footer { display: flex; align-items: center; justify-content: space-between; gap: 24px; margin-top: 20px; padding: 20px 28px; border: 1px solid #ebeef5; border-radius: 4px; background: #fff; font-size: 14px; }
.pending-summary, .service-summary { display: flex; align-items: center; gap: 14px; }
.pending-summary strong { color: #303133; }
.pending-summary i { color: #c0c4cc; font-style: normal; }
.service-summary span { display: inline-flex; align-items: center; gap: 7px; }
.service-summary span > i { width: 8px; height: 8px; border-radius: 50%; background: #c0c4cc; }
.service-summary span > i.ready { background: #28b487; }
.service-summary span > i.warning { background: #e6a23c; }
.service-summary span > i.error { background: #f56c6c; }

@media (max-width: 900px) {
  .metric-strip { grid-template-columns: 1fr 1fr; }
  .metric-cell:nth-child(2) { border-right: 0; }
  .metric-cell:nth-child(-n+2) { border-bottom: 1px solid #ebeef5; }
  .overview-sheet { grid-template-columns: minmax(0, 1fr); }
  .overview-sheet > section + section { border-top: 1px solid #ebeef5; border-left: 0; }
  .overview-footer { align-items: flex-start; flex-direction: column; }
}

@media (max-width: 560px) {
  .metric-strip { grid-template-columns: minmax(0, 1fr); }
  .metric-cell { padding: 18px 20px; border-right: 0; border-bottom: 1px solid #ebeef5; }
  .metric-cell:last-child { border-bottom: 0; }
  .overview-sheet > section { padding: 20px 16px; }
  .overview-footer { padding: 18px 16px; }
  .pending-summary, .service-summary { align-items: flex-start; flex-wrap: wrap; }
}
</style>
