import os
from typing import Optional, Dict, Any
from langchain_openai import ChatOpenAI
from langchain_community.llms import HuggingFaceHub
from langchain_community.chat_models import ChatHuggingFace

class ModelManager:
    """模型管理器 - 负责初始化大模型并支持本地降级"""
    
    @staticmethod
    def get_llm(
        use_local: bool = False,
        model_name: Optional[str] = None,
        temperature: float = 0.7,
        streaming: bool = True,
        timeout: int = 600
    ):
        """
        获取大语言模型实例
        
        Args:
            use_local: 是否使用本地模型
            model_name: 模型名称
            temperature: 温度参数
            streaming: 是否流式输出
            timeout: 超时时间
            
        Returns:
            语言模型实例
        """
        if use_local:
            # 使用本地模型（这里使用 HuggingFace 作为示例）
            # 实际部署时需要根据具体的本地模型配置进行调整
            try:
                model_name = model_name or "mistralai/Mistral-7B-v0.1"
                llm = HuggingFaceHub(
                    repo_id=model_name,
                    model_kwargs={
                        "temperature": temperature,
                        "max_length": 1024
                    }
                )
                chat_model = ChatHuggingFace(llm=llm)
                return chat_model
            except Exception as e:
                print(f"本地模型初始化失败，回退到远程模型: {e}")
                # 本地模型失败时回退到远程模型
                use_local = False
        
        # 使用远程模型（OpenAI 兼容接口）
        api_key = os.getenv("OPENAI_API_KEY", os.getenv("COZE_WORKLOAD_IDENTITY_API_KEY"))
        base_url = os.getenv("OPENAI_BASE_URL", os.getenv("COZE_INTEGRATION_MODEL_BASE_URL"))
        
        model_name = model_name or os.getenv("MODEL_NAME", "gpt-4o-mini")
        
        llm = ChatOpenAI(
            model=model_name,
            api_key=api_key,
            base_url=base_url,
            temperature=temperature,
            streaming=streaming,
            timeout=timeout
        )
        
        return llm

    @staticmethod
    def get_model_config() -> Dict[str, Any]:
        """
        获取模型配置
        
        Returns:
            模型配置字典
        """
        return {
            "default_model": os.getenv("MODEL_NAME", "gpt-4o-mini"),
            "api_key": os.getenv("OPENAI_API_KEY", os.getenv("COZE_WORKLOAD_IDENTITY_API_KEY")),
            "base_url": os.getenv("OPENAI_BASE_URL", os.getenv("COZE_INTEGRATION_MODEL_BASE_URL")),
            "temperature": float(os.getenv("MODEL_TEMPERATURE", "0.7")),
            "timeout": int(os.getenv("MODEL_TIMEOUT", "600"))
        }
