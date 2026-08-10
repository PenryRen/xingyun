"""Disabled legacy Agent/Supabase paper-generation tools.

Intelligent paper generation has no verified local question-bank integration in
the current architecture and therefore remains explicitly unavailable.
"""

_REMOVAL_MESSAGE = "Legacy intelligent-paper tools are disabled until a verified local question bank is integrated."


def _disabled(*_args, **_kwargs):
    raise RuntimeError(_REMOVAL_MESSAGE)


generate_intelligent_paper = _disabled
generate_enhanced_paper_by_weak_points = _disabled
