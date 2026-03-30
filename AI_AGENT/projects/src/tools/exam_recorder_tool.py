"""
试卷入库工具 - 将试卷和答案记录到数据库
"""
import json
from datetime import datetime
from langchain.tools import tool
from langchain.tools import ToolRuntime
from storage.database.supabase_client import get_supabase_client


@tool
def add_questions_to_database(questions_data: str, runtime: ToolRuntime = None) -> str:
    """
    批量添加题目到题库
    
    参数:
        questions_data: 题目数据，JSON数组格式，每个元素包含题目信息
    
    返回:
        添加结果
    """
    # 移除对coze_coding_utils的依赖
    try:
        questions_list = json.loads(questions_data)
    except json.JSONDecodeError:
        return "错误：题目数据格式不正确，必须是JSON数组格式"
    
    if not isinstance(questions_list, list):
        return "错误：题目数据必须是数组格式"
    
    client = get_supabase_client()
    
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
                "category": q['category'],
                "difficulty": float(q['difficulty']),
                "answer": q['answer'],
                "explanation": q.get('explanation', ''),
                "tags": q.get('tags', {})
            }
            
            client.table('questions').insert(question_data).execute()
            success_count += 1
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
    ctx = runtime.context if runtime else new_context(method="record_exam_result")
    
    client = get_supabase_client()
    
    try:
        exam_info = json.loads(exam_data)
    except json.JSONDecodeError:
        return "错误：试卷数据格式不正确，必须是JSON格式"
    
    # 查询或创建学生
    student_response = client.table('students').select('*').eq('student_id', student_id).execute()
    
    if not student_response.data:
        # 创建学生记录
        student_data = {
            "name": str(student_name),
            "student_id": str(student_id)
        }
        student_response = client.table('students').insert(student_data).execute()
        student_id_db = int(student_response.data[0].get('id', 0)) if student_response.data[0].get('id') is not None else 0
        
        # 创建学情档案
        profile_data = {
            "student_id": student_id_db,
            "weak_points": [],
            "mastered_points": []
        }
        client.table('learning_profiles').insert(profile_data).execute()
    else:
        student_id_db = int(student_response.data[0].get('id', 0)) if student_response.data[0].get('id') is not None else 0
    
    # 创建考试记录
    exam_record_data = {
        "student_id": student_id_db,
        "exam_name": str(exam_name),
        "exam_date": datetime.now().isoformat(),
        "total_score": float(total_score),
        "max_score": float(max_score)
    }
    
    exam_record_response = client.table('exam_records').insert(exam_record_data).execute()
    exam_record_id = exam_record_response.data[0]['id']
    
    # 记录答题记录
    answer_records = []
    
    # 假设 exam_data 是一个数组，每个元素包含题目ID和学生答案
    if isinstance(exam_info, list):
        for idx, item in enumerate(exam_info):
            question_id = item.get('question_id')
            student_answer = str(item.get('student_answer', ''))
            correct_answer = str(item.get('correct_answer', ''))
            score = item.get('score', 0)
            
            if question_id:
                is_correct = str(student_answer).strip().lower() == str(correct_answer).strip().lower()
                
                answer_record_data = {
                    "exam_record_id": exam_record_id,
                    "question_id": int(question_id),
                    "student_answer": student_answer,
                    "is_correct": is_correct,
                    "score": float(score)
                }
                
                answer_records.append(answer_record_data)
    
    # 批量插入答题记录
    if answer_records:
        client.table('answer_records').insert(answer_records).execute()
    
    result = {
        "exam_record_id": exam_record_id,
        "exam_name": exam_name,
        "student_id": student_id,
        "total_score": f"{total_score}/{max_score}",
        "answer_records_count": len(answer_records)
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
    # 移除对coze_coding_utils的依赖
    client = get_supabase_client()
    
    query = client.table('questions').select('*')
    
    if topic:
        query = query.ilike('question_text', f'%{topic}%')
    
    if category:
        query = query.ilike('category', f'%{category}%')
    
    response = query.limit(100).execute()
    
    result = {
        "total": len(response.data),
        "questions": response.data
    }
    
    return json.dumps(result, ensure_ascii=False, indent=2)
