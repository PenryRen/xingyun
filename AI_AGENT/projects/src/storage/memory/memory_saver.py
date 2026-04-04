from langgraph.checkpoint.memory import MemorySaver
from langgraph.checkpoint.base import BaseCheckpointSaver
from typing import Optional
import logging

logger = logging.getLogger(__name__)


class MemoryManager:
    """Memory Manager 单例类
    
    使用纯内存存储，不依赖任何数据库
    """

    _instance: Optional['MemoryManager'] = None
    _checkpointer: Optional[MemorySaver] = None

    def __new__(cls):
        if cls._instance is None:
            cls._instance = super().__new__(cls)
        return cls._instance

    def get_checkpointer(self) -> BaseCheckpointSaver:
        """获取内存 checkpointer"""
        if self._checkpointer is None:
            self._checkpointer = MemorySaver()
            logger.info("MemorySaver initialized successfully")
        return self._checkpointer


_memory_manager: Optional[MemoryManager] = None


def get_memory_saver() -> BaseCheckpointSaver:
    """获取内存 checkpointer
    
    Returns:
        BaseCheckpointSaver: 内存存储实例
    """
    global _memory_manager
    if _memory_manager is None:
        _memory_manager = MemoryManager()
    return _memory_manager.get_checkpointer()
