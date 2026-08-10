"""Disabled compatibility module for the retired Supabase data path.

The current system uses authenticated WebBE/MySQL access. Keeping these names as
fail-fast shims prevents an old import from silently restoring an external
database connection.
"""

_REMOVAL_MESSAGE = "Supabase access is disabled; use WebBE's authenticated local MySQL services."


def get_supabase_credentials():
    raise RuntimeError(_REMOVAL_MESSAGE)


def get_supabase_client(*_args, **_kwargs):
    raise RuntimeError(_REMOVAL_MESSAGE)
