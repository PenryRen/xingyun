"""
数据库工具 - 替代Supabase实现的数据库操作功能

使用达梦数据库实现原本由Supabase提供的功能
"""

import json
from datetime import datetime
from storage.database.db import get_session
from storage.database.shared.model import (
    Student,
    LearningProfile,
    StudentKnowledgeProgress,
    ExamRecord,
    AnswerRecord,
    Question
)
from sqlalchemy import desc


def get_student_by_student_id(student_id: str):
    """
    根据学号获取学生信息
    
    Args:
        student_id: 学号
        
    Returns:
        Student对象或None
    """
    session = get_session()
    try:
        student = session.query(Student).filter(Student.student_id == student_id).first()
        return student
    finally:
        session.close()


def create_student(student_name: str, student_id: str, grade: str = None, class_name: str = None):
    """
    创建学生记录
    
    Args:
        student_name: 学生姓名
        student_id: 学号
        grade: 年级
        class_name: 班级
        
    Returns:
        Student对象或None
    """
    session = get_session()
    try:
        # 检查学生是否已存在
        existing_student = session.query(Student).filter(Student.student_id == student_id).first()
        if existing_student:
            return None
        
        # 创建学生记录
        student = Student(
            name=student_name,
            student_id=student_id,
            grade=grade,
            class_name=class_name
        )
        session.add(student)
        session.commit()
        session.refresh(student)
        return student
    except Exception as e:
        session.rollback()
        print(f"创建学生失败: {e}")
        return None
    finally:
        session.close()


def create_learning_profile(student_id: int):
    """
    创建学情档案
    
    Args:
        student_id: 学生ID
        
    Returns:
        LearningProfile对象或None
    """
    session = get_session()
    try:
        profile = LearningProfile(
            student_id=student_id,
            weak_points="[]",  # JSON数组字符串
            mastered_points="[]"  # JSON数组字符串
        )
        session.add(profile)
        session.commit()
        session.refresh(profile)
        return profile
    except Exception as e:
        session.rollback()
        print(f"创建学情档案失败: {e}")
        return None
    finally:
        session.close()


def update_learning_profile(student_id: int, weak_points: str, mastered_points: str, overall_score: float = None):
    """
    更新学情档案
    
    Args:
        student_id: 学生ID
        weak_points: 薄弱知识点（JSON数组字符串）
        mastered_points: 已掌握知识点（JSON数组字符串）
        overall_score: 综合评分
        
    Returns:
        bool: 是否更新成功
    """
    session = get_session()
    try:
        profile = session.query(LearningProfile).filter(LearningProfile.student_id == student_id).first()
        if not profile:
            return False
        
        profile.weak_points = weak_points
        profile.mastered_points = mastered_points
        profile.last_update = datetime.now()
        if overall_score is not None:
            profile.overall_score = overall_score
        
        session.commit()
        return True
    except Exception as e:
        session.rollback()
        print(f"更新学情档案失败: {e}")
        return False
    finally:
        session.close()


def get_learning_profile(student_id: int):
    """
    获取学情档案
    
    Args:
        student_id: 学生ID
        
    Returns:
        LearningProfile对象或None
    """
    session = get_session()
    try:
        profile = session.query(LearningProfile).filter(LearningProfile.student_id == student_id).first()
        return profile
    finally:
        session.close()


def get_student_exams(student_id: int, limit: int = 10):
    """
    获取学生考试记录
    
    Args:
        student_id: 学生ID
        limit: 限制数量
        
    Returns:
        List[ExamRecord]
    """
    session = get_session()
    try:
        exams = session.query(ExamRecord).filter(
            ExamRecord.student_id == student_id
        ).order_by(
            desc(ExamRecord.exam_date)
        ).limit(limit).all()
        return exams
    finally:
        session.close()


def get_student_knowledge_progress(student_id: int, knowledge_point: str = None):
    """
    获取学生知识点进度
    
    Args:
        student_id: 学生ID
        knowledge_point: 知识点名称（可选）
        
    Returns:
        List[StudentKnowledgeProgress]
    """
    session = get_session()
    try:
        query = session.query(StudentKnowledgeProgress).filter(
            StudentKnowledgeProgress.student_id == student_id
        )
        
        if knowledge_point:
            query = query.filter(StudentKnowledgeProgress.knowledge_point == knowledge_point)
        
        progress = query.order_by(
            StudentKnowledgeProgress.mastery_level
        ).all()
        return progress
    finally:
        session.close()


