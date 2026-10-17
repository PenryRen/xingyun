"""Deterministic, read-only learning-data queries against WebBE's MySQL.

The functions in this module are deliberately not decorated as LangChain tools
and are not registered in the Agent graph. They form a standalone integration
boundary that the main Agent workflow can call later.
"""

from __future__ import annotations

from dataclasses import dataclass
from datetime import date, datetime
from statistics import fmean, pstdev
from typing import Any, Callable, Iterable, Mapping, Optional, Sequence

from sqlalchemy import Engine, text
from sqlalchemy.engine import Connection

from storage.database.mysql_client import get_mysql_engine, readonly_connection


STATUS_WAIT_JUDGE = 1
STATUS_COMPLETE = 2
STATUS_WAIT_CHECK = 3
STATUS_CHECK_ERROR = 4

KNOWLEDGE_POINTS_NOT_AVAILABLE = "QUESTION_TAGS_NOT_AVAILABLE"

_EXAM_ANSWER_QUERY = text(
    """
    SELECT
        id,
        user_score,
        paper_score,
        question_correct,
        question_count,
        status,
        create_time
    FROM t_exam_paper_answer
    WHERE deleted = 0
      AND create_user = :user_id
    ORDER BY create_time DESC, id DESC
    """
)


def _validate_user_id(user_id: int) -> int:
    if isinstance(user_id, bool) or not isinstance(user_id, int) or user_id <= 0:
        raise ValueError("user_id must be a positive integer from a trusted context")
    return user_id


def _optional_int(value: Any) -> Optional[int]:
    if value is None:
        return None
    return int(value)


def _iso_datetime(value: Optional[datetime | date | str]) -> Optional[str]:
    if value is None:
        return None
    if isinstance(value, (datetime, date)):
        return value.isoformat()
    return str(value)


@dataclass(frozen=True)
class ExamAnswerRecord:
    """The minimal columns needed to calculate an anonymized learning summary."""

    answer_id: int
    user_score: Optional[int]
    paper_score: Optional[int]
    question_correct: Optional[int]
    question_count: Optional[int]
    status: Optional[int]
    create_time: Optional[datetime | date | str]

    @classmethod
    def from_mapping(cls, row: Mapping[str, Any]) -> "ExamAnswerRecord":
        return cls(
            answer_id=int(row["id"]),
            user_score=_optional_int(row.get("user_score")),
            paper_score=_optional_int(row.get("paper_score")),
            question_correct=_optional_int(row.get("question_correct")),
            question_count=_optional_int(row.get("question_count")),
            status=_optional_int(row.get("status")),
            create_time=row.get("create_time"),
        )

    @property
    def score_percent(self) -> Optional[float]:
        if self.user_score is None or self.paper_score is None or self.paper_score <= 0:
            return None
        return self.user_score * 100.0 / self.paper_score


ConnectionFactory = Callable[[], Any]


