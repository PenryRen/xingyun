import os
from typing import Optional

import httpx
from supabase import create_client, Client, ClientOptions

_env_loaded = False


def _load_env() -> None:
    global _env_loaded

    if _env_loaded or (os.getenv("COZE_SUPABASE_URL") and os.getenv("COZE_SUPABASE_ANON_KEY")):
        return

    try:
        from dotenv import load_dotenv
        load_dotenv()
        if os.getenv("COZE_SUPABASE_URL") and os.getenv("COZE_SUPABASE_ANON_KEY"):
            _env_loaded = True
            return
    except ImportError:
        pass

    try:
        from coze_workload_identity import Client as WorkloadClient

        client = WorkloadClient()
        env_vars = client.get_project_env_vars()
        client.close()

        for env_var in env_vars:
            if not os.getenv(env_var.key):
                os.environ[env_var.key] = env_var.value

        _env_loaded = True
    except Exception:
        pass


def get_supabase_credentials() -> tuple[str, str]:
    _load_env()

    url = os.getenv("COZE_SUPABASE_URL")
    anon_key = os.getenv("COZE_SUPABASE_ANON_KEY")

    if not url:
        raise ValueError("COZE_SUPABASE_URL is not set")
    if not anon_key:
        raise ValueError("COZE_SUPABASE_ANON_KEY is not set")

    return url, anon_key


def get_supabase_client(token: Optional[str] = None) -> Client:
    url, anon_key = get_supabase_credentials()

    http_client = httpx.Client(
        timeout=httpx.Timeout(
            connect=20.0,
            read=60.0,
            write=60.0,
            pool=10.0,
        ),
        limits=httpx.Limits(
            max_connections=100,
            max_keepalive_connections=20,
            keepalive_expiry=30.0,
        ),
        http2=True,
        follow_redirects=True,
    )

    if token:
        options = ClientOptions(
            httpx_client=http_client,
            headers={"Authorization": f"Bearer {token}"},
            auto_refresh_token=False,
        )
    else:
        options = ClientOptions(
            httpx_client=http_client,
            auto_refresh_token=False,
        )

    return create_client(url, anon_key, options=options)
