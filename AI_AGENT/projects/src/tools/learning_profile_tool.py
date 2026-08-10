"""Disabled legacy Supabase learning-profile tools.

Learning evidence is now read from local MySQL by WebBE. These compatibility
names fail fast so a future import cannot silently restore the retired data
path.
"""

_REMOVAL_MESSAGE = "Legacy learning-profile tools are disabled; use WebBE's authenticated AI learning APIs."


def _disabled(*_args, **_kwargs):
    raise RuntimeError(_REMOVAL_MESSAGE)


create_student_profile = _disabled
update_learning_profile = _disabled
get_student_learning_profile = _disabled
update_knowledge_progress = _disabled
get_knowledge_progress = _disabled
