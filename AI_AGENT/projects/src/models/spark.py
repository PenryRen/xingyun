"""讯飞星火 HTTP 接口与 LangChain 的轻量兼容层。"""

from typing import Any

from langchain_core.language_models import LanguageModelInput
from langchain_openai import ChatOpenAI


class ChatSpark(ChatOpenAI):
    """保留 ChatOpenAI 的 Agent/流式能力，适配星火协议字段。"""

    use_responses_api: bool = False
    stream_usage: bool = False

    @property
    def lc_secrets(self) -> dict[str, str]:
        return {"openai_api_key": "SPARK_API_PASSWORD"}

    def _get_request_payload(
        self,
        input_: LanguageModelInput,
        *,
        stop: list[str] | None = None,
        **kwargs: Any,
    ) -> dict:
        payload = super()._get_request_payload(input_, stop=stop, **kwargs)
        # LangChain 会将 max_tokens 改名，但星火 HTTP 协议仍使用 max_tokens。
        if "max_completion_tokens" in payload:
            payload["max_tokens"] = payload.pop("max_completion_tokens")
        payload.pop("parallel_tool_calls", None)

        # 星火默认可能返回对象；LangChain/OpenAI 工具解析要求数组格式。
        extra_body = dict(payload.get("extra_body") or {})
        extra_body["tool_calls_switch"] = True
        payload["extra_body"] = extra_body
        return payload
