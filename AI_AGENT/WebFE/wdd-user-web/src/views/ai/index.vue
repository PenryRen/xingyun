<template>
  <main class="ai-learning-page" v-loading="pageLoading">
    <header class="page-intro">
      <div class="intro-copy">
        <h1>AI 学习中心</h1>
        <p>用真实考试数据，看清表现、整理档案、规划下一步。</p>
      </div>
      <div class="page-meta">
        <span>数据读取于：{{ dataUpdatedAt }}</span>
        <button class="agent-state" :class="serviceStatusClass" type="button" @click="refreshAgentHealth">
          <i></i>{{ serviceStatusText }}
          <el-icon :class="{rotating: healthLoading}"><Refresh/></el-icon>
        </button>
      </div>
    </header>

    <nav class="module-nav" aria-label="AI 学习中心模块导航">
      <button :class="{active: activeSection === 'overview'}" type="button" @click="scrollToSection('overview')">总览</button>
      <button :class="{active: activeSection === 'exam-analysis'}" type="button" @click="scrollToSection('exam-analysis')">考情分析</button>
      <button :class="{active: activeSection === 'learning-profile'}" type="button" @click="scrollToSection('learning-profile')">学情档案</button>
      <button :class="{active: activeSection === 'ai-assistant'}" type="button" @click="scrollToSection('ai-assistant')">AI 助教</button>
    </nav>

    <div id="overview" class="primary-grid">
      <section id="exam-analysis" class="module-panel exam-module">
        <header class="module-header">
          <div class="module-heading">
            <span class="heading-icon"><TrendCharts/></span>
            <div>
              <h2>考情分析</h2>
              <p>基于正式考试数据的表现分析与核验状态</p>
            </div>
          </div>
          <button class="module-action" type="button" @click="openAnalysisReport">
            查看完整报告 <el-icon><ArrowRight/></el-icon>
          </button>
        </header>

        <div v-if="trendRecords.length" class="exam-overview-grid">
          <article class="chart-canvas">
            <div class="canvas-title">最近 {{ trendRecords.length }} 场考试得分趋势</div>
            <div class="chart-kpis">
              <div class="average-kpi">
                <span>平均分</span>
                <strong>{{ overallScore }}</strong><small>/100</small>
              </div>
              <div v-if="trendRecords.length > 1" class="window-change" :class="improvement >= 0 ? 'positive' : 'negative'">
                <span>较本窗口首场</span>
                <strong>{{ improvement >= 0 ? '+' : '-' }}{{ Math.abs(improvement).toFixed(1) }}</strong>
                <small>个百分点</small>
              </div>
              <div v-else class="window-change neutral"><span>趋势判断</span><strong>样本不足</strong></div>
            </div>
            <svg class="trend-chart" viewBox="0 0 620 225" role="img" aria-label="最近考试得分趋势图">
              <g class="grid-lines">
                <template v-for="tick in chartTicks" :key="tick.value">
                  <line x1="54" :y1="tick.y" x2="590" :y2="tick.y"/>
                  <text x="43" :y="tick.y + 4" text-anchor="end">{{ tick.value }}</text>
                </template>
              </g>
              <polyline :points="linePoints" fill="none" stroke="#1497a5" stroke-width="3" stroke-linecap="round" stroke-linejoin="round"/>
              <g v-for="point in chartPoints" :key="point.id" class="chart-point">
                <line :x1="point.x" y1="180" :x2="point.x" :y2="point.y" class="point-crosshair"/>
                <circle :cx="point.x" :cy="point.y" r="5"/>
                <circle v-if="point.isLatest" :cx="point.x" :cy="point.y" r="10" class="latest-ring"/>
                <text :x="point.x" :y="Math.max(18, point.y - 13)" text-anchor="middle" class="point-score">{{ point.score }}</text>
                <text :x="point.x" y="210" text-anchor="middle" class="point-label">{{ point.label }}</text>
              </g>
            </svg>
            <div class="chart-footnote">
              <span>样本数 <b>{{ trendRecords.length }}</b></span>
              <span>标准差（近场） <b>{{ scoreStandardDeviation === null ? '--' : `${scoreStandardDeviation.toFixed(1)}pp` }}</b></span>
            </div>
          </article>

          <article class="status-canvas">
            <div class="canvas-title">{{ records.length }} 场正式考试状态</div>
            <div class="status-visual">
              <div class="status-donut" :style="statusRingStyle">
                <div><strong>{{ records.length }}</strong><span>场考试</span></div>
              </div>
              <div class="status-legend">
                <div v-for="status in examStatusSummary" :key="status.label">
                  <i :style="{backgroundColor: status.color}"></i>
                  <span>{{ status.label }}</span>
                  <strong>{{ status.count }}</strong>
                  <small>{{ status.note }}</small>
                </div>
              </div>
            </div>
            <p class="status-explain">只有“已定稿”且分数有效的记录会进入趋势；待批改、待核验与核验失败分别展示。</p>
          </article>
        </div>

        <div v-else class="empty-state">
          <div class="empty-icon"><TrendCharts/></div>
          <div>
            <strong>{{ dataLoadError ? '正式考试数据读取失败' : '还没有可分析的已定稿考试' }}</strong>
            <p>{{ dataLoadError ? '请重试；系统不会把读取失败显示成 0 分。' : '完成正式考试并等待成绩定稿后，这里会生成趋势。' }}</p>
          </div>
          <el-button type="primary" @click="dataLoadError ? loadExamData() : router.push('/paper/index')">{{ dataLoadError ? '重新读取' : '去试卷中心' }}</el-button>
        </div>

        <div class="recent-records">
          <div class="subsection-heading">
            <div><i></i><h3>最近正式考试</h3><span>{{ dataSourceLabel }}</span></div>
            <button type="button" @click="router.push('/record/index')">查看全部考试记录 <el-icon><ArrowRight/></el-icon></button>
          </div>
          <div v-if="recentFormalRecords.length" class="record-list" role="table" aria-label="最近正式考试">
            <div class="record-head" role="row">
              <span>考试名称</span><span>考试时间</span><span>状态</span><span>得分率</span><span>完全答对</span><span>操作</span>
            </div>
            <div v-for="record in recentFormalRecords" :key="record.id" class="record-row" role="row">
              <strong :title="record.paperName || '未命名试卷'">{{ record.paperName || '未命名试卷' }}</strong>
              <span class="record-date">{{ formatDateTime(record.createTime) }}</span>
              <span class="record-status" :class="statusClassOf(record.status)">{{ statusLabelOf(record.status) }}</span>
              <span class="record-score">{{ scoreDisplayOf(record) }}</span>
              <span class="record-questions">{{ questionSummaryOf(record) }}</span>
              <button type="button" @click="router.push('/record/index')">查看</button>
            </div>
          </div>
          <p v-else class="record-empty">暂无正式考试记录</p>
        </div>
      </section>

      <section id="learning-profile" class="module-panel profile-module">
        <header class="module-header">
          <div class="module-heading">
            <span class="heading-icon"><User/></span>
            <div>
              <h2>学情档案</h2>
              <p>基于正式考试数据形成的个人学习记录</p>
            </div>
          </div>
          <span class="source-text">数据来源：正式考试数据</span>
        </header>

        <div class="identity-strip">
          <div class="student-identity">
            <span>{{ studentInitial }}</span>
            <div><strong>{{ studentDisplayName }}</strong><small>{{ studentProfile.userName || user.userName || '--' }}</small></div>
          </div>
          <div v-for="field in studentArchiveFields" :key="field.label" class="identity-field">
            <span>{{ field.label }}</span><strong>{{ field.value || '--' }}</strong>
          </div>
        </div>
        <p class="archive-time">档案更新于：{{ dataUpdatedAt }}</p>

        <div class="profile-metrics">
          <div v-for="metric in profileMetricCards" :key="metric.label">
            <span>{{ metric.label }}</span>
            <strong>{{ metric.display }}</strong>
            <small>{{ metric.note }}</small>
          </div>
        </div>

        <div class="profile-scales">
          <article v-for="metric in profileScales" :key="metric.label" :style="{'--marker': `${metric.position}%`, '--metric-color': metric.color}">
            <div class="scale-heading"><span>{{ metric.label }}</span><strong>{{ metric.display }}</strong><em :class="metric.tone">{{ metric.level }}</em></div>
            <div class="scale-track"><i></i></div>
            <div class="scale-labels"><span>{{ metric.minLabel }}</span><span>{{ metric.maxLabel }}</span></div>
          </article>
        </div>

        <div class="profile-evidence-grid">
          <article class="evidence-timeline">
            <div class="canvas-title">学习证据时间线 <span>最近 {{ trendRecords.length }} 场</span></div>
            <div v-if="trendRecords.length" class="timeline-list">
              <div v-for="record in trendRecords" :key="record.id" class="timeline-row">
                <i></i>
                <strong>{{ scoreOf(record) }}</strong>
                <div><span>{{ record.paperName || '未命名试卷' }}</span><small>{{ formatDateTime(record.createTime) }}</small></div>
                <em :class="statusClassOf(record.status)">{{ statusLabelOf(record.status) }}</em>
              </div>
            </div>
            <p v-else class="timeline-empty">暂无可写入档案的已定稿成绩</p>
          </article>

          <article class="knowledge-empty">
            <div class="knowledge-icon"><Document/></div>
            <strong>暂无知识点级数据</strong>
            <p>当前只能定位到考试与题目汇总；完成题目标签接入后，可生成掌握点与薄弱点。</p>
            <button type="button" @click="router.push('/record/index')">了解当前考试证据 <el-icon><ArrowRight/></el-icon></button>
          </article>
        </div>

        <div class="profile-notes">
          <div><span><DataAnalysis/></span><p>档案由已加载的正式考试记录自动计算，不包含作业或课堂数据。</p></div>
          <div><span><Document/></span><p>真实 0 分会保留；缺失或非法分数不会伪装成 0 分进入趋势。</p></div>
          <button type="button" @click="askAboutProfile"><ChatDotRound/> 向 AI 询问这份档案 <el-icon><ArrowRight/></el-icon></button>
        </div>
      </section>
    </div>

    <section class="review-rail">
      <header>
        <div><span class="heading-icon"><Aim/></span><div><h2>规则计算的复盘重点</h2><p>只展示当前数据能够验证的低分场次、作答汇总与异常状态。</p></div></div>
        <span>本地数据规则</span>
      </header>
      <div class="review-grid">
        <article v-for="(insight, index) in insights" :key="insight.title">
          <span>{{ String(index + 1).padStart(2, '0') }}</span>
          <div><strong>{{ insight.title }}</strong><p>{{ insight.description }}</p></div>
          <em>{{ insight.evidence }}</em>
        </article>
      </div>
    </section>

    <section id="ai-assistant" class="assistant-panel">
      <header class="module-header">
        <div class="module-heading">
          <span class="heading-icon"><ChatDotRound/></span>
          <div><h2>AI 助教</h2><p>把真实考试摘要带入对话；数据不足时要求模型明确说明。</p></div>
        </div>
        <span class="assistant-state"><i :class="modelReady ? 'ready' : 'waiting'"></i>{{ modelReady ? '模型已就绪' : '等待模型连接' }}</span>
      </header>
      <div class="assistant-layout">
        <div class="assistant-composer">
          <div class="prompt-row">
            <button v-for="prompt in suggestionPrompts" :key="prompt" type="button" :disabled="askLoading" @click="askSuggested(prompt)">{{ prompt }}</button>
          </div>
          <div v-if="assistantReply" class="assistant-reply">
            <span>AI</span><p>{{ assistantReply }}</p>
          </div>
          <div class="ask-box">
            <el-input v-model="question" :disabled="askLoading" maxlength="1000" placeholder="输入你的问题，或从上方选择问题后提问……" @keyup.enter="sendQuestion"/>
            <el-button type="primary" :loading="askLoading" :disabled="askLoading || !question.trim()" aria-label="发送问题" @click="sendQuestion">
              <el-icon v-if="!askLoading"><Promotion/></el-icon>
            </el-button>
          </div>
          <small>回答由 AI 生成，请结合考试记录与课程资料核验。</small>
        </div>
        <aside class="capability-rail">
          <div class="capability-title">能力状态</div>
          <div v-for="capability in capabilities" :key="capability.name" class="capability-row">
            <component :is="capability.icon"/>
            <span>{{ capability.name }}</span>
            <i :class="capability.ready ? 'ready' : 'waiting'"></i>
            <small>{{ capability.ready ? capability.readyText : capability.blockedText }}</small>
          </div>
          <div class="assistant-actions">
            <button type="button" @click="openAnalysisReport"><TrendCharts/> 分析最近考试</button>
            <button type="button" :disabled="askLoading || !practiceReady || !completedRecords.length" @click="requestPractice"><EditPen/> {{ practiceReady ? '生成考试主题练习' : '题库尚未就绪' }}</button>
          </div>
        </aside>
      </div>
    </section>

    <el-dialog v-model="reportVisible" title="考试分析报告" width="820px" class="analysis-dialog">
      <div class="report-meta">
        <el-tag type="success">基于真实考试记录</el-tag>
        <span>趋势使用近 {{ trendRecords.length }} 场；复盘线索覆盖全部 {{ completedRecords.length }} 场有效已定稿记录</span>
      </div>
      <div class="report-score">
        <strong>{{ trendRecords.length ? overallScore : '--' }}</strong>
        <span>近场平均分</span>
        <p>{{ reportSummary }}</p>
      </div>
      <div class="report-sections">
        <section>
          <h3>数据结论</h3>
          <ul><li v-for="insight in insights" :key="insight.title"><b>{{ insight.title }}：</b>{{ insight.description }}</li></ul>
        </section>
        <section>
          <h3>下一步行动</h3>
          <ol><li v-for="item in recommendations" :key="item.title"><b>{{ item.title }}</b><span>{{ item.description }}</span></li></ol>
        </section>
      </div>
      <template #footer>
        <el-button @click="reportVisible = false">关闭</el-button>
        <el-button type="primary" :disabled="askLoading || !practiceReady || !completedRecords.length" @click="requestPractice">{{ practiceReady ? '生成考试主题练习' : '题库尚未就绪' }}</el-button>
      </template>
    </el-dialog>
  </main>
