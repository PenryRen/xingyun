"""
考试分析工具 - 分析学生的试卷，识别薄弱知识点
"""
import json
from langchain.tools import tool
from langchain.tools import ToolRuntime
from coze_coding_utils.runtime_ctx.context import new_context
from coze_coding_dev_sdk import LLMClient
from langchain_core.messages import SystemMessage, HumanMessage
from storage.database.supabase_client import get_supabase_client


@tool
def analyze_exam_paper(
    student_name: str,
    student_id: str,
    exam_name: str,
    exam_data: str,
    score: float,
    max_score: float,
    runtime: ToolRuntime = None
) -> str:
    """
    分析学生的试卷，识别薄弱知识点并生成分析报告
    
    参数:
        student_name: 学生姓名
        student_id: 学号
        exam_name: 考试名称
        exam_data: 试卷数据，格式为JSON字符串，包含题目和学生答案
        score: 学生得分
        max_score: 满分
    
    返回:
        分析报告，包含薄弱知识点、错题统计、改进建议等
    """
    ctx = runtime.context if runtime else new_context(method="analyze_exam_paper")
    
    # 解析考试数据
    try:
        exam_info = json.loads(exam_data)
    except json.JSONDecodeError:
        return "错误：试卷数据格式不正确，必须是JSON格式"
    
    # 使用LLM分析试卷
    client = LLMClient(ctx=ctx)
    
    system_prompt = """你是一位专业的教育分析师，擅长分析学生的考试表现并识别薄弱知识点。

你的任务：
1. 分析学生的错题，找出薄弱的知识领域
2. 统计各题目的错误原因
3. 识别知识点的掌握程度
4. 提供针对性的改进建议

输出格式（JSON）：
{
    "weak_points": ["知识点1", "知识点2", ...],  // 薄弱知识点列表
    "error_analysis": {
        "知识点1": {"error_count": 3, "error_rate": 0.6, "common_errors": ["错误类型1", "错误类型2"]},
        "知识点2": {"error_count": 2, "error_rate": 0.4, "common_errors": ["错误类型1"]}
    },
    "overall_performance": {
        "score": 85,
        "max_score": 100,
        "pass_rate": 0.85,
        "comment": "整体表现评价"
    },
    "recommendations": [
        "建议1",
        "建议2",
        ...
    ]
}
"""
    
    human_message = f"""请分析以下试卷数据：

学生姓名：{student_name}
学号：{student_id}
考试名称：{exam_name}
得分：{score}/{max_score}

试卷数据：
{json.dumps(exam_info, ensure_ascii=False, indent=2)}

请按照要求生成JSON格式的分析报告。"""
    
    messages = [
        SystemMessage(content=system_prompt),
        HumanMessage(content=human_message)
    ]
    
    response = client.invoke(
        messages=messages,
        model="doubao-seed-2-0-pro-260215",
        temperature=0.3
    )
    
    # 提取文本内容
    def get_text_content(content):
        if isinstance(content, str):
            return content
        elif isinstance(content, list):
            if content and isinstance(content[0], str):
                return " ".join(content)
            else:
                text_parts = []
                for item in content:
                    if isinstance(item, dict) and item.get("type") == "text":
                        text_parts.append(item.get("text", ""))
                return " ".join(text_parts)
        return str(content)
    
    analysis_text = get_text_content(response.content)
    
    return analysis_text


@tool
def get_student_weak_points(student_name: str, student_id: str, runtime: ToolRuntime = None) -> str:
    """
    获取学生的薄弱知识点历史记录
    
    参数:
        student_name: 学生姓名
        student_id: 学号
    
    返回:
        该学生历次考试中识别出的薄弱知识点及变化趋势
    """
    ctx = runtime.context if runtime else new_context(method="get_student_weak_points")
    
    client = get_supabase_client()
    
    # 查询学生信息
    student_response = client.table('students').select('*').eq('student_id', student_id).execute()
    
    if not student_response.data:
        return f"未找到学号为 {student_id} 的学生记录"
    
    student = student_response.data[0]
    student_db_id = student['id']
    
    # 查询该学生的所有考试记录
    exam_response = client.table('exam_records').select('*').eq('student_id', student_db_id).order('exam_date', desc=True).execute()
    
    if not exam_response.data:
        return f"学生 {student_name} 暂无考试记录"
    
    # 查询学情档案
    profile_response = client.table('learning_profiles').select('*').eq('student_id', student_db_id).execute()
    
    weak_points_history = []
    for exam in exam_response.data:
        exam_id = exam['id']
        exam_name = exam['exam_name']
        exam_date = exam['exam_date']
        total_score = exam['total_score']
        max_score = exam['max_score']
        
        weak_points_history.append({
            "exam_name": str(exam_name),
            "exam_date": str(exam_date) if exam_date else "",
            "score": f"{total_score}/{max_score}",
            "rate": round(total_score / max_score * 100, 2) if max_score and max_score > 0 else 0
        })
    
    result = {
        "student_name": student_name,
        "student_id": student_id,
        "exam_history": weak_points_history,
        "current_weak_points": list(profile_response.data[0]['weak_points']) if profile_response.data else [],
        "mastered_points": list(profile_response.data[0]['mastered_points']) if profile_response.data else []
    }
    
    return json.dumps(result, ensure_ascii=False, indent=2)
