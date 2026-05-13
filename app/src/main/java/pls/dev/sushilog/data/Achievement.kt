package pls.dev.sushilog.data

import androidx.annotation.DrawableRes
import pls.dev.sushilog.R

data class Achievement(
    val id: String,
    val titleKey: String,
    val descriptionKey: String,
    @DrawableRes val iconRes: Int = R.drawable.ic_launcher_foreground,
    val category: AchievementCategory,
    val requirement: AchievementRequirement
)

data class AchievementProgress(
    val current: Int,
    val target: Int,
    val isComplete: Boolean
) {
    val displayCurrent: Int get() = if (isComplete) target else current

    val percentage: Float get() = (displayCurrent.toFloat() / target).coerceIn(0f, 1f)
}

data class AchievementWithStatus(
    val achievement: Achievement,
    val isUnlocked: Boolean,
    val progress: AchievementProgress
)

enum class AchievementCategory {
    TOTAL_PIECES, SESSION_PIECES, SPECIFIC_PIECE, SESSIONS_COUNT, VARIETY, SPECIAL
}

sealed class AchievementRequirement {
    data class TotalPieces(val count: Int) : AchievementRequirement()
    data class SessionPieces(val count: Int) : AchievementRequirement()
    data class SpecificPieceTotal(val pieceId: String, val count: Int) : AchievementRequirement()
    data class SpecificPieceSession(val pieceId: String, val count: Int) : AchievementRequirement()
    data class SessionsCompleted(val count: Int) : AchievementRequirement()
    data class PieceVariety(val count: Int) : AchievementRequirement()
    data class AllPiecesInSession(val minCount: Int) : AchievementRequirement()
}

val ACHIEVEMENTS = listOf(
    Achievement(
        id = "total_100",
        titleKey = "achievement_total_100_title",
        descriptionKey = "achievement_total_100_desc",
        category = AchievementCategory.TOTAL_PIECES,
        requirement = AchievementRequirement.TotalPieces(100)
    ),
    Achievement(
        id = "total_500",
        titleKey = "achievement_total_500_title",
        descriptionKey = "achievement_total_500_desc",
        category = AchievementCategory.TOTAL_PIECES,
        requirement = AchievementRequirement.TotalPieces(500)
    ),
    Achievement(
        id = "total_1000",
        titleKey = "achievement_total_1000_title",
        descriptionKey = "achievement_total_1000_desc",
        category = AchievementCategory.TOTAL_PIECES,
        requirement = AchievementRequirement.TotalPieces(1000)
    ),
    Achievement(
        id = "total_5000",
        titleKey = "achievement_total_5000_title",
        descriptionKey = "achievement_total_5000_desc",
        category = AchievementCategory.TOTAL_PIECES,
        requirement = AchievementRequirement.TotalPieces(5000)
    ),

    Achievement(
        id = "session_30",
        titleKey = "achievement_session_30_title",
        descriptionKey = "achievement_session_30_desc",
        category = AchievementCategory.SESSION_PIECES,
        requirement = AchievementRequirement.SessionPieces(30)
    ),
    Achievement(
        id = "session_50",
        titleKey = "achievement_session_50_title",
        descriptionKey = "achievement_session_50_desc",
        category = AchievementCategory.SESSION_PIECES,
        requirement = AchievementRequirement.SessionPieces(50)
    ),
    Achievement(
        id = "session_100",
        titleKey = "achievement_session_100_title",
        descriptionKey = "achievement_session_100_desc",
        category = AchievementCategory.SESSION_PIECES,
        requirement = AchievementRequirement.SessionPieces(100)
    ),

    Achievement(
        id = "nigiri_100",
        titleKey = "achievement_nigiri_100_title",
        descriptionKey = "achievement_nigiri_100_desc",
        category = AchievementCategory.SPECIFIC_PIECE,
        requirement = AchievementRequirement.SpecificPieceTotal("nigiri", 100)
    ),
    Achievement(
        id = "sashimi_100",
        titleKey = "achievement_sashimi_100_title",
        descriptionKey = "achievement_sashimi_100_desc",
        category = AchievementCategory.SPECIFIC_PIECE,
        requirement = AchievementRequirement.SpecificPieceTotal("sashimi", 100)
    ),
    Achievement(
        id = "maki_100",
        titleKey = "achievement_maki_100_title",
        descriptionKey = "achievement_maki_100_desc",
        category = AchievementCategory.SPECIFIC_PIECE,
        requirement = AchievementRequirement.SpecificPieceTotal("maki", 100)
    ),
    Achievement(
        id = "gyoza_50",
        titleKey = "achievement_gyoza_50_title",
        descriptionKey = "achievement_gyoza_50_desc",
        category = AchievementCategory.SPECIFIC_PIECE,
        requirement = AchievementRequirement.SpecificPieceTotal("gyoza", 50)
    ),

    Achievement(
        id = "nigiri_session_30",
        titleKey = "achievement_nigiri_session_30_title",
        descriptionKey = "achievement_nigiri_session_30_desc",
        category = AchievementCategory.SPECIFIC_PIECE,
        requirement = AchievementRequirement.SpecificPieceSession("nigiri", 30)
    ),
    Achievement(
        id = "sashimi_session_20",
        titleKey = "achievement_sashimi_session_20_title",
        descriptionKey = "achievement_sashimi_session_20_desc",
        category = AchievementCategory.SPECIFIC_PIECE,
        requirement = AchievementRequirement.SpecificPieceSession("sashimi", 20)
    ),

    Achievement(
        id = "sessions_5",
        titleKey = "achievement_sessions_5_title",
        descriptionKey = "achievement_sessions_5_desc",
        category = AchievementCategory.SESSIONS_COUNT,
        requirement = AchievementRequirement.SessionsCompleted(5)
    ),
    Achievement(
        id = "sessions_25",
        titleKey = "achievement_sessions_25_title",
        descriptionKey = "achievement_sessions_25_desc",
        category = AchievementCategory.SESSIONS_COUNT,
        requirement = AchievementRequirement.SessionsCompleted(25)
    ),
    Achievement(
        id = "sessions_50",
        titleKey = "achievement_sessions_50_title",
        descriptionKey = "achievement_sessions_50_desc",
        category = AchievementCategory.SESSIONS_COUNT,
        requirement = AchievementRequirement.SessionsCompleted(50)
    ),

    Achievement(
        id = "variety_6",
        titleKey = "achievement_variety_6_title",
        descriptionKey = "achievement_variety_6_desc",
        category = AchievementCategory.VARIETY,
        requirement = AchievementRequirement.PieceVariety(6)
    ),
    Achievement(
        id = "variety_all",
        titleKey = "achievement_variety_all_title",
        descriptionKey = "achievement_variety_all_desc",
        category = AchievementCategory.VARIETY,
        requirement = AchievementRequirement.PieceVariety(12)
    ),

    Achievement(
        id = "all_in_one",
        titleKey = "achievement_all_in_one_title",
        descriptionKey = "achievement_all_in_one_desc",
        category = AchievementCategory.SPECIAL,
        requirement = AchievementRequirement.AllPiecesInSession(1)
    ),
    Achievement(
        id = "first_session",
        titleKey = "achievement_first_session_title",
        descriptionKey = "achievement_first_session_desc",
        category = AchievementCategory.SESSIONS_COUNT,
        requirement = AchievementRequirement.SessionsCompleted(1)
    ),
)
