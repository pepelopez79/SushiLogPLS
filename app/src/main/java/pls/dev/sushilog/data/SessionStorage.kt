package pls.dev.sushilog.data

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import androidx.annotation.RequiresApi
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.DayOfWeek
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters
import androidx.core.content.edit

/**
 * Gestiona la persistencia de sesiones de sushi en SharedPreferences.
 * Soporta CRUD y cálculo de estadísticas filtradas por periodo.
 */
class SessionStorage(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("sushi_log_sessions", Context.MODE_PRIVATE)
    private val gson = Gson()
    private val key = "sessions"

    fun getSessions(): List<SessionRecord> {
        val json = prefs.getString(key, null) ?: return emptyList()
        val type = object : TypeToken<List<SessionRecord>>() {}.type
        return gson.fromJson(json, type)
    }

    fun saveSession(session: SessionRecord) {
        val sessions = getSessions().toMutableList()
        sessions.add(0, session)
        prefs.edit { putString(key, gson.toJson(sessions)) }
    }

    fun deleteSession(id: String) {
        val sessions = getSessions().toMutableList()
        sessions.removeAll { it.id == id }
        prefs.edit { putString(key, gson.toJson(sessions)) }
    }

    fun deleteAllSessions() {
        prefs.edit { remove(key) }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getStats(filter: StatsFilter): StatsResult {
        val sessions = getSessions()
        val now = LocalDate.now()

        val startDate: LocalDate = when (filter) {
            StatsFilter.ALL -> LocalDate.MIN
            StatsFilter.YEAR -> now.withDayOfYear(1)
            StatsFilter.MONTH -> now.withDayOfMonth(1)
            StatsFilter.WEEK -> now.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
        }

        val filtered = if (filter == StatsFilter.ALL) {
            sessions
        } else {
            sessions.filter { session ->
                try {
                    val sessionDate = LocalDateTime.parse(session.date, DateTimeFormatter.ISO_DATE_TIME)
                        .toLocalDate()
                    !sessionDate.isBefore(startDate)
                } catch (e: Exception) {
                    false
                }
            }
        }

        val pieceStats = mutableMapOf<String, Int>()
        var total = 0

        for (session in filtered) {
            for ((pieceId, count) in session.pieces) {
                if (count > 0) {
                    pieceStats[pieceId] = (pieceStats[pieceId] ?: 0) + count
                    total += count
                }
            }
        }

        val avgPerSession = if (filtered.isNotEmpty()) total.toDouble() / filtered.size else 0.0
        val maxInSession = filtered.maxOfOrNull { it.totalPieces } ?: 0

        return StatsResult(pieceStats, total, filtered.size, avgPerSession, maxInSession)
    }
}

/** Filtros temporales para estadísticas. */
enum class StatsFilter {
    ALL, YEAR, MONTH, WEEK
}

/** Resultado agregado de estadísticas por periodo. */
data class StatsResult(
    val pieceStats: Map<String, Int>,
    val total: Int,
    val sessionCount: Int = 0,
    val avgPerSession: Double = 0.0,
    val maxInSession: Int = 0
)
