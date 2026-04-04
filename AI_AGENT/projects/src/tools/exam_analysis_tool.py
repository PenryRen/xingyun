"""
考试分析工具 - 分析学生的试卷，识别薄弱知识点
"""
import json
from models.model_manager import ModelManager

# 尝试导入 langchain 相关模块
try:
    from langchain.tools import tool
    from langchain.tools import ToolRuntime
    from langchain_core.messages import SystemMessage, HumanMessage
    from tools.data_base_tools import (
        get_student_by_student_id,
        get_student_exams,
        get_learning_profile,
        model_to_dict
    )
except ImportError as e:
    print(f"警告: 无法导入工具相关模块: {e}")
    tool = None
    ToolRuntime = None
    SystemMessage = None
    HumanMessage = None
    get_student_by_student_id = None
    get_student_exams = None
    get_learning_profile = None
    model_to_dict = None


if tool:
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
        # 解析考试数据
        try:
            exam_info = json.loads(exam_data)
        except json.JSONDecodeError:
            return "错误：试卷数据格式不正确，必须是JSON格式"
        
        # 使用ModelManager获取LLM
        llm = ModelManager.get_llm(
            use_local=False,  # 分析任务使用远程模型更可靠
            temperature=0.3
        )
        
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
        
        # 直接使用llm调用
        response = llm.invoke(messages)
        analysis_text = response.content
        
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
        # 查询学生信息
        student = get_student_by_student_id(student_id)
        
        if not student:
            return f"未找到学号为 {student_id} 的学生记录"
        
        # 查询该学生的所有考试记录
        exams = get_student_exams(student.id, limit=100)
        
        if not exams:
            return f"学生 {student_name} 暂无考试记录"
        
        # 查询学情档案
        profile = get_learning_profile(student.id)
        
        weak_points_history = []
        for exam in exams:
            exam_data = model_to_dict(exam)
            weak_points_history.append({
                "exam_name": str(exam_data.get('exam_name', '')),
                "exam_date": str(exam_data.get('exam_date', '')) if exam_data.get('exam_date') else "",
                "score": f"{exam_data.get('total_score', 0)}/{exam_data.get('max_score', 0)}",
                "rate": round(exam_data.get('total_score', 0) / exam_data.get('max_score', 1) * 100, 2) if exam_data.get('max_score', 0) > 0 else 0
            })
        
        # 解析JSON字段
        current_weak_points = []
        mastered_points = []
        if profile:
            try:
                current_weak_points = json.loads(profile.weak_points) if profile.weak_points else []
                mastered_points = json.loads(profile.mastered_points) if profile.mastered_points else []
            except json.JSONDecodeError:
                current_weak_points = []
                mastered_points = []
        
        result = {
            "student_name": student_name,
            "student_id": student_id,
            "exam_history": weak_points_history,
            "current_weak_points": current_weak_points,
            "mastered_points": mastered_points
        }
        
        return json.dumps(result, ensure_ascii=False, indent=2)
else:
    # 如果 tool 不可用，定义空函数
    def analyze_exam_paper(*args, **kwargs):
        return "工具不可用"
    
    def get_student_weak_points(*args, **kwargs):
        return "工具不可用"
