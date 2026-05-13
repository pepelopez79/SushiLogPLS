package pls.dev.sushilog.data

data class SushiPiece(
    val id: String,
    val name: String,
    val iconId: Int,
    val kcal: Int = 0,
    val salmonCount: Int = 0,
    val riceGrams: Int = 0
)

val SUSHI_PIECES = listOf(
    SushiPiece("nigiri",    "Nigiri",   pls.dev.sushilog.R.drawable.nigiri, kcal = 50,  salmonCount = 1, riceGrams = 10),
    SushiPiece("sashimi",   "Sashimi",  pls.dev.sushilog.R.drawable.sashimi, kcal = 35,  salmonCount = 1, riceGrams = 0),
    SushiPiece("maki",      "Maki",     pls.dev.sushilog.R.drawable.maki, kcal = 40,  salmonCount = 0, riceGrams = 15),
    SushiPiece("onigiri",   "Onigiri",  pls.dev.sushilog.R.drawable.onigiri, kcal = 120, salmonCount = 0, riceGrams = 80),
    SushiPiece("uramaki",   "Uramaki",  pls.dev.sushilog.R.drawable.uramaki, kcal = 45,  salmonCount = 0, riceGrams = 15),
    SushiPiece("gunkan",    "Gunkan",   pls.dev.sushilog.R.drawable.gunkan, kcal = 60,  salmonCount = 0, riceGrams = 15),
    SushiPiece("temaki",    "Temaki",   pls.dev.sushilog.R.drawable.temaki, kcal = 100, salmonCount = 0, riceGrams = 30),
    SushiPiece("gyoza",     "Gyoza",    pls.dev.sushilog.R.drawable.gyoza, kcal = 45,  salmonCount = 0, riceGrams = 0),
    SushiPiece("tempura",   "Tempura",  pls.dev.sushilog.R.drawable.shrimp, kcal = 60,  salmonCount = 0, riceGrams = 0),
    SushiPiece("edamame",   "Edamame",  pls.dev.sushilog.R.drawable.edamame, kcal = 15,  salmonCount = 0, riceGrams = 0),
    SushiPiece("takoyaki",  "Takoyaki", pls.dev.sushilog.R.drawable.takoyaki, kcal = 55,  salmonCount = 0, riceGrams = 0),
)

fun getPieceIconId(id: String, customPieces: List<CustomPiece> = emptyList()): Int {
    SUSHI_PIECES.find { it.id == id }?.let { return it.iconId }
    customPieces.find { it.id == id }?.let { return it.iconId }
    if (id.startsWith("custom_")) return pls.dev.sushilog.R.drawable.nigiri
    return pls.dev.sushilog.R.drawable.nigiri
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
