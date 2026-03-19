"""
学情管理工具 - 维护学生的学情档案
"""
import json
from datetime import datetime
from langchain.tools import tool
from langchain.tools import ToolRuntime
from coze_coding_utils.runtime_ctx.context import new_context
from coze_coding_dev_sdk import LLMClient
from langchain_core.messages import SystemMessage, HumanMessage
from storage.database.supabase_client import get_supabase_client


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
    ctx = runtime.context if runtime else new_context(method="create_student_profile")
    
    client = get_supabase_client()
    
    # 检查学生是否已存在
    existing_response = client.table('students').select('*').eq('student_id', student_id).execute()
    
    if existing_response.data:
        return f"学号为 {student_id} 的学生已存在"
    
    # 创建学生记录
    student_data = {
        "name": student_name,
        "student_id": student_id,
        "grade": grade,
        "class_name": class_name
    }
    
    student_response = client.table('students').insert(student_data).execute()
    
    if student_response.data:
        student_id_db = student_response.data[0]['id']
        
        # 创建学情档案
        profile_data = {
            "student_id": student_id_db,
            "weak_points": [],
            "mastered_points": []
        }
        
        profile_response = client.table('learning_profiles').insert(profile_data).execute()
        
        return f"成功创建学生档案：{student_name}（学号：{student_id}）"
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
    ctx = runtime.context if runtime else new_context(method="update_learning_profile")
    
    try:
        weak_points_list = json.loads(weak_points)
        mastered_points_list = json.loads(mastered_points)
    except json.JSONDecodeError:
        return "错误：知识点格式不正确，必须是JSON数组格式"
    
    client = get_supabase_client()
    
    # 查询学生
    student_response = client.table('students').select('*').eq('student_id', student_id).execute()
    
    if not student_response.data:
        return f"未找到学号为 {student_id} 的学生"
    
    student_id_db = student_response.data[0]['id']
    
    # 更新学情档案
    update_data = {
        "weak_points": weak_points_list,
        "mastered_points": mastered_points_list,
        "last_update": datetime.now().isoformat()
    }
    
    if overall_score is not None:
        update_data['overall_score'] = overall_score
    
    profile_response = (client.table('learning_profiles')
                       .update(update_data)
                       .eq('student_id', student_id_db)
                       .execute())
    
    # 更新知识点进度
    for point in weak_points_list:
        update_knowledge_progress(student_id, point, mastery_level=0.3)
    
    for point in mastered_points_list:
        update_knowledge_progress(student_id, point, mastery_level=0.8)
    
    return f"成功更新学情档案"


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
    ctx = runtime.context if runtime else new_context(method="get_student_learning_profile")
    
    client = get_supabase_client()
    
    # 查询学生
    student_response = client.table('students').select('*').eq('student_id', student_id).execute()
    
    if not student_response.data:
        return f"未找到学号为 {student_id} 的学生"
    
    student = student_response.data[0]
    student_id_db = student['id']
    
    # 查询学情档案
    profile_response = client.table('learning_profiles').select('*').eq('student_id', student_id_db).execute()
    
    if not profile_response.data:
        return f"未找到学号为 {student_id} 的学情档案"
    
    profile = profile_response.data[0]
    
    # 查询考试历史
    exam_response = (client.table('exam_records')
                    .select('*')
                    .eq('student_id', student_id_db)
                    .order('exam_date', desc=True)
                    .limit(10)
                    .execute())
    
    # 查询知识点进度
    progress_response = (client.table('student_knowledge_progress')
                        .select('*')
                        .eq('student_id', student_id_db)
                        .order('mastery_level', desc=False)
                        .execute())
    
    result = {
        "student_info": {
            "name": str(student.get('name', '')),
            "student_id": str(student.get('student_id', '')),
            "grade": str(student.get('grade', '')) if student.get('grade') else '',
            "class_name": str(student.get('class_name', '')) if student.get('class_name') else ''
        },
        "learning_profile": {
            "weak_points": list(profile.get('weak_points', [])) if profile.get('weak_points') else [],
            "mastered_points": list(profile.get('mastered_points', [])) if profile.get('mastered_points') else [],
            "overall_score": float(profile.get('overall_score', 0)) if profile.get('overall_score') is not None else None,
            "last_update": str(profile.get('last_update', ''))
        },
        "recent_exams": list(exam_response.data),
        "knowledge_progress": list(progress_response.data)
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
    client = get_supabase_client()
    
    # 查询学生
    student_response = client.table('students').select('*').eq('student_id', student_id).execute()
    if not student_response.data:
        return
    
    student_id_db = student_response.data[0]['id']
    
    # 检查是否已存在该知识点进度记录
    progress_response = (client.table('student_knowledge_progress')
                        .select('*')
                        .eq('student_id', student_id_db)
                        .eq('knowledge_point', knowledge_point)
                        .execute())
    
    if progress_response.data:
        # 更新现有记录
        existing_progress = progress_response.data[0]
        current_mastery = float(existing_progress.get('mastery_level', 0)) if existing_progress.get('mastery_level') is not None else 0
        practice_count = int(existing_progress.get('practice_count', 0)) if existing_progress.get('practice_count') is not None else 0
        correct_count = int(existing_progress.get('correct_count', 0)) if existing_progress.get('correct_count') is not None else 0
        
        # 更新掌握程度（加权平均）
        new_mastery = (current_mastery * practice_count + mastery_level) / (practice_count + 1)
        
        client.table('student_knowledge_progress').update({
            "mastery_level": round(float(new_mastery), 3),
            "last_practice_date": datetime.now().isoformat(),
            "practice_count": practice_count + 1,
            "correct_count": correct_count + (1 if mastery_level >= 0.6 else 0)
        }).eq('id', int(existing_progress['id'])).execute()
    else:
        # 创建新记录
        client.table('student_knowledge_progress').insert({
            "student_id": student_id_db,
            "knowledge_point": knowledge_point,
            "mastery_level": mastery_level,
            "last_practice_date": datetime.now().isoformat(),
            "practice_count": 1,
            "correct_count": 1 if mastery_level >= 0.6 else 0
        }).execute()


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
    ctx = runtime.context if runtime else new_context(method="get_knowledge_progress")
    
    client = get_supabase_client()
    
    # 查询学生
    student_response = client.table('students').select('*').eq('student_id', student_id).execute()
    if not student_response.data:
        return f"未找到学号为 {student_id} 的学生"
    
    student_id_db = student_response.data[0]['id']
    
    # 查询知识点进度
    query = client.table('student_knowledge_progress').select('*').eq('student_id', student_id_db)
    
    if knowledge_point:
        query = query.eq('knowledge_point', knowledge_point)
    
    progress_response = query.order('mastery_level', desc=False).execute()
    
    result = {
        "student_id": str(student_id),
        "knowledge_progress": list(progress_response.data)
    }
    
    return json.dumps(result, ensure_ascii=False, indent=2)
