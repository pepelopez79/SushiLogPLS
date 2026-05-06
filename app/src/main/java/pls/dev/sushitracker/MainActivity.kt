package pls.dev.sushitracker

import android.app.Activity
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import pls.dev.sushitracker.data.*
import pls.dev.sushitracker.ui.navigation.SushiNavGraph
import pls.dev.sushitracker.ui.theme.SushiTrackerTheme
import pls.dev.sushitracker.ui.theme.getColorsForTheme
import android.media.RingtoneManager
import kotlinx.coroutines.delay
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

object GlobalAchievementNotifier {
    private val _achievements = MutableSharedFlow<List<Achievement>>(extraBufferCapacity = 1)
    val achievements = _achievements.asSharedFlow()
    fun notify(unlocked: List<Achievement>) {
        if (unlocked.isNotEmpty()) {
            _achievements.tryEmit(unlocked)
        }
    }
}

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        val settingsManager = AppSettingsManager(this)

        setContent {
            var currentTheme by remember { mutableStateOf(settingsManager.getTheme()) }
            var currentLanguage by remember { mutableStateOf(settingsManager.getLanguage()) }
            val strings = remember(currentLanguage) { AppStrings.get(currentLanguage) }

            val colors = getColorsForTheme(currentTheme)

            val view = LocalView.current
            if (!view.isInEditMode) {
                SideEffect {
                    val window = (view.context as Activity).window

                    window.statusBarColor = colors.background.toArgb()
                    window.navigationBarColor = colors.background.toArgb()

                    val isDarkTheme = currentTheme == AppTheme.DARK
                    WindowCompat.getInsetsController(window, view).apply {
                        isAppearanceLightStatusBars = !isDarkTheme
                        isAppearanceLightNavigationBars = !isDarkTheme
                    }
                }
            }

            var visiblePopup by remember { mutableStateOf<Achievement?>(null) }
            LaunchedEffect(Unit) {
                GlobalAchievementNotifier.achievements.collect { unlockedList ->
                    for (achievement in unlockedList) {
                        try {
                            val uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
                            val ringtone = RingtoneManager.getRingtone(applicationContext, uri)
                            ringtone.play()
                        } catch (e: Exception) { e.printStackTrace() }
                        visiblePopup = achievement
                        delay(4000)
                        visiblePopup = null
                        delay(500)
                    }
                }
            }

            SushiTrackerTheme(colors = colors) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = colors.background
                ) {
                    val navController = rememberNavController()

                    Box(modifier = Modifier.safeDrawingPadding()) {
                        SushiNavGraph(
                            navController = navController,
                            currentTheme = currentTheme,
                            currentLanguage = currentLanguage,
                            strings = strings,
                            onThemeChange = { newTheme ->
                                currentTheme = newTheme
                                settingsManager.setTheme(newTheme)
                            },
                            onLanguageChange = { newLang ->
                                currentLanguage = newLang
                                settingsManager.setLanguage(newLang)
                            },
                            colors = colors
                        )

                        AnimatedVisibility(
                            visible = visiblePopup != null,
                            enter = slideInVertically(initialOffsetY = { -it }) + fadeIn(),
                            exit = slideOutVertically(targetOffsetY = { -it }) + fadeOut(),
                            modifier = Modifier.align(Alignment.TopCenter).padding(horizontal = 16.dp, vertical = 24.dp)
                        ) {
                            visiblePopup?.let { achievement ->
                                Card(
                                    shape = RoundedCornerShape(16.dp),
                                    colors = CardDefaults.cardColors(containerColor = colors.primary),
                                    elevation = CardDefaults.cardElevation(8.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(16.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                                    ) {
                                        Box(
                                            modifier = Modifier.size(40.dp).background(colors.onPrimary.copy(alpha = 0.2f), CircleShape),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Icon(Icons.Filled.Star, contentDescription = null, tint = colors.onPrimary)
                                        }
                                        Column {
                                            Text(strings.unlocked, color = colors.onPrimary.copy(alpha = 0.8f), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                            Text(AppStrings.getAchievementTitle(achievement.id, strings), color = colors.onPrimary, fontSize = 16.sp, fontWeight = FontWeight.ExtraBold)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}