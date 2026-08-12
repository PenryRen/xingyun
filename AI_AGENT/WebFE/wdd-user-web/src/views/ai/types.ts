export type AiTabName = 'overview' | 'exam-analysis' | 'profile' | 'assistant';

export interface ExamRecord {
  id: number;
  paperName?: string;
  scoreRatePercent?: number | null;
  questionCount?: number | null;
  questionCorrect?: number | null;
  status?: number;
  statusName?: string;
  createTime?: string;
  watch?: boolean;
}

export interface StudentProfile {
  userName?: string;
  realName?: string;
  workNo?: string;
  departmentName?: string;
  jobTitle?: string;
}

export interface AiExamCounts {
  total: number;
  finalized: number;
  validFinalized: number;
  invalidFinalized: number;
  waitingReview: number;
  waitingVerification: number;
  verificationFailed: number;
  other: number;
}

export interface AiExamSummary {
  trendSampleCount: number;
  recentAveragePercent: number | null;
  changeFromFirstPercentPoints: number | null;
  volatilityPercentPoints: number | null;
  questionAccuracyPercent: number | null;
  accuracySampleCount: number;
  finalizedPercent: number | null;
}

export interface AiLearningWorkspace {
  student: StudentProfile;
  counts: AiExamCounts;
  summary: AiExamSummary;
  trend: ExamRecord[];
  recentExams: ExamRecord[];
  insights: InsightItem[];
  report: {summary: string; evidenceExamCount: number};
  knowledgePoints: {available: boolean; reasonCode: string; message: string};
  meta: {source: string; scope: string; complete: boolean; generatedAt: string};
}

export interface MetricItem {
  label: string;
  value: string;
  note?: string;
}

export interface StatusItem {
  label: string;
  count: number;
  note: string;
  color: string;
}

export interface InsightItem {
  title: string;
  description: string;
  evidence: string;
}

export interface AiConversationMessage {
  id: string;
  role: 'user' | 'assistant';
  content: string;
  error?: boolean;
}