def update_knowledge_progress(student_id: int, knowledge_point: str, mastery_level: float):
    """
    更新知识点进度
    
    Args:
        student_id: 学生ID
        knowledge_point: 知识点名称
        mastery_level: 掌握程度（0.0-1.0）
        
    Returns:
        bool: 是否更新成功
    """
    session = get_session()
    try:
        # 检查是否已存在该知识点进度记录
        progress = session.query(StudentKnowledgeProgress).filter(
            StudentKnowledgeProgress.student_id == student_id,
            StudentKnowledgeProgress.knowledge_point == knowledge_point
        ).first()
        
        if progress:
            # 更新现有记录
            current_mastery = progress.mastery_level or 0
            practice_count = progress.practice_count or 0
            correct_count = progress.correct_count or 0
            
            # 更新掌握程度（加权平均）
            new_mastery = (current_mastery * practice_count + mastery_level) / (practice_count + 1)
            
            progress.mastery_level = round(new_mastery, 3)
            progress.last_practice_date = datetime.now()
            progress.practice_count = practice_count + 1
            progress.correct_count = correct_count + (1 if mastery_level >= 0.6 else 0)
        else:
            # 创建新记录
            progress = StudentKnowledgeProgress(
                student_id=student_id,
                knowledge_point=knowledge_point,
                mastery_level=mastery_level,
                last_practice_date=datetime.now(),
                practice_count=1,
                correct_count=1 if mastery_level >= 0.6 else 0
            )
            session.add(progress)
        
        session.commit()
        return True
    except Exception as e:
        session.rollback()
        print(f"更新知识点进度失败: {e}")
        return False
    finally:
        session.close()


def get_question_bank(category: str = None, difficulty_min: float = 0, difficulty_max: float = 1.0, limit: int = 100):
    """
    获取题库
    
    Args:
        category: 题目分类（可选）
        difficulty_min: 最小难度
        difficulty_max: 最大难度
        limit: 限制数量
        
    Returns:
        List[Question]
    """
    session = get_session()
    try:
        query = session.query(Question).filter(
            Question.difficulty >= difficulty_min,
            Question.difficulty <= difficulty_max
        )
        
        if category:
            query = query.filter(Question.category == category)
        
        questions = query.limit(limit).all()
        return questions
    finally:
        session.close()


def get_questions(topic: str = None, subject: str = None, limit: int = 100):
    """
    获取题目列表
    
    Args:
        topic: 主题关键词（可选）
        subject: 科目/分类（可选）
        limit: 限制数量
        
    Returns:
        List[Question]
    """
    session = get_session()
    try:
        query = session.query(Question)
        
        if topic:
            # 使用模糊查询
            query = query.filter(Question.category.like(f"%{topic}%"))
        
        if subject:
            query = query.filter(Question.category == subject)
        
        questions = query.limit(limit).all()
        return questions
    finally:
        session.close()


def create_question(question_text: str, question_type: str = None, difficulty: float = 0.5, 
                   subject: str = None, knowledge_points: str = None, options: str = None,
                   correct_answer: str = None, answer_analysis: str = None, created_at: datetime = None):
    """
    创建题目
    
    Args:
        question_text: 题目内容
        question_type: 题目类型
        difficulty: 难度系数（0.0-1.0）
        subject: 科目/分类
        knowledge_points: 知识点（JSON 字符串）
        options: 选项（JSON 字符串）
        correct_answer: 正确答案
        answer_analysis: 答案解析
        created_at: 创建时间
        
    Returns:
        Question 对象或 None
    """
    session = get_session()
    try:
        question = Question(
            question_text=question_text,
            category=subject or "general",
            difficulty=difficulty,
            answer=correct_answer or "",
            explanation=answer_analysis,
            tags=knowledge_points
        )
        session.add(question)
        session.commit()
        session.refresh(question)
        return question
    except Exception as e:
        session.rollback()
        print(f"创建题目失败：{e}")
        return None
    finally:
        session.close()


