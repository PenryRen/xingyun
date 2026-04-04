"""
测试 vector_store 导入
"""

import os
import sys

# 添加src到Python路径
sys.path.insert(0, os.path.join(os.path.dirname(__file__), 'src'))

print("测试 vector_store 导入...")

try:
    from knowledge.vector_store import VectorStore
    print("[OK] vector_store 导入成功")
    
    # 测试创建实例
    print("测试创建 VectorStore 实例...")
    vs = VectorStore()
    print("[OK] VectorStore 实例创建成功")
    
    # 测试方法
    print("测试 add_documents 方法...")
    vs.add_documents(["test1", "test2"])
    print("[OK] add_documents 方法测试成功")
    
    print("测试 similarity_search 方法...")
    result = vs.similarity_search("test")
    print(f"[OK] similarity_search 方法测试成功，结果: {result}")
    
    print("测试 save 方法...")
    vs.save()
    print("[OK] save 方法测试成功")
    
    print("所有测试通过！")
except Exception as e:
    print(f"[ERROR] 测试失败: {e}")
    import traceback
    traceback.print_exc()

print("测试完成！")
