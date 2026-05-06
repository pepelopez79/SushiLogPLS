package pls.dev.sushitracker.data

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import androidx.core.content.edit

class AchievementManager(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("sushi_achievements", Context.MODE_PRIVATE)
    private val gson = Gson()
    private val unlockedKey = "unlocked_achievements"

    private val sessionStorage = SessionStorage(context)

    fun getUnlockedIds(): Set<String> {
        val json = prefs.getString(unlockedKey, null) ?: return emptySet()
        val type = object : TypeToken<Set<String>>() {}.type
        return gson.fromJson(json, type)
    }

    private fun saveUnlockedIds(ids: Set<String>) {
        prefs.edit { putString(unlockedKey, gson.toJson(ids)) }
    }

    fun getProgress(achievement: Achievement): AchievementProgress {
        val sessions = sessionStorage.getSessions()
        val unlockedIds = getUnlockedIds()
        val alreadyUnlocked = unlockedIds.contains(achievement.id)

        val rawCurrent = when (val req = achievement.requirement) {
            is AchievementRequirement.TotalPieces -> {
                sessions.sumOf { it.totalPieces }
            }
            is AchievementRequirement.SessionPieces -> {
                sessions.maxOfOrNull { it.totalPieces } ?: 0
            }
            is AchievementRequirement.SpecificPieceTotal -> {
                sessions.sumOf { it.pieces[req.pieceId] ?: 0 }
            }
            is AchievementRequirement.SpecificPieceSession -> {
                sessions.maxOfOrNull { it.pieces[req.pieceId] ?: 0 } ?: 0
            }
            is AchievementRequirement.SessionsCompleted -> {
                sessions.size
            }
            is AchievementRequirement.PieceVariety -> {
                sessions.flatMap { it.pieces.keys }
                    .filter { pieceId -> sessions.any { (it.pieces[pieceId] ?: 0) > 0 } }
                    .toSet().size
            }
            is AchievementRequirement.AllPiecesInSession -> {
                sessions.maxOfOrNull { session ->
                    session.pieces.count { (_, count) -> count >= req.minCount }
                } ?: 0
            }
        }

        val target = if (achievement.requirement is AchievementRequirement.AllPiecesInSession) {
            SUSHI_PIECES.size
        } else {
            when (val req = achievement.requirement) {
                is AchievementRequirement.TotalPieces -> req.count
                is AchievementRequirement.SessionPieces -> req.count
                is AchievementRequirement.SpecificPieceTotal -> req.count
                is AchievementRequirement.SpecificPieceSession -> req.count
                is AchievementRequirement.SessionsCompleted -> req.count
                is AchievementRequirement.PieceVariety -> req.count
                else -> 0
            }
        }

        val isComplete = alreadyUnlocked || rawCurrent >= target

        return AchievementProgress(
            current = rawCurrent,
            target = target,
            isComplete = isComplete
        )
    }

    fun checkAndUnlockAll(): List<Achievement> {
        val newlyUnlocked = mutableListOf<Achievement>()
        val currentUnlocked = getUnlockedIds().toMutableSet()

        for (achievement in ACHIEVEMENTS) {
            if (!currentUnlocked.contains(achievement.id)) {
                val progress = getProgress(achievement)
                if (progress.isComplete) {
                    currentUnlocked.add(achievement.id)
                    newlyUnlocked.add(achievement)
                }
            }
        }

        if (newlyUnlocked.isNotEmpty()) {
            saveUnlockedIds(currentUnlocked)
        }
        return newlyUnlocked
    }

    fun getAllAchievementsWithStatus(): List<AchievementWithStatus> {
        val unlockedIds = getUnlockedIds()
        return ACHIEVEMENTS.map { achievement ->
            val progress = getProgress(achievement)
            AchievementWithStatus(
                achievement = achievement,
                isUnlocked = unlockedIds.contains(achievement.id),
                progress = progress
            )
        }
    }

    fun syncAchievements() {
        val currentUnlocked = getUnlockedIds().toMutableSet()
        val validUnlocked = mutableSetOf<String>()
        val sessions = sessionStorage.getSessions()

        saveUnlockedIds(emptySet())
        for (achievement in ACHIEVEMENTS) {
            val progress = getProgress(achievement)
            if (progress.current >= progress.target) {
                validUnlocked.add(achievement.id)
            }
        }
        saveUnlockedIds(validUnlocked)
    }

    fun clearAchievements() {
        saveUnlockedIds(emptySet())
    }
}