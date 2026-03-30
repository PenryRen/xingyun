"""
知识库管理模块

整合向量存储和文档处理功能，提供知识库的创建和查询
"""

from typing import List, Optional, Dict, Any
from langchain_core.documents import Document
from knowledge.vector_store import VectorStore
from knowledge.document_processor import DocumentProcessor


class KnowledgeBase:
    """
    知识库管理器
    
    负责知识库的创建、更新和查询
    """
    
    def __init__(
        self,
        word_dir: str = "AI_AGENT/projects/src/word",
        index_path: str = "AI_AGENT/projects/src/knowledge/vector_index",
        use_local: bool = True
    ):
        """
        初始化知识库
        
        Args:
            word_dir: 文档目录路径
            index_path: 向量索引保存路径
            use_local: 是否使用本地模型
        """
        self.word_dir = word_dir
        self.index_path = index_path
        self.use_local = use_local
        
        # 初始化向量存储
        self.vector_store = VectorStore(index_path=index_path, use_local=use_local)
        
        # 初始化文档处理器
        self.document_processor = DocumentProcessor(word_dir=word_dir)
    
    def build_knowledge_base(self) -> int:
        """
        构建知识库
        
        处理word目录下的所有文档并添加到向量存储
        
        Returns:
            添加的文档数量
        """
        print("开始构建知识库...")
        
        # 处理文档
        documents = self.document_processor.process_documents()
        
        if documents:
            # 添加到向量存储
            self.vector_store.add_documents(documents)
            print(f"知识库构建完成，添加了 {len(documents)} 个文档")
            return len(documents)
        else:
            print("知识库构建失败，没有文档需要处理")
            return 0
    
    def update_knowledge_base(self) -> int:
        """
        更新知识库
        
        重新处理word目录下的所有文档并更新向量存储
        
        Returns:
            更新的文档数量
        """
        print("开始更新知识库...")
        
        # 清空现有向量存储
        self.vector_store.clear()
        
        # 重新构建知识库
        return self.build_knowledge_base()
    
    def query(self, query: str, k: int = 3) -> List[Document]:
        """
        查询知识库
        
        Args:
            query: 查询文本
            k: 返回结果数量
            
        Returns:
            相关文档列表
        """
        print(f"查询知识库: {query}")
        return self.vector_store.search(query, k=k)
    
    def query_with_score(self, query: str, k: int = 3) -> List[tuple[Document, float]]:
        """
        查询知识库并返回相似度分数
        
        Args:
            query: 查询文本
            k: 返回结果数量
            
        Returns:
            (文档, 相似度分数) 列表
        """
        print(f"查询知识库: {query}")
        return self.vector_store.search_with_score(query, k=k)
    
    def get_document_count(self) -> int:
        """
        获取知识库中的文档数量
        
        Returns:
            文档数量
        """
        count = self.vector_store.get_document_count()
        print(f"知识库中的文档数量: {count}")
        return count
    
    def add_document(self, text: str, metadata: Optional[Dict[str, Any]] = None) -> bool:
        """
        向知识库添加单个文档
        
        Args:
            text: 文档文本
            metadata: 文档元数据
            
        Returns:
            是否添加成功
        """
        try:
            document = Document(page_content=text, metadata=metadata or {})
            self.vector_store.add_documents([document])
            print("文档添加成功")
            return True
        except Exception as e:
            print(f"文档添加失败: {e}")
            return False
    
    def clear(self):
        """
        清空知识库
        """
        self.vector_store.clear()
        print("知识库已清空")
    
    def get_relevant_context(self, query: str, k: int = 3) -> str:
        """
        获取与查询相关的上下文
        
        Args:
            query: 查询文本
            k: 返回结果数量
            
        Returns:
            相关上下文文本
        """
        docs = self.query(query, k=k)
        
        if not docs:
            return ""
        
        context = "\n---\n".join([doc.page_content for doc in docs])
        return context
