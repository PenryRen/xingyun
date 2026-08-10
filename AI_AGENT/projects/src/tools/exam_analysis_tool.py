"""Legacy compatibility shim for the removed model/database exam tools.

Exam queries, score calculations, reports, and learning-profile evidence now live
in WebBE's ``AiLearningService`` and are scoped to the authenticated user.  The
Agent deliberately has no database tools and receives only an anonymous numeric
summary for optional explanation.

The old functions remain as explicit failure points so an accidental future
import cannot silently restore Supabase access or send full answer data to a
model.
"""


_REMOVAL_MESSAGE = (
    "Legacy Agent exam tools are disabled. "
    "Use WebBE POST /api/ai/learning/workspace for authenticated local data."
)


def analyze_exam_paper(*_args, **_kwargs):
    """Prevent accidental restoration of the old full-answer model analysis."""
    raise RuntimeError(_REMOVAL_MESSAGE)


def get_student_weak_points(*_args, **_kwargs):
    """Prevent accidental restoration of the old direct Supabase query."""
    raise RuntimeError(_REMOVAL_MESSAGE)