class MysqlLearningQueryService:
    """Load one student's answer rows and calculate stable business metrics."""

    def __init__(
        self,
        engine: Optional[Engine] = None,
        connection_factory: Optional[ConnectionFactory] = None,
    ) -> None:
        if engine is not None and connection_factory is not None:
            raise ValueError("provide engine or connection_factory, not both")
        self._engine = engine
        self._connection_factory = connection_factory

    def _connections(self):
        if self._connection_factory is not None:
            return self._connection_factory()
        return readonly_connection(self._engine or get_mysql_engine())

    def get_exam_records(self, user_id: int) -> list[ExamAnswerRecord]:
        """Return only the current student's non-deleted exam answer rows."""

        trusted_user_id = _validate_user_id(user_id)
        with self._connections() as connection:
            rows = connection.execute(
                _EXAM_ANSWER_QUERY, {"user_id": trusted_user_id}
            ).mappings()
            return [ExamAnswerRecord.from_mapping(row) for row in rows]

    def get_exam_count(self, user_id: int) -> int:
        return len(self.get_exam_records(user_id))

    def get_exam_status_counts(self, user_id: int) -> dict[str, int]:
        return self._status_counts(self.get_exam_records(user_id))

    def get_recent_scores(self, user_id: int, limit: int = 6) -> list[dict[str, Any]]:
        if isinstance(limit, bool) or not isinstance(limit, int) or limit <= 0:
            raise ValueError("limit must be a positive integer")
        return self._recent_scores(self.get_exam_records(user_id), limit)

    def get_score_summary(self, user_id: int, recent_limit: int = 6) -> dict[str, Any]:
        records = self.get_exam_records(user_id)
        recent_scores = self._recent_scores(records, recent_limit)
        values = [item["scorePercent"] for item in recent_scores]
        return {
            "recentAverageScorePercent": self._rounded_mean(values),
            "scoreStandardDeviationPercent": self._rounded_stddev(values),
            "recentScores": recent_scores,
        }

    def get_question_accuracy(self, user_id: int) -> Optional[float]:
        return self._question_accuracy(self.get_exam_records(user_id))

    def get_knowledge_status(self, user_id: int) -> dict[str, Any]:
        _validate_user_id(user_id)
        return {
            "available": False,
            "reason": KNOWLEDGE_POINTS_NOT_AVAILABLE,
        }

    def get_learning_context(self, user_id: int, recent_limit: int = 6) -> dict[str, Any]:
        """Return the anonymized contract intended for later Agent integration."""

        if isinstance(recent_limit, bool) or not isinstance(recent_limit, int) or recent_limit <= 0:
            raise ValueError("recent_limit must be a positive integer")

        records = self.get_exam_records(user_id)
        status_counts = self._status_counts(records)
        recent_scores = self._recent_scores(records, recent_limit)
        score_values = [item["scorePercent"] for item in recent_scores]

        return {
            "examCount": len(records),
            "finalizedCount": status_counts["finalizedCount"],
            "pendingReviewCount": status_counts["pendingReviewCount"],
            "pendingVerifyCount": status_counts["pendingVerifyCount"],
            "verifyFailedCount": status_counts["verifyFailedCount"],
            "recentAverageScorePercent": self._rounded_mean(score_values),
            "scoreStandardDeviationPercent": self._rounded_stddev(score_values),
            "questionAccuracyPercent": self._question_accuracy(records),
            "recentScores": recent_scores,
            "knowledgePoints": {
                "available": False,
                "reason": KNOWLEDGE_POINTS_NOT_AVAILABLE,
            },
            "meta": {
                "source": "WEBBE_MYSQL",
                "complete": True,
            },
        }

    @staticmethod
    def _status_counts(records: Iterable[ExamAnswerRecord]) -> dict[str, int]:
        counts = {
            "finalizedCount": 0,
            "pendingReviewCount": 0,
            "pendingVerifyCount": 0,
            "verifyFailedCount": 0,
        }
        for record in records:
            if record.status == STATUS_COMPLETE:
                counts["finalizedCount"] += 1
            elif record.status == STATUS_WAIT_JUDGE:
                counts["pendingReviewCount"] += 1
            elif record.status == STATUS_WAIT_CHECK:
                counts["pendingVerifyCount"] += 1
            elif record.status == STATUS_CHECK_ERROR:
                counts["verifyFailedCount"] += 1
        return counts

    @staticmethod
    def _recent_scores(
        records: Sequence[ExamAnswerRecord], limit: int
    ) -> list[dict[str, Any]]:
        newest_first = [
            record
            for record in records
            if record.status == STATUS_COMPLETE and record.score_percent is not None
        ][:limit]
        chronological = list(reversed(newest_first))
        return [
            {
                "sequence": index,
                "examDate": _iso_datetime(record.create_time),
                "scorePercent": round(float(record.score_percent), 2),
            }
            for index, record in enumerate(chronological, start=1)
        ]

    @staticmethod
    def _question_accuracy(records: Iterable[ExamAnswerRecord]) -> Optional[float]:
        correct = 0
        total = 0
        for record in records:
            if (
                record.status == STATUS_COMPLETE
                and record.question_correct is not None
                and record.question_count is not None
                and record.question_count > 0
            ):
                correct += record.question_correct
                total += record.question_count
        if total == 0:
            return None
        return round(correct * 100.0 / total, 2)

    @staticmethod
    def _rounded_mean(values: Sequence[float]) -> Optional[float]:
        if not values:
            return None
        return round(fmean(values), 2)

    @staticmethod
    def _rounded_stddev(values: Sequence[float]) -> Optional[float]:
        if not values:
            return None
        return round(pstdev(values), 2)


def _service(service: Optional[MysqlLearningQueryService]) -> MysqlLearningQueryService:
    return service or MysqlLearningQueryService()


def get_student_exam_count(
    user_id: int, *, service: Optional[MysqlLearningQueryService] = None
) -> int:
    return _service(service).get_exam_count(user_id)


def get_student_exam_status_counts(
    user_id: int, *, service: Optional[MysqlLearningQueryService] = None
) -> dict[str, int]:
    return _service(service).get_exam_status_counts(user_id)


def get_recent_exam_scores(
    user_id: int,
    limit: int = 6,
    *,
    service: Optional[MysqlLearningQueryService] = None,
) -> list[dict[str, Any]]:
    return _service(service).get_recent_scores(user_id, limit)


def get_student_score_summary(
    user_id: int,
    recent_limit: int = 6,
    *,
    service: Optional[MysqlLearningQueryService] = None,
) -> dict[str, Any]:
    return _service(service).get_score_summary(user_id, recent_limit)


def get_student_question_accuracy(
    user_id: int, *, service: Optional[MysqlLearningQueryService] = None
) -> Optional[float]:
    return _service(service).get_question_accuracy(user_id)


def get_student_knowledge_status(
    user_id: int, *, service: Optional[MysqlLearningQueryService] = None
) -> dict[str, Any]:
    return _service(service).get_knowledge_status(user_id)


def get_student_learning_context(
    user_id: int,
    recent_limit: int = 6,
    *,
    service: Optional[MysqlLearningQueryService] = None,
) -> dict[str, Any]:
    return _service(service).get_learning_context(user_id, recent_limit)


__all__ = [
    "ExamAnswerRecord",
    "KNOWLEDGE_POINTS_NOT_AVAILABLE",
    "MysqlLearningQueryService",
    "get_recent_exam_scores",
    "get_student_exam_count",
    "get_student_exam_status_counts",
    "get_student_knowledge_status",
    "get_student_learning_context",
    "get_student_question_accuracy",
    "get_student_score_summary",
]
