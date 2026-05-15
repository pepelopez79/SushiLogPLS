package pls.dev.sushilog.data

import androidx.annotation.DrawableRes
import pls.dev.sushilog.R

/** Definición de un logro con su icono, categoría y requisito de desbloqueo. */
data class Achievement(
    val id: String,
    val titleKey: String,
    val descriptionKey: String,
    @DrawableRes val iconRes: Int = R.drawable.ic_launcher_foreground,
    val category: AchievementCategory,
    val requirement: AchievementRequirement
)

/** Progreso actual del usuario hacia un logro específico. */
data class AchievementProgress(
    val current: Int,
    val target: Int,
    val isComplete: Boolean
) {
    val displayCurrent: Int get() = if (isComplete) target else current

    val percentage: Float get() = (displayCurrent.toFloat() / target).coerceIn(0f, 1f)
}

/** Logro con su estado de desbloqueo y progreso actual. */
data class AchievementWithStatus(
    val achievement: Achievement,
    val isUnlocked: Boolean,
    val progress: AchievementProgress
)

/** Categorías de logros para agrupar en la UI. */
enum class AchievementCategory {
    SESSIONS_COUNT, TOTAL_PIECES, SESSION_PIECES, SPECIFIC_PIECE, VARIETY
}

/** Tipos de requisitos para desbloquear logros. */
sealed class AchievementRequirement {
    data class TotalPieces(val count: Int) : AchievementRequirement()
    data class SessionPieces(val count: Int) : AchievementRequirement()
    data class SpecificPieceTotal(val pieceId: String, val count: Int) : AchievementRequirement()
    data class SpecificPieceSession(val pieceId: String, val count: Int) : AchievementRequirement()
    data class SessionsCompleted(val count: Int) : AchievementRequirement()
    data class PieceVariety(val count: Int) : AchievementRequirement()
    data class AllPiecesInSession(val minCount: Int) : AchievementRequirement()
}

