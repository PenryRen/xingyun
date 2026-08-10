"""Disabled legacy Supabase exam-recorder tools.

Exam records are owned by WebBE/MySQL. Agent-side writes are intentionally not
available.
"""

_REMOVAL_MESSAGE = "Legacy Agent exam-recorder tools are disabled; use WebBE's authenticated exam APIs."


def _disabled(*_args, **_kwargs):
    raise RuntimeError(_REMOVAL_MESSAGE)


add_questions_to_database = _disabled
record_exam_result = _disabled
get_question_bank = _disabled
