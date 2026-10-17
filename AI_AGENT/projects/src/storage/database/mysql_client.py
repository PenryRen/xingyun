"""Shared MySQL connection support for the AI Agent.

Both the deterministic WebBE learning-data queries and the Agent-owned ``ai_*``
tables use this module. Credentials are read from environment variables and are
never interpolated into log messages or source files.
"""

from __future__ import annotations

import os
from contextlib import contextmanager
from dataclasses import dataclass
from functools import lru_cache
from typing import Iterator, Optional

from sqlalchemy import Engine, URL, create_engine
from sqlalchemy.engine import Connection


class MysqlConfigurationError(ValueError):
    """Raised when required local MySQL connection settings are missing."""


@dataclass(frozen=True)
class MysqlSettings:
    """Connection settings for the project's existing Docker MySQL database."""

    host: str
    port: int
    database: str
    username: str
    password: str

    @classmethod
    def from_env(cls) -> "MysqlSettings":
        username = os.getenv("XINGYUN_MYSQL_USERNAME", "").strip()
        password = os.getenv("XINGYUN_MYSQL_PASSWORD", "")
        if not username:
            raise MysqlConfigurationError("XINGYUN_MYSQL_USERNAME is not set")
        if not password:
            raise MysqlConfigurationError("XINGYUN_MYSQL_PASSWORD is not set")

        raw_port = os.getenv("XINGYUN_MYSQL_PORT", "3306").strip()
        try:
            port = int(raw_port)
        except ValueError as exc:
            raise MysqlConfigurationError(
                "XINGYUN_MYSQL_PORT must be an integer"
            ) from exc
        if not 1 <= port <= 65535:
            raise MysqlConfigurationError(
                "XINGYUN_MYSQL_PORT must be between 1 and 65535"
            )

        return cls(
            host=os.getenv("XINGYUN_MYSQL_HOST", "mysql").strip() or "mysql",
            port=port,
            database=os.getenv("XINGYUN_MYSQL_DATABASE", "wdd").strip() or "wdd",
            username=username,
            password=password,
        )

    def sqlalchemy_url(self) -> URL:
        """Build a URL without interpolating credentials into a string."""

        return URL.create(
            drivername="mysql+pymysql",
            username=self.username,
            password=self.password,
            host=self.host,
            port=self.port,
            database=self.database,
            query={"charset": "utf8mb4"},
        )


def create_mysql_engine(settings: Optional[MysqlSettings] = None) -> Engine:
    """Create a bounded SQLAlchemy pool shared by read and write repositories."""

    resolved = settings or MysqlSettings.from_env()
    return create_engine(
        resolved.sqlalchemy_url(),
        pool_pre_ping=True,
        pool_size=5,
        max_overflow=5,
        pool_recycle=1800,
        pool_timeout=10,
        connect_args={"connect_timeout": 5, "read_timeout": 10, "write_timeout": 10},
    )


@lru_cache(maxsize=1)
def get_mysql_engine() -> Engine:
    """Return the process-wide engine without opening a connection eagerly."""

    return create_mysql_engine()


@contextmanager
def readonly_connection(engine: Optional[Engine] = None) -> Iterator[Connection]:
    """Yield a transaction-scoped connection.

    MySQL sessions are explicitly switched to read-only and restored before
    returning to the pool. Other SQLAlchemy dialects are accepted so the query
    service can be unit-tested with an in-memory database.
    """

    resolved_engine = engine or get_mysql_engine()
    with resolved_engine.connect() as connection:
        is_mysql = connection.dialect.name == "mysql"
        if is_mysql:
            connection.exec_driver_sql("SET SESSION TRANSACTION READ ONLY")
            connection.commit()

        try:
            with connection.begin():
                yield connection
        finally:
            connection.rollback()
            if is_mysql:
                connection.exec_driver_sql("SET SESSION TRANSACTION READ WRITE")
                connection.commit()


__all__ = [
    "MysqlConfigurationError",
    "MysqlSettings",
    "create_mysql_engine",
    "get_mysql_engine",
    "readonly_connection",
]
