import os
from typing import Optional, Dict, Any
from langchain_openai import OpenAIEmbeddings
from models.spark import ChatSpark


SPARK_DEFAULT_BASE_URL = "https://spark-api-open.xf-yun.com/v1/"
SPARK_DEFAULT_MODEL = "4.0Ultra"


class ModelManager:
    """模型管理器 - 负责初始化星火大模型并支持本地降级"""
    
    @staticmethod
    def get_llm(
        use_local: bool = False,
        model_name: Optional[str] = None,
        temperature: float = 0.7,
        streaming: bool = True,
        timeout: int = 600,
        max_tokens: int = 8000
    ):
        """
        获取大语言模型实例
        
        Args:
            use_local: 是否使用本地模型
            model_name: 模型名称
            temperature: 温度参数
            streaming: 是否流式输出
            timeout: 超时时间
            max_tokens: 最大输出 token 数，Spark Ultra-32K 允许 1-32768
            
        Returns:
            语言模型实例
        """
        
        # 优先使用星火 OpenAI 兼容接口
        try:
            api_password = os.getenv("SPARK_API_PASSWORD", "").strip()
            base_url = os.getenv("SPARK_BASE_URL") or SPARK_DEFAULT_BASE_URL

            if not api_password:
                raise Exception("星火 APIPassword 未配置，请设置 SPARK_API_PASSWORD")
            
            remote_model_name = model_name or os.getenv("SPARK_MODEL") or SPARK_DEFAULT_MODEL
            
            llm = ChatSpark(
                model=remote_model_name,
                api_key=api_password,
                base_url=base_url,
                temperature=temperature,
                streaming=streaming,
                timeout=timeout,
                max_tokens=max_tokens
            )
            
            return llm
        except Exception as e:
            detail = str(e).replace(api_password, "[REDACTED]") if api_password else str(e)
            print(f"星火模型初始化失败: {type(e).__name__}: {detail}")
            
        # 远程模型失败时回退到本地模型
        if use_local:
            try:
                from langchain_huggingface import HuggingFaceEndpoint, ChatHuggingFace

                local_model_name = model_name or "mistralai/Mistral-7B-v0.1"
                llm = HuggingFaceEndpoint(
                    repo_id=local_model_name,
                    model_kwargs={
                        "temperature": temperature,
                        "max_length": 1024
                    }
                )
                chat_model = ChatHuggingFace(llm=llm)
                return chat_model
            except Exception as e:
                print(f"本地模型初始化失败: {e}")
        
        raise Exception("所有模型初始化失败，请检查配置")

    @staticmethod
    def get_model_config() -> Dict[str, Any]:
        """
        获取模型配置
        
        Returns:
            模型配置字典
        """
        return {
            "provider": "spark",
            "display_name": "Spark Ultra-32K",
            "default_model": os.getenv("SPARK_MODEL") or SPARK_DEFAULT_MODEL,
            "configured": bool(os.getenv("SPARK_API_PASSWORD", "").strip()),
            "base_url": os.getenv("SPARK_BASE_URL") or SPARK_DEFAULT_BASE_URL,
            "temperature": float(os.getenv("MODEL_TEMPERATURE", "0.7")),
            "timeout": int(os.getenv("MODEL_TIMEOUT", "600"))
        }
    
    @staticmethod
    def get_embedding(
        use_local: bool = True,
        model_name: Optional[str] = None,
        chunk_size: int = 1000
    ):
        """
        获取embedding模型实例
        
        Args:
            use_local: 是否使用本地模型（默认True，以本地优先）
            model_name: 模型名称
            chunk_size: 分块大小
            
        Returns:
            embedding模型实例
        """
        # 优先使用本地模型
        if use_local:
            try:
                from langchain_huggingface import HuggingFaceEmbeddings

                local_model_name = model_name or "sentence-transformers/all-MiniLM-L6-v2"
                embedding = HuggingFaceEmbeddings(
                    model_name=local_model_name,
                    model_kwargs={"device": "cpu"},
                    encode_kwargs={"normalize_embeddings": True}
                )
                # 测试embedding
                embedding.embed_query("test")
                print(f"本地embedding模型初始化成功: {local_model_name}")
                return embedding
            except Exception as e:
                print(f"本地embedding模型初始化失败，回退到远程模型: {e}")
                use_local = False
        
        # 回退到独立配置的远程 Embedding 服务。
        # 星火 Ultra-32K 是聊天模型，不能复用其接口作为 Embedding 服务。
        try:
            api_key = os.getenv("EMBEDDING_API_KEY")
            base_url = os.getenv("EMBEDDING_BASE_URL")
            
            if not api_key:
                raise Exception("远程 embedding API 密钥未配置，请设置 EMBEDDING_API_KEY")
            
            remote_model_name = model_name or os.getenv("EMBEDDING_MODEL", "text-embedding-3-small")
            embedding = OpenAIEmbeddings(
                model=remote_model_name,
                api_key=api_key,
                base_url=base_url,
                chunk_size=chunk_size
            )
            
            # 测试embedding
            embedding.embed_query("test")
            print(f"远程embedding模型初始化成功: {remote_model_name}")
            return embedding
        except Exception as e:
            print(f"远程embedding模型初始化失败: {e}")
            raise Exception("所有embedding模型初始化失败，请检查配置")
