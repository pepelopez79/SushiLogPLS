package pls.dev.sushilog

import org.junit.Test
import org.junit.Assert.*
import pls.dev.sushilog.data.*

/**
 * Tests para la lógica de piezas personalizadas.
 * Verifica que las funciones de consulta nutricional resuelven correctamente
 * las piezas custom por ID.
 */
class CustomPieceTest {

    @Test
    fun customPiece_nutritionalValues_areCorrect() {
        val customPiece = CustomPiece(
            id = "custom_test",
            name = "Test Custom",
            kcal = 100,
            salmonCount = 2,
            riceGrams = 50
        )
        val customPieces = listOf(customPiece)

        assertEquals(100, getPieceKcal("custom_test", customPieces))
        assertEquals(2, getPieceSalmonCount("custom_test", customPieces))
        assertEquals(50, getPieceRiceGrams("custom_test", customPieces))
    }

    @Test
    fun customPiece_defaultValues_areZero() {
        val piece = CustomPiece(id = "custom_default", name = "Default")

        assertEquals(0, piece.kcal)
        assertEquals(0, piece.salmonCount)
        assertEquals(0, piece.riceGrams)
    }

    @Test
    fun unknownPiece_returnsZeroNutrition() {
        val customPieces = emptyList<CustomPiece>()

        assertEquals(0, getPieceKcal("nonexistent", customPieces))
        assertEquals(0, getPieceSalmonCount("nonexistent", customPieces))
        assertEquals(0, getPieceRiceGrams("nonexistent", customPieces))
    }
}