def create_answer_record(exam_record_id: int, question_id: int, student_answer: str, 
                        is_correct: bool, score: float):
    """
    创建答题记录
    
    Args:
        exam_record_id: 考试记录 ID
        question_id: 题目 ID
        student_answer: 学生答案
        is_correct: 是否正确
        score: 得分
        
    Returns:
        AnswerRecord 对象或 None
    """
    session = get_session()
    try:
        answer_record = AnswerRecord(
            exam_record_id=exam_record_id,
            question_id=question_id,
            student_answer=student_answer,
            is_correct=is_correct,
            score=score
        )
        session.add(answer_record)
        session.commit()
        session.refresh(answer_record)
        return answer_record
    except Exception as e:
        session.rollback()
        print(f"创建答题记录失败：{e}")
        return None
    finally:
        session.close()


def create_exam_record(student_id: int, exam_name: str, total_score: float, 
                      max_score: float, exam_date: datetime = None):
    """
    创建考试记录
    
    Args:
        student_id: 学生 ID
        exam_name: 考试名称
        total_score: 学生得分
        max_score: 满分
        exam_date: 考试日期
        
    Returns:
        ExamRecord 对象或 None
    """
    session = get_session()
    try:
        exam_record = ExamRecord(
            student_id=student_id,
            exam_name=exam_name,
            total_score=total_score,
            max_score=max_score,
            exam_date=exam_date or datetime.now()
        )
        session.add(exam_record)
        session.commit()
        session.refresh(exam_record)
        return exam_record
    except Exception as e:
        session.rollback()
        print(f"创建考试记录失败：{e}")
        return None
    finally:
        session.close()


def add_question(question_text: str, category: str, difficulty: float, answer: str, explanation: str = None, tags: str = None):
    """
    添加题目到题库
    
    Args:
        question_text: 题目内容
        category: 题目分类
        difficulty: 难度系数（0.0-1.0）
        answer: 正确答案
        explanation: 题目解析
        tags: 标签（JSON字符串）
        
    Returns:
        Question对象或None
    """
    session = get_session()
    try:
        question = Question(
            question_text=question_text,
            category=category,
            difficulty=difficulty,
            answer=answer,
            explanation=explanation,
            tags=tags
        )
        session.add(question)
        session.commit()
        session.refresh(question)
        return question
    except Exception as e:
        session.rollback()
        print(f"添加题目失败: {e}")
        return None
    finally:
        session.close()


def add_exam_record(student_id: int, exam_name: str, total_score: float, max_score: float):
    """
    添加考试记录
    
    Args:
        student_id: 学生ID
        exam_name: 考试名称
        total_score: 学生得分
        max_score: 满分
        
    Returns:
        ExamRecord对象或None
    """
    session = get_session()
    try:
        exam_record = ExamRecord(
            student_id=student_id,
            exam_name=exam_name,
            total_score=total_score,
            max_score=max_score
        )
        session.add(exam_record)
        session.commit()
        session.refresh(exam_record)
        return exam_record
    except Exception as e:
        session.rollback()
        print(f"添加考试记录失败: {e}")
        return None
    finally:
        session.close()


def add_answer_record(exam_record_id: int, question_id: int, student_answer: str, is_correct: bool, score: float):
    """
    添加答题记录
    
    Args:
        exam_record_id: 考试记录ID
        question_id: 题目ID
        student_answer: 学生答案
        is_correct: 是否正确
        score: 得分
        
    Returns:
        AnswerRecord对象或None
    """
    session = get_session()
    try:
        answer_record = AnswerRecord(
            exam_record_id=exam_record_id,
            question_id=question_id,
            student_answer=student_answer,
            is_correct=is_correct,
            score=score
        )
        session.add(answer_record)
        session.commit()
        session.refresh(answer_record)
        return answer_record
    except Exception as e:
        session.rollback()
        print(f"添加答题记录失败: {e}")
        return None
    finally:
        session.close()


def model_to_dict(model):
    """
    将SQLAlchemy模型转换为字典
    
    Args:
        model: SQLAlchemy模型实例
        
    Returns:
        dict
    """
    if not model:
        return {}
    
    result = {}
    for column in model.__table__.columns:
        value = getattr(model, column.name)
        if isinstance(value, datetime):
            result[column.name] = value.isoformat()
        else:
            result[column.name] = value
    return result
