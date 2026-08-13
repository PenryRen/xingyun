"""MySQL ORM models owned by the AI Agent.

WebBE remains the source of truth for users, papers and official answers. These
``ai_*`` tables only store Agent-created profiles, knowledge progress, generated
questions and ad-hoc practice records, avoiding collisions with WebBE tables.
"""

from __future__ import annotations

from datetime import datetime
from typing import Optional

from sqlalchemy import (
    Boolean,
    DateTime,
    Float,
    ForeignKey,
    Index,
    Integer,
    String,
    Text,
    UniqueConstraint,
    func,
)
from sqlalchemy.orm import DeclarativeBase, Mapped, mapped_column


class Base(DeclarativeBase):
    pass


class Student(Base):
    __tablename__ = "ai_student"

    id: Mapped[int] = mapped_column(Integer, primary_key=True, autoincrement=True)
    name: Mapped[str] = mapped_column(String(128), nullable=False)
    student_id: Mapped[str] = mapped_column(String(64), nullable=False, unique=True)
    grade: Mapped[Optional[str]] = mapped_column(String(32))
    class_name: Mapped[Optional[str]] = mapped_column(String(64))
    created_at: Mapped[datetime] = mapped_column(DateTime, server_default=func.now())
    updated_at: Mapped[Optional[datetime]] = mapped_column(DateTime, onupdate=func.now())

    __table_args__ = (Index("ix_ai_student_student_id", "student_id"),)


class LearningProfile(Base):
    __tablename__ = "ai_learning_profile"

    id: Mapped[int] = mapped_column(Integer, primary_key=True, autoincrement=True)
    student_id: Mapped[int] = mapped_column(
        ForeignKey("ai_student.id", ondelete="CASCADE"), nullable=False, unique=True
    )
    weak_points: Mapped[str] = mapped_column(Text, nullable=False, default="[]")
    mastered_points: Mapped[str] = mapped_column(Text, nullable=False, default="[]")
    overall_score: Mapped[Optional[float]] = mapped_column(Float)
    last_update: Mapped[datetime] = mapped_column(DateTime, server_default=func.now())
    created_at: Mapped[datetime] = mapped_column(DateTime, server_default=func.now())


class StudentKnowledgeProgress(Base):
    __tablename__ = "ai_student_knowledge_progress"

    id: Mapped[int] = mapped_column(Integer, primary_key=True, autoincrement=True)
    student_id: Mapped[int] = mapped_column(
        ForeignKey("ai_student.id", ondelete="CASCADE"), nullable=False
    )
    knowledge_point: Mapped[str] = mapped_column(String(191), nullable=False)
    mastery_level: Mapped[float] = mapped_column(Float, nullable=False)
    last_practice_date: Mapped[Optional[datetime]] = mapped_column(DateTime)
    practice_count: Mapped[int] = mapped_column(Integer, nullable=False, default=0)
    correct_count: Mapped[int] = mapped_column(Integer, nullable=False, default=0)
    created_at: Mapped[datetime] = mapped_column(DateTime, server_default=func.now())
    updated_at: Mapped[Optional[datetime]] = mapped_column(DateTime, onupdate=func.now())

    __table_args__ = (
        UniqueConstraint(
            "student_id", "knowledge_point", name="uq_ai_progress_student_point"
        ),
        Index("ix_ai_progress_student_id", "student_id"),
    )


class Question(Base):
    __tablename__ = "ai_question"

    id: Mapped[int] = mapped_column(Integer, primary_key=True, autoincrement=True)
    question_text: Mapped[str] = mapped_column(Text, nullable=False)
    question_type: Mapped[str] = mapped_column(
        String(32), nullable=False, default="multiple_choice"
    )
    difficulty: Mapped[float] = mapped_column(Float, nullable=False)
    subject: Mapped[str] = mapped_column(String(128), nullable=False, default="general")
    knowledge_points: Mapped[Optional[str]] = mapped_column(Text)
    options: Mapped[Optional[str]] = mapped_column(Text)
    correct_answer: Mapped[str] = mapped_column(Text, nullable=False)
    answer_analysis: Mapped[Optional[str]] = mapped_column(Text)
    created_at: Mapped[datetime] = mapped_column(DateTime, server_default=func.now())

    __table_args__ = (
        Index("ix_ai_question_subject", "subject"),
        Index("ix_ai_question_difficulty", "difficulty"),
    )


class ExamRecord(Base):
    __tablename__ = "ai_exam_record"

    id: Mapped[int] = mapped_column(Integer, primary_key=True, autoincrement=True)
    student_id: Mapped[int] = mapped_column(
        ForeignKey("ai_student.id", ondelete="CASCADE"), nullable=False
    )
    exam_name: Mapped[str] = mapped_column(String(255), nullable=False)
    exam_date: Mapped[datetime] = mapped_column(DateTime, server_default=func.now())
    total_score: Mapped[float] = mapped_column(Float, nullable=False)
    max_score: Mapped[float] = mapped_column(Float, nullable=False)
    created_at: Mapped[datetime] = mapped_column(DateTime, server_default=func.now())

    __table_args__ = (
        Index("ix_ai_exam_student_id", "student_id"),
        Index("ix_ai_exam_date", "exam_date"),
    )


class AnswerRecord(Base):
    __tablename__ = "ai_answer_record"

    id: Mapped[int] = mapped_column(Integer, primary_key=True, autoincrement=True)
    exam_record_id: Mapped[int] = mapped_column(
        ForeignKey("ai_exam_record.id", ondelete="CASCADE"), nullable=False
    )
    question_id: Mapped[int] = mapped_column(
        ForeignKey("ai_question.id", ondelete="CASCADE"), nullable=False
    )
    student_answer: Mapped[str] = mapped_column(Text, nullable=False)
    is_correct: Mapped[bool] = mapped_column(Boolean, nullable=False)
    score: Mapped[float] = mapped_column(Float, nullable=False)
    created_at: Mapped[datetime] = mapped_column(DateTime, server_default=func.now())

    __table_args__ = (
        Index("ix_ai_answer_exam_id", "exam_record_id"),
        Index("ix_ai_answer_question_id", "question_id"),
    )


__all__ = [
    "AnswerRecord",
    "Base",
    "ExamRecord",
    "LearningProfile",
    "Question",
    "Student",
    "StudentKnowledgeProgress",
]
