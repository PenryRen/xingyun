"""MySQL repositories used by the existing multi-Agent tools.

The public function names intentionally remain compatible with the original
tools. All persistence now goes through SQLAlchemy/PyMySQL into isolated
``ai_*`` tables in the project's existing ``wdd`` MySQL database.
"""

from __future__ import annotations

import logging
from datetime import date, datetime
from typing import Any, Optional

from sqlalchemy import inspect, or_
from sqlalchemy.exc import SQLAlchemyError

from storage.database.db import get_session
from storage.database.shared.model import (
    AnswerRecord,
    ExamRecord,
    LearningProfile,
    Question,
    Student,
    StudentKnowledgeProgress,
)


logger = logging.getLogger(__name__)


def get_student_by_student_id(student_id: str) -> Optional[Student]:
    session = get_session()
    try:
        return (
            session.query(Student)
            .filter(Student.student_id == str(student_id).strip())
            .first()
        )
    finally:
        session.close()


def create_student(
    student_name: str,
    student_id: str,
    grade: str | None = None,
    class_name: str | None = None,
) -> Optional[Student]:
    normalized_id = str(student_id).strip()
    normalized_name = str(student_name).strip()
    if not normalized_id or not normalized_name:
        return None

    session = get_session()
    try:
        existing = (
            session.query(Student).filter(Student.student_id == normalized_id).first()
        )
        if existing:
            return existing

        student = Student(
            name=normalized_name,
            student_id=normalized_id,
            grade=grade,
            class_name=class_name,
        )
        session.add(student)
        session.commit()
        return student
    except SQLAlchemyError:
        session.rollback()
        logger.exception("Failed to create AI student record")
        return None
    finally:
        session.close()


def create_learning_profile(student_id: int) -> Optional[LearningProfile]:
    session = get_session()
    try:
        existing = (
            session.query(LearningProfile)
            .filter(LearningProfile.student_id == student_id)
            .first()
        )
        if existing:
            return existing

        profile = LearningProfile(
            student_id=student_id,
            weak_points="[]",
            mastered_points="[]",
        )
        session.add(profile)
        session.commit()
        return profile
    except SQLAlchemyError:
        session.rollback()
        logger.exception("Failed to create AI learning profile")
        return None
    finally:
        session.close()


def update_learning_profile(
    student_id: int,
    weak_points: str,
    mastered_points: str,
    overall_score: float | None = None,
) -> bool:
    session = get_session()
    try:
        profile = (
            session.query(LearningProfile)
            .filter(LearningProfile.student_id == student_id)
            .first()
        )
        if profile is None:
            profile = LearningProfile(student_id=student_id)
            session.add(profile)

        profile.weak_points = weak_points
        profile.mastered_points = mastered_points
        profile.last_update = datetime.now()
        if overall_score is not None:
            profile.overall_score = overall_score
        session.commit()
        return True
    except SQLAlchemyError:
        session.rollback()
        logger.exception("Failed to update AI learning profile")
        return False
    finally:
        session.close()


def get_learning_profile(student_id: int) -> Optional[LearningProfile]:
    session = get_session()
    try:
        return (
            session.query(LearningProfile)
            .filter(LearningProfile.student_id == student_id)
            .first()
        )
    finally:
        session.close()


def get_student_exams(student_id: int, limit: int = 10) -> list[ExamRecord]:
    safe_limit = max(1, min(int(limit), 500))
    session = get_session()
    try:
        return (
            session.query(ExamRecord)
            .filter(ExamRecord.student_id == student_id)
            .order_by(ExamRecord.exam_date.desc(), ExamRecord.id.desc())
            .limit(safe_limit)
            .all()
        )
    finally:
        session.close()


def get_student_knowledge_progress(
    student_id: int, knowledge_point: str | None = None
) -> list[StudentKnowledgeProgress]:
    session = get_session()
    try:
        query = session.query(StudentKnowledgeProgress).filter(
            StudentKnowledgeProgress.student_id == student_id
        )
        if knowledge_point:
            query = query.filter(
                StudentKnowledgeProgress.knowledge_point == knowledge_point
            )
        return query.order_by(StudentKnowledgeProgress.mastery_level).all()
    finally:
        session.close()


def update_knowledge_progress(
    student_id: int, knowledge_point: str, mastery_level: float
) -> bool:
    normalized_point = str(knowledge_point).strip()
    if not normalized_point:
        return False
    normalized_level = max(0.0, min(float(mastery_level), 1.0))

    session = get_session()
    try:
        progress = (
            session.query(StudentKnowledgeProgress)
            .filter(
                StudentKnowledgeProgress.student_id == student_id,
                StudentKnowledgeProgress.knowledge_point == normalized_point,
            )
            .first()
        )
        if progress:
            practice_count = progress.practice_count or 0
            current_mastery = progress.mastery_level or 0.0
            progress.mastery_level = round(
                (current_mastery * practice_count + normalized_level)
                / (practice_count + 1),
                3,
            )
            progress.practice_count = practice_count + 1
            progress.correct_count = (progress.correct_count or 0) + (
                1 if normalized_level >= 0.6 else 0
            )
            progress.last_practice_date = datetime.now()
        else:
            progress = StudentKnowledgeProgress(
                student_id=student_id,
                knowledge_point=normalized_point,
                mastery_level=normalized_level,
                last_practice_date=datetime.now(),
                practice_count=1,
                correct_count=1 if normalized_level >= 0.6 else 0,
            )
            session.add(progress)
        session.commit()
        return True
    except SQLAlchemyError:
        session.rollback()
        logger.exception("Failed to update AI knowledge progress")
        return False
    finally:
        session.close()


