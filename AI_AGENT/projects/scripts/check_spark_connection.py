"""发送一条最小请求，检查讯飞星火聊天模型连通性。"""

from __future__ import annotations

import json
import os
import sys
from pathlib import Path


PROJECT_ROOT = Path(__file__).resolve().parents[1]
SRC_ROOT = PROJECT_ROOT / "src"
if str(SRC_ROOT) not in sys.path:
    sys.path.insert(0, str(SRC_ROOT))

try:
    from dotenv import load_dotenv

    load_dotenv(PROJECT_ROOT / ".env")
except ImportError:
    pass

from langchain_core.messages import HumanMessage  # noqa: E402
from models.model_manager import ModelManager  # noqa: E402


def _safe_exception_chain(exc: BaseException, secret: str) -> list[dict[str, str]]:
    chain = []
    current: BaseException | None = exc
    seen: set[int] = set()
    while current is not None and id(current) not in seen:
        seen.add(id(current))
        message = str(current).replace(secret, "[REDACTED]")
        chain.append({"type": type(current).__name__, "message": message})
        current = current.__cause__ or current.__context__
    return chain


def main() -> int:
    secret = os.getenv("SPARK_API_PASSWORD", "").strip()
    if not secret:
        print(json.dumps({"ok": False, "error": "SPARK_API_PASSWORD 未配置"}, ensure_ascii=False))
        return 2

    result = {"ok": False, "provider": "spark", "model": "4.0Ultra"}
    try:
        llm = ModelManager.get_llm(
            streaming=False,
            temperature=0.1,
            timeout=30,
            max_tokens=32,
        )
        response = llm.invoke([HumanMessage(content="连通性测试：请只回复 OK")])
        result.update(ok=True, reply=str(response.content)[:100])
        print(json.dumps(result, ensure_ascii=False))
        return 0
    except Exception as exc:
        safe_message = str(exc).replace(secret, "[REDACTED]")
        result.update(
            error_type=type(exc).__name__,
            error=safe_message,
            causes=_safe_exception_chain(exc, secret),
        )
        print(json.dumps(result, ensure_ascii=False))
        return 1


if __name__ == "__main__":
    raise SystemExit(main())
