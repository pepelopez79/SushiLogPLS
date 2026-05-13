package pls.dev.sushilog
import org.junit.Test
import org.junit.Assert.*
import pls.dev.sushilog.data.*
import java.time.LocalDate
class SettingsTest {
    @Test
    fun appLanguageEnumTest() {
        assertEquals("en", AppLanguage.ENGLISH.code)
        assertEquals("es", AppLanguage.SPANISH.code)
    }
    @Test
    fun appThemeEnumTest() {
        val themes = AppTheme.values()
        assertEquals(3, themes.size)
        assertTrue(themes.contains(AppTheme.DARK))
        assertTrue(themes.contains(AppTheme.LIGHT))
        assertTrue(themes.contains(AppTheme.SALMON))
    }
}