</template>

<script setup lang="ts">
import {computed, markRaw, onBeforeUnmount, onMounted, ref} from 'vue';
import {useRouter} from 'vue-router';
import {ElMessage} from 'element-plus';
import {
  Aim,
  ArrowRight,
  ChatDotRound,
  DataAnalysis,
  Document,
  EditPen,
  Promotion,
  Reading,
  Refresh,
  TrendCharts,
  User
} from '@element-plus/icons-vue';
import {page as paperPage} from '@/api/examPaperAnswer';
import {askAgent, getAgentHealth} from '@/api/aiAgent';
import {getCurrentUser} from '@/api/user';
import useStore from '@/store';

interface ExamRecord {
  id: number;
  paperName?: string;
  paperScore?: number | string | null;
  userScore?: number | string | null;
  questionCount?: number | string | null;
  questionCorrect?: number | string | null;
  status?: number;
  statusStr?: string;
  passed?: boolean | null;
  createTime?: string;
}

interface StudentProfile {
  userName?: string;
  realName?: string;
  workNo?: string;
  departmentStr?: string;
  jobTitle?: string;
}

const router = useRouter();
const {user} = useStore();
const pageLoading = ref(false);
const healthLoading = ref(false);
const healthChecked = ref(false);
const serviceOnline = ref(false);
const modelReady = ref(false);
const agentDataReady = ref(false);
const records = ref<ExamRecord[]>([]);
const studentProfile = ref<StudentProfile>({});
const allFormalRecordsLoaded = ref(false);
const dataLoadError = ref(false);
const dataUpdatedAt = ref('--');
const reportVisible = ref(false);
const question = ref('');
const askLoading = ref(false);
const assistantReply = ref('');
const clamp = (value: number) => Math.max(0, Math.min(100, Math.round(Number.isFinite(value) ? value : 0)));
const finiteNumber = (value: unknown): number | null => {
  if (value === null || value === undefined || value === '') return null;
  const parsed = Number(value);
  return Number.isFinite(parsed) ? parsed : null;
};
const rawScoreOf = (record: ExamRecord): number | null => {
  const paperScore = finiteNumber(record.paperScore);
  const userScore = finiteNumber(record.userScore);
  if (paperScore === null || userScore === null || paperScore <= 0 || userScore < 0 || userScore > paperScore) return null;
  return (userScore / paperScore) * 100;
};
const scoreOf = (record: ExamRecord) => Math.round(rawScoreOf(record) || 0);
const timeOf = (record: ExamRecord) => {
  if (!record.createTime) return Number(record.id || 0);
  const parsed = new Date(record.createTime.replace(' ', 'T')).getTime();
  return Number.isFinite(parsed) ? parsed : Number(record.id || 0);
};

