package pls.dev.sushitracker
import org.junit.Test
import org.junit.Assert.*
import pls.dev.sushitracker.data.*
class StatsLogicTest {
    @Test
    fun testNutritionalCalculations() {
        val pieces = mapOf(
            "nigiri" to 10,  
            "maki" to 5      
        )
        val record = SessionRecord(
            id = "test-1",
            date = "2024-01-01T12:00:00",
            restaurant = "Test",
            pieces = pieces,
            totalPieces = 15
        )
        var totalKcal = 0
        var totalSalmon = 0
        var totalRice = 0
        val customPieces = emptyList<CustomPiece>()
        record.pieces.forEach { (id, count) ->
            totalKcal += getPieceKcal(id, customPieces) * count
            totalSalmon += getPieceSalmonCount(id, customPieces) * count
            totalRice += getPieceRiceGrams(id, customPieces) * count
        }
        assertEquals(700, totalKcal)
        assertEquals(10, totalSalmon)
        assertEquals(175, totalRice)
    }
}
