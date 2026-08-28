"""
初始化知识库脚本

用于构建和测试知识库功能
"""

import os
import sys

# 添加src到Python路径
sys.path.insert(0, os.path.join(os.path.dirname(__file__), '..'))

from knowledge import KnowledgeBase


def main():
    """主函数"""
    print("=== 知识库初始化脚本 ===")
    
    # 初始化知识库
    knowledge_base = KnowledgeBase(use_local=True)
    
    # 构建知识库
    print("\n1. 构建知识库...")
    document_count = knowledge_base.build_knowledge_base()
    
    if document_count > 0:
        print(f"\n2. 知识库构建成功，包含 {document_count} 个文档")
        
        # 测试查询
        print("\n3. 测试知识库查询...")
        test_queries = [
            "什么是麒麟系统？",
            "微内核架构的特点",
            "进程管理的原理",
            "内存管理的方法"
        ]
        
        for query in test_queries:
            print(f"\n查询: {query}")
            docs = knowledge_base.query(query, k=2)
            if docs:
                for i, doc in enumerate(docs, 1):
                    print(f"  结果 {i}:")
                    print(f"    内容: {doc.page_content[:100]}...")
                    if doc.metadata:
                        print(f"    来源: {doc.metadata.get('source', '未知')}")
            else:
                print("  未找到相关信息")
        
        # 获取文档数量
        print(f"\n4. 知识库文档总数: {knowledge_base.get_document_count()}")
        
    else:
        print("\n知识库构建失败，word目录中可能没有文档")
        print("请在 AI_AGENT/projects/src/word 目录中添加文档文件")


if __name__ == "__main__":
    main()
