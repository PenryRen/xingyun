export type AiTabName = 'overview' | 'exam-analysis' | 'profile' | 'assistant';

export interface ExamRecord {
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
  watch?: boolean;
}

export interface StudentProfile {
  userName?: string;
  realName?: string;
  workNo?: string;
  departmentStr?: string;
  jobTitle?: string;
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