def create_question(
    question_text: str,
    question_type: str = "multiple_choice",
    difficulty: float = 0.5,
    subject: str = "general",
    knowledge_points: str | None = None,
    options: str | None = None,
    correct_answer: str = "",
    answer_analysis: str | None = None,
    created_at: datetime | None = None,
) -> Optional[Question]:
    if not str(question_text).strip() or not str(correct_answer).strip():
        return None

    session = get_session()
    try:
        question = Question(
            question_text=str(question_text).strip(),
            question_type=str(question_type or "multiple_choice"),
            difficulty=max(0.0, min(float(difficulty), 1.0)),
            subject=str(subject or "general"),
            knowledge_points=knowledge_points,
            options=options,
            correct_answer=str(correct_answer),
            answer_analysis=answer_analysis,
        )
        if created_at is not None:
            question.created_at = created_at
        session.add(question)
        session.commit()
        return question
    except (SQLAlchemyError, ValueError, TypeError):
        session.rollback()
        logger.exception("Failed to create AI question")
        return None
    finally:
        session.close()


def add_question(
    question_text: str,
    category: str,
    difficulty: float,
    answer: str,
    explanation: str | None = None,
    tags: str | None = None,
) -> Optional[Question]:
    return create_question(
        question_text=question_text,
        subject=category,
        difficulty=difficulty,
        correct_answer=answer,
        answer_analysis=explanation,
        knowledge_points=tags,
    )


def get_questions(
    topic: str | None = None,
    subject: str | None = None,
    limit: int = 100,
    difficulty_min: float = 0.0,
    difficulty_max: float = 1.0,
) -> list[Question]:
    safe_limit = max(1, min(int(limit), 500))
    session = get_session()
    try:
        query = session.query(Question).filter(
            Question.difficulty >= float(difficulty_min),
            Question.difficulty <= float(difficulty_max),
        )
        if subject:
            query = query.filter(Question.subject == subject)
        if topic:
            query = query.filter(
                or_(
                    Question.question_text.contains(topic, autoescape=True),
                    Question.subject.contains(topic, autoescape=True),
                    Question.knowledge_points.contains(topic, autoescape=True),
                )
            )
        return query.order_by(Question.id.desc()).limit(safe_limit).all()
    finally:
        session.close()


def get_question_bank(
    category: str | None = None,
    difficulty_min: float = 0.0,
    difficulty_max: float = 1.0,
    limit: int = 100,
) -> list[Question]:
    return get_questions(
        subject=category,
        difficulty_min=difficulty_min,
        difficulty_max=difficulty_max,
        limit=limit,
    )


def create_exam_record(
    student_id: int,
    exam_name: str,
    total_score: float,
    max_score: float,
    exam_date: datetime | None = None,
) -> Optional[ExamRecord]:
    session = get_session()
    try:
        record = ExamRecord(
            student_id=student_id,
            exam_name=str(exam_name).strip(),
            total_score=float(total_score),
            max_score=float(max_score),
        )
        if exam_date is not None:
            record.exam_date = exam_date
        session.add(record)
        session.commit()
        return record
    except (SQLAlchemyError, ValueError, TypeError):
        session.rollback()
        logger.exception("Failed to create AI exam record")
        return None
    finally:
        session.close()


def add_exam_record(
    student_id: int, exam_name: str, total_score: float, max_score: float
) -> Optional[ExamRecord]:
    return create_exam_record(student_id, exam_name, total_score, max_score)


def create_answer_record(
    exam_record_id: int,
    question_id: int,
    student_answer: str,
    is_correct: bool,
    score: float,
) -> Optional[AnswerRecord]:
    session = get_session()
    try:
        record = AnswerRecord(
            exam_record_id=exam_record_id,
            question_id=question_id,
            student_answer=str(student_answer),
            is_correct=bool(is_correct),
            score=float(score),
        )
        session.add(record)
        session.commit()
        return record
    except (SQLAlchemyError, ValueError, TypeError):
        session.rollback()
        logger.exception("Failed to create AI answer record")
        return None
    finally:
        session.close()


def add_answer_record(
    exam_record_id: int,
    question_id: int,
    student_answer: str,
    is_correct: bool,
    score: float,
) -> Optional[AnswerRecord]:
    return create_answer_record(
        exam_record_id, question_id, student_answer, is_correct, score
    )


# 别名：兼容旧函数名
create_question = add_question
create_exam_record = add_exam_record
get_questions = get_question_bank


def model_to_dict(model: Any) -> dict[str, Any]:
    """将 SQLAlchemy 模型实例转换为字典"""
    if model is None:
        return {}
    if isinstance(model, dict):
        return dict(model)

    result: dict[str, Any] = {}
    for attribute in inspect(model).mapper.column_attrs:
        value = getattr(model, attribute.key)
        if isinstance(value, (datetime, date)):
            result[attribute.key] = value.isoformat()
        else:
            result[attribute.key] = value
    return result


__all__ = [
    "add_answer_record",
    "add_exam_record",
    "add_question",
    "create_answer_record",
    "create_exam_record",
    "create_learning_profile",
    "create_question",
    "create_student",
    "get_learning_profile",
    "get_question_bank",
    "get_questions",
    "get_student_by_student_id",
    "get_student_exams",
    "get_student_knowledge_progress",
    "model_to_dict",
    "update_knowledge_progress",
    "update_learning_profile",
]
