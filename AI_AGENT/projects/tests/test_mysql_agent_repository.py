from __future__ import annotations

import json
import sys
from pathlib import Path

import pytest
from sqlalchemy import create_engine
from sqlalchemy.orm import sessionmaker
from sqlalchemy.pool import StaticPool


PROJECT_ROOT = Path(__file__).resolve().parents[1]
SRC_ROOT = PROJECT_ROOT / "src"
if str(SRC_ROOT) not in sys.path:
    sys.path.insert(0, str(SRC_ROOT))

from storage.database.shared.model import Base  # noqa: E402
from tools import data_base_tools as repository  # noqa: E402


@pytest.fixture(autouse=True)
def isolated_repository(monkeypatch):
    engine = create_engine(
        "sqlite+pysqlite:///:memory:",
        connect_args={"check_same_thread": False},
        poolclass=StaticPool,
    )
    Base.metadata.create_all(engine)
    sessions = sessionmaker(bind=engine, autoflush=False, expire_on_commit=False)
    monkeypatch.setattr(repository, "get_session", sessions)


def test_agent_models_are_isolated_from_webbe_business_tables():
    assert set(Base.metadata.tables) == {
        "ai_answer_record",
        "ai_exam_record",
        "ai_learning_profile",
        "ai_question",
        "ai_student",
        "ai_student_knowledge_progress",
    }


def test_existing_agent_database_function_contract_completes_a_mysql_style_flow():
    student = repository.create_student("测试学生", "student-001", "一年级", "一班")
    assert student is not None
    assert repository.create_learning_profile(student.id) is not None

    question = repository.create_question(
        question_text="Linux 中查看当前目录使用什么命令？",
        subject="Linux 基础",
        difficulty=0.2,
        knowledge_points=json.dumps({"topic": "目录管理"}, ensure_ascii=False),
        options=json.dumps(["pwd", "cd"], ensure_ascii=False),
        correct_answer="pwd",
        answer_analysis="pwd 输出当前工作目录。",
    )
    assert question is not None
    assert repository.get_questions(topic="目录管理")[0].id == question.id

    exam = repository.create_exam_record(
        student_id=student.id,
        exam_name="Linux 基础练习",
        total_score=10,
        max_score=10,
    )
    assert exam is not None
    answer = repository.create_answer_record(
        exam_record_id=exam.id,
        question_id=question.id,
        student_answer="pwd",
        is_correct=True,
        score=10,
    )
    assert answer is not None

    assert repository.update_learning_profile(
        student.id,
        weak_points="[]",
        mastered_points='["目录管理"]',
        overall_score=100,
    )
    assert repository.update_knowledge_progress(student.id, "目录管理", 0.8)

    profile = repository.get_learning_profile(student.id)
    progress = repository.get_student_knowledge_progress(student.id)
    exams = repository.get_student_exams(student.id)
    assert profile is not None and profile.overall_score == 100
    assert progress[0].knowledge_point == "目录管理"
    assert exams[0].exam_name == "Linux 基础练习"
    assert repository.model_to_dict(question)["subject"] == "Linux 基础"


def test_legacy_tool_import_names_are_available():
    expected = {
        "create_answer_record",
        "create_exam_record",
        "create_question",
        "get_questions",
    }
    assert expected.issubset(set(repository.__all__))


def test_mysql_migration_is_additive_and_covers_all_agent_tables():
    migration = (
        PROJECT_ROOT / "migrations" / "001_mysql_ai_tables.sql"
    ).read_text(encoding="utf-8")
    normalized = migration.upper()

    assert "DROP TABLE" not in normalized
    assert "ALTER TABLE" not in normalized
    for table_name in Base.metadata.tables:
        assert f"CREATE TABLE IF NOT EXISTS `{table_name}`" in migration
