package pls.dev.sushilog

import org.junit.Test
import org.junit.Assert.*
import pls.dev.sushilog.data.*

/**
 * Tests para los enums de configuración de la app.
 * Verifica temas, idiomas y sus propiedades.
 */
class SettingsTest {

    @Test
    fun appLanguage_codes_areCorrect() {
        assertEquals("en", AppLanguage.ENGLISH.code)
        assertEquals("es", AppLanguage.SPANISH.code)
        assertEquals("fr", AppLanguage.FRENCH.code)
        assertEquals("it", AppLanguage.ITALIAN.code)
    }

    @Test
    fun appLanguage_has4Entries() {
        assertEquals(4, AppLanguage.entries.size)
    }

    @Test
    fun appTheme_has3Entries() {
        val themes = AppTheme.entries
        assertEquals(3, themes.size)
        assertTrue(themes.contains(AppTheme.DARK))
        assertTrue(themes.contains(AppTheme.LIGHT))
        assertTrue(themes.contains(AppTheme.SALMON))
    }

    @Test
    fun appTheme_ids_areCorrect() {
        assertEquals("dark", AppTheme.DARK.id)
        assertEquals("light", AppTheme.LIGHT.id)
        assertEquals("salmon", AppTheme.SALMON.id)
    }

    @Test
    fun appStrings_returnsStringsForAllLanguages() {
        AppLanguage.entries.forEach { lang ->
            val strings = AppStrings.get(lang)
            assertTrue(strings.back.isNotEmpty())
            assertTrue(strings.settingsTitle.isNotEmpty())
            assertTrue(strings.share.isNotEmpty())
        }
    }
}