/** Catálogo completo de logros disponibles en la app (20 logros). */
val ACHIEVEMENTS = listOf(
    Achievement(
        id = "first_session",
        titleKey = "achievement_first_session_title",
        descriptionKey = "achievement_first_session_desc",
        iconRes = R.drawable.achievement20,
        category = AchievementCategory.SESSIONS_COUNT,
        requirement = AchievementRequirement.SessionsCompleted(1)
    ),
    Achievement(
        id = "sessions_5",
        titleKey = "achievement_sessions_5_title",
        descriptionKey = "achievement_sessions_5_desc",
        iconRes = R.drawable.achievement14,
        category = AchievementCategory.SESSIONS_COUNT,
        requirement = AchievementRequirement.SessionsCompleted(5)
    ),
    Achievement(
        id = "sessions_25",
        titleKey = "achievement_sessions_25_title",
        descriptionKey = "achievement_sessions_25_desc",
        iconRes = R.drawable.achievement15,
        category = AchievementCategory.SESSIONS_COUNT,
        requirement = AchievementRequirement.SessionsCompleted(25)
    ),
    Achievement(
        id = "sessions_50",
        titleKey = "achievement_sessions_50_title",
        descriptionKey = "achievement_sessions_50_desc",
        iconRes = R.drawable.achievement16,
        category = AchievementCategory.SESSIONS_COUNT,
        requirement = AchievementRequirement.SessionsCompleted(50)
    ),

    Achievement(
        id = "total_100",
        titleKey = "achievement_total_100_title",
        descriptionKey = "achievement_total_100_desc",
        iconRes = R.drawable.achievement1,
        category = AchievementCategory.TOTAL_PIECES,
        requirement = AchievementRequirement.TotalPieces(100)
    ),
    Achievement(
        id = "total_500",
        titleKey = "achievement_total_500_title",
        descriptionKey = "achievement_total_500_desc",
        iconRes = R.drawable.achievement2,
        category = AchievementCategory.TOTAL_PIECES,
        requirement = AchievementRequirement.TotalPieces(500)
    ),
    Achievement(
        id = "total_1000",
        titleKey = "achievement_total_1000_title",
        descriptionKey = "achievement_total_1000_desc",
        iconRes = R.drawable.achievement3,
        category = AchievementCategory.TOTAL_PIECES,
        requirement = AchievementRequirement.TotalPieces(1000)
    ),
    Achievement(
        id = "total_5000",
        titleKey = "achievement_total_5000_title",
        descriptionKey = "achievement_total_5000_desc",
        iconRes = R.drawable.achievement4,
        category = AchievementCategory.TOTAL_PIECES,
        requirement = AchievementRequirement.TotalPieces(5000)
    ),

    Achievement(
        id = "session_30",
        titleKey = "achievement_session_30_title",
        descriptionKey = "achievement_session_30_desc",
        iconRes = R.drawable.achievement5,
        category = AchievementCategory.SESSION_PIECES,
        requirement = AchievementRequirement.SessionPieces(30)
    ),
    Achievement(
        id = "session_50",
        titleKey = "achievement_session_50_title",
        descriptionKey = "achievement_session_50_desc",
        iconRes = R.drawable.achievement6,
        category = AchievementCategory.SESSION_PIECES,
        requirement = AchievementRequirement.SessionPieces(50)
    ),
    Achievement(
        id = "session_100",
        titleKey = "achievement_session_100_title",
        descriptionKey = "achievement_session_100_desc",
        iconRes = R.drawable.achievement7,
        category = AchievementCategory.SESSION_PIECES,
        requirement = AchievementRequirement.SessionPieces(100)
    ),

    Achievement(
        id = "sashimi_session_20",
        titleKey = "achievement_sashimi_session_20_title",
        descriptionKey = "achievement_sashimi_session_20_desc",
        iconRes = R.drawable.achievement13,
        category = AchievementCategory.SPECIFIC_PIECE,
        requirement = AchievementRequirement.SpecificPieceSession("sashimi", 20)
    ),
    Achievement(
        id = "nigiri_session_30",
        titleKey = "achievement_nigiri_session_30_title",
        descriptionKey = "achievement_nigiri_session_30_desc",
        iconRes = R.drawable.achievement12,
        category = AchievementCategory.SPECIFIC_PIECE,
        requirement = AchievementRequirement.SpecificPieceSession("nigiri", 30)
    ),
    Achievement(
        id = "gyoza_50",
        titleKey = "achievement_gyoza_50_title",
        descriptionKey = "achievement_gyoza_50_desc",
        iconRes = R.drawable.achievement11,
        category = AchievementCategory.SPECIFIC_PIECE,
        requirement = AchievementRequirement.SpecificPieceTotal("gyoza", 50)
    ),
    Achievement(
        id = "nigiri_100",
        titleKey = "achievement_nigiri_100_title",
        descriptionKey = "achievement_nigiri_100_desc",
        iconRes = R.drawable.achievement8,
        category = AchievementCategory.SPECIFIC_PIECE,
        requirement = AchievementRequirement.SpecificPieceTotal("nigiri", 100)
    ),
    Achievement(
        id = "sashimi_100",
        titleKey = "achievement_sashimi_100_title",
        descriptionKey = "achievement_sashimi_100_desc",
        iconRes = R.drawable.achievement9,
        category = AchievementCategory.SPECIFIC_PIECE,
        requirement = AchievementRequirement.SpecificPieceTotal("sashimi", 100)
    ),
    Achievement(
        id = "maki_100",
        titleKey = "achievement_maki_100_title",
        descriptionKey = "achievement_maki_100_desc",
        iconRes = R.drawable.achievement10,
        category = AchievementCategory.SPECIFIC_PIECE,
        requirement = AchievementRequirement.SpecificPieceTotal("maki", 100)
    ),

    Achievement(
        id = "variety_6",
        titleKey = "achievement_variety_6_title",
        descriptionKey = "achievement_variety_6_desc",
        iconRes = R.drawable.achievement17,
        category = AchievementCategory.VARIETY,
        requirement = AchievementRequirement.PieceVariety(6)
    ),
    Achievement(
        id = "variety_all",
        titleKey = "achievement_variety_all_title",
        descriptionKey = "achievement_variety_all_desc",
        iconRes = R.drawable.achievement18,
        category = AchievementCategory.VARIETY,
        requirement = AchievementRequirement.PieceVariety(12)
    ),
    Achievement(
        id = "all_in_one",
        titleKey = "achievement_all_in_one_title",
        descriptionKey = "achievement_all_in_one_desc",
        iconRes = R.drawable.achievement19,
        category = AchievementCategory.VARIETY,
        requirement = AchievementRequirement.AllPiecesInSession(1)
    ),
)
