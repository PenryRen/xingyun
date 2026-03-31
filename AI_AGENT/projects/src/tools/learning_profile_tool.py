"""
学情管理工具 - 维护学生的学情档案
"""
import json
from datetime import datetime
from langchain.tools import tool
from langchain.tools import ToolRuntime
from langchain_core.messages import SystemMessage, HumanMessage
from tools.data_base_tools import (
    get_student_by_student_id,
    create_student,
    create_learning_profile,
    update_learning_profile,
    get_learning_profile,
    get_student_exams,
    get_student_knowledge_progress,
    update_knowledge_progress,
    model_to_dict
)
from models.model_manager import ModelManager


@tool
def create_student_profile(
    student_name: str,
    student_id: str,
    grade: str = None,
    class_name: str = None,
    runtime: ToolRuntime = None
) -> str:
    """
    创建学生档案
    
    参数:
        student_name: 学生姓名
        student_id: 学号
        grade: 年级（可选）
        class_name: 班级（可选）
    
    返回:
        创建结果
    """
    # 检查学生是否已存在
    existing_student = get_student_by_student_id(student_id)
    if existing_student:
        return f"学号为 {student_id} 的学生已存在"
    
    # 创建学生记录
    student = create_student(
        student_name=student_name,
        student_id=student_id,
        grade=grade,
        class_name=class_name
    )
    
    if student:
        # 创建学情档案
        profile = create_learning_profile(student.id)
        if profile:
            return f"成功创建学生档案：{student_name}（学号：{student_id}）"
        else:
            return "创建学情档案失败"
    else:
        return "创建学生档案失败"


@tool
def update_learning_profile(
    student_id: str,
    weak_points: str,
    mastered_points: str,
    overall_score: float = None,
    runtime: ToolRuntime = None
) -> str:
    """
    更新学生的学情档案
    
    参数:
        student_id: 学号
        weak_points: 薄弱知识点列表，JSON数组格式
        mastered_points: 已掌握知识点列表，JSON数组格式
        overall_score: 综合评分（可选，0.0-100.0）
    
    返回:
        更新结果
    """
    try:
        weak_points_list = json.loads(weak_points)
        mastered_points_list = json.loads(mastered_points)
    except json.JSONDecodeError:
        return "错误：知识点格式不正确，必须是JSON数组格式"
    
    # 查询学生
    student = get_student_by_student_id(student_id)
    if not student:
        return f"未找到学号为 {student_id} 的学生"
    
    # 更新学情档案
    from tools.data_base_tools import update_learning_profile as db_update_learning_profile
    success = db_update_learning_profile(
        student_id=student.id,
        weak_points=weak_points,
        mastered_points=mastered_points,
        overall_score=overall_score
    )
    
    if success:
        # 更新知识点进度
        from tools.data_base_tools import update_knowledge_progress as db_update_knowledge_progress
        for point in weak_points_list:
            db_update_knowledge_progress(student.id, point, mastery_level=0.3)
        
        for point in mastered_points_list:
            db_update_knowledge_progress(student.id, point, mastery_level=0.8)
        
        return f"成功更新学情档案"
    else:
        return "更新学情档案失败"


@tool
def get_student_learning_profile(student_name: str, student_id: str, runtime: ToolRuntime = None) -> str:
    """
    获取学生的学情档案
    
    参数:
        student_name: 学生姓名
        student_id: 学号
    
    返回:
        学情档案详情
    """
    # 查询学生
    student = get_student_by_student_id(student_id)
    if not student:
        return f"未找到学号为 {student_id} 的学生"
    
    # 查询学情档案
    profile = get_learning_profile(student.id)
    if not profile:
        return f"未找到学号为 {student_id} 的学情档案"
    
    # 查询考试历史
    exams = get_student_exams(student.id, limit=10)
    exam_data = [model_to_dict(exam) for exam in exams]
    
    # 查询知识点进度
    progress = get_student_knowledge_progress(student.id)
    progress_data = [model_to_dict(p) for p in progress]
    
    # 解析JSON字段
    try:
        weak_points = json.loads(profile.weak_points) if profile.weak_points else []
        mastered_points = json.loads(profile.mastered_points) if profile.mastered_points else []
    except json.JSONDecodeError:
        weak_points = []
        mastered_points = []
    
    result = {
        "student_info": {
            "name": student.name,
            "student_id": student.student_id,
            "grade": student.grade or '',
            "class_name": student.class_name or ''
        },
        "learning_profile": {
            "weak_points": weak_points,
            "mastered_points": mastered_points,
            "overall_score": profile.overall_score,
            "last_update": model_to_dict(profile).get('last_update', '')
        },
        "recent_exams": exam_data,
        "knowledge_progress": progress_data
    }
    
    return json.dumps(result, ensure_ascii=False, indent=2)


def update_knowledge_progress(student_id: str, knowledge_point: str, mastery_level: float):
    """
    更新知识点进度（内部函数）
    
    参数:
        student_id: 学号
        knowledge_point: 知识点名称
        mastery_level: 掌握程度（0.0-1.0）
    """
    from tools.data_base_tools import get_student_by_student_id as db_get_student
    from tools.data_base_tools import update_knowledge_progress as db_update_progress
    
    # 查询学生
    student = db_get_student(student_id)
    if not student:
        return
    
    # 更新知识点进度
    db_update_progress(student.id, knowledge_point, mastery_level)


@tool
def get_knowledge_progress(student_id: str, knowledge_point: str = None, runtime: ToolRuntime = None) -> str:
    """
    获取学生的知识点进度
    
    参数:
        student_id: 学号
        knowledge_point: 知识点名称（可选，不指定则返回所有知识点进度）
    
    返回:
        知识点进度信息
    """
    # 查询学生
    student = get_student_by_student_id(student_id)
    if not student:
        return f"未找到学号为 {student_id} 的学生"
    
    # 查询知识点进度
    progress = get_student_knowledge_progress(student.id, knowledge_point)
    progress_data = [model_to_dict(p) for p in progress]
    
    result = {
        "student_id": str(student_id),
        "knowledge_progress": progress_data
    }
    
    return json.dumps(result, ensure_ascii=False, indent=2)
