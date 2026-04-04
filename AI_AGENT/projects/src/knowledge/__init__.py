"""
知识库模块

包含向量存储、文档处理和知识库管理功能
"""

from .vector_store import VectorStore

# 尝试导入其他模块
try:
    from .document_processor import DocumentProcessor
    from .knowledge_base import KnowledgeBase
    __all__ = [
        "VectorStore",
        "DocumentProcessor",
        "KnowledgeBase"
    ]
except Exception as e:
    print(f"警告: 无法导入所有知识库模块: {e}")
    __all__ = [
        "VectorStore"
    ]
