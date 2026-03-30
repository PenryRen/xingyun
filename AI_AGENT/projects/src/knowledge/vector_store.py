"""
向量存储管理模块

使用FAISS向量数据库存储和检索向量嵌入
"""

import os
import pickle
from typing import List, Optional, Dict, Any
from langchain_community.vectorstores import FAISS
from langchain_core.documents import Document
from models.model_manager import ModelManager


class VectorStore:
    """
    向量存储管理器
    
    负责创建和管理FAISS向量数据库
    """
    
    def __init__(
        self,
        index_path: str = "AI_AGENT/projects/src/knowledge/vector_index",
        use_local: bool = True
    ):
        """
        初始化向量存储
        
        Args:
            index_path: 向量索引保存路径
            use_local: 是否使用本地embedding模型
        """
        self.index_path = index_path
        self.use_local = use_local
        self.embedding = ModelManager.get_embedding(use_local=use_local)
        self.vector_store = None
        
        # 加载已有的向量索引
        self._load_index()
    
    def _load_index(self):
        """加载已有的向量索引"""
        if os.path.exists(self.index_path):
            try:
                self.vector_store = FAISS.load_local(
                    self.index_path,
                    self.embedding,
                    allow_dangerous_deserialization=True
                )
                print(f"成功加载向量索引: {self.index_path}")
            except Exception as e:
                print(f"加载向量索引失败: {e}")
                self.vector_store = None
    
    def add_documents(self, documents: List[Document]):
        """
        添加文档到向量存储
        
        Args:
            documents: 文档列表
        """
        if not documents:
            return
        
        if self.vector_store is None:
            # 首次创建向量存储
            self.vector_store = FAISS.from_documents(documents, self.embedding)
        else:
            # 向现有向量存储添加文档
            self.vector_store.add_documents(documents)
        
        # 保存向量索引
        self.save_index()
        print(f"成功添加 {len(documents)} 个文档到向量存储")
    
    def add_texts(self, texts: List[str], metadatas: Optional[List[Dict[str, Any]]] = None):
        """
        添加文本到向量存储
        
        Args:
            texts: 文本列表
            metadatas: 元数据列表
        """
        documents = []
        for i, text in enumerate(texts):
            metadata = metadatas[i] if metadatas and i < len(metadatas) else {}
            documents.append(Document(page_content=text, metadata=metadata))
        
        self.add_documents(documents)
    
    def search(self, query: str, k: int = 3) -> List[Document]:
        """
        搜索相关文档
        
        Args:
            query: 查询文本
            k: 返回结果数量
            
        Returns:
            相关文档列表
        """
        if self.vector_store is None:
            print("向量存储为空，无法搜索")
            return []
        
        try:
            docs = self.vector_store.similarity_search(query, k=k)
            return docs
        except Exception as e:
            print(f"搜索失败: {e}")
            return []
    
    def search_with_score(self, query: str, k: int = 3) -> List[tuple[Document, float]]:
        """
        搜索相关文档并返回相似度分数
        
        Args:
            query: 查询文本
            k: 返回结果数量
            
        Returns:
            (文档, 相似度分数) 列表
        """
        if self.vector_store is None:
            print("向量存储为空，无法搜索")
            return []
        
        try:
            docs_with_score = self.vector_store.similarity_search_with_score(query, k=k)
            return docs_with_score
        except Exception as e:
            print(f"搜索失败: {e}")
            return []
    
    def save_index(self):
        """保存向量索引"""
        if self.vector_store is not None:
            try:
                # 创建目录（如果不存在）
                os.makedirs(os.path.dirname(self.index_path), exist_ok=True)
                self.vector_store.save_local(self.index_path)
                print(f"成功保存向量索引到: {self.index_path}")
            except Exception as e:
                print(f"保存向量索引失败: {e}")
    
    def clear(self):
        """清空向量存储"""
        self.vector_store = None
        # 删除索引文件
        if os.path.exists(self.index_path):
            try:
                import shutil
                shutil.rmtree(self.index_path)
                print(f"成功清空向量存储")
            except Exception as e:
                print(f"清空向量存储失败: {e}")
    
    def get_document_count(self) -> int:
        """
        获取向量存储中的文档数量
        
        Returns:
            文档数量
        """
        if self.vector_store is None:
            return 0
        
        try:
            return len(self.vector_store.docstore)
        except Exception as e:
            print(f"获取文档数量失败: {e}")
            return 0
