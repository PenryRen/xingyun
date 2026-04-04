"""
测试导入路径
"""

import os
import sys

# 添加src到Python路径
sys.path.insert(0, os.path.join(os.path.dirname(__file__), 'src'))

print("测试导入路径...")

# 测试直接导入vector_store模块
try:
    print("1. 测试直接导入 vector_store 模块...")
    import knowledge.vector_store
    print("[OK] 直接导入 vector_store 模块成功")
except Exception as e:
    print(f"[ERROR] 直接导入 vector_store 模块失败: {e}")
    import traceback
    traceback.print_exc()

# 测试从vector_store导入VectorStore类
try:
    print("2. 测试从 vector_store 导入 VectorStore 类...")
    from knowledge.vector_store import VectorStore
    print("[OK] 从 vector_store 导入 VectorStore 类成功")
except Exception as e:
    print(f"[ERROR] 从 vector_store 导入 VectorStore 类失败: {e}")
    import traceback
    traceback.print_exc()

print("测试完成！")
