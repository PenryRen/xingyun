"""
智能组卷工具 - 根据要求自动生成试卷
"""
import json
import random
from langchain.tools import tool
from langchain.tools import ToolRuntime
from langchain_core.messages import SystemMessage, HumanMessage
from tools.data_base_tools import (
    get_questions,
    model_to_dict
)
from models.model_manager import ModelManager


@tool
def generate_intelligent_paper(
    student_name: str,
    student_id: str,
    paper_type: str,
    topic: str,
    difficulty: str,
    question_count: int,
    runtime: ToolRuntime = None
) -> str:
    """
    智能组卷 - 根据学生已完成的课程智能组卷
    
    参数:
        student_name: 学生姓名
        student_id: 学号
        paper_type: 试卷类型 ("normal"|"enhanced")，normal为常规测试，enhanced为强化测试
        topic: 试卷主题/知识点范围
        difficulty: 难度要求 ("easy"|"medium"|"hard")
        question_count: 题目数量
    
    返回:
        生成的试卷，包含题目列表和参考答案
    """
    # 查询题库
    questions = get_questions(topic=topic, limit=100)
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
    
    if not question_data:
        return f"题库中没有关于 {topic} 的题目"
    
    # 根据难度筛选题目
    difficulty_map = {
        "easy": (0.0, 0.4),
        "medium": (0.4, 0.7),
        "hard": (0.7, 1.0)
    }
    
    min_diff, max_diff = difficulty_map.get(difficulty, (0.0, 1.0))
    filtered_questions = []
    for q in question_data:
        q_difficulty = float(q.get('difficulty', 0)) if q.get('difficulty') is not None else 0
        if min_diff <= q_difficulty < max_diff:
            filtered_questions.append(q)
    
    # 如果筛选后题目不足，使用全部题目
    if len(filtered_questions) < question_count:
        filtered_questions = question_data[:min(question_count * 2, len(question_data))]
    
    # 如果是强化测试，优先选择薄弱知识点相关的题目
    if paper_type == "enhanced":
        from tools.data_base_tools import get_student_by_student_id, get_learning_profile
        # 查询学生的薄弱知识点
        student = get_student_by_student_id(student_id)
        if student:
            profile = get_learning_profile(student.id)
            if profile:
                try:
                    weak_points = json.loads(profile.weak_points) if profile.weak_points else []
                except json.JSONDecodeError:
                    weak_points = []
                
                # 优先选择包含薄弱知识点的题目
                weak_questions = []
                other_questions = []
                for q in filtered_questions:
                    q_tags = q.get('knowledge_points', {})
                    q_category = str(q.get('subject', '')) if q.get('subject') else ''
                    # 检查题目是否包含薄弱知识点
                    is_weak_related = any(
                        (wp.lower() in q_category.lower()) or
                        (wp.lower() in str(q_tags).lower())
                        for wp in weak_points
                    )
                    if is_weak_related:
                        weak_questions.append(q)
                    else:
                        other_questions.append(q)
                
                # 70%薄弱知识点题目，30%其他题目
                weak_count = int(question_count * 0.7)
                other_count = question_count - weak_count
                
                if weak_questions:
                    selected = random.sample(weak_questions, min(weak_count, len(weak_questions)))
                    if len(selected) < question_count:
                        remaining = question_count - len(selected)
                        if other_questions:
                            selected.extend(random.sample(other_questions, min(remaining, len(other_questions))))
                    
                    filtered_questions = selected
    
    # 随机选择指定数量的题目
    if len(filtered_questions) > question_count:
        selected_questions = random.sample(filtered_questions, question_count)
    else:
        selected_questions = filtered_questions
    
    # 构建试卷
    paper = {
        "paper_name": f"{topic} {'强化测试' if paper_type == 'enhanced' else '综合测试'}",
        "student_name": student_name,
        "student_id": student_id,
        "type": paper_type,
        "difficulty": difficulty,
        "total_questions": len(selected_questions),
        "questions": []
    }
    
    for idx, q in enumerate(selected_questions, 1):
        paper["questions"].append({
            "question_number": idx,
            "question_id": int(q.get('id', 0)) if q.get('id') is not None else 0,
            "question_text": str(q.get('question_text', '')) if q.get('question_text') else '',
            "category": str(q.get('subject', '')) if q.get('subject') else '',
            "difficulty": float(q.get('difficulty', 0)) if q.get('difficulty') is not None else 0,
            "answer": str(q.get('correct_answer', '')) if q.get('correct_answer') else '',
            "explanation": str(q.get('answer_analysis', '')) if q.get('answer_analysis') else '',
            "tags": q.get('knowledge_points') if isinstance(q.get('knowledge_points'), dict) else {}
        })
    
    return json.dumps(paper, ensure_ascii=False, indent=2)


