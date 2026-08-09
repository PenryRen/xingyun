<template>
  <div v-if="points.length" class="trend-chart" role="img" aria-label="最近考试得分趋势图">
    <svg viewBox="0 0 640 230" preserveAspectRatio="none">
      <g v-for="tick in ticks" :key="tick.value">
        <line x1="52" x2="620" :y1="tick.y" :y2="tick.y" class="grid-line"/>
        <text x="38" :y="tick.y + 4" text-anchor="end" class="axis-text">{{ tick.value }}</text>
      </g>
      <polyline :points="linePoints" class="trend-line"/>
      <g v-for="point in points" :key="point.id">
        <circle :cx="point.x" :cy="point.y" r="4" class="trend-point"/>
        <circle v-if="point.isLatest" :cx="point.x" :cy="point.y" r="8" class="latest-ring"/>
        <text :x="point.x" :y="Math.max(15, point.y - 11)" text-anchor="middle" class="score-text">{{ point.score }}</text>
        <text :x="point.x" y="218" text-anchor="middle" class="axis-text">{{ point.label }}</text>
      </g>
    </svg>
  </div>
  <div v-else class="chart-empty">完成正式考试并等待成绩定稿后，这里会生成趋势。</div>
</template>

<script setup lang="ts">
import {computed} from 'vue';
import type {ExamRecord} from '../types';

const props = defineProps<{ records: ExamRecord[] }>();

const scoreOf = (record: ExamRecord) => {
  const total = Number(record.paperScore);
  const score = Number(record.userScore);
  if (!Number.isFinite(total) || !Number.isFinite(score) || total <= 0 || score < 0 || score > total) return 0;
  return Math.round(score / total * 100);
};

const shortDate = (value?: string) => {
  if (!value) return '--';
  const date = new Date(value.replace(' ', 'T'));
  return Number.isNaN(date.getTime())
    ? value.slice(5, 10)
    : `${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')}`;
};

const ticks = [0, 25, 50, 75, 100].map(value => ({value, y: 190 - value * 1.55}));
const points = computed(() => props.records.map((record, index, list) => {
  const score = scoreOf(record);
  return {
    id: record.id,
    score,
    x: list.length === 1 ? 336 : 66 + index * (530 / (list.length - 1)),
    y: 190 - score * 1.55,
    label: shortDate(record.createTime),
    isLatest: index === list.length - 1
  };
}));
const linePoints = computed(() => points.value.map(point => `${point.x},${point.y}`).join(' '));
</script>

<style scoped>
.trend-chart { width: 100%; height: 230px; }
.trend-chart svg { display: block; width: 100%; height: 100%; overflow: visible; }
.grid-line { stroke: #ebeef5; stroke-width: 1; stroke-dasharray: 4 4; }
.axis-text { fill: #909399; font-size: 11px; }
.score-text { fill: #4c596b; font-size: 12px; font-weight: 600; }
.trend-line { fill: none; stroke: #45aec8; stroke-width: 2.5; stroke-linecap: round; stroke-linejoin: round; }
.trend-point { fill: #45aec8; stroke: #fff; stroke-width: 2; }
.latest-ring { fill: none; stroke: rgba(69, 174, 200, .3); stroke-width: 4; }
.chart-empty { display: flex; align-items: center; justify-content: center; min-height: 230px; color: #909399; font-size: 14px; }
</style>
