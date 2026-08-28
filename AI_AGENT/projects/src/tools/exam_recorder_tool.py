"""
考试记录工具 - 记录学生的考试成绩和分析
"""
import json
from datetime import datetime
from langchain.tools import tool
from langchain.tools import ToolRuntime
from tools.data_base_tools import (
    get_student_by_student_id,
    create_exam_record,
    get_student_exams,
    model_to_dict
)


@tool
def add_questions_to_database(questions_data: str, runtime: ToolRuntime = None) -> str:
    """
    批量添加题目到题库
    
    参数:
        questions_data: 题目数据，JSON数组格式，每个元素包含题目信息
    
    返回:
        添加结果
    """
    try:
        questions_list = json.loads(questions_data)
    except json.JSONDecodeError:
        return "错误：题目数据格式不正确，必须是JSON数组格式"
    
    if not isinstance(questions_list, list):
        return "错误：题目数据必须是数组格式"
    
    from tools.data_base_tools import create_question
    success_count = 0
    failed_questions = []
    
    for q in questions_list:
        # 验证必要字段
        required_fields = ['question_text', 'category', 'difficulty', 'answer']
        if not all(field in q for field in required_fields):
            failed_questions.append({
                "question_text": q.get('question_text', '')[:50],
                "reason": "缺少必要字段"
            })
            continue
        
        # 插入题目
        try:
            question_data = {
                "question_text": q['question_text'],
                "question_type": q.get('question_type', 'multiple_choice'),
                "difficulty": q['difficulty'],
                "subject": q.get('category', 'general'),
                "knowledge_points": json.dumps(q.get('tags', {})),
                "options": json.dumps(q.get('options', [])),
                "correct_answer": q['answer'],
                "answer_analysis": q.get('explanation', ''),
                "created_at": datetime.now()
            }
            
            result = create_question(**question_data)
            if result:
                success_count += 1
            else:
                failed_questions.append({
                    "question_text": q.get('question_text', '')[:50],
                    "reason": "添加失败"
                })
        except Exception as e:
            failed_questions.append({
                "question_text": q.get('question_text', '')[:50],
                "reason": str(e)
            })
    
    result = {
        "total": len(questions_list),
        "success": success_count,
        "failed": len(failed_questions),
        "failed_questions": failed_questions
    }
    
    return json.dumps(result, ensure_ascii=False, indent=2)


@tool
def record_exam_result(
    student_name: str,
    student_id: str,
    exam_name: str,
    total_score: float,
    max_score: float,
    exam_data: str,
    runtime: ToolRuntime = None
) -> str:
    """
    记录考试结果到数据库
    
    参数:
        student_name: 学生姓名
        student_id: 学号
        exam_name: 考试名称
        total_score: 学生得分
        max_score: 满分
        exam_data: 试卷数据，JSON格式，包含题目和学生答案
    
    返回:
        记录结果
    """
    try:
        exam_info = json.loads(exam_data)
    except json.JSONDecodeError:
        return "错误：试卷数据格式不正确，必须是JSON格式"
    
    # 查询或创建学生
    student = get_student_by_student_id(student_id)
    
    if not student:
        # 创建学生记录
        from tools.data_base_tools import create_student, create_learning_profile
        student = create_student(
            student_name=student_name,
            student_id=student_id
        )
        if not student:
            return "创建学生记录失败"
        
        # 创建学情档案
        profile = create_learning_profile(student.id)
        if not profile:
            return "创建学情档案失败"
    
    # 创建考试记录
    exam_record = create_exam_record(
        student_id=student.id,
        exam_name=exam_name,
        total_score=total_score,
        max_score=max_score,
        exam_date=datetime.now()
    )
    
    if not exam_record:
        return "创建考试记录失败"
    
    # 记录答题记录
    answer_records_count = 0
    
    # 假设 exam_data 是一个数组，每个元素包含题目ID和学生答案
    if isinstance(exam_info, list):
        from tools.data_base_tools import create_answer_record
        for item in exam_info:
            question_id = item.get('question_id')
            student_answer = str(item.get('student_answer', ''))
            correct_answer = str(item.get('correct_answer', ''))
            score = item.get('score', 0)
            
            if question_id:
                is_correct = str(student_answer).strip().lower() == str(correct_answer).strip().lower()
                
                answer_record = create_answer_record(
                    exam_record_id=exam_record.id,
                    question_id=question_id,
                    student_answer=student_answer,
                    is_correct=is_correct,
                    score=score
                )
                if answer_record:
                    answer_records_count += 1
    
    result = {
        "exam_record_id": exam_record.id,
        "exam_name": exam_name,
        "student_id": student_id,
        "total_score": f"{total_score}/{max_score}",
        "answer_records_count": answer_records_count
    }
    
    return json.dumps(result, ensure_ascii=False, indent=2)


@tool
def get_question_bank(topic: str = None, category: str = None, runtime: ToolRuntime = None) -> str:
    """
    查询题库
    
    参数:
        topic: 主题关键词（可选）
        category: 分类（可选）
    
    返回:
        题目列表
    """
    from tools.data_base_tools import get_questions
    
    # 查询题目
    questions = get_questions(topic=topic, subject=category, limit=100)
    question_data = [model_to_dict(q) for q in questions]
    
    # 解析JSON字段
    for q in question_data:
        if 'knowledge_points' in q and q['knowledge_points']:
            try:
                q['knowledge_points'] = json.loads(q['knowledge_points'])
            except json.JSONDecodeError:
                q['knowledge_points'] = {}
        if 'options' in q and q['options']:
            try:
                q['options'] = json.loads(q['options'])
            except json.JSONDecodeError:
                q['options'] = []
    
    result = {
        "total": len(question_data),
        "questions": question_data
    }
    
    return json.dumps(result, ensure_ascii=False, indent=2)
