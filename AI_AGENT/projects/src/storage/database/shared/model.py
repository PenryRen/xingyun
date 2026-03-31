from sqlalchemy.ext.declarative import declarative_base

from sqlalchemy import BigInteger, Boolean, Column, DateTime, Double, Integer, Numeric, PrimaryKeyConstraint, Table, Text, text, String, Float, ForeignKey, JSON, func, Index
from sqlalchemy.orm import relationship
from typing import Optional, List
import datetime

from sqlalchemy.orm import Mapped, mapped_column

# 创建基础模型类
Base = declarative_base()

class HealthCheck(Base):
    __tablename__ = 'health_check'
    __table_args__ = (
        PrimaryKeyConstraint('id', name='health_check_pkey'),
    )

    id: Mapped[int] = mapped_column(Integer, primary_key=True)
    updated_at: Mapped[Optional[datetime.datetime]] = mapped_column(DateTime(True), server_default=text('now()'))


t_pg_stat_statements = Table(
    'pg_stat_statements', Base.metadata,
    Column('userid', OID),
    Column('dbid', OID),
    Column('toplevel', Boolean),
    Column('queryid', BigInteger),
    Column('query', Text),
    Column('plans', BigInteger),
    Column('total_plan_time', Double(53)),
    Column('min_plan_time', Double(53)),
    Column('max_plan_time', Double(53)),
    Column('mean_plan_time', Double(53)),
    Column('stddev_plan_time', Double(53)),
    Column('calls', BigInteger),
    Column('total_exec_time', Double(53)),
    Column('min_exec_time', Double(53)),
    Column('max_exec_time', Double(53)),
    Column('mean_exec_time', Double(53)),
    Column('stddev_exec_time', Double(53)),
    Column('rows', BigInteger),
    Column('shared_blks_hit', BigInteger),
    Column('shared_blks_read', BigInteger),
    Column('shared_blks_dirtied', BigInteger),
    Column('shared_blks_written', BigInteger),
    Column('local_blks_hit', BigInteger),
    Column('local_blks_read', BigInteger),
    Column('local_blks_dirtied', BigInteger),
    Column('local_blks_written', BigInteger),
    Column('temp_blks_read', BigInteger),
    Column('temp_blks_written', BigInteger),
    Column('shared_blk_read_time', Double(53)),
    Column('shared_blk_write_time', Double(53)),
    Column('local_blk_read_time', Double(53)),
    Column('local_blk_write_time', Double(53)),
    Column('temp_blk_read_time', Double(53)),
    Column('temp_blk_write_time', Double(53)),
    Column('wal_records', BigInteger),
    Column('wal_fpi', BigInteger),
    Column('wal_bytes', Numeric),
    Column('jit_functions', BigInteger),
    Column('jit_generation_time', Double(53)),
    Column('jit_inlining_count', BigInteger),
    Column('jit_inlining_time', Double(53)),
    Column('jit_optimization_count', BigInteger),
    Column('jit_optimization_time', Double(53)),
    Column('jit_emission_count', BigInteger),
    Column('jit_emission_time', Double(53)),
    Column('jit_deform_count', BigInteger),
    Column('jit_deform_time', Double(53)),
    Column('stats_since', DateTime(True)),
    Column('minmax_stats_since', DateTime(True))
)


t_pg_stat_statements_info = Table(
    'pg_stat_statements_info', Base.metadata,
    Column('dealloc', BigInteger),
    Column('stats_reset', DateTime(True))
)

# ==================== 教育平台表结构 ====================

class Student(Base):
    """学生信息表"""
    __tablename__ = 'students'
    
    id: Mapped[int] = mapped_column(Integer, primary_key=True, comment="学生ID")
    name: Mapped[str] = mapped_column(String(128), nullable=False, comment="学生姓名")
    student_id: Mapped[str] = mapped_column(String(64), unique=True, nullable=False, comment="学号")
    grade: Mapped[Optional[str]] = mapped_column(String(32), nullable=True, comment="年级")
    class_name: Mapped[Optional[str]] = mapped_column(String(32), nullable=True, comment="班级")
    created_at: Mapped[datetime.datetime] = mapped_column(DateTime(timezone=True), server_default=func.now(), nullable=False, comment="创建时间")
    updated_at: Mapped[Optional[datetime.datetime]] = mapped_column(DateTime(timezone=True), onupdate=func.now(), nullable=True, comment="更新时间")
    
    # 关系
    exam_records: Mapped[list["ExamRecord"]] = relationship("ExamRecord", back_populates="student")
    learning_profile: Mapped[Optional["LearningProfile"]] = relationship("LearningProfile", back_populates="student", uselist=False)
    knowledge_progress: Mapped[list["StudentKnowledgeProgress"]] = relationship("StudentKnowledgeProgress", back_populates="student")
    
    __table_args__ = (
        Index('ix_students_student_id', 'student_id'),
    )


class Question(Base):
    """题库表"""
    __tablename__ = 'questions'
    
    id: Mapped[int] = mapped_column(Integer, primary_key=True, comment="题目ID")
    question_text: Mapped[str] = mapped_column(Text, nullable=False, comment="题目内容")
    category: Mapped[str] = mapped_column(String(128), nullable=False, comment="题目分类")
    difficulty: Mapped[float] = mapped_column(Float, nullable=False, comment="难度系数(0.0-1.0)")
    answer: Mapped[str] = mapped_column(Text, nullable=False, comment="正确答案")
    explanation: Mapped[Optional[str]] = mapped_column(Text, nullable=True, comment="题目解析")
    tags: Mapped[Optional[dict]] = mapped_column(Text, nullable=True, comment="标签(JSON格式)")
    created_at: Mapped[datetime.datetime] = mapped_column(DateTime(timezone=True), server_default=func.now(), nullable=False, comment="创建时间")
    
    # 关系
    answer_records: Mapped[list["AnswerRecord"]] = relationship("AnswerRecord", back_populates="question")
    
    __table_args__ = (
        Index('ix_questions_category', 'category'),
        Index('ix_questions_difficulty', 'difficulty'),
    )


