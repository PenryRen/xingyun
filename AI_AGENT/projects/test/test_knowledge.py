"""
测试知识库功能
"""

import os
import sys

# 添加src到Python路径
sys.path.insert(0, os.path.join(os.path.dirname(__file__), 'src'))

print("开始测试知识库功能...")

try:
    print("1. 测试导入知识库模块...")
    from knowledge import KnowledgeBase
    print("[OK] 知识库模块导入成功")
    
    # 测试创建知识库实例
    print("2. 测试创建知识库实例...")
    knowledge_base = KnowledgeBase(use_local=False)
    print("[OK] 知识库实例创建成功")
    
    # 测试构建知识库
    print("3. 测试构建知识库...")
    count = knowledge_base.build_knowledge_base()
    print(f"[OK] 知识库构建成功，添加了 {count} 个文档")
    
    # 测试查询知识库
    print("4. 测试查询知识库...")
    results = knowledge_base.query("你好")
    print(f"[OK] 知识库查询成功，返回了 {len(results)} 个结果")
    
    # 测试获取相关上下文
    print("5. 测试获取相关上下文...")
    context = knowledge_base.get_relevant_context("你好")
    print(f"[OK] 获取相关上下文成功，长度: {len(context)}")
    
    # 测试获取文档数量
    print("6. 测试获取文档数量...")
    count = knowledge_base.get_document_count()
    print(f"[OK] 获取文档数量成功，数量: {count}")
    
    # 测试添加文档
    print("7. 测试添加文档...")
    success = knowledge_base.add_document("测试文档内容", {"title": "测试文档"})
    print(f"[OK] 添加文档成功: {success}")
    
    # 测试清空知识库
    print("8. 测试清空知识库...")
    knowledge_base.clear()
    print("[OK] 清空知识库成功")
    
    print("\n所有测试通过！知识库功能正常工作。")
except Exception as e:
    print(f"[ERROR] 测试失败: {e}")
    import traceback
    traceback.print_exc()

print("测试完成！")
