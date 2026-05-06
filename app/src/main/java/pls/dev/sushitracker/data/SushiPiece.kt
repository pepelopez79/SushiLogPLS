package pls.dev.sushitracker.data

import pls.dev.sushitracker.R

data class SushiPiece(
    val id: String,
    val name: String,
    val imageRes: Int,
    val emoji: String,
    val kcal: Int = 0,
    val salmonCount: Int = 0,
    val riceGrams: Int = 0
)

val SUSHI_PIECES = listOf(
    SushiPiece("nigiri",    "Nigiri",         R.drawable.nigiri,    "🍣", kcal = 50, salmonCount = 1, riceGrams = 10),
    SushiPiece("sashimi",   "Sashimi",        R.drawable.sashimi,   "🐟", kcal = 35, salmonCount = 1, riceGrams = 0),
    SushiPiece("maki",      "Maki",           R.drawable.maki,      "🍥", kcal = 40, salmonCount = 0, riceGrams = 15),
    SushiPiece("onigiri",   "Onigiri",        R.drawable.onigiri,   "🍙", kcal = 120, salmonCount = 0, riceGrams = 80),
    SushiPiece("uramaki",   "Uramaki",        R.drawable.uramaki,   "🍘", kcal = 45, salmonCount = 0, riceGrams = 15),
    SushiPiece("gunkan",    "Gunkan",         R.drawable.gunkan,    "🫔", kcal = 60, salmonCount = 0, riceGrams = 15),
    SushiPiece("temaki",    "Temaki",         R.drawable.temaki,    "🌮", kcal = 100, salmonCount = 0, riceGrams = 30),
    SushiPiece("gyoza",     "Gyoza",          R.drawable.gyoza,     "🥟", kcal = 45, salmonCount = 0, riceGrams = 0),
    SushiPiece("tempura",   "Tempura",        R.drawable.tempura,   "🍤", kcal = 60, salmonCount = 0, riceGrams = 0),
    SushiPiece("edamame",   "Edamame",        R.drawable.edamame,   "🫛", kcal = 15, salmonCount = 0, riceGrams = 0),
    SushiPiece("takoyaki",  "Takoyaki",       R.drawable.takoyaki,  "🐙", kcal = 55, salmonCount = 0, riceGrams = 0),
)

fun getPieceEmoji(id: String, customPieces: List<CustomPiece> = emptyList()): String {
    SUSHI_PIECES.find { it.id == id }?.let { return it.emoji }
    customPieces.find { it.id == id }?.let { return it.emoji }
    if (id.startsWith("custom_")) return "❓"
    return "🍣"
}

fun getPieceName(id: String, customPieces: List<CustomPiece> = emptyList()): String {
    SUSHI_PIECES.find { it.id == id }?.let { return it.name }
    customPieces.find { it.id == id }?.let { return it.name }
    if (id.startsWith("custom_")) return "Pieza eliminada"
    return id.replaceFirstChar { it.uppercase() }
}
