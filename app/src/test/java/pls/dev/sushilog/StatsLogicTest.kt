package pls.dev.sushilog

import org.junit.Test
import org.junit.Assert.*
import pls.dev.sushilog.data.*

/**
 * Tests para la lógica de cálculos nutricionales y estadísticas.
 * Verifica kcal, arroz y pescado a partir de sesiones.
 */
class StatsLogicTest {

    @Test
    fun nutritionalCalculations_withBuiltInPieces() {
        val pieces = mapOf("nigiri" to 10, "maki" to 5)
        val record = SessionRecord(
            id = "test-1",
            date = "2024-01-01T12:00:00",
            restaurant = "Test Sushi",
            pieces = pieces,
            totalPieces = 15
        )

        val customPieces = emptyList<CustomPiece>()
        var totalKcal = 0
        var totalSalmon = 0
        var totalRice = 0

        record.pieces.forEach { (id, count) ->
            totalKcal += getPieceKcal(id, customPieces) * count
            totalSalmon += getPieceSalmonCount(id, customPieces) * count
            totalRice += getPieceRiceGrams(id, customPieces) * count
        }

        // nigiri: 50kcal*10 + maki: 40kcal*5 = 700
        assertEquals(700, totalKcal)
        // nigiri: 1*10 + maki: 0*5 = 10
        assertEquals(10, totalSalmon)
        // nigiri: 10g*10 + maki: 15g*5 = 175
        assertEquals(175, totalRice)
    }

    @Test
    fun nutritionalCalculations_withCustomPieces() {
        val custom = CustomPiece(
            id = "custom_roll",
            name = "Super Roll",
            kcal = 80,
            salmonCount = 2,
            riceGrams = 20
        )
        val pieces = mapOf("custom_roll" to 3)

        var totalKcal = 0
        var totalSalmon = 0
        var totalRice = 0

        pieces.forEach { (id, count) ->
            totalKcal += getPieceKcal(id, listOf(custom)) * count
            totalSalmon += getPieceSalmonCount(id, listOf(custom)) * count
            totalRice += getPieceRiceGrams(id, listOf(custom)) * count
        }

        assertEquals(240, totalKcal)
        assertEquals(6, totalSalmon)
        assertEquals(60, totalRice)
    }

    @Test
    fun builtInPieces_haveCorrectCount() {
        assertEquals(11, SUSHI_PIECES.size)
    }

    @Test
    fun builtInPiece_lookup_byId() {
        val nigiri = SUSHI_PIECES.find { it.id == "nigiri" }
        assertNotNull(nigiri)
        assertEquals(50, nigiri!!.kcal)
        assertEquals(1, nigiri.salmonCount)
        assertEquals(10, nigiri.riceGrams)
    }

    @Test
    fun sessionRecord_totalPieces_matchesSum() {
        val pieces = mapOf("nigiri" to 5, "sashimi" to 3, "maki" to 2)
        val total = pieces.values.sum()
        val record = SessionRecord(
            id = "test-sum",
            date = "2024-06-15T19:00:00",
            restaurant = "Sakura",
            pieces = pieces,
            totalPieces = total
        )
        assertEquals(10, record.totalPieces)
    }
}
