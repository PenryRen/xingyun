from __future__ import annotations

import sys
from pathlib import Path

import pytest
from sqlalchemy import create_engine, text
from sqlalchemy.pool import StaticPool


PROJECT_ROOT = Path(__file__).resolve().parents[1]
SRC_ROOT = PROJECT_ROOT / "src"
if str(SRC_ROOT) not in sys.path:
    sys.path.insert(0, str(SRC_ROOT))

from tools.mysql_learning_query import MysqlLearningQueryService  # noqa: E402


@pytest.fixture()
def query_service() -> MysqlLearningQueryService:
    engine = create_engine(
        "sqlite+pysqlite:///:memory:",
        connect_args={"check_same_thread": False},
        poolclass=StaticPool,
    )
    with engine.begin() as connection:
        connection.execute(
            text(
                """
                CREATE TABLE t_exam_paper_answer (
                    id INTEGER PRIMARY KEY,
                    user_score INTEGER NULL,
                    paper_score INTEGER NULL,
                    question_correct INTEGER NULL,
                    question_count INTEGER NULL,
                    status INTEGER NULL,
                    create_user INTEGER NOT NULL,
                    create_time TEXT NULL,
                    deleted INTEGER NOT NULL DEFAULT 0,
                    paper_type INTEGER NULL
                )
                """
            )
        )
        connection.execute(
            text(
                """
                INSERT INTO t_exam_paper_answer (
                    id, user_score, paper_score, question_correct, question_count,
                    status, create_user, create_time, deleted, paper_type
                ) VALUES
                    (1, 50, 100, 5, 10, 2, 101, '2026-01-01 08:00:00', 0, 1),
                    (2,  0, 100, 0, 10, 2, 101, '2026-01-02 08:00:00', 0, 2),
                    (3, NULL, 100, 2, 5, 2, 101, '2026-01-03 08:00:00', 0, 3),
                    (4, NULL, 100, NULL, 10, 1, 101, '2026-01-04 08:00:00', 0, 1),
                    (5, NULL, 100, NULL, 10, 3, 101, '2026-01-05 08:00:00', 0, 1),
                    (6, NULL, 100, NULL, 10, 4, 101, '2026-01-06 08:00:00', 0, 1),
                    (7, 99, 100, 9, 10, 2, 101, '2026-01-07 08:00:00', 1, 1),
                    (8, 88, 100, 8, 10, 2, 202, '2026-01-08 08:00:00', 0, 1)
                """
            )
        )
    return MysqlLearningQueryService(engine=engine)


def test_learning_context_is_student_scoped_and_anonymized(query_service):
    context = query_service.get_learning_context(101)

    assert context["examCount"] == 6
    assert context["finalizedCount"] == 3
    assert context["pendingReviewCount"] == 1
    assert context["pendingVerifyCount"] == 1
    assert context["verifyFailedCount"] == 1
    assert context["meta"] == {"source": "WEBBE_MYSQL", "complete": True}

    serialized_keys = str(context.keys()).lower()
    assert "name" not in serialized_keys
    assert "cookie" not in serialized_keys
    assert "token" not in serialized_keys
    assert "password" not in serialized_keys


def test_real_zero_is_kept_but_missing_score_is_excluded(query_service):
    context = query_service.get_learning_context(101)

    assert [item["scorePercent"] for item in context["recentScores"]] == [50.0, 0.0]
    assert context["recentAverageScorePercent"] == 25.0
    assert context["scoreStandardDeviationPercent"] == 25.0


def test_question_accuracy_uses_finalized_rows_with_valid_totals(query_service):
    context = query_service.get_learning_context(101)

    assert context["questionAccuracyPercent"] == 28.0


def test_knowledge_points_are_explicitly_unavailable(query_service):
    context = query_service.get_learning_context(101)

    assert context["knowledgePoints"] == {
        "available": False,
        "reason": "QUESTION_TAGS_NOT_AVAILABLE",
    }


def test_paper_type_does_not_filter_exam_answers(query_service):
    records = query_service.get_exam_records(101)

    assert len(records) == 6


@pytest.mark.parametrize("invalid_user_id", [None, 0, -1, True, "101"])
def test_user_id_must_be_a_trusted_positive_integer(query_service, invalid_user_id):
    with pytest.raises(ValueError, match="user_id"):
        query_service.get_learning_context(invalid_user_id)


def test_empty_student_has_null_metrics_not_fabricated_zero(query_service):
    context = query_service.get_learning_context(303)

    assert context["examCount"] == 0
    assert context["recentScores"] == []
    assert context["recentAverageScorePercent"] is None
    assert context["scoreStandardDeviationPercent"] is None
    assert context["questionAccuracyPercent"] is None
