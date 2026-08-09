<template>
  <section class="exam-tab" aria-label="考情分析">
    <div class="metric-strip">
      <div v-for="item in metrics" :key="item.label" class="metric-cell">
        <span>{{ item.label }}</span>
        <strong>{{ item.value }}</strong>
        <small v-if="item.note">{{ item.note }}</small>
      </div>
    </div>

    <div class="analysis-sheet">
      <section class="chart-panel">
        <h2>近 {{ trendRecords.length }} 场考试得分趋势</h2>
        <ExamTrendChart :records="trendRecords"/>
      </section>
      <section class="status-panel">
        <h2>{{ totalRecords }} 场考试状态</h2>
        <div class="status-body">
          <div class="status-donut" :style="statusRingStyle">
            <div><strong>{{ totalRecords }}</strong><span>总场次</span></div>
          </div>
          <ul>
            <li v-for="item in statusItems" :key="item.label">
              <i :style="{backgroundColor: item.color}"></i>
              <span>{{ item.label }}</span>
              <strong>{{ item.count }}</strong>
              <small>{{ item.note }}</small>
            </li>
          </ul>
        </div>
      </section>
    </div>

    <section class="records-sheet">
      <header class="section-head">
        <div>
          <h2>最近正式考试</h2>
          <span>{{ dataSourceLabel }}</span>
        </div>
        <button type="button" @click="$emit('open-report')">查看完整报告</button>
      </header>
      <el-table :data="recentRecords" border empty-text="暂无正式考试记录" class="exam-table">
        <el-table-column prop="paperName" label="考试名称" min-width="220" show-overflow-tooltip/>
        <el-table-column label="考试时间" min-width="170">
          <template #default="scope">{{ formatDateTime(scope.row.createTime) }}</template>
        </el-table-column>
        <el-table-column label="状态" width="100">
          <template #default="scope">
            <el-tag :type="statusType(scope.row.status)" effect="light">{{ statusLabel(scope.row.status) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="得分率" width="100">
          <template #default="scope"><strong>{{ scoreDisplay(scope.row) }}</strong></template>
        </el-table-column>
        <el-table-column label="完全答对" width="130">
          <template #default="scope">{{ questionSummary(scope.row) }}</template>
        </el-table-column>
        <el-table-column label="操作" width="90" align="center">
          <template #default="scope">
            <el-button link type="primary" @click="$emit('view-record', scope.row)">{{ scope.row.watch ? '查看' : '去记录页' }}</el-button>
          </template>
        </el-table-column>
      </el-table>
    </section>

    <section class="insight-sheet">
      <header class="section-head"><div><h2>复盘结论</h2><span>基于已定稿正式考试的规则计算</span></div></header>
      <div class="insight-list">
        <article v-for="(item, index) in insights" :key="item.title">
          <span>{{ String(index + 1).padStart(2, '0') }}</span>
          <div><strong>{{ item.title }}</strong><p>{{ item.description }}</p></div>
          <em>{{ item.evidence }}</em>
        </article>
      </div>
    </section>
  </section>
</template>

<script setup lang="ts">
import ExamTrendChart from '../components/ExamTrendChart.vue';
import type {CSSProperties} from 'vue';
import type {ExamRecord, InsightItem, MetricItem, StatusItem} from '../types';

defineProps<{
  metrics: MetricItem[];
  trendRecords: ExamRecord[];
  recentRecords: ExamRecord[];
  totalRecords: number;
  statusItems: StatusItem[];
  statusRingStyle: CSSProperties;
  dataSourceLabel: string;
  insights: InsightItem[];
}>();

defineEmits<{
  (event: 'open-report'): void;
  (event: 'view-record', record: ExamRecord): void;
}>();

const finiteNumber = (value: unknown): number | null => {
  if (value === null || value === undefined || value === '') return null;
  const parsed = Number(value);
  return Number.isFinite(parsed) ? parsed : null;
};

const scoreDisplay = (record: ExamRecord) => {
  const total = finiteNumber(record.paperScore);
  const score = finiteNumber(record.userScore);
  if (record.status !== 2 || total === null || score === null || total <= 0 || score < 0 || score > total) return '--';
  return `${Math.round(score / total * 100)}%`;
};

const questionSummary = (record: ExamRecord) => {
  const correct = finiteNumber(record.questionCorrect);
  const count = finiteNumber(record.questionCount);
  return correct !== null && count !== null && Number.isInteger(correct) && Number.isInteger(count) && count > 0 && correct >= 0 && correct <= count
    ? `${correct} / ${count} 题`
    : '--';
};

const statusLabel = (status?: number) => ({1: '待批改', 2: '已定稿', 3: '待核验', 4: '核验失败'}[Number(status)] || '未知状态');
const statusType = (status?: number) => ({1: 'warning', 2: 'success', 3: 'warning', 4: 'danger'}[Number(status)] || 'info') as 'success' | 'warning' | 'danger' | 'info';
const formatDateTime = (value?: string) => value ? value.slice(0, 16) : '--';
</script>

<style scoped>
.exam-tab { color: #4c596b; }
.metric-strip { display: grid; grid-template-columns: repeat(4, minmax(0, 1fr)); border: 1px solid #ebeef5; border-radius: 4px; background: #fff; }
.metric-cell { min-width: 0; padding: 18px 28px; border-right: 1px solid #ebeef5; }
.metric-cell:last-child { border-right: 0; }
.metric-cell span { display: block; color: #606266; font-size: 14px; }
.metric-cell strong { display: block; margin-top: 7px; color: #303133; font-size: 24px; font-weight: 500; }
.metric-cell small { display: block; margin-top: 4px; color: #909399; font-size: 12px; }
.analysis-sheet { display: grid; grid-template-columns: minmax(0, 1.55fr) minmax(380px, 1fr); margin-top: 20px; border: 1px solid #ebeef5; border-radius: 4px; background: #fff; }
.analysis-sheet > section { min-width: 0; padding: 22px 28px; }
.analysis-sheet > section + section { border-left: 1px solid #ebeef5; }
.analysis-sheet h2, .section-head h2 { margin: 0; color: #303133; font-size: 18px; font-weight: 500; }
.status-body { display: grid; grid-template-columns: 130px minmax(0, 1fr); align-items: center; gap: 18px; min-height: 230px; }
.status-donut { position: relative; width: 120px; height: 120px; border-radius: 50%; }
.status-donut::after { position: absolute; inset: 24px; border-radius: 50%; background: #fff; content: ''; }
.status-donut > div { position: absolute; z-index: 1; inset: 0; display: flex; align-items: center; justify-content: center; flex-direction: column; }
.status-donut strong { color: #303133; font-size: 32px; font-weight: 500; }
.status-donut span { margin-top: 3px; color: #909399; font-size: 12px; }
.status-panel ul { margin: 0; padding: 0; list-style: none; }
.status-panel li { display: grid; grid-template-columns: 9px minmax(54px, 1fr) 26px 62px; align-items: center; gap: 6px; min-height: 36px; font-size: 13px; }
.status-panel li i { width: 9px; height: 9px; border-radius: 50%; }
.status-panel li strong { color: #303133; font-size: 15px; }
.status-panel li small { color: #909399; }
.records-sheet, .insight-sheet { margin-top: 20px; padding: 20px; border: 1px solid #ebeef5; border-radius: 4px; background: #fff; }
.section-head { display: flex; align-items: center; justify-content: space-between; gap: 20px; margin-bottom: 16px; }
.section-head > div { display: flex; align-items: baseline; gap: 12px; }
.section-head span { color: #909399; font-size: 12px; }
.section-head button { padding: 0; border: 0; background: transparent; color: #409eff; font-size: 14px; cursor: pointer; }
.exam-table :deep(.el-table__cell) { font-size: 14px; }
.exam-table :deep(.el-table__header th) { background: #fafafa; color: #606266; font-weight: 500; }
.exam-table :deep(.el-tag) { border-radius: 4px; }
.insight-list { display: grid; grid-template-columns: repeat(2, minmax(0, 1fr)); border-top: 1px solid #ebeef5; }
.insight-list article { display: grid; grid-template-columns: 34px minmax(0, 1fr) auto; gap: 12px; padding: 18px 20px; border-right: 1px solid #ebeef5; border-bottom: 1px solid #ebeef5; }
.insight-list article:nth-child(even) { border-right: 0; }
.insight-list article > span { color: #a8b3bd; font-size: 13px; }
.insight-list strong { color: #303133; font-size: 14px; font-weight: 500; }
.insight-list p { margin: 6px 0 0; color: #606266; font-size: 13px; line-height: 1.6; }
.insight-list em { align-self: start; color: #e6a23c; font-size: 12px; font-style: normal; white-space: nowrap; }

@media (max-width: 900px) {
  .metric-strip { grid-template-columns: 1fr 1fr; }
  .metric-cell:nth-child(2) { border-right: 0; }
  .metric-cell:nth-child(-n+2) { border-bottom: 1px solid #ebeef5; }
  .analysis-sheet { grid-template-columns: minmax(0, 1fr); }
  .analysis-sheet > section + section { border-top: 1px solid #ebeef5; border-left: 0; }
  .insight-list { grid-template-columns: minmax(0, 1fr); }
  .insight-list article { border-right: 0; }
}

@media (max-width: 560px) {
  .metric-strip { grid-template-columns: minmax(0, 1fr); }
  .metric-cell { padding: 16px 18px; border-right: 0; border-bottom: 1px solid #ebeef5; }
  .metric-cell:last-child { border-bottom: 0; }
  .analysis-sheet > section, .records-sheet, .insight-sheet { padding: 18px 14px; }
  .status-body { grid-template-columns: minmax(0, 1fr); justify-items: center; }
  .status-panel ul { width: 100%; }
}
</style>