# 星火 HTTP 接口要求工具名不超过 32 个字符；保留 Python 函数名兼容既有导入。
@tool("generate_enhanced_paper")
def generate_enhanced_paper_by_weak_points(
    student_name: str,
    student_id: str,
    weak_points: str,
    question_count: int,
    runtime: ToolRuntime = None
) -> str:
    """
    强化测试 - 根据学生的薄弱点针对性组卷
    
    参数:
        student_name: 学生姓名
        student_id: 学号
        weak_points: 薄弱知识点列表，JSON数组格式
        question_count: 题目数量
    
    返回:
        针对薄弱点的强化测试试卷
    """
    try:
        weak_points_list = json.loads(weak_points)
    except json.JSONDecodeError:
        return "错误：薄弱知识点格式不正确，必须是JSON数组格式"
    
    if not isinstance(weak_points_list, list):
        return "错误：薄弱知识点必须是数组格式"
    
    # 从题库中筛选与薄弱点相关的题目
    selected_questions = []
    
    for weak_point in weak_points_list:
        # 搜索与薄弱点相关的题目
        questions = get_questions(topic=weak_point, limit=50)
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
        
        selected_questions.extend(question_data)
    
    # 去重
    unique_questions = []
    seen_ids = set()
    for q in selected_questions:
        if q.get('id') not in seen_ids:
            unique_questions.append(q)
            seen_ids.add(q.get('id'))
    
    if not unique_questions:
        return f"题库中没有找到与薄弱点 {weak_points_list} 相关的题目"
    
    # 优先选择难度适中的题目
    # 按难度排序，选择中间难度的题目
    unique_questions.sort(key=lambda x: float(x.get('difficulty', 0)) if x.get('difficulty') is not None else 0)
    mid_index = len(unique_questions) // 2
    selected = unique_questions[mid_index:min(mid_index + question_count * 2, len(unique_questions))]
    
    # 随机选择指定数量
    if len(selected) > question_count:
        final_questions = random.sample(selected, question_count)
    else:
        final_questions = selected
    
    # 构建试卷
    paper = {
        "paper_name": "薄弱知识点强化测试",
        "student_name": student_name,
        "student_id": student_id,
        "type": "enhanced",
        "target_weak_points": weak_points_list,
        "total_questions": len(final_questions),
        "questions": []
    }
    
    for idx, q in enumerate(final_questions, 1):
        paper["questions"].append({
            "question_number": idx,
            "question_id": int(q.get('id', 0)) if q.get('id') is not None else 0,
            "question_text": str(q.get('question_text', '')) if q.get('question_text') else '',
            "category": str(q.get('subject', '')) if q.get('subject') else '',
            "difficulty": float(q.get('difficulty', 0)) if q.get('difficulty') is not None else 0,
            "answer": str(q.get('correct_answer', '')) if q.get('correct_answer') else '',
            "explanation": str(q.get('answer_analysis', '')) if q.get('answer_analysis') else '',
            "tags": q.get('knowledge_points') if isinstance(q.get('knowledge_points'), dict) else {}
        })
    
    return json.dumps(paper, ensure_ascii=False, indent=2)