const statusLabels: Record<number, string> = {1: '待批改', 2: '已定稿', 3: '待核验', 4: '核验失败'};
const statusClasses: Record<number, string> = {1: 'review', 2: 'finalized', 3: 'verification', 4: 'failed'};
const statusLabelOf = (status?: number) => statusLabels[Number(status)] || '未知状态';
const statusClassOf = (status?: number) => statusClasses[Number(status)] || 'unknown';
const scoreDisplayOf = (record: ExamRecord) => record.status === 2 && rawScoreOf(record) !== null ? `${scoreOf(record)}%` : '--';
const questionSummaryOf = (record: ExamRecord) => {
  const correct = finiteNumber(record.questionCorrect);
  const count = finiteNumber(record.questionCount);
  return correct !== null && count !== null && Number.isInteger(correct) && Number.isInteger(count) && count > 0 && correct >= 0 && correct <= count
    ? `${correct} / ${count} 题`
    : '--';
};
const formatDateTime = (value?: string) => {
  if (!value) return '--';
  const date = new Date(value.replace(' ', 'T'));
  if (Number.isNaN(date.getTime())) return value;
  const pad = (part: number) => String(part).padStart(2, '0');
  return `${date.getFullYear()}-${pad(date.getMonth() + 1)}-${pad(date.getDate())} ${pad(date.getHours())}:${pad(date.getMinutes())}`;
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
const accuracyRate = computed(() => accuracySummary.value.count
  ? clamp((accuracySummary.value.correct / accuracySummary.value.count) * 100)
  : 0);
const accuracySampleCount = computed(() => accuracySummary.value.samples);

const completionRate = computed(() => records.value.length
  ? clamp((records.value.filter(item => item.status === 2).length / records.value.length) * 100)
  : 0);

const scoreStandardDeviation = computed<number | null>(() => {
  if (normalizedScores.value.length < 3) return null;
  const mean = normalizedScores.value.reduce((sum, value) => sum + value, 0) / normalizedScores.value.length;
  const variance = normalizedScores.value.reduce((sum, value) => sum + Math.pow(value - mean, 2), 0) / normalizedScores.value.length;
  return Math.sqrt(variance);
});

const profileMetricCards = computed(() => [
  {label: '近期平均分', display: trendRecords.value.length ? `${overallScore.value}/100` : '--', note: `最近 ${trendRecords.value.length} 场已定稿考试`},
  {label: '题目正确率', display: accuracySampleCount.value ? `${accuracyRate.value}%` : '--', note: `${accuracySampleCount.value} 场含题目汇总`},
  {label: '成绩已定稿率', display: records.value.length ? `${completionRate.value}%` : '--', note: `${records.value.filter(item => item.status === 2).length}/${records.value.length} 条记录`},
  {label: '近场波动（标准差）', display: scoreStandardDeviation.value === null ? '--' : `${scoreStandardDeviation.value.toFixed(1)}pp`, note: scoreStandardDeviation.value === null ? '至少需要 3 场考试' : `最近 ${trendRecords.value.length} 场`}
]);

const profileScales = computed(() => {
  const volatility = scoreStandardDeviation.value;
  return [
    {label: '得分表现', display: trendRecords.value.length ? `${overallScore.value}/100` : '--', position: trendRecords.value.length ? overallScore.value : 0, level: trendRecords.value.length ? (overallScore.value >= 80 ? '较好' : overallScore.value >= 60 ? '中等' : '偏低') : '样本不足', tone: overallScore.value >= 60 ? 'positive' : 'warning', color: '#1497a5', minLabel: '0 偏低', maxLabel: '100 优秀'},
    {label: '题目正确率', display: accuracySampleCount.value ? `${accuracyRate.value}%` : '--', position: accuracySampleCount.value ? accuracyRate.value : 0, level: accuracySampleCount.value ? (accuracyRate.value >= 80 ? '较好' : accuracyRate.value >= 60 ? '中等' : '偏低') : '样本不足', tone: accuracyRate.value >= 60 ? 'positive' : 'warning', color: '#1497a5', minLabel: '0% 偏低', maxLabel: '100% 优秀'},
    {label: '成绩波动（标准差）', display: volatility === null ? '--' : `${volatility.toFixed(1)}pp`, position: volatility === null ? 0 : Math.min(100, volatility / 60 * 100), level: volatility === null ? '样本不足' : volatility <= 10 ? '稳定' : volatility <= 25 ? '有波动' : '波动较大', tone: volatility !== null && volatility <= 15 ? 'positive' : 'warning', color: '#ed8b2d', minLabel: '0pp 稳定', maxLabel: '60pp 波动大'}
  ];
});

const studentDisplayName = computed(() => studentProfile.value.realName || user.realName || studentProfile.value.userName || user.userName || '学生');
const studentInitial = computed(() => studentDisplayName.value.trim().slice(0, 1).toUpperCase() || 'S');
const studentArchiveFields = computed(() => [
  {label: '用户名', value: studentProfile.value.userName || user.userName || '--'},
  {label: '工号', value: studentProfile.value.workNo || '--'},
  {label: '班级', value: studentProfile.value.departmentStr || '--'},
  {label: '身份', value: studentProfile.value.jobTitle || '学生'}
]);

const formatShortDate = (value?: string) => {
  if (!value) return '--';
  const date = new Date(value.replace(' ', 'T'));
  if (Number.isNaN(date.getTime())) return value.slice(5, 10);
  return `${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')}`;
};

const chartTicks = [0, 25, 50, 75, 100].map(value => ({value, y: 180 - value * 1.3}));
const chartPoints = computed(() => trendRecords.value.map((record, index, list) => ({
  id: record.id,
  score: scoreOf(record),
  x: list.length === 1 ? 322 : 64 + index * (516 / (list.length - 1)),
  y: 180 - scoreOf(record) * 1.3,
  label: formatShortDate(record.createTime),
  isLatest: index === list.length - 1
})));
const linePoints = computed(() => chartPoints.value.map(point => `${point.x},${point.y}`).join(' '));
const areaPoints = computed(() => chartPoints.value.length
  ? `${chartPoints.value[0].x},180 ${linePoints.value} ${chartPoints.value[chartPoints.value.length - 1].x},180`
  : '');

const lowestRecord = computed(() => completedRecords.value.length
  ? completedRecords.value.reduce((lowest, current) => scoreOf(current) < scoreOf(lowest) ? current : lowest)
  : null);
const latestRecord = computed(() => completedRecords.value[completedRecords.value.length - 1] || null);
const waitingReviewCount = computed(() => records.value.filter(item => item.status === 1).length);
const waitingVerificationCount = computed(() => records.value.filter(item => item.status === 3).length);
const verificationFailedCount = computed(() => records.value.filter(item => item.status === 4).length);
const finalizedCount = computed(() => records.value.filter(item => item.status === 2).length);
const unknownStatusCount = computed(() => Math.max(0, records.value.length - finalizedCount.value - waitingReviewCount.value - waitingVerificationCount.value - verificationFailedCount.value));
const recentFormalRecords = computed(() => [...records.value]
  .sort((a, b) => timeOf(b) - timeOf(a) || Number(b.id) - Number(a.id))
  .slice(0, 6));
const examStatusSummary = computed(() => [
  {label: '已定稿', count: finalizedCount.value, note: records.value.length ? `${Math.round(finalizedCount.value / records.value.length * 100)}%` : '--', color: '#13a3a4'},
  {label: '待批改', count: waitingReviewCount.value, note: '不计入趋势', color: '#2f80ed'},
  {label: '待核验', count: waitingVerificationCount.value, note: '成绩未定稿', color: '#f4a63a'},
  {label: '核验失败', count: verificationFailedCount.value, note: '需处理', color: '#ef6a6a'},
  ...(unknownStatusCount.value ? [{label: '其他状态', count: unknownStatusCount.value, note: '待确认', color: '#b9c2ca'}] : [])
]);
const statusRingStyle = computed(() => {
  if (!records.value.length) return {background: '#e7edf1'};
  let cursor = 0;
  const segments = examStatusSummary.value.filter(item => item.count > 0).map(item => {
    const start = cursor;
    cursor += item.count / records.value.length * 100;
    return `${item.color} ${start}% ${cursor}%`;
  });
  return {background: `conic-gradient(${segments.join(', ')})`};
});
const dataSourceLabel = computed(() => allFormalRecordsLoaded.value
  ? `全部正式考试 · ${records.value.length} 条`
  : `已加载正式考试 · ${records.value.length} 条`);

const insights = computed(() => {
  if (!completedRecords.value.length) {
    return [{title: '分析样本不足', description: dataLoadError.value ? '正式考试数据读取失败，请重试。' : '当前没有分数已定稿的正式考试，暂时不能形成复盘结论。', evidence: '0 场'}];
  }
  const latest = latestRecord.value as ExamRecord;
  const latestQuestionCount = finiteNumber(latest.questionCount);
  const latestQuestionCorrect = finiteNumber(latest.questionCorrect);
  const latestNeedsReview = latestQuestionCount !== null && latestQuestionCorrect !== null
    && Number.isInteger(latestQuestionCount) && Number.isInteger(latestQuestionCorrect)
    && latestQuestionCount > 0 && latestQuestionCorrect >= 0 && latestQuestionCorrect <= latestQuestionCount
    ? Math.max(0, latestQuestionCount - latestQuestionCorrect)
    : null;
  const lowest = lowestRecord.value as ExamRecord;
  const items = [
    {
      title: `最低得分出现在《${lowest.paperName || '未命名试卷'}》`,
      description: `该场得分率为 ${scoreOf(lowest)}%，应优先复盘失分题目和作答过程。`,
      evidence: `失分 ${Math.max(0, Number(lowest.paperScore || 0) - Number(lowest.userScore || 0))} 分`
    },
    {
      title: '最近一次正式考试作答线索',
      description: latestNeedsReview === null
        ? `《${latest.paperName || '未命名试卷'}》缺少题目汇总，当前只能按得分复盘。`
        : `最近完成《${latest.paperName || '未命名试卷'}》，完全答对 ${latestQuestionCorrect} / ${latestQuestionCount} 题。`,
      evidence: latestNeedsReview === null ? '无题目汇总' : `${latestNeedsReview} 题需复盘`
    }
  ];
  if (verificationFailedCount.value) {
    items.push({title: '存在核验失败记录', description: '核验失败不是未完成考试，建议先在考试记录中检查并联系管理员处理。', evidence: `${verificationFailedCount.value} 条异常`});
  }
  if (waitingReviewCount.value || waitingVerificationCount.value) {
    items.push({title: '存在成绩尚未定稿的记录', description: `待批改 ${waitingReviewCount.value} 条，待核验 ${waitingVerificationCount.value} 条；定稿前不计入趋势。`, evidence: `${waitingReviewCount.value + waitingVerificationCount.value} 条待处理`});
  }
  if (invalidCompletedCount.value) {
    items.push({title: '存在分数字段异常的已完成记录', description: '这些记录没有被当作 0 分，也不会进入趋势，请检查数据完整性。', evidence: `${invalidCompletedCount.value} 条异常`});
  }
  if (!verificationFailedCount.value && !waitingReviewCount.value && !waitingVerificationCount.value && !invalidCompletedCount.value) {
    items.push({title: '正式考试成绩均已定稿', description: '当前记录完整，可通过新增正式考试扩大趋势样本。', evidence: `${records.value.length} 条记录`});
  }
  return items;
});

const practiceReady = computed(() => serviceOnline.value && modelReady.value && agentDataReady.value);

const recommendations = computed(() => {
  if (!completedRecords.value.length) {
    return [
      {title: '完成首场能力检测', description: '成绩定稿后生成个人趋势与复盘线索。', icon: markRaw(Document)},
      {title: '浏览试卷中心', description: '选择一份与你当前课程相关的试卷。', icon: markRaw(Reading)},
      {title: '检查 Agent 配置', description: modelReady.value ? '本地智能体已就绪，可以开始答疑。' : '配置模型后解锁生成式答疑与组卷。', icon: markRaw(ChatDotRound)}
    ];
  }
  const lowest = lowestRecord.value as ExamRecord;
  const latest = latestRecord.value as ExamRecord;
  const questionCount = finiteNumber(latest.questionCount);
  const questionCorrect = finiteNumber(latest.questionCorrect);
  const needsReview = questionCount !== null && questionCorrect !== null
    && Number.isInteger(questionCount) && Number.isInteger(questionCorrect)
    && questionCount > 0 && questionCorrect >= 0 && questionCorrect <= questionCount
    ? questionCount - questionCorrect
    : null;
  return [
    {title: `优先复盘《${lowest.paperName || '最低分试卷'}》`, description: `当前最低得分率 ${scoreOf(lowest)}%，先定位主要失分原因。`, icon: markRaw(Aim)},
    {title: '整理最近作答', description: needsReview === null ? '当前缺少题目汇总，请从考试记录打开答卷复盘。' : needsReview > 0 ? `最近一次有 ${needsReview} 题未完全答对，建议逐题复盘。` : '最近一次汇总显示全部答对，继续保持。', icon: markRaw(Reading)},
    {title: practiceReady.value ? '生成考试主题练习' : '完成智能组卷配置', description: practiceReady.value ? '当前没有知识点标签，将按最低分考试名称生成候选练习并提示人工核验。' : '模型与题库都就绪后才开放智能组卷。', icon: markRaw(ChatDotRound)}
  ];
});

const capabilities = computed(() => [
  {name: '考情基础分析', icon: markRaw(DataAnalysis), ready: completedRecords.value.length > 0, readyText: '平台数据可用', blockedText: '等待考试数据', color: '#3478f6', background: '#edf4ff'},
  {name: '学情档案', icon: markRaw(User), ready: completedRecords.value.length > 0, readyText: '考试派生档案', blockedText: '等待考试数据', color: '#25a57a', background: '#ebf8f3'},
  {name: '智能组卷', icon: markRaw(EditPen), ready: modelReady.value && agentDataReady.value, readyText: '模型与题库就绪', blockedText: modelReady.value ? '待配置题库' : '待配置模型', color: '#745bdc', background: '#f2efff'},
  {name: 'AI 助教', icon: markRaw(ChatDotRound), ready: modelReady.value, readyText: '模型已就绪', blockedText: '待配置模型', color: '#ed8b2d', background: '#fff4e9'}
]);

const serviceStatusText = computed(() => {
  if (!healthChecked.value) return '正在检查 Agent';
  if (!serviceOnline.value) return 'Agent 未连接';
  return modelReady.value ? 'Agent 与模型已就绪' : 'Agent 在线 · 待配置';
});
const serviceStatusClass = computed(() => !healthChecked.value ? 'checking' : !serviceOnline.value ? 'offline' : modelReady.value ? 'ready' : 'warning');
const reportSummary = computed(() => {
  if (!trendRecords.value.length) return '需要先完成考试，才能形成可验证的学习结论。';
  if (trendRecords.value.length < 2) return `当前只有 1 场已定稿正式考试，得分率为 ${overallScore.value}%，样本不足以判断趋势。`;
  const direction = improvement.value > 0
    ? `末场较本窗口首场高 ${Math.abs(improvement.value).toFixed(1)} 个百分点`
    : improvement.value < 0
      ? `末场较本窗口首场低 ${Math.abs(improvement.value).toFixed(1)} 个百分点`
      : '末场与本窗口首场得分率相同';
  return `最近 ${trendRecords.value.length} 场已定稿正式考试平均得分率为 ${overallScore.value}%，${direction}。中间场次可能波动，建议结合折线逐场复盘。`;
});

const suggestionPrompts = ['我该先复习什么？', '解释最近成绩趋势', '给我一份学习计划'];

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
    const remainingLists = remainingResults.flatMap(result => Array.isArray(result?.response?.list) ? result.response.list : []);
    records.value = [...firstList, ...remainingLists];
    allFormalRecordsLoaded.value = records.value.length >= total;
    dataUpdatedAt.value = new Date().toLocaleString('zh-CN', {hour12: false});
  } catch (error: any) {
    dataLoadError.value = true;
    allFormalRecordsLoaded.value = false;
    ElMessage.error(error?.message || '考试数据加载失败');
  } finally {
    pageLoading.value = false;
  }
};

