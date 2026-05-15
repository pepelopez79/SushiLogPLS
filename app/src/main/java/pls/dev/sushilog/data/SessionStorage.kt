package pls.dev.sushilog.data

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.DayOfWeek
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters
import androidx.core.content.edit

/**
 * Gestiona la persistencia de sesiones de sushi en SharedPreferences.
 * Incluye backup automático para prevenir pérdida de datos por corrupción.
 */
class SessionStorage(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("sushi_log_sessions", Context.MODE_PRIVATE)
    private val gson = Gson()
    private val key = "sessions"
    private val backupKey = "sessions_backup"

    companion object {
        private const val TAG = "SessionStorage"
    }

    /**
     * Obtiene las sesiones guardadas de forma segura.
     * Si los datos están corruptos, intenta restaurar desde backup.
     */
    fun getSessions(): List<SessionRecord> {
        val json = prefs.getString(key, null) ?: return emptyList()
        if (json.isBlank()) return emptyList()

        return try {
            val type = object : TypeToken<List<SessionRecord>>() {}.type
            val sessions: List<SessionRecord>? = gson.fromJson(json, type)
            sessions ?: emptyList()
        } catch (e: JsonSyntaxException) {
            Log.e(TAG, "JSON corrupto en sesiones principales, intentando backup", e)
            restoreFromBackup()
        } catch (e: Exception) {
            Log.e(TAG, "Error inesperado leyendo sesiones, intentando backup", e)
            restoreFromBackup()
        }
    }

    /**
     * Intenta restaurar las sesiones desde el backup.
     */
    private fun restoreFromBackup(): List<SessionRecord> {
        val backupJson = prefs.getString(backupKey, null) ?: return emptyList()
        return try {
            val type = object : TypeToken<List<SessionRecord>>() {}.type
            val sessions: List<SessionRecord>? = gson.fromJson(backupJson, type)
            if (sessions != null && sessions.isNotEmpty()) {
                // Restaurar backup como datos principales
                prefs.edit { putString(key, backupJson) }
                Log.i(TAG, "Sesiones restauradas desde backup: ${sessions.size} sesiones")
            }
            sessions ?: emptyList()
        } catch (e: Exception) {
            Log.e(TAG, "Backup también corrupto, datos perdidos", e)
            emptyList()
        }
    }

    /**
     * Crea un backup de las sesiones actuales antes de cualquier escritura.
     */
    private fun backupCurrentSessions() {
        val currentJson = prefs.getString(key, null)
        if (!currentJson.isNullOrBlank()) {
            prefs.edit { putString(backupKey, currentJson) }
        }
    }

    /**
     * Guarda una sesión nueva de forma segura.
     * Hace backup antes de escribir para prevenir pérdida de datos.
     */
    fun saveSession(session: SessionRecord) {
        backupCurrentSessions()
        val sessions = getSessions().toMutableList()
        sessions.add(0, session)
        val json = gson.toJson(sessions)
        prefs.edit(commit = true) { putString(key, json) }
    }

    /**
     * Elimina una sesión individual por ID.
     * Hace backup antes de escribir.
     */
    fun deleteSession(id: String) {
        backupCurrentSessions()
        val sessions = getSessions().toMutableList()
        val sizeBefore = sessions.size
        sessions.removeAll { it.id == id }

        // Protección: no permitir borrado masivo accidental
        if (sizeBefore > 1 && sessions.isEmpty()) {
            Log.e(TAG, "Intento de borrado masivo bloqueado: se intentó borrar todas las sesiones al eliminar id=$id")
            return
        }

        val json = gson.toJson(sessions)
        prefs.edit(commit = true) { putString(key, json) }
    }

    /**
     * Elimina todas las sesiones (solo desde ajustes, con confirmación del usuario).
     */
    fun deleteAllSessions() {
        backupCurrentSessions()
        prefs.edit(commit = true) { remove(key) }
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
