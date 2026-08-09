"""Resolve model API hosts outside local Fake-IP DNS and refresh /etc/hosts.

The user's local proxy may return 198.18.0.0/15 Fake-IP addresses to Docker.
For explicitly enabled local development, this module resolves only the model
API hostname through Cloudflare's IP-address DoH endpoint. No credentials are
sent to the resolver and resolved addresses are never persisted in the repo.
"""

from __future__ import annotations

import json
import logging
import os
import threading
import time
import urllib.parse
import urllib.request
from pathlib import Path
from typing import Iterable

logger = logging.getLogger(__name__)

_HOSTS_PATH = Path("/etc/hosts")
_MARKER = "# xingyun-direct-doh"
_thread_started = False
_thread_lock = threading.Lock()


def _model_hostname() -> str | None:
    base_url = os.getenv("COZE_INTEGRATION_MODEL_BASE_URL", "").strip()
    hostname = urllib.parse.urlparse(base_url).hostname
    return hostname.lower() if hostname else None


def _resolve_ipv4(hostname: str) -> list[str]:
    query = urllib.parse.quote(hostname, safe="")
    request = urllib.request.Request(
        f"https://1.1.1.1/dns-query?name={query}&type=A",
        headers={"Accept": "application/dns-json", "User-Agent": "xingyun-agent/1.0"},
    )
    with urllib.request.urlopen(request, timeout=10) as response:
        payload = json.loads(response.read().decode("utf-8"))
    addresses = []
    for answer in payload.get("Answer", []):
        value = str(answer.get("data", "")).strip()
        if answer.get("type") == 1 and value and value not in addresses:
            addresses.append(value)
    if not addresses:
        raise RuntimeError("DoH response did not contain an IPv4 address")
    return addresses


def _replace_hosts_mapping(hostname: str, addresses: Iterable[str]) -> None:
    existing = _HOSTS_PATH.read_text(encoding="utf-8") if _HOSTS_PATH.exists() else ""
    kept = [line for line in existing.splitlines() if _MARKER not in line]
    kept.extend(f"{address} {hostname} {_MARKER}" for address in addresses)
    with _HOSTS_PATH.open("w", encoding="utf-8", newline="\n") as hosts_file:
        hosts_file.write("\n".join(kept).rstrip() + "\n")


def refresh_model_host() -> bool:
    if os.getenv("COZE_DIRECT_DOH_ENABLED", "false").lower() not in {"1", "true", "yes"}:
        return False
    hostname = _model_hostname()
    if not hostname:
        return False
    try:
        addresses = _resolve_ipv4(hostname)
        _replace_hosts_mapping(hostname, addresses)
        logger.info("Refreshed direct DNS mapping for model host (%s address(es))", len(addresses))
        return True
    except Exception as exc:  # network hardening must not prevent Agent startup
        logger.warning("Direct DNS refresh failed: %s", type(exc).__name__)
        return False


def start_model_host_refresher() -> None:
    global _thread_started
    if os.getenv("COZE_DIRECT_DOH_ENABLED", "false").lower() not in {"1", "true", "yes"}:
        return
    with _thread_lock:
        if _thread_started:
            return
        _thread_started = True

    refresh_model_host()
    interval = max(60, int(os.getenv("COZE_DOH_REFRESH_SECONDS", "300")))

    def _refresh_loop() -> None:
        while True:
            time.sleep(interval)
            refresh_model_host()

    threading.Thread(target=_refresh_loop, name="model-doh-refresh", daemon=True).start()
