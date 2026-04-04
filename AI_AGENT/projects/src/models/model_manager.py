import os
from typing import Optional, Dict, Any

# 尝试导入 Ollama 相关模块
try:
    from langchain_ollama import ChatOllama, OllamaEmbeddings
except ImportError:
    print("警告: langchain-ollama 未安装，本地模型功能将不可用")
    ChatOllama = None
    OllamaEmbeddings = None

# 尝试导入 OpenAI 相关模块
try:
    from langchain_openai import ChatOpenAI, OpenAIEmbeddings
except ImportError:
    print("警告: langchain-openai 未安装，远程模型功能将不可用")
    ChatOpenAI = None
    OpenAIEmbeddings = None

# 尝试导入 HuggingFace 相关模块
try:
    from langchain_community.embeddings import HuggingFaceEmbeddings
except ImportError:
    print("警告: langchain-community 未安装，HuggingFace 模型功能将不可用")
    HuggingFaceEmbeddings = None


class ModelManager:
    """模型管理器 - 负责初始化大模型并支持本地Ollama部署"""

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
            use_local: 是否使用本地Ollama模型
            model_name: 模型名称
            temperature: 温度参数
            streaming: 是否流式输出
            timeout: 超时时间

        Returns:
            语言模型实例
        """
        # 检查是否有可用的模型
        if not ChatOllama and not ChatOpenAI:
            raise Exception("没有可用的模型，请安装 langchain-ollama 或 langchain-openai")

        if use_local:
            # 使用本地Ollama模型
            if ChatOllama:
                try:
                    ollama_base_url = os.getenv("OLLAMA_BASE_URL", "http://localhost:11434")
                    ollama_model = model_name or os.getenv("OLLAMA_MODEL", "qwen2.5:14b")

                    llm = ChatOllama(
                        model=ollama_model,
                        base_url=ollama_base_url,
                        temperature=temperature,
                        streaming=streaming
                    )

                    # 测试本地模型连接
                    llm.invoke("test")
                    print(f"本地Ollama模型初始化成功: {ollama_model}")
                    return llm
                except Exception as e:
                    print(f"本地Ollama模型初始化失败: {e}")
            else:
                print("警告: ChatOllama 不可用，无法使用本地模型")

        # 优先使用远程模型（OpenAI 兼容接口）
        if ChatOpenAI:
            try:
                api_key = os.getenv("OPENAI_API_KEY")
                base_url = os.getenv("OPENAI_BASE_URL")

                # 检查远程模型配置是否完整
                if not api_key:
                    raise Exception("远程模型 API 密钥未配置")

                remote_model_name = model_name or os.getenv("MODEL_NAME", "gpt-4o-mini")

                llm = ChatOpenAI(
                    model=remote_model_name,
                    api_key=api_key,
                    base_url=base_url,
                    temperature=temperature,
                    streaming=streaming,
                    timeout=timeout
                )

                # 测试远程模型连接
                llm.invoke("test")
                print(f"远程模型初始化成功: {remote_model_name}")
                return llm
            except Exception as e:
                print(f"远程模型初始化失败: {e}")
        else:
            print("警告: ChatOpenAI 不可用，无法使用远程模型")

        # 远程模型失败时回退到本地Ollama模型
        if ChatOllama:
            try:
                ollama_base_url = os.getenv("OLLAMA_BASE_URL", "http://localhost:11434")
                ollama_model = model_name or os.getenv("OLLAMA_MODEL", "qwen2.5:14b")

                llm = ChatOllama(
                    model=ollama_model,
                    base_url=ollama_base_url,
                    temperature=temperature,
                    streaming=streaming
                )

                # 测试本地模型连接
                llm.invoke("test")
                print(f"回退到本地Ollama模型初始化成功: {ollama_model}")
                return llm
            except Exception as e:
                print(f"本地Ollama模型初始化失败: {e}")

        # 所有模型都失败
        raise Exception("所有模型初始化失败，请检查配置")

    @staticmethod
    def get_model_config() -> Dict[str, Any]:
        """
        获取模型配置

        Returns:
            模型配置字典
        """
        return {
            "default_model": os.getenv("MODEL_NAME", "gpt-4o-mini"),
            "ollama_model": os.getenv("OLLAMA_MODEL", "qwen2.5:14b"),
            "ollama_base_url": os.getenv("OLLAMA_BASE_URL", "http://localhost:11434"),
            "api_key": os.getenv("OPENAI_API_KEY"),
            "base_url": os.getenv("OPENAI_BASE_URL"),
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
        # 尝试本地Ollama embedding模型（无论use_local值如何，都作为备选）
        def try_local_ollama():
            try:
                # 检查Ollama模块是否可用
                if not OllamaEmbeddings:
                    raise Exception("OllamaEmbeddings 模块未导入")
                
                ollama_base_url = os.getenv("OLLAMA_BASE_URL", "http://localhost:11434")
                ollama_embedding_model = model_name or os.getenv("OLLAMA_EMBEDDING_MODEL", "qwen3-embedding:8b")
                
                print(f"尝试初始化本地Ollama embedding模型: {ollama_embedding_model}，URL: {ollama_base_url}")

                embedding = OllamaEmbeddings(
                    model=ollama_embedding_model,
                    base_url=ollama_base_url
                )

                # 测试embedding
                print("测试Ollama embedding模型...")
                result = embedding.embed_query("test")
                print(f"Ollama embedding测试成功，向量长度: {len(result)}")
                print(f"本地Ollama embedding模型初始化成功: {ollama_embedding_model}")
                return embedding
            except Exception as e:
                print(f"本地Ollama embedding模型初始化失败: {e}")
                import traceback
                traceback.print_exc()
                return None

        # 尝试HuggingFace本地模型
        def try_huggingface():
            try:
                if not HuggingFaceEmbeddings:
                    raise Exception("HuggingFaceEmbeddings 模块未导入")
                hf_model_name = model_name or "sentence-transformers/all-MiniLM-L6-v2"
                embedding = HuggingFaceEmbeddings(
                    model_name=hf_model_name,
                    model_kwargs={"device": "cpu"},
                    encode_kwargs={"normalize_embeddings": True}
                )
                # 测试embedding
                embedding.embed_query("test")
                print(f"本地HuggingFace embedding模型初始化成功: {hf_model_name}")
                return embedding
            except Exception as e:
                print(f"本地HuggingFace embedding模型初始化失败: {e}")
                return None

        # 尝试远程模型
        def try_remote():
            try:
                if not OpenAIEmbeddings:
                    raise Exception("OpenAIEmbeddings 模块未导入")
                api_key = os.getenv("EMBEDDING_API_KEY") or os.getenv("OPENAI_API_KEY")
                base_url = os.getenv("EMBEDDING_BASE_URL") or os.getenv("OPENAI_BASE_URL")

                if not api_key:
                    raise Exception("远程embedding模型 API 密钥未配置")

                # 尝试不同的embedding模型
                embedding_models = [
                    model_name or os.getenv("EMBEDDING_MODEL_NAME"),
                    "text-embedding-3-small",
                    "text-embedding-ada-002"
                ]
                
                for remote_model_name in embedding_models:
                    if not remote_model_name:
                        continue
                    try:
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
                        print(f"远程embedding模型 {remote_model_name} 初始化失败: {e}")
                        continue
                return None
            except Exception as e:
                print(f"远程embedding模型初始化失败: {e}")
                return None

        # 执行模型初始化逻辑
        if use_local:
            # 优先本地模型
            embedding = try_local_ollama()
            if embedding:
                return embedding
            
            embedding = try_huggingface()
            if embedding:
                return embedding
            
            # 本地模型失败，尝试远程模型
            embedding = try_remote()
            if embedding:
                return embedding
        else:
            # 优先远程模型
            embedding = try_remote()
            if embedding:
                return embedding
            
            # 远程模型失败，尝试本地模型作为备选
            print("远程模型失败，尝试本地Ollama模型...")
            embedding = try_local_ollama()
            if embedding:
                return embedding
            
            embedding = try_huggingface()
            if embedding:
                return embedding

        # 所有模型都失败
        raise Exception("所有embedding模型初始化失败，请检查配置")