const refreshAgentHealth = async () => {
  if (healthLoading.value) return;
  healthLoading.value = true;
  try {
    const health = await getAgentHealth();
    serviceOnline.value = health.service_ready !== false;
    modelReady.value = health.model_ready === true;
    agentDataReady.value = health.data_source_ready === true;
  } catch {
    serviceOnline.value = false;
    modelReady.value = false;
    agentDataReady.value = false;
  } finally {
    healthChecked.value = true;
    healthLoading.value = false;
  }
};

const openAnalysisReport = () => {
  reportVisible.value = true;
};

const sendQuestion = async () => {
  const text = question.value.trim();
  if (!text || askLoading.value) return;
  if (!serviceOnline.value || !modelReady.value) {
    assistantReply.value = serviceOnline.value
      ? '本地 Agent 服务已经接通，但还没有配置星火模型。请在项目环境变量中配置 SPARK_API_PASSWORD，随后重启 Agent。'
      : '当前无法连接本地 Agent。请确认一键部署中的 agent 服务已经启动。';
    ElMessage.warning(!serviceOnline.value ? 'AI Agent 未连接' : '大模型尚未配置');
    return;
  }
  askLoading.value = true;
  assistantReply.value = '';
  try {
    const result = await askAgent(text);
    assistantReply.value = result.content;
    question.value = '';
  } catch (error: any) {
    assistantReply.value = 'Agent 调用失败，请检查模型地址、密钥和容器日志。';
    ElMessage.error(error?.response?.data?.error?.message || error?.message || 'AI Agent 调用失败');
  } finally {
    askLoading.value = false;
  }
};

const askSuggested = (prompt: string) => {
  if (askLoading.value) return;
  question.value = prompt;
  sendQuestion();
};

const askAboutProfile = () => {
  if (askLoading.value) return;
  question.value = '请解释我的学情档案，并根据现有考试证据给出下一步建议；没有知识点级数据时请明确说明。';
  document.getElementById('ai-assistant')?.scrollIntoView({behavior: 'smooth', block: 'start'});
};

const activeSection = ref('overview');
const scrollToSection = (sectionId: string) => {
  activeSection.value = sectionId;
  document.getElementById(sectionId)?.scrollIntoView({behavior: 'smooth', block: 'start'});
};

