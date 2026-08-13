"""Compatibility session API backed exclusively by the project MySQL.

Older Agent tools import ``get_session`` from this module. Keeping that API
lets the multi-Agent graph stay unchanged while using one MySQL database path.
"""

from __future__ import annotations

import os
from threading import Lock

from sqlalchemy import Engine, URL
from sqlalchemy.orm import Session, sessionmaker

from storage.database.mysql_client import MysqlSettings, get_mysql_engine


_SessionLocal: sessionmaker[Session] | None = None
_schema_initialized = False
_schema_lock = Lock()


def get_db_url() -> URL:
    """Return a structured MySQL URL without exposing credentials in strings."""

    return MysqlSettings.from_env().sqlalchemy_url()


def get_engine() -> Engine:
    """Return the shared MySQL engine."""

    return get_mysql_engine()


def _auto_create_enabled() -> bool:
    value = os.getenv("XINGYUN_MYSQL_AUTO_CREATE_AI_SCHEMA", "true")
    return value.strip().lower() in {"1", "true", "yes", "on"}


def ensure_ai_schema(engine: Engine | None = None) -> None:
    """Create only missing ``ai_*`` tables; never drop or rewrite business data."""

    global _schema_initialized
    if _schema_initialized or not _auto_create_enabled():
        return

    with _schema_lock:
        if _schema_initialized:
            return
        from storage.database.shared.model import Base

        Base.metadata.create_all(bind=engine or get_engine(), checkfirst=True)
        _schema_initialized = True


def get_sessionmaker() -> sessionmaker[Session]:
    global _SessionLocal
    if _SessionLocal is None:
        engine = get_engine()
        ensure_ai_schema(engine)
        _SessionLocal = sessionmaker(
            bind=engine,
            autoflush=False,
            expire_on_commit=False,
        )
    return _SessionLocal


def get_session() -> Session:
    return get_sessionmaker()()

__all__ = [
    "get_db_url",
    "get_engine",
    "ensure_ai_schema",
    "get_sessionmaker",
    "get_session",
]

