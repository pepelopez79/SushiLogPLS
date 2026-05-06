package pls.dev.sushitracker.ui.theme

import androidx.compose.ui.graphics.Color
import pls.dev.sushitracker.data.AppTheme

data class SushiColors(
    val background: Color,
    val surface: Color,
    val primary: Color,
    val primaryDark: Color,
    val onPrimary: Color,
    val secondary: Color,
    val onSecondary: Color,
    val onBackground: Color,
    val onSurface: Color,
    val mutedForeground: Color,
    val border: Color,
    val itemBackground: Color,
    val itemForeground: Color
)

val DarkThemeColors = SushiColors(
    background = Color(0xFF1B2838),      // Deep Navy Blue
    surface = Color(0xFF2A3A4A),         // Elevated surface
    primary = Color(0xFF4ECDC4),         // Mint Green/Teal playful
    primaryDark = Color(0xFF2A9D8F),     
    onPrimary = Color(0xFF1B2838),       // Deep Navy text on primary for contrast
    secondary = Color(0xFF3D4D5C),       
    onSecondary = Color(0xFFFFFFFF),
    onBackground = Color(0xFFFFFFFF),
    onSurface = Color(0xFFFFFFFF),
    mutedForeground = Color(0xFF94A3B3),
    border = Color(0xFF394959),
    itemBackground = Color(0xFF2A3A4A),
    itemForeground = Color(0xFFFFFFFF)
)

val LightThemeColors = SushiColors(
    background = Color(0xFFF5F7FA),      // Soft bluish-white
    surface = Color(0xFFFFFFFF),         
    primary = Color(0xFF4ECDC4),         // Mint Green/Teal
    primaryDark = Color(0xFF2A9D8F),
    onPrimary = Color(0xFFFFFFFF),       
    secondary = Color(0xFFE2E8F0),       
    onSecondary = Color(0xFF1B2838),
    onBackground = Color(0xFF1B2838),    
    onSurface = Color(0xFF1B2838),
    mutedForeground = Color(0xFF718096),
    border = Color(0xFFCBD5E0),
    itemBackground = Color(0xFFFFFFFF),
    itemForeground = Color(0xFF1B2838)
)

val SalmonThemeColors = SushiColors(
    background = Color(0xFFFFF3F0),      // Soft Salmon Wash
    surface = Color(0xFFFFFFFF),         // Pure White
    primary = Color(0xFFFF6B57),         // Bright Salmon
    primaryDark = Color(0xFFE65542),
    onPrimary = Color(0xFFFFFFFF),
    secondary = Color(0xFFFFE4DE),       // Light Salmon Base
    onSecondary = Color(0xFFD64A35),     // Deep Salmon accent
    onBackground = Color(0xFF4A3431),    // Warm dark brown
    onSurface = Color(0xFF4A3431),
    mutedForeground = Color(0xFFB58079),
    border = Color(0xFFFFD5CE),
    itemBackground = Color(0xFFFFFFFF),
    itemForeground = Color(0xFF4A3431)
)

fun getColorsForTheme(theme: AppTheme): SushiColors {
    return when (theme) {
        AppTheme.DARK -> DarkThemeColors
        AppTheme.LIGHT -> LightThemeColors
        AppTheme.SALMON -> SalmonThemeColors
    }
}
