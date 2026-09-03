from __future__ import annotations

import asyncio
import json
import sys
from pathlib import Path

import httpx
import pytest


PROJECT_ROOT = Path(__file__).resolve().parents[1]
SRC_ROOT = PROJECT_ROOT / "src"
if str(SRC_ROOT) not in sys.path:
    sys.path.insert(0, str(SRC_ROOT))

from models import model_manager  # noqa: E402
from models.model_manager import (  # noqa: E402
    ModelManager,
    SPARK_DEFAULT_BASE_URL,
    SPARK_DEFAULT_MODEL,
)
from models.spark import ChatSpark  # noqa: E402
from langchain_core.messages import HumanMessage  # noqa: E402


def test_model_config_uses_spark_defaults(monkeypatch):
    monkeypatch.setenv("SPARK_API_PASSWORD", "test-password")
    monkeypatch.delenv("SPARK_BASE_URL", raising=False)
    monkeypatch.delenv("SPARK_MODEL", raising=False)

    config = ModelManager.get_model_config()

    assert config["provider"] == "spark"
    assert config["display_name"] == "Spark Ultra-32K"
    assert config["configured"] is True
    assert config["base_url"] == SPARK_DEFAULT_BASE_URL
    assert config["default_model"] == SPARK_DEFAULT_MODEL


def test_get_llm_passes_spark_credentials_to_compatible_client(monkeypatch):
    captured = {}

    class FakeChatSpark:
        def __init__(self, **kwargs):
            captured.update(kwargs)

    monkeypatch.setattr(model_manager, "ChatSpark", FakeChatSpark)
    monkeypatch.setenv("SPARK_API_PASSWORD", "test-password")
    monkeypatch.setenv("SPARK_BASE_URL", "https://spark.example/v1/")
    monkeypatch.setenv("SPARK_MODEL", "4.0Ultra")

    llm = ModelManager.get_llm(temperature=0.3, streaming=False, timeout=30)

    assert isinstance(llm, FakeChatSpark)
    assert captured == {
        "model": "4.0Ultra",
        "api_key": "test-password",
        "base_url": "https://spark.example/v1/",
        "temperature": 0.3,
        "streaming": False,
        "timeout": 30,
        "max_tokens": 8000,
    }


def test_get_llm_rejects_missing_spark_password(monkeypatch):
    monkeypatch.delenv("SPARK_API_PASSWORD", raising=False)

    with pytest.raises(Exception, match="所有模型初始化失败"):
        ModelManager.get_llm(use_local=False)


def test_chat_spark_translates_protocol_fields():
    llm = ChatSpark(
        model="4.0Ultra",
        api_key="test-password",
        base_url="https://spark.example/v1/",
        max_tokens=8000,
    )

    payload = llm._get_request_payload(
        [HumanMessage(content="你好")],
        parallel_tool_calls=True,
    )

    assert payload["max_tokens"] == 8000
    assert "max_completion_tokens" not in payload
    assert "parallel_tool_calls" not in payload
    assert payload["extra_body"]["tool_calls_switch"] is True


def test_chat_spark_sends_expected_http_body():
    captured = {}

    def handler(request: httpx.Request) -> httpx.Response:
        captured.update(json.loads(request.content))
        return httpx.Response(
            200,
            request=request,
            json={
                "id": "test-id",
                "object": "chat.completion",
                "created": 0,
                "model": "4.0Ultra",
                "choices": [
                    {
                        "index": 0,
                        "message": {"role": "assistant", "content": "ok"},
                        "finish_reason": "stop",
                    }
                ],
                "usage": {
                    "prompt_tokens": 1,
                    "completion_tokens": 1,
                    "total_tokens": 2,
                },
            },
        )

    http_client = httpx.Client(transport=httpx.MockTransport(handler))
    llm = ChatSpark(
        model="4.0Ultra",
        api_key="test-password",
        base_url="https://spark.example/v1/",
        max_tokens=8000,
        http_client=http_client,
    )

    response = llm.invoke([HumanMessage(content="你好")])

    assert response.content == "ok"
    assert captured["model"] == "4.0Ultra"
    assert captured["max_tokens"] == 8000
    assert "max_completion_tokens" not in captured
    assert captured["tool_calls_switch"] is True


def test_teaching_agent_can_start_when_embedding_backend_is_unavailable(monkeypatch):
    from agents import teaching_assistant_agent

    def unavailable_knowledge_base():
        raise RuntimeError("embedding backend unavailable")

    monkeypatch.setenv("SPARK_API_PASSWORD", "test-password")
    monkeypatch.setattr(
        teaching_assistant_agent,
        "_create_knowledge_base",
        unavailable_knowledge_base,
    )

    agent = teaching_assistant_agent.build_teaching_assistant_agent()

    assert agent is not None


def test_health_reports_missing_spark_password(monkeypatch):
    import main

    monkeypatch.delenv("SPARK_API_PASSWORD", raising=False)
    main._health_cache.clear()

    result = asyncio.run(main.health_check(force=True))

    assert result["model_configured"] is False
    assert result["model_provider"] == "spark"
    assert result["model_name"] == "4.0Ultra"
    assert result["model_probe_code"] == "CONFIG_INCOMPLETE"
    assert "SPARK_API_PASSWORD" in result["model_message"]


def test_health_probes_spark_without_exposing_password(monkeypatch):
    import main

    probed_models = []

    async def fake_probe(model_name):
        probed_models.append(model_name)
        return {"ok": True, "code": "OK", "message": "test model available"}

    monkeypatch.setenv("SPARK_API_PASSWORD", "test-password")
    monkeypatch.setattr(main, "_probe_model", fake_probe)
    main._health_cache.clear()

    result = asyncio.run(main.health_check(force=True))

    assert result["model_configured"] is True
    assert result["model_available"] is True
    assert probed_models == ["4.0Ultra"]
    assert "test-password" not in json.dumps(result)


def test_default_service_entry_passes_text_to_agent_selector(monkeypatch):
    import main
    from agents import agent as agent_module

    selected = object()

    def select_agent(request):
        assert isinstance(request, str)
        return selected

    monkeypatch.setattr(agent_module, "get_agent_by_request", select_agent)

    assert main.GraphService()._get_graph() is selected
