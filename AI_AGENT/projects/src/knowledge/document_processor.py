"""
文档处理模块

负责处理word文件夹中的文件，将其转换为向量存储的文档
"""

import os
import re
from typing import List, Dict, Any
from langchain_core.documents import Document
from langchain_community.document_loaders import (
    TextLoader,
    PyPDFLoader,
    Docx2txtLoader,
    UnstructuredFileLoader
)
from langchain_text_splitters import RecursiveCharacterTextSplitter


class DocumentProcessor:
    """
    文档处理器
    
    负责加载和处理各种格式的文档
    """
    
    def __init__(
        self,
        word_dir: str = "AI_AGENT/projects/src/word",
        chunk_size: int = 1000,
        chunk_overlap: int = 100
    ):
        """
        初始化文档处理器
        
        Args:
            word_dir: 文档目录路径
            chunk_size: 文档分块大小
            chunk_overlap: 分块重叠大小
        """
        self.word_dir = word_dir
        self.chunk_size = chunk_size
        self.chunk_overlap = chunk_overlap
        self.text_splitter = RecursiveCharacterTextSplitter(
            chunk_size=chunk_size,
            chunk_overlap=chunk_overlap,
            separators=["\n\n", "\n", " ", ""]
        )
    
    def load_file(self, file_path: str) -> List[Document]:
        """
        加载单个文件
        
        Args:
            file_path: 文件路径
            
        Returns:
            文档列表
        """
        try:
            file_extension = os.path.splitext(file_path)[1].lower()
            
            if file_extension == ".txt":
                loader = TextLoader(file_path, encoding="utf-8")
            elif file_extension == ".pdf":
                loader = PyPDFLoader(file_path)
            elif file_extension == ".docx":
                loader = Docx2txtLoader(file_path)
            else:
                # 尝试使用通用加载器
                loader = UnstructuredFileLoader(file_path)
            
            documents = loader.load()
            print(f"成功加载文件: {file_path}")
            return documents
        except Exception as e:
            print(f"加载文件失败 {file_path}: {e}")
            return []
    
    def load_all_files(self) -> List[Document]:
        """
        加载word目录下的所有文件
        
        Returns:
            文档列表
        """
        documents = []
        
        if not os.path.exists(self.word_dir):
            print(f"文档目录不存在: {self.word_dir}")
            return documents
        
        for root, dirs, files in os.walk(self.word_dir):
            for file in files:
                file_path = os.path.join(root, file)
                file_docs = self.load_file(file_path)
                documents.extend(file_docs)
        
        print(f"成功加载 {len(documents)} 个文档")
        return documents
    
    def split_documents(self, documents: List[Document]) -> List[Document]:
        """
        分块处理文档
        
        Args:
            documents: 文档列表
            
        Returns:
            分块后的文档列表
        """
        if not documents:
            return []
        
        split_docs = self.text_splitter.split_documents(documents)
        print(f"成功将 {len(documents)} 个文档分块为 {len(split_docs)} 个块")
        return split_docs
    
    def process_documents(self) -> List[Document]:
        """
        处理所有文档
        
        加载并分块处理word目录下的所有文档
        
        Returns:
            处理后的文档列表
        """
        # 加载所有文件
        documents = self.load_all_files()
        
        # 分块处理
        split_docs = self.split_documents(documents)
        
        # 清理文档内容
        cleaned_docs = []
        for doc in split_docs:
            cleaned_content = self._clean_text(doc.page_content)
            if cleaned_content.strip():
                cleaned_doc = Document(
                    page_content=cleaned_content,
                    metadata=doc.metadata
                )
                cleaned_docs.append(cleaned_doc)
        
        print(f"成功处理 {len(cleaned_docs)} 个文档块")
        return cleaned_docs
    
    def _clean_text(self, text: str) -> str:
        """
        清理文本内容
        
        Args:
            text: 原始文本
            
        Returns:
            清理后的文本
        """
        # 移除多余的空白字符
        text = re.sub(r'\s+', ' ', text)
        # 移除首尾空白
        text = text.strip()
        return text
    
    def add_document_to_vector_store(self, vector_store) -> int:
        """
        将处理后的文档添加到向量存储
        
        Args:
            vector_store: 向量存储实例
            
        Returns:
            添加的文档数量
        """
        # 处理文档
        documents = self.process_documents()
        
        if documents:
            # 添加到向量存储
            vector_store.add_documents(documents)
            return len(documents)
        else:
            print("没有文档需要添加到向量存储")
            return 0