class ExamRecord(Base):
    """考试记录表"""
    __tablename__ = 'exam_records'
    
    id: Mapped[int] = mapped_column(Integer, primary_key=True, comment="考试记录ID")
    student_id: Mapped[int] = mapped_column(Integer, ForeignKey('students.id'), nullable=False, comment="学生ID")
    exam_name: Mapped[str] = mapped_column(String(128), nullable=False, comment="考试名称")
    exam_date: Mapped[datetime.datetime] = mapped_column(DateTime(timezone=True), server_default=func.now(), nullable=False, comment="考试日期")
    total_score: Mapped[float] = mapped_column(Float, nullable=False, comment="学生得分")
    max_score: Mapped[float] = mapped_column(Float, nullable=False, comment="满分")
    created_at: Mapped[datetime.datetime] = mapped_column(DateTime(timezone=True), server_default=func.now(), nullable=False, comment="创建时间")
    
    # 关系
    student: Mapped["Student"] = relationship("Student", back_populates="exam_records")
    answer_records: Mapped[list["AnswerRecord"]] = relationship("AnswerRecord", back_populates="exam_record")
    
    __table_args__ = (
        Index('ix_exam_records_student_id', 'student_id'),
        Index('ix_exam_records_exam_date', 'exam_date'),
    )


class AnswerRecord(Base):
    """答题记录表"""
    __tablename__ = 'answer_records'
    
    id: Mapped[int] = mapped_column(Integer, primary_key=True, comment="答题记录ID")
    exam_record_id: Mapped[int] = mapped_column(Integer, ForeignKey('exam_records.id'), nullable=False, comment="考试记录ID")
    question_id: Mapped[int] = mapped_column(Integer, ForeignKey('questions.id'), nullable=False, comment="题目ID")
    student_answer: Mapped[str] = mapped_column(Text, nullable=False, comment="学生答案")
    is_correct: Mapped[bool] = mapped_column(Boolean, nullable=False, comment="是否正确")
    score: Mapped[float] = mapped_column(Float, nullable=False, comment="得分")
    created_at: Mapped[datetime.datetime] = mapped_column(DateTime(timezone=True), server_default=func.now(), nullable=False, comment="创建时间")
    
    # 关系
    exam_record: Mapped["ExamRecord"] = relationship("ExamRecord", back_populates="answer_records")
    question: Mapped["Question"] = relationship("Question", back_populates="answer_records")
    
    __table_args__ = (
        Index('ix_answer_records_exam_record_id', 'exam_record_id'),
        Index('ix_answer_records_question_id', 'question_id'),
    )


class LearningProfile(Base):
    """学情档案表"""
    __tablename__ = 'learning_profiles'
    
    id: Mapped[int] = mapped_column(Integer, primary_key=True, comment="学情档案ID")
    student_id: Mapped[int] = mapped_column(Integer, ForeignKey('students.id'), unique=True, nullable=False, comment="学生ID")
    weak_points: Mapped[Optional[str]] = mapped_column(Text, nullable=False, comment="薄弱知识点(JSON数组)")
    mastered_points: Mapped[Optional[str]] = mapped_column(Text, nullable=False, comment="已掌握知识点(JSON数组)")
    overall_score: Mapped[Optional[float]] = mapped_column(Float, nullable=True, comment="综合评分(0.0-100.0)")
    last_update: Mapped[datetime.datetime] = mapped_column(DateTime(timezone=True), server_default=func.now(), nullable=False, comment="最后更新时间")
    created_at: Mapped[datetime.datetime] = mapped_column(DateTime(timezone=True), server_default=func.now(), nullable=False, comment="创建时间")
    
    # 关系
    student: Mapped["Student"] = relationship("Student", back_populates="learning_profile")
    
    __table_args__ = (
        Index('ix_learning_profiles_student_id', 'student_id'),
    )


class StudentKnowledgeProgress(Base):
    """学生知识点进度表"""
    __tablename__ = 'student_knowledge_progress'
    
    id: Mapped[int] = mapped_column(Integer, primary_key=True, comment="进度ID")
    student_id: Mapped[int] = mapped_column(Integer, ForeignKey('students.id'), nullable=False, comment="学生ID")
    knowledge_point: Mapped[str] = mapped_column(String(128), nullable=False, comment="知识点名称")
    mastery_level: Mapped[float] = mapped_column(Float, nullable=False, comment="掌握程度(0.0-1.0)")
    last_practice_date: Mapped[Optional[datetime.datetime]] = mapped_column(DateTime(timezone=True), nullable=True, comment="最后练习日期")
    practice_count: Mapped[int] = mapped_column(Integer, nullable=False, server_default="0", comment="练习次数")
    correct_count: Mapped[int] = mapped_column(Integer, nullable=False, server_default="0", comment="正确次数")
    created_at: Mapped[datetime.datetime] = mapped_column(DateTime(timezone=True), server_default=func.now(), nullable=False, comment="创建时间")
    updated_at: Mapped[Optional[datetime.datetime]] = mapped_column(DateTime(timezone=True), onupdate=func.now(), nullable=True, comment="更新时间")
    
    # 关系
    student: Mapped["Student"] = relationship("Student", back_populates="knowledge_progress")
    
    __table_args__ = (
        Index('ix_student_knowledge_progress_student_id', 'student_id'),
        Index('ix_student_knowledge_progress_knowledge_point', 'knowledge_point'),
    )
