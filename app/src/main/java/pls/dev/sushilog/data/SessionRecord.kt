package pls.dev.sushilog.data

/** Registro de una sesión de sushi completada. */
data class SessionRecord(
    val id: String,
    val date: String,
    val restaurant: String,
    val pieces: Map<String, Int>,
    val totalPieces: Int
)
