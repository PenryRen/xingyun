from typing import Optional
import logging

# 尝试导入 langgraph 相关模块
try:
    from langgraph.checkpoint.memory import MemorySaver
    from langgraph.checkpoint.base import BaseCheckpointSaver
except ImportError as e:
    print(f"警告: langgraph 未安装，内存存储功能将不可用: {e}")
    MemorySaver = None
    BaseCheckpointSaver = None

logger = logging.getLogger(__name__)


class MemoryManager:
    """Memory Manager 单例类
    
    使用纯内存存储，不依赖任何数据库
    """

    _instance: Optional['MemoryManager'] = None
    _checkpointer = None

    def __new__(cls):
        if cls._instance is None:
            cls._instance = super().__new__(cls)
        return cls._instance

    def get_checkpointer(self):
        """获取内存 checkpointer"""
        if self._checkpointer is None:
            if MemorySaver:
                self._checkpointer = MemorySaver()
                logger.info("MemorySaver initialized successfully")
            else:
                logger.warning("MemorySaver 不可用，返回 None")
        return self._checkpointer


_memory_manager: Optional[MemoryManager] = None


def get_memory_saver():
    """获取内存 checkpointer
    
    Returns:
        BaseCheckpointSaver: 内存存储实例或 None
    """
    global _memory_manager
    if _memory_manager is None:
        _memory_manager = MemoryManager()
    return _memory_manager.get_checkpointer()
