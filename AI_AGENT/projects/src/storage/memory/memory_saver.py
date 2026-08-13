"""LangGraph process-local short-term memory.

Business and learning data are persisted in MySQL. Conversation checkpoints
remain process-local until a reviewed MySQL checkpointer is introduced; this
keeps the Agent database stack single-vendor without pretending PostgreSQL is
still available.
"""

from __future__ import annotations

from threading import Lock

from langgraph.checkpoint.base import BaseCheckpointSaver
from langgraph.checkpoint.memory import MemorySaver


_checkpointer: MemorySaver | None = None
_lock = Lock()


def get_memory_saver() -> BaseCheckpointSaver:
    global _checkpointer
    if _checkpointer is None:
        with _lock:
            if _checkpointer is None:
                _checkpointer = MemorySaver()
    return _checkpointer


__all__ = ["get_memory_saver"]