const requestPractice = () => {
  if (askLoading.value) return;
  reportVisible.value = false;
  if (!completedRecords.value.length) {
    ElMessage.warning('还没有可用于生成练习的已定稿考试');
    return;
  }
  if (!practiceReady.value) {
    assistantReply.value = agentDataReady.value
      ? '智能组卷暂不可用，请先确认 Agent 与模型服务在线。'
      : '模型可以答疑，但 Agent 题库数据源尚未配置，因此暂不开放组卷。';
    ElMessage.warning(agentDataReady.value ? '智能组卷服务未就绪' : '智能组卷题库尚未配置');
    scrollToSection('ai-assistant');
    return;
  }
  const target = lowestRecord.value?.paperName || '最近考试中的薄弱内容';
  question.value = `请围绕低分考试“${target}”生成一份候选练习，并说明题量、难度和训练目标。当前没有知识点标签，请明确这是按考试名称推断的主题，并提醒我人工核验。`;
  sendQuestion();
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
.ai-learning-page {
  --navy: #15324a;
  --teal: #249eb8;
  --blue: #3478f6;
  --muted: #70808f;
  width: min(1480px, calc(100% - 56px));
  min-width: 0;
  margin: 0 auto;
  padding: 28px 0 72px;
  color: var(--navy);
}

.ai-hero {
  position: relative;
  min-height: 150px;
  display: flex;
  align-items: center;
  overflow: hidden;
  padding: 26px 32px;
  border: 1px solid #cfe8ef;
  border-radius: 14px;
  background: linear-gradient(112deg, #eef9ff 0%, #f9fdff 55%, #eaf8fd 100%);
  box-shadow: 0 14px 30px rgba(21, 50, 74, 0.06);
}

.hero-orbit {
  position: relative;
  width: 158px;
  height: 104px;
  flex: 0 0 158px;
  display: grid;
  place-items: center;
  margin-right: 32px;
}

.hero-orbit::before,
.hero-orbit::after {
  content: '';
  position: absolute;
  width: 134px;
  height: 52px;
  border: 2px solid rgba(52, 120, 246, 0.32);
  border-radius: 50%;
  transform: rotate(-9deg);
}

.hero-orbit::after { width: 92px; height: 92px; border-color: rgba(36, 158, 184, 0.2); transform: rotate(56deg); }
.hero-cube {
  position: relative;
  z-index: 2;
  width: 70px;
  height: 70px;
  display: grid;
  place-items: center;
  border: 1px solid rgba(255,255,255,.8);
  border-radius: 17px;
  background: linear-gradient(145deg, #70d3df, #3ca9c8 48%, #3478f6);
  box-shadow: 0 16px 26px rgba(52, 120, 246, .24), inset 0 1px rgba(255,255,255,.55);
  color: #fff;
  font-size: 26px;
  font-weight: 750;
  letter-spacing: -1px;
  transform: rotate(-4deg);
}

.orbit-dot { position: absolute; z-index: 3; width: 10px; height: 10px; border-radius: 50%; background: #4f9af8; box-shadow: 0 0 0 5px rgba(79,154,248,.12); }
.dot-one { top: 10px; left: 18px; }
.dot-two { right: 12px; bottom: 12px; width: 7px; height: 7px; background: #45c7a1; }

.hero-copy { position: relative; z-index: 2; }
.hero-eyebrow { margin-bottom: 6px; color: #2e91aa; font-size: 11px; font-weight: 700; letter-spacing: 2.2px; }
.hero-copy h1 { margin: 0; color: #12365d; font-size: 36px; line-height: 1.15; letter-spacing: -1px; }
.hero-copy p { margin: 12px 0 0; color: #49657c; font-size: 16px; }

.hero-status { position: relative; z-index: 2; margin-left: auto; display: flex; flex-direction: column; align-items: flex-end; gap: 12px; color: #657b8e; font-size: 12px; }
.status-pill { display: inline-flex; align-items: center; gap: 8px; min-height: 38px; padding: 0 14px; border: 1px solid rgba(255,255,255,.9); border-radius: 10px; background: rgba(255,255,255,.88); box-shadow: 0 8px 22px rgba(21,50,74,.1); color: #3d566a; cursor: pointer; }
.status-dot { width: 8px; height: 8px; border-radius: 50%; background: #9aa8b4; }
.status-pill.ready .status-dot { background: #20a574; box-shadow: 0 0 0 4px rgba(32,165,116,.12); }
.status-pill.warning .status-dot { background: #e6a23c; box-shadow: 0 0 0 4px rgba(230,162,60,.12); }
.status-pill.offline .status-dot { background: #e25757; box-shadow: 0 0 0 4px rgba(226,87,87,.12); }
.rotating { animation: rotate 1s linear infinite; }
@keyframes rotate { to { transform: rotate(360deg); } }
.hero-lines { position: absolute; right: 0; bottom: 10px; width: 42%; height: 80px; opacity: .34; }
.hero-lines i { position: absolute; inset: 10px -60px auto 0; height: 30px; border-top: 1px solid #62bad0; border-radius: 50%; transform: rotate(-4deg); }
.hero-lines i:nth-child(2) { top: 26px; transform: rotate(2deg); opacity: .7; }
.hero-lines i:nth-child(3) { top: 43px; transform: rotate(-1deg); opacity: .5; }

.dashboard-grid { display: grid; grid-template-columns: minmax(0, 1.65fr) minmax(390px, .95fr); gap: 18px; margin-top: 18px; align-items: start; }
.main-column, .side-column { display: grid; gap: 18px; }
.panel { border: 1px solid #e1e8ee; border-radius: 13px; background: #fff; box-shadow: 0 8px 24px rgba(21,50,74,.045); }
.panel-header { display: flex; justify-content: space-between; align-items: flex-start; padding: 22px 24px 0; }
.panel-header h2 { display: flex; align-items: center; gap: 9px; margin: 0; color: #17364f; font-size: 18px; line-height: 1.2; }
.panel-header h2 .el-icon { color: var(--teal); font-size: 20px; }
.panel-header p { margin: 7px 0 0; color: #8795a1; font-size: 12px; }
.panel-header.compact { padding-top: 19px; }
.source-badge, .engine-label { padding: 6px 9px; border-radius: 7px; background: #edf8fb; color: #278ca4; font-size: 11px; white-space: nowrap; }
.engine-label { background: #f4f7fa; color: #71818f; }

.performance-body { display: grid; grid-template-columns: 190px minmax(0, 1fr); gap: 20px; padding: 22px 24px 18px; }
.score-summary { min-height: 220px; padding: 12px 24px 0 2px; border-right: 1px solid #edf0f3; }
.score-summary > span { display: block; color: #627383; font-size: 13px; }
.score-summary > strong { display: inline-block; margin-top: 8px; color: #12283f; font-size: 60px; line-height: 1; letter-spacing: -3px; }
.score-summary > small { margin-left: 5px; color: #7b8995; font-size: 15px; }
.score-change { margin-top: 28px; }
.score-change span { display: block; margin-bottom: 5px; color: #84929e; font-size: 12px; }
.score-change b { font-size: 22px; }
.score-change.positive b { color: #209b72; }
.score-change.negative b { color: #d65a5a; }
.score-change.neutral b { color: #7a8995; font-size: 16px; }
.score-sample { margin-top: 18px; color: #9aa6af; font-size: 11px; }
.chart-title { margin: 0 0 4px 8px; color: #536a7d; font-size: 12px; }
.trend-chart { display: block; width: 100%; min-height: 220px; overflow: visible; }
.grid-lines line { stroke: #e6edf1; stroke-width: 1; stroke-dasharray: 4 4; }
.grid-lines text { fill: #83929e; font-size: 10px; }
.chart-point > circle:first-child { fill: #249eb8; stroke: #fff; stroke-width: 2; }
.latest-ring { fill: none; stroke: rgba(36,158,184,.28); stroke-width: 3; }
.point-score { fill: #314b61; font-size: 11px; font-weight: 700; }
.point-label { fill: #7d8c98; font-size: 10px; }
.empty-state { min-height: 225px; display: flex; align-items: center; justify-content: center; gap: 18px; padding: 28px; }
.empty-icon { width: 52px; height: 52px; display: grid; place-items: center; border-radius: 14px; background: #edf8fb; color: #249eb8; font-size: 25px; }
.empty-state strong { color: #2c4458; }
.empty-state p { margin: 6px 0 0; color: #8795a1; font-size: 12px; }

.ability-grid { display: grid; grid-template-columns: 1fr 1fr; column-gap: 46px; row-gap: 18px; padding: 20px 24px 23px; }
.ability-label { display: flex; justify-content: space-between; margin-bottom: 8px; color: #41586b; font-size: 13px; }
.ability-label b { color: #2c4458; }
.ability-row small { display: block; margin-top: 7px; color: #98a4ae; font-size: 10px; }

.text-action { display: inline-flex; align-items: center; gap: 3px; border: 0; background: transparent; color: #3478f6; font-size: 12px; cursor: pointer; }
.insight-list { padding: 12px 24px 20px; }
.insight-item { display: grid; grid-template-columns: 36px minmax(0, 1fr) auto; align-items: center; gap: 12px; padding: 15px 0; border-bottom: 1px solid #edf1f4; }
.insight-item:last-child { border-bottom: 0; }
.insight-index { color: #a0adb7; font-size: 11px; font-weight: 700; letter-spacing: 1px; }
.insight-copy strong { display: block; color: #314b60; font-size: 13px; }
.insight-copy p { margin: 5px 0 0; color: #85939f; font-size: 11px; line-height: 1.5; }
.evidence { padding: 5px 8px; border-radius: 6px; background: #fff5ea; color: #d8802c; font-size: 11px; }

.recommendation-list { list-style: none; margin: 0; padding: 15px 18px 18px; }
.recommendation-list li { display: grid; grid-template-columns: 24px 40px minmax(0, 1fr) 18px; align-items: center; gap: 10px; min-height: 70px; margin-bottom: 9px; padding: 10px 12px; border: 1px solid #edf1f4; border-radius: 10px; background: linear-gradient(90deg, #fff, #f8fafc); }
.recommendation-list li:last-child { margin-bottom: 0; }
.priority { width: 21px; height: 25px; display: grid; place-items: center; border-radius: 5px; background: #3478f6; color: #fff; font-size: 12px; }
.priority-2 { background: #2aae82; }
.priority-3 { background: #eea23a; }
.recommendation-icon { width: 38px; height: 38px; display: grid; place-items: center; border-radius: 50%; background: #edf4ff; color: #3478f6; font-size: 20px; }
.recommendation-list strong { display: block; overflow: hidden; color: #2d465a; font-size: 13px; text-overflow: ellipsis; white-space: nowrap; }
.recommendation-list p { margin: 5px 0 0; color: #83919d; font-size: 10px; line-height: 1.35; }
.recommendation-list > li > .el-icon { color: #8b98a3; }

.capability-grid { display: grid; grid-template-columns: repeat(4, 1fr); gap: 8px; padding: 16px 18px 20px; }
.capability-item { min-width: 0; padding: 13px 5px 11px; border: 1px solid #edf1f4; border-radius: 10px; text-align: center; }
.capability-item.disabled { background: #fbfbfc; }
.capability-icon { width: 38px; height: 38px; display: grid; place-items: center; margin: 0 auto 9px; border-radius: 10px; font-size: 20px; }
.capability-item strong { display: block; color: #344c60; font-size: 11px; white-space: nowrap; }
.capability-item > span { display: flex; align-items: center; justify-content: center; gap: 4px; margin-top: 7px; color: #7e8c97; font-size: 9px; white-space: nowrap; }
.capability-item i { width: 6px; height: 6px; border-radius: 50%; }
.capability-item i.ready { background: #24a878; }
.capability-item i.waiting { background: #e4a23d; }

.quick-actions { display: grid; grid-template-columns: 1fr 1fr; gap: 12px; }
.quick-action { min-height: 90px; display: grid; grid-template-columns: 44px minmax(0,1fr) 20px; align-items: center; gap: 10px; padding: 15px; border: 0; border-radius: 12px; color: #fff; text-align: left; cursor: pointer; transition: transform .18s ease, box-shadow .18s ease; }
.quick-action:hover { transform: translateY(-2px); box-shadow: 0 12px 24px rgba(28,102,170,.2); }
.quick-action:disabled { opacity: .65; cursor: wait; transform: none; }
.analysis-action { background: linear-gradient(128deg, #32b6cb, #239cb8); }
.practice-action { background: linear-gradient(128deg, #3478f6, #245fd9); }
.quick-icon { width: 42px; height: 42px; display: grid; place-items: center; border-radius: 10px; background: rgba(255,255,255,.16); font-size: 23px; }
.quick-action strong { display: block; font-size: 13px; }
.quick-action small { display: block; margin-top: 6px; color: rgba(255,255,255,.78); font-size: 9px; }

.ask-panel { padding: 18px; }
.ask-header { display: flex; align-items: center; justify-content: space-between; }
.ask-header h2 { margin: 0; color: #17364f; font-size: 16px; }
.ask-header span { color: #8b99a4; font-size: 10px; }
.mini-status { width: 8px; height: 8px; border-radius: 50%; }
.mini-status.ready { background: #24a878; box-shadow: 0 0 0 4px rgba(36,168,120,.12); }
.mini-status.waiting { background: #e4a23d; box-shadow: 0 0 0 4px rgba(228,162,61,.12); }
.assistant-reply { display: grid; grid-template-columns: 26px minmax(0,1fr); gap: 8px; margin-top: 13px; padding: 10px 11px; border-radius: 9px; background: #f3f8fb; }
.assistant-reply > span { width: 24px; height: 24px; display: grid; place-items: center; border-radius: 7px; background: #249eb8; color: #fff; font-size: 9px; font-weight: 700; }
.assistant-reply p { max-height: 118px; overflow: auto; margin: 2px 0 0; color: #4d6578; font-size: 11px; line-height: 1.6; white-space: pre-wrap; }
.ask-box { display: flex; gap: 7px; margin-top: 13px; }
.ask-box .el-button { width: 42px; padding: 0; background: #249eb8; border-color: #249eb8; }
.suggestion-chips { display: flex; flex-wrap: wrap; gap: 6px; margin-top: 10px; }
.suggestion-chips button { padding: 5px 8px; border: 1px solid #dfe7ed; border-radius: 999px; background: #fff; color: #6a7c8b; font-size: 9px; cursor: pointer; }
.suggestion-chips button:hover { border-color: #59b7cb; color: #278da5; }
.suggestion-chips button:disabled { opacity: .55; cursor: wait; }

.report-meta { display: flex; align-items: center; gap: 10px; color: #81909c; font-size: 12px; }
.report-score { display: grid; grid-template-columns: auto minmax(0,1fr); column-gap: 12px; align-items: end; margin-top: 18px; padding: 18px 20px; border-radius: 12px; background: #eff8fb; }
.report-score strong { grid-row: 1 / span 2; color: #197f98; font-size: 46px; line-height: 1; }
.report-score span { color: #526b7e; font-size: 13px; }
.report-score p { margin: 5px 0 0; color: #6f8291; font-size: 12px; line-height: 1.5; }
.report-sections { display: grid; grid-template-columns: 1fr 1fr; gap: 24px; margin-top: 20px; }
.report-sections h3 { margin: 0 0 10px; color: #29465c; font-size: 14px; }
.report-sections ul, .report-sections ol { margin: 0; padding-left: 20px; color: #657988; font-size: 12px; line-height: 1.65; }
.report-sections li { margin-bottom: 9px; }
.report-sections b { color: #3c576a; }
.report-sections ol span { display: block; color: #8795a0; }

@media (max-width: 1320px) {
  .dashboard-grid { grid-template-columns: minmax(0, 1.5fr) 390px; }
  .performance-body { grid-template-columns: 160px minmax(0,1fr); }
  .capability-grid { grid-template-columns: 1fr 1fr; }
}

@media (max-width: 1100px) {
  .dashboard-grid { grid-template-columns: minmax(0, 1fr); }
  .hero-status { max-width: 210px; }
  .capability-grid { grid-template-columns: repeat(4, 1fr); }
  .performance-body { grid-template-columns: 190px minmax(0, 1fr); }
  .score-change b { font-size: 18px; white-space: nowrap; }
}

/* AI learning center redesign: exam analysis + learning profile */
.ai-learning-page {
  --ink: #102f50;
  --ink-soft: #425d75;
  --muted: #7a8b9b;
  --line: #dfe7ed;
  --line-soft: #edf2f5;
  --teal: #1497a5;
  --teal-soft: #e8f7f7;
  --blue: #2f80ed;
  --amber: #ed8b2d;
  --danger: #e45f5f;
  width: min(1500px, calc(100% - 48px));
  min-width: 0;
  margin: 0 auto;
  padding: 32px 0 76px;
  color: var(--ink);
  font-family: Inter, "PingFang SC", "Microsoft YaHei", sans-serif;
}

.page-intro {
  display: flex;
  align-items: flex-end;
  justify-content: space-between;
  gap: 28px;
  padding: 0 8px 20px;
}
.intro-copy h1 { margin: 0; color: #0d2c50; font-size: 34px; line-height: 1.15; letter-spacing: -.8px; }
.intro-copy p { margin: 10px 0 0; color: #60768a; font-size: 14px; line-height: 1.6; }
.page-meta { display: flex; align-items: center; gap: 18px; color: #718395; font-size: 12px; white-space: nowrap; }
.agent-state { display: inline-flex; align-items: center; gap: 8px; min-height: 34px; padding: 0; border: 0; background: transparent; color: #526a7f; font: inherit; cursor: pointer; }
.agent-state > i, .assistant-state > i, .capability-row > i { width: 7px; height: 7px; border-radius: 50%; background: #9ca9b4; }
.agent-state.ready > i, .assistant-state > i.ready, .capability-row > i.ready { background: #24a878; box-shadow: 0 0 0 4px rgba(36,168,120,.1); }
.agent-state.warning > i, .assistant-state > i.waiting, .capability-row > i.waiting { background: #e7a03b; box-shadow: 0 0 0 4px rgba(231,160,59,.1); }
.agent-state.offline > i { background: var(--danger); box-shadow: 0 0 0 4px rgba(228,95,95,.1); }

.module-nav {
  display: flex;
  align-items: center;
  gap: 34px;
  min-height: 48px;
  margin-bottom: 16px;
  padding: 0 8px;
  border-top: 1px solid var(--line);
  border-bottom: 1px solid var(--line);
  overflow-x: auto;
}
.module-nav button { position: relative; display: inline-flex; align-items: center; min-height: 48px; padding: 0; border: 0; background: transparent; color: #536b81; font: inherit; font-size: 13px; font-weight: 600; white-space: nowrap; cursor: pointer; }
.module-nav button:hover, .module-nav button:focus-visible { color: var(--teal); outline: none; }
.module-nav button.active { color: #0e6572; }
.module-nav button.active::after { content: ''; position: absolute; right: 0; bottom: -1px; left: 0; height: 2px; background: var(--teal); }

.primary-grid { display: grid; grid-template-columns: minmax(0, 1fr) minmax(0, 1fr); gap: 16px; align-items: start; }
.module-panel, .review-rail, .assistant-panel {
  min-width: 0;
  border: 1px solid var(--line);
  border-radius: 14px;
  background: #fff;
  box-shadow: 0 5px 18px rgba(16, 47, 80, .035);
}
.module-panel { padding: 18px; scroll-margin-top: 76px; }
.module-header { display: flex; align-items: flex-start; justify-content: space-between; gap: 20px; }
.module-heading { display: flex; align-items: flex-start; gap: 12px; min-width: 0; }
.heading-icon { width: 28px; height: 28px; display: grid; place-items: center; flex: 0 0 28px; color: #0d7180; font-size: 23px; }
.module-heading h2, .review-rail h2 { margin: 0; color: #102f50; font-size: 20px; line-height: 1.3; letter-spacing: -.25px; }
.module-heading p, .review-rail header p { margin: 5px 0 0; color: #7e8f9e; font-size: 11px; line-height: 1.5; }
.module-action { display: inline-flex; align-items: center; gap: 4px; padding: 4px 0; border: 0; background: transparent; color: #2679df; font-size: 12px; font-weight: 600; cursor: pointer; white-space: nowrap; }
.module-action:hover, .module-action:focus-visible { color: #125bb3; outline: none; }
.source-text { padding-top: 5px; color: #64798d; font-size: 11px; white-space: nowrap; }

.exam-overview-grid { display: grid; grid-template-columns: minmax(0, 1.35fr) minmax(220px, .78fr); gap: 10px; margin-top: 16px; }
.chart-canvas, .status-canvas, .evidence-timeline, .knowledge-empty { min-width: 0; border: 1px solid var(--line-soft); border-radius: 10px; background: #fff; }
.chart-canvas, .status-canvas { padding: 14px; }
.canvas-title { color: #1d3c5c; font-size: 13px; font-weight: 700; }
.canvas-title span { margin-left: 5px; color: #81909e; font-size: 10px; font-weight: 500; }
.chart-kpis { display: flex; align-items: flex-end; justify-content: space-between; gap: 18px; min-height: 62px; margin-top: 4px; }
.average-kpi > span, .window-change > span { display: block; color: #677c8e; font-size: 10px; }
.average-kpi strong { display: inline-block; margin-top: 4px; color: #0d2d55; font-size: 38px; line-height: 1; letter-spacing: -1.5px; }
.average-kpi small { margin-left: 4px; color: #738598; font-size: 12px; }
.window-change { padding-bottom: 5px; text-align: right; }
.window-change strong { display: inline-block; margin-top: 4px; font-size: 20px; }
.window-change small { display: block; margin-top: 2px; color: #7d8e9c; font-size: 9px; }
.window-change.positive strong { color: #269b63; }
.window-change.negative strong { color: var(--danger); }
.window-change.neutral strong { color: #748696; font-size: 13px; }
.trend-chart { display: block; width: 100%; min-height: 190px; margin-top: -3px; overflow: visible; }
.grid-lines line { stroke: #e5edf1; stroke-width: 1; stroke-dasharray: 3 4; }
.grid-lines text { fill: #7e8f9e; font-size: 10px; }
.point-crosshair { stroke: #dfe8ed; stroke-width: 1; stroke-dasharray: 3 3; }
.chart-point > circle:first-of-type { fill: #1497a5; stroke: #fff; stroke-width: 2; }
.latest-ring { fill: none; stroke: rgba(20,151,165,.28); stroke-width: 3; }
.point-score { fill: #173756; font-size: 11px; font-weight: 750; }
.point-label { fill: #718496; font-size: 10px; }
.chart-footnote { display: flex; justify-content: space-between; gap: 16px; margin-top: -4px; padding: 9px 11px; background: #f7f9fb; color: #6f8293; font-size: 10px; }
.chart-footnote b { margin-left: 5px; color: #213f5d; font-size: 12px; }
.status-visual { display: grid; grid-template-columns: 116px minmax(0, 1fr); align-items: center; gap: 12px; margin-top: 18px; }
.status-donut { position: relative; width: 108px; height: 108px; display: grid; place-items: center; border-radius: 50%; }
.status-donut::after { content: ''; position: absolute; inset: 19px; border-radius: 50%; background: #fff; }
.status-donut > div { position: relative; z-index: 1; display: grid; text-align: center; }
.status-donut strong { color: #123351; font-size: 25px; line-height: 1; }
.status-donut span { margin-top: 4px; color: #6b7f90; font-size: 9px; }
.status-legend { display: grid; gap: 8px; min-width: 0; }
.status-legend > div { display: grid; grid-template-columns: 7px minmax(0, 1fr) auto auto; align-items: center; gap: 6px; font-size: 9px; }
.status-legend i { width: 7px; height: 7px; border-radius: 50%; }
.status-legend span { overflow: hidden; color: #435c71; text-overflow: ellipsis; white-space: nowrap; }
.status-legend strong { color: #183956; font-size: 11px; }
.status-legend small { min-width: 42px; color: #81919e; text-align: right; }
.status-explain { margin: 16px 0 0; padding: 9px 10px; background: #f7f9fb; color: #758695; font-size: 9px; line-height: 1.55; }
.empty-state { min-height: 240px; display: flex; align-items: center; justify-content: center; gap: 16px; margin-top: 16px; padding: 24px; border: 1px solid var(--line-soft); border-radius: 10px; }
.empty-icon { width: 48px; height: 48px; display: grid; place-items: center; border-radius: 10px; background: var(--teal-soft); color: var(--teal); font-size: 24px; }
.empty-state strong { color: #28465f; }
.empty-state p { max-width: 360px; margin: 5px 0 0; color: #81919f; font-size: 11px; line-height: 1.55; }

.recent-records { margin-top: 14px; }
.subsection-heading { display: flex; align-items: center; justify-content: space-between; gap: 14px; min-height: 32px; }
.subsection-heading > div { display: flex; align-items: center; gap: 7px; min-width: 0; }
.subsection-heading > div > i { width: 2px; height: 16px; background: var(--teal); }
.subsection-heading h3 { margin: 0; color: #1a3958; font-size: 13px; }
.subsection-heading span { color: #81909d; font-size: 9px; }
.subsection-heading button, .knowledge-empty button { display: inline-flex; align-items: center; gap: 3px; padding: 3px 0; border: 0; background: transparent; color: #2a78d6; font-size: 10px; cursor: pointer; }
.record-list { margin-top: 8px; overflow: hidden; border: 1px solid var(--line-soft); border-radius: 8px; }
.record-head, .record-row { display: grid; grid-template-columns: minmax(135px, 1.8fr) 102px 70px 56px 84px 34px; align-items: center; gap: 7px; }
.record-head { min-height: 32px; padding: 0 10px; background: #f7f9fb; color: #617588; font-size: 9px; font-weight: 650; }
.record-row { min-height: 38px; padding: 0 10px; border-top: 1px solid var(--line-soft); color: #587084; font-size: 9px; transition: background .16s ease; }
.record-row:hover { background: #f7fbfc; }
.record-row > strong { overflow: hidden; color: #24425e; font-size: 10px; text-overflow: ellipsis; white-space: nowrap; }
.record-status { justify-self: start; padding: 3px 5px; border-radius: 4px; font-size: 8px; white-space: nowrap; }
.record-status.finalized, .timeline-row em.finalized { background: #e5f7f3; color: #12876e; }
.record-status.review, .timeline-row em.review { background: #eaf3ff; color: #2f78d8; }
.record-status.verification, .timeline-row em.verification { background: #fff3e4; color: #d37820; }
.record-status.failed, .timeline-row em.failed { background: #ffe9e9; color: #d94b4b; }
.record-status.unknown, .timeline-row em.unknown { background: #eef1f3; color: #75838e; }
.record-score { color: #123d63; font-weight: 700; }
.record-row button { padding: 2px 0; border: 0; background: transparent; color: #2879da; font-size: 9px; cursor: pointer; }
.record-empty { margin: 10px 0 0; padding: 18px; background: #f8fafb; color: #8b99a5; font-size: 11px; text-align: center; }

.identity-strip { display: grid; grid-template-columns: minmax(150px, 1.2fr) repeat(4, minmax(74px, .7fr)); align-items: center; gap: 0; margin-top: 16px; padding: 12px 14px; border-radius: 9px; background: #0e315c; color: #fff; }
.student-identity { display: flex; align-items: center; gap: 9px; min-width: 0; padding-right: 12px; }
.student-identity > span { width: 33px; height: 33px; display: grid; place-items: center; flex: 0 0 33px; border: 1px solid rgba(255,255,255,.35); border-radius: 50%; background: rgba(255,255,255,.12); font-size: 14px; font-weight: 700; }
.student-identity div { min-width: 0; }
.student-identity strong, .student-identity small { display: block; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }
.student-identity strong { font-size: 13px; }
.student-identity small { margin-top: 2px; color: rgba(255,255,255,.6); font-size: 8px; }
.identity-field { min-width: 0; padding: 2px 10px; border-left: 1px solid rgba(255,255,255,.2); }
.identity-field span, .identity-field strong { display: block; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }
.identity-field span { color: rgba(255,255,255,.62); font-size: 8px; }
.identity-field strong { margin-top: 4px; font-size: 10px; }
.archive-time { margin: 9px 2px 0; color: #80909e; font-size: 9px; text-align: right; }
.profile-metrics { display: grid; grid-template-columns: repeat(4, 1fr); margin-top: 12px; border-top: 1px solid var(--line-soft); border-bottom: 1px solid var(--line-soft); }
.profile-metrics > div { min-width: 0; padding: 14px 12px; border-right: 1px solid var(--line-soft); }
.profile-metrics > div:first-child { padding-left: 4px; }
.profile-metrics > div:last-child { border-right: 0; }
.profile-metrics span, .profile-metrics small { display: block; }
.profile-metrics span { color: #5d7387; font-size: 9px; }
.profile-metrics strong { display: block; margin-top: 5px; color: #0d2f58; font-size: 21px; letter-spacing: -.4px; }
.profile-metrics small { overflow: hidden; margin-top: 4px; color: #8b98a4; font-size: 8px; text-overflow: ellipsis; white-space: nowrap; }
.profile-scales { display: grid; grid-template-columns: repeat(3, 1fr); gap: 0; margin-top: 12px; border: 1px solid var(--line-soft); border-radius: 9px; }
.profile-scales article { min-width: 0; padding: 13px; border-right: 1px solid var(--line-soft); }
.profile-scales article:last-child { border-right: 0; }
.scale-heading { display: flex; align-items: center; gap: 5px; min-height: 22px; }
.scale-heading > span { color: #304e69; font-size: 9px; font-weight: 650; }
.scale-heading strong { margin-left: auto; color: #173959; font-size: 13px; }
.scale-heading em { padding: 2px 4px; border-radius: 4px; font-size: 7px; font-style: normal; white-space: nowrap; }
.scale-heading em.positive { background: #e6f6f1; color: #178268; }
.scale-heading em.warning { background: #fff0df; color: #d7741d; }
.scale-track { position: relative; height: 4px; margin-top: 11px; border-radius: 4px; background: #dde5ea; }
.scale-track i { display: block; width: var(--marker); height: 100%; border-radius: inherit; background: var(--metric-color); }
.scale-track::after { content: ''; position: absolute; top: 50%; left: var(--marker); width: 8px; height: 8px; border: 3px solid var(--metric-color); border-radius: 50%; background: #fff; transform: translate(-50%, -50%); }
.scale-labels { display: flex; justify-content: space-between; margin-top: 7px; color: #83929e; font-size: 7px; }
.profile-evidence-grid { display: grid; grid-template-columns: 1fr 1fr; gap: 10px; margin-top: 12px; }
.evidence-timeline, .knowledge-empty { min-height: 228px; padding: 14px; }
.timeline-list { position: relative; margin-top: 11px; }
.timeline-list::before { content: ''; position: absolute; top: 14px; bottom: 14px; left: 5px; width: 1px; background: #31a8ac; }
.timeline-row { position: relative; display: grid; grid-template-columns: 12px 28px minmax(0, 1fr) auto; align-items: center; gap: 7px; min-height: 30px; padding: 3px 0; }
.timeline-row > i { position: relative; z-index: 1; width: 8px; height: 8px; border: 2px solid #1497a5; border-radius: 50%; background: #fff; }
.timeline-row > strong { color: #078b96; font-size: 13px; }
.timeline-row > div { min-width: 0; }
.timeline-row > div span, .timeline-row > div small { display: block; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }
.timeline-row > div span { color: #38546b; font-size: 9px; }
.timeline-row > div small { margin-top: 2px; color: #8a98a4; font-size: 7px; }
.timeline-row > em { padding: 2px 4px; border-radius: 4px; font-size: 7px; font-style: normal; white-space: nowrap; }
.timeline-empty { margin: 70px 0 0; color: #8997a3; font-size: 10px; text-align: center; }
.knowledge-empty { display: flex; flex-direction: column; align-items: center; justify-content: center; text-align: center; }
.knowledge-icon { width: 48px; height: 48px; display: grid; place-items: center; color: #9dafbd; font-size: 40px; }
.knowledge-empty strong { margin-top: 6px; color: #27445f; font-size: 13px; }
.knowledge-empty p { max-width: 260px; margin: 9px 0 10px; color: #7e8f9d; font-size: 9px; line-height: 1.6; }
.profile-notes { display: grid; grid-template-columns: 1fr 1fr auto; align-items: stretch; margin-top: 12px; border: 1px solid var(--line-soft); border-radius: 9px; }
.profile-notes > div { display: grid; grid-template-columns: 24px minmax(0, 1fr); align-items: center; gap: 7px; padding: 11px; border-right: 1px solid var(--line-soft); }
.profile-notes > div > span { width: 23px; height: 23px; display: grid; place-items: center; color: #167e8c; font-size: 16px; }
.profile-notes p { margin: 0; color: #708393; font-size: 8px; line-height: 1.5; }
.profile-notes > button { display: inline-flex; align-items: center; justify-content: center; gap: 5px; padding: 0 12px; border: 0; background: #f8fbfc; color: #176f7c; font-size: 9px; font-weight: 650; cursor: pointer; white-space: nowrap; }
.profile-notes > button svg { width: 16px; }

.review-rail { margin-top: 16px; padding: 18px; }
.review-rail > header { display: flex; align-items: flex-start; justify-content: space-between; gap: 18px; }
.review-rail > header > div { display: flex; gap: 12px; }
.review-rail > header > span { padding: 5px 7px; background: #f2f5f7; color: #6f8190; font-size: 9px; }
.review-grid { display: grid; grid-template-columns: repeat(3, 1fr); margin-top: 13px; border-top: 1px solid var(--line-soft); }
.review-grid article { display: grid; grid-template-columns: 28px minmax(0, 1fr); gap: 8px; min-width: 0; padding: 15px 18px 5px 0; }
.review-grid article + article { padding-left: 18px; border-left: 1px solid var(--line-soft); }
.review-grid article > span { color: #95a3ae; font-size: 9px; font-weight: 700; letter-spacing: 1px; }
.review-grid strong { display: block; overflow: hidden; color: #27465f; font-size: 11px; text-overflow: ellipsis; white-space: nowrap; }
.review-grid p { min-height: 30px; margin: 5px 0 0; color: #7d8d99; font-size: 9px; line-height: 1.55; }
.review-grid em { grid-column: 2; justify-self: start; margin-top: 7px; padding: 3px 6px; border-radius: 4px; background: #fff1df; color: #d77924; font-size: 8px; font-style: normal; }

.assistant-panel { margin-top: 16px; padding: 18px; scroll-margin-top: 76px; }
.assistant-state { display: inline-flex; align-items: center; gap: 7px; padding-top: 5px; color: #63798c; font-size: 10px; white-space: nowrap; }
.assistant-layout { display: grid; grid-template-columns: minmax(0, 1.65fr) minmax(290px, .75fr); gap: 18px; margin-top: 14px; }
.assistant-composer { min-width: 0; padding-right: 18px; border-right: 1px solid var(--line-soft); }
.prompt-row { display: flex; flex-wrap: wrap; gap: 7px; }
.prompt-row button { padding: 7px 9px; border: 1px solid #dce5eb; border-radius: 6px; background: #f8fafb; color: #526a7d; font-size: 9px; cursor: pointer; }
.prompt-row button:hover, .prompt-row button:focus-visible { border-color: #76bac2; background: #fff; color: #147884; outline: none; }
.prompt-row button:disabled { opacity: .55; cursor: wait; }
.assistant-reply { display: grid; grid-template-columns: 28px minmax(0, 1fr); gap: 9px; margin-top: 12px; padding: 12px; border-left: 2px solid var(--teal); background: #f5fafb; }
.assistant-reply > span { width: 25px; height: 25px; display: grid; place-items: center; border-radius: 6px; background: #127e8c; color: #fff; font-size: 8px; font-weight: 700; }
.assistant-reply p { max-height: 180px; overflow: auto; margin: 2px 0 0; color: #425d73; font-size: 11px; line-height: 1.65; white-space: pre-wrap; }
.ask-box { display: flex; gap: 8px; margin-top: 11px; }
.ask-box :deep(.el-input__wrapper) { min-height: 42px; border-radius: 7px; box-shadow: 0 0 0 1px #d9e3e9 inset; }
.ask-box :deep(.el-input__inner) { color: #324f67; font-size: 11px; }
.ask-box .el-button { width: 44px; height: 42px; padding: 0; border-radius: 7px; background: #147f8d; border-color: #147f8d; }
.assistant-composer > small { display: block; margin-top: 7px; color: #95a0aa; font-size: 8px; }
.capability-title { margin-bottom: 7px; color: #345069; font-size: 10px; font-weight: 700; }
.capability-row { display: grid; grid-template-columns: 17px minmax(0, 1fr) 7px auto; align-items: center; gap: 7px; min-height: 27px; color: #50687c; font-size: 9px; }
.capability-row > svg { width: 15px; color: #4b7894; }
.capability-row small { color: #8795a1; font-size: 8px; }
.assistant-actions { display: grid; grid-template-columns: 1fr 1fr; gap: 7px; margin-top: 10px; }
.assistant-actions button { min-height: 36px; display: inline-flex; align-items: center; justify-content: center; gap: 5px; border: 1px solid #dce5eb; border-radius: 6px; background: #fff; color: #315d77; font-size: 9px; cursor: pointer; }
.assistant-actions button:hover { border-color: #70b4bd; color: #147985; }
.assistant-actions button:disabled { opacity: .55; cursor: wait; }
.assistant-actions button svg { width: 14px; }

.report-meta { display: flex; align-items: center; gap: 10px; color: #81909c; font-size: 11px; }
.report-score { display: grid; grid-template-columns: auto minmax(0,1fr); column-gap: 12px; align-items: end; margin-top: 18px; padding: 18px 20px; border-radius: 10px; background: #eef8f8; }
.report-score strong { grid-row: 1 / span 2; color: #147f8d; font-size: 46px; line-height: 1; }
.report-score span { color: #526b7e; font-size: 13px; }
.report-score p { margin: 5px 0 0; color: #6f8291; font-size: 11px; line-height: 1.5; }
.report-sections { display: grid; grid-template-columns: 1fr 1fr; gap: 24px; margin-top: 20px; }
.report-sections h3 { margin: 0 0 10px; color: #29465c; font-size: 14px; }
.report-sections ul, .report-sections ol { margin: 0; padding-left: 20px; color: #657988; font-size: 11px; line-height: 1.65; }
.report-sections li { margin-bottom: 9px; }
.report-sections b { color: #3c576a; }
.report-sections ol span { display: block; color: #8795a0; }

@media (prefers-reduced-motion: no-preference) {
  .module-panel, .review-rail, .assistant-panel { animation: module-enter .35s ease both; }
  .profile-module { animation-delay: .05s; }
  .review-rail { animation-delay: .1s; }
  @keyframes module-enter { from { opacity: 0; transform: translateY(5px); } to { opacity: 1; transform: translateY(0); } }
}

@media (max-width: 1220px) {
  .primary-grid { grid-template-columns: minmax(0, 1fr); }
  .module-panel { padding: 20px; }
  .record-head, .record-row { grid-template-columns: minmax(180px, 1.8fr) 130px 86px 70px 100px 40px; }
}

@media (max-width: 900px) {
  .ai-learning-page { width: min(100% - 28px, 1500px); padding-top: 24px; }
  .page-intro { align-items: flex-start; flex-direction: column; gap: 14px; }
  .page-meta { width: 100%; justify-content: space-between; }
  .exam-overview-grid, .profile-evidence-grid, .assistant-layout { grid-template-columns: minmax(0, 1fr); }
  .assistant-composer { padding-right: 0; padding-bottom: 16px; border-right: 0; border-bottom: 1px solid var(--line-soft); }
  .review-grid { grid-template-columns: minmax(0, 1fr); }
  .review-grid article + article { padding-left: 0; border-left: 0; border-top: 1px solid var(--line-soft); }
  .profile-notes { grid-template-columns: 1fr 1fr; }
  .profile-notes > button { grid-column: 1 / -1; min-height: 42px; border-top: 1px solid var(--line-soft); }
}

@media (max-width: 720px) {
  .ai-learning-page { width: min(100% - 20px, 1500px); }
  .intro-copy h1 { font-size: 28px; }
  .page-meta { align-items: flex-start; flex-direction: column; gap: 8px; }
  .module-nav { gap: 24px; }
  .module-panel, .review-rail, .assistant-panel { padding: 14px; border-radius: 11px; }
  .module-header { align-items: flex-start; flex-direction: column; gap: 10px; }
  .module-action { align-self: flex-end; }
  .identity-strip { grid-template-columns: 1fr 1fr; row-gap: 10px; }
  .student-identity { grid-column: 1 / -1; padding-bottom: 10px; border-bottom: 1px solid rgba(255,255,255,.18); }
  .identity-field { border-left: 0; padding-left: 0; }
  .profile-metrics { grid-template-columns: 1fr 1fr; }
  .profile-metrics > div:nth-child(2) { border-right: 0; }
  .profile-metrics > div:nth-child(-n+2) { border-bottom: 1px solid var(--line-soft); }
  .profile-scales { grid-template-columns: minmax(0, 1fr); }
  .profile-scales article { border-right: 0; border-bottom: 1px solid var(--line-soft); }
  .profile-scales article:last-child { border-bottom: 0; }
  .record-head { display: none; }
  .record-row { grid-template-columns: minmax(0, 1fr) auto auto; gap: 8px; min-height: 48px; }
  .record-row .record-date, .record-row .record-questions { display: none; }
  .record-row button { display: none; }
  .review-rail > header { flex-direction: column; }
  .profile-notes { grid-template-columns: minmax(0, 1fr); }
  .profile-notes > div { border-right: 0; border-bottom: 1px solid var(--line-soft); }
  .profile-notes > button { grid-column: auto; }
  .report-sections { grid-template-columns: minmax(0, 1fr); }
}

@media (max-width: 480px) {
  .profile-metrics, .assistant-actions { grid-template-columns: minmax(0, 1fr); }
  .profile-metrics > div { border-right: 0; border-bottom: 1px solid var(--line-soft); }
  .profile-metrics > div:last-child { border-bottom: 0; }
  .status-visual { grid-template-columns: 96px minmax(0, 1fr); }
  .status-donut { width: 90px; height: 90px; }
  .status-donut::after { inset: 16px; }
  .subsection-heading { align-items: flex-start; flex-direction: column; }
}

:global(#app.ai-learning-app) { min-width: 0; }
:global(.analysis-dialog) { max-width: calc(100vw - 32px); }
</style>
