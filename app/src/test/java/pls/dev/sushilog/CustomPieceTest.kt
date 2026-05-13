package pls.dev.sushilog
import org.junit.Test
import org.junit.Assert.*
import pls.dev.sushilog.data.*
class CustomPieceTest {
    @Test
    fun testCustomPieceLogic() {
        val customPiece = CustomPiece(
            id = "custom_test",
            name = "Test Custom",
            emoji = "🍱",
            kcal = 100,
            salmonCount = 2,
            riceGrams = 50
        )
        val customPieces = listOf(customPiece)
        val kcal = getPieceKcal("custom_test", customPieces)
        val salmon = getPieceSalmonCount("custom_test", customPieces)
        val rice = getPieceRiceGrams("custom_test", customPieces)
        assertEquals(100, kcal)
        assertEquals(2, salmon)
        assertEquals(50, rice)
    }
}
