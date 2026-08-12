from __future__ import annotations

import sys
from pathlib import Path

import pytest


PROJECT_ROOT = Path(__file__).resolve().parents[1]
SRC_ROOT = PROJECT_ROOT / "src"
if str(SRC_ROOT) not in sys.path:
    sys.path.insert(0, str(SRC_ROOT))

from storage.database.mysql_client import (  # noqa: E402
    MysqlConfigurationError,
    MysqlSettings,
)


def test_settings_require_credentials(monkeypatch):
    monkeypatch.delenv("XINGYUN_MYSQL_USERNAME", raising=False)
    monkeypatch.delenv("XINGYUN_MYSQL_PASSWORD", raising=False)

    with pytest.raises(MysqlConfigurationError, match="USERNAME"):
        MysqlSettings.from_env()


def test_settings_build_structured_url_without_manual_interpolation(monkeypatch):
    monkeypatch.setenv("XINGYUN_MYSQL_HOST", "mysql")
    monkeypatch.setenv("XINGYUN_MYSQL_PORT", "3306")
    monkeypatch.setenv("XINGYUN_MYSQL_DATABASE", "wdd")
    monkeypatch.setenv("XINGYUN_MYSQL_USERNAME", "reader")
    monkeypatch.setenv("XINGYUN_MYSQL_PASSWORD", "p@ss/word")

    url = MysqlSettings.from_env().sqlalchemy_url()

    assert url.drivername == "mysql+pymysql"
    assert url.host == "mysql"
    assert url.database == "wdd"
    assert url.password == "p@ss/word"
    assert "p@ss/word" not in str(url)
