<template>
  <section class="profile-tab" aria-label="学情档案">
    <div class="identity-strip">
      <div class="identity-name"><strong>{{ studentName }}</strong></div>
      <div v-for="field in archiveFields" :key="field.label" class="identity-field">
        <span>{{ field.label }}</span><strong :title="field.value">{{ field.value }}</strong>
      </div>
    </div>

    <div class="metric-strip">
      <div v-for="item in metrics" :key="item.label" class="metric-cell">
        <span>{{ item.label }}</span><strong>{{ item.value }}</strong><small>{{ item.note }}</small>
      </div>
    </div>

    <div class="profile-sheet">
      <section class="evidence-panel">
        <h2>学习证据时间线</h2>
        <el-table :data="trendRecords" border empty-text="暂无可写入档案的已定稿成绩" class="evidence-table">
          <el-table-column label="得分" width="90">
            <template #default="scope"><strong>{{ scoreOf(scope.row) }}</strong></template>
          </el-table-column>
          <el-table-column label="日期" width="165">
            <template #default="scope">{{ formatDateTime(scope.row.createTime) }}</template>
          </el-table-column>
          <el-table-column prop="paperName" label="考试名称" min-width="220" show-overflow-tooltip/>
          <el-table-column label="状态" width="100"><template #default><el-tag type="success" effect="light">已定稿</el-tag></template></el-table-column>
        </el-table>
      </section>

      <section class="knowledge-panel">
        <h2>知识点档案</h2>
        <div class="knowledge-empty">
          <el-icon><Document/></el-icon>
          <strong>暂无知识点级数据</strong>
          <p>完成题目标签接入后，可生成掌握点与薄弱点。</p>
          <button type="button" @click="$emit('ask-profile')">让 AI 结合档案给建议</button>
        </div>
      </section>
    </div>

    <footer class="profile-footer">
      <div><span>档案仅来自正式考试记录</span><i></i><span>真实 0 分保留，缺失分数不进入趋势</span></div>
      <el-button @click="$emit('ask-profile')">让 AI 结合档案制定计划</el-button>
    </footer>
  </section>
</template>

<script setup lang="ts">
import {Document} from '@element-plus/icons-vue';
import type {ExamRecord, MetricItem} from '../types';

defineProps<{
  studentName: string;
  archiveFields: Array<{label: string; value: string}>;
  metrics: MetricItem[];
  trendRecords: ExamRecord[];
}>();

defineEmits<{(event: 'ask-profile'): void}>();

const scoreOf = (record: ExamRecord) => {
  if (record.scoreRatePercent === null || record.scoreRatePercent === undefined) return '--';
  const score = Number(record.scoreRatePercent);
  return Number.isFinite(score) ? Math.round(score) : '--';
};
const formatDateTime = (value?: string) => value ? value.slice(0, 16) : '--';
</script>

<style scoped>
.profile-tab { color: #4c596b; }
.identity-strip { display: grid; grid-template-columns: 1.1fr repeat(4, minmax(0, 1fr)); border: 1px solid #ebeef5; border-radius: 4px; background: #fff; }
.identity-strip > div { min-width: 0; padding: 20px 26px; border-right: 1px solid #ebeef5; }
.identity-strip > div:last-child { border-right: 0; }
.identity-name { display: flex; align-items: center; }
.identity-name strong { color: #303133; font-size: 20px; font-weight: 500; }
.identity-field span { display: block; color: #909399; font-size: 12px; }
.identity-field strong { display: block; overflow: hidden; margin-top: 7px; color: #303133; font-size: 14px; font-weight: 500; text-overflow: ellipsis; white-space: nowrap; }
.metric-strip { display: grid; grid-template-columns: repeat(4, minmax(0, 1fr)); margin-top: 20px; border: 1px solid #ebeef5; border-radius: 4px; background: #fff; }
.metric-cell { min-width: 0; padding: 20px 28px; border-right: 1px solid #ebeef5; }
.metric-cell:last-child { border-right: 0; }
.metric-cell span { display: block; color: #606266; font-size: 14px; }
.metric-cell strong { display: block; margin-top: 7px; color: #303133; font-size: 25px; font-weight: 500; }
.metric-cell small { display: block; margin-top: 5px; color: #909399; font-size: 12px; }
.profile-sheet { display: grid; grid-template-columns: minmax(0, 1.35fr) minmax(320px, .9fr); gap: 20px; margin-top: 20px; }
.profile-sheet > section { min-width: 0; padding: 22px; border: 1px solid #ebeef5; border-radius: 4px; background: #fff; }
.profile-sheet h2 { margin: 0 0 18px; color: #303133; font-size: 18px; font-weight: 500; }
.evidence-table :deep(.el-table__cell) { font-size: 14px; }
.evidence-table :deep(.el-table__header th) { background: #fafafa; color: #606266; font-weight: 500; }
.evidence-table :deep(.el-tag) { border-radius: 4px; }
.evidence-table strong { color: #303133; font-weight: 500; }
.knowledge-empty { display: flex; align-items: center; justify-content: center; flex-direction: column; min-height: 310px; padding: 20px; text-align: center; }
.knowledge-empty .el-icon { color: #a8b3bd; font-size: 66px; }
.knowledge-empty strong { margin-top: 18px; color: #303133; font-size: 16px; font-weight: 500; }
.knowledge-empty p { max-width: 320px; margin: 10px 0 0; color: #909399; font-size: 14px; line-height: 1.7; }
.knowledge-empty button { margin-top: 20px; padding: 0; border: 0; background: transparent; color: #409eff; font-size: 14px; cursor: pointer; }
.profile-footer { display: flex; align-items: center; justify-content: space-between; gap: 24px; margin-top: 20px; padding: 17px 22px; border: 1px solid #ebeef5; border-radius: 4px; background: #fff; }
.profile-footer > div { display: flex; align-items: center; gap: 18px; color: #606266; font-size: 13px; }
.profile-footer i { width: 1px; height: 18px; background: #dcdfe6; }

@media (max-width: 980px) {
  .identity-strip { grid-template-columns: 1fr 1fr; }
  .identity-strip > div { border-bottom: 1px solid #ebeef5; }
  .identity-strip > div:nth-child(even) { border-right: 0; }
  .identity-strip > div:last-child { border-bottom: 0; }
  .profile-sheet { grid-template-columns: minmax(0, 1fr); }
}

@media (max-width: 760px) {
  .metric-strip { grid-template-columns: 1fr 1fr; }
  .metric-cell:nth-child(2) { border-right: 0; }
  .metric-cell:nth-child(-n+2) { border-bottom: 1px solid #ebeef5; }
  .profile-footer { align-items: flex-start; flex-direction: column; }
}

@media (max-width: 560px) {
  .identity-strip, .metric-strip { grid-template-columns: minmax(0, 1fr); }
  .identity-strip > div, .metric-cell { padding: 16px 18px; border-right: 0; border-bottom: 1px solid #ebeef5; }
  .profile-sheet > section { padding: 18px 14px; }
  .profile-footer { padding: 16px; }
  .profile-footer > div { align-items: flex-start; flex-direction: column; gap: 8px; }
  .profile-footer i { display: none; }
}
</style>
