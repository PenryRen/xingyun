"""
测试服务启动的诊断脚本
"""

import os
import sys

# 添加src到Python路径
sys.path.insert(0, os.path.join(os.path.dirname(__file__), 'src'))

print("开始诊断服务启动问题...")

# 测试导入顺序
try:
    print("1. 测试基本导入...")
    import fastapi
    import uvicorn
    print("[OK] 基本导入成功")
except Exception as e:
    print(f"[ERROR] 基本导入失败: {e}")

try:
    print("2. 测试模型管理器导入...")
    from models.model_manager import ModelManager
    print("[OK] 模型管理器导入成功")
except Exception as e:
    print(f"[ERROR] 模型管理器导入失败: {e}")

try:
    print("3. 测试向量存储导入...")
    from knowledge.vector_store import VectorStore
    print("[OK] 向量存储导入成功")
except Exception as e:
    print(f"[ERROR] 向量存储导入失败: {e}")

try:
    print("4. 测试智能体导入...")
    from agents.agent import build_agent
    print("[OK] 智能体导入成功")
except Exception as e:
    print(f"[ERROR] 智能体导入失败: {e}")

try:
    print("5. 测试工作流导入...")
    from graphs import create_learning_workflow
    print("[OK] 工作流导入成功")
except Exception as e:
    print(f"[ERROR] 工作流导入失败: {e}")

print("诊断完成！")
