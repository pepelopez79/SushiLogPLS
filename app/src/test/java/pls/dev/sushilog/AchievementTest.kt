package pls.dev.sushilog

import org.junit.Test
import org.junit.Assert.*
import pls.dev.sushilog.data.*

/**
 * Tests para el sistema de logros.
 * Verifica que todos los logros están correctamente definidos
 * y que la lógica de progreso funciona.
 */
class AchievementTest {

    @Test
    fun allAchievements_haveUniqueIds() {
        val ids = ACHIEVEMENTS.map { it.id }
        assertEquals(ids.size, ids.toSet().size)
    }

    @Test
    fun allAchievements_haveValidCategory() {
        ACHIEVEMENTS.forEach { achievement ->
            assertNotNull(achievement.category)
        }
    }

    @Test
    fun achievementProgress_percentage_isClamped() {
        val progress = AchievementProgress(current = 200, target = 100, isComplete = true)
        assertEquals(1f, progress.percentage, 0.001f)

        val empty = AchievementProgress(current = 0, target = 100, isComplete = false)
        assertEquals(0f, empty.percentage, 0.001f)
    }

    @Test
    fun achievementProgress_displayCurrent_showsTargetWhenComplete() {
        val progress = AchievementProgress(current = 75, target = 100, isComplete = true)
        assertEquals(100, progress.displayCurrent)
    }

    @Test
    fun achievementProgress_displayCurrent_showsActualWhenIncomplete() {
        val progress = AchievementProgress(current = 75, target = 100, isComplete = false)
        assertEquals(75, progress.displayCurrent)
    }

    @Test
    fun achievements_count_is20() {
        assertEquals(20, ACHIEVEMENTS.size)
    }

    @Test
    fun achievementStrings_existForAllIds() {
        val strings = AppStrings.get(AppLanguage.ENGLISH)
        ACHIEVEMENTS.forEach { achievement ->
            val title = AppStrings.getAchievementTitle(achievement.id, strings)
            val desc = AppStrings.getAchievementDescription(achievement.id, strings)
            assertTrue("Missing title for ${achievement.id}", title.isNotEmpty())
            assertTrue("Missing desc for ${achievement.id}", desc.isNotEmpty())
        }
    }
}

