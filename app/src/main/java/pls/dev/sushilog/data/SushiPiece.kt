package pls.dev.sushilog.data

data class SushiPiece(
    val id: String,
    val name: String,
    val emoji: String,
    val kcal: Int = 0,
    val salmonCount: Int = 0,
    val riceGrams: Int = 0
)

val SUSHI_PIECES = listOf(
    SushiPiece("nigiri",    "Nigiri",   "🍣", kcal = 50,  salmonCount = 1, riceGrams = 10),
    SushiPiece("sashimi",   "Sashimi",  "🐟", kcal = 35,  salmonCount = 1, riceGrams = 0),
    SushiPiece("maki",      "Maki",     "🍥", kcal = 40,  salmonCount = 0, riceGrams = 15),
    SushiPiece("onigiri",   "Onigiri",  "🍙", kcal = 120, salmonCount = 0, riceGrams = 80),
    SushiPiece("uramaki",   "Uramaki",  "🍘", kcal = 45,  salmonCount = 0, riceGrams = 15),
    SushiPiece("gunkan",    "Gunkan",   "🫔", kcal = 60,  salmonCount = 0, riceGrams = 15),
    SushiPiece("temaki",    "Temaki",   "🌮", kcal = 100, salmonCount = 0, riceGrams = 30),
    SushiPiece("gyoza",     "Gyoza",    "🥟", kcal = 45,  salmonCount = 0, riceGrams = 0),
    SushiPiece("tempura",   "Tempura",  "🍤", kcal = 60,  salmonCount = 0, riceGrams = 0),
    SushiPiece("edamame",   "Edamame",  "🫛", kcal = 15,  salmonCount = 0, riceGrams = 0),
    SushiPiece("takoyaki",  "Takoyaki", "🐙", kcal = 55,  salmonCount = 0, riceGrams = 0),
)

fun getPieceEmoji(id: String, customPieces: List<CustomPiece> = emptyList()): String {
    SUSHI_PIECES.find { it.id == id }?.let { return it.emoji }
    customPieces.find { it.id == id }?.let { return it.emoji }
    if (id.startsWith("custom_")) return "❓"
    return "🍣"
}

fun getPieceName(id: String, customPieces: List<CustomPiece> = emptyList(), strings: AppStrings.Strings? = null): String {
    SUSHI_PIECES.find { it.id == id }?.let { return it.name }
    customPieces.find { it.id == id }?.let { return it.name }
    if (id.startsWith("custom_")) return strings?.deletedPiece ?: "Pieza eliminada"
    return id.replaceFirstChar { it.uppercase() }
}

fun getPieceKcal(id: String, customPieces: List<CustomPiece> = emptyList()): Int {
    SUSHI_PIECES.find { it.id == id }?.let { return it.kcal }
    customPieces.find { it.id == id }?.let { return it.kcal }
    return 0
}

fun getPieceSalmonCount(id: String, customPieces: List<CustomPiece> = emptyList()): Int {
    SUSHI_PIECES.find { it.id == id }?.let { return it.salmonCount }
    customPieces.find { it.id == id }?.let { return it.salmonCount }
    return 0
}

fun getPieceRiceGrams(id: String, customPieces: List<CustomPiece> = emptyList()): Int {
    SUSHI_PIECES.find { it.id == id }?.let { return it.riceGrams }
    customPieces.find { it.id == id }?.let { return it.riceGrams }
    return 0
}
