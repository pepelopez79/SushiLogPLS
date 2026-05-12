package pls.dev.sushitracker.ui.screens

import android.annotation.SuppressLint
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pls.dev.sushitracker.data.*
import pls.dev.sushitracker.ui.theme.*
import java.util.UUID

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("DefaultLocale")
@Composable
fun SettingsScreen(
    colors: SushiColors,
    strings: AppStrings.Strings,
    currentTheme: AppTheme,
    currentLanguage: AppLanguage,
    onThemeChange: (AppTheme) -> Unit,
    onLanguageChange: (AppLanguage) -> Unit,
    onOpenCustomPieces: () -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val settingsManager = remember { AppSettingsManager(context) }
    val sessionManager = remember { SessionStorage(context) }

    var showResetDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            IconButton(
                onClick = onBack,
                modifier = Modifier.size(40.dp).clip(CircleShape).background(colors.secondary)
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = strings.back,
                    tint = colors.onSecondary,
                    modifier = Modifier.size(20.dp)
                )
            }
            Text(
                text = strings.settingsTitle,
                color = colors.onBackground,
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier.weight(1f)
            )
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(bottom = 32.dp)
        ) {
            item {
                SectionLabel(strings.appearance, colors)
                Card(
                    modifier = Modifier.fillMaxWidth().shadow(4.dp, RoundedCornerShape(16.dp)),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = colors.surface)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(strings.appTheme, color = colors.onSurface, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            AppTheme.entries.forEach { theme ->
                                ThemeOption(
                                    theme = theme,
                                    isSelected = currentTheme == theme,
                                    colors = colors,
                                    strings = strings,
                                    onClick = { onThemeChange(theme) },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                }
            }

            item {
                SectionLabel(strings.language, colors)
                Card(
                    modifier = Modifier.fillMaxWidth().shadow(4.dp, RoundedCornerShape(16.dp)),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = colors.surface)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            AppLanguage.entries.forEach { lang ->
                                LanguageOption(
                                    language = lang,
                                    isSelected = currentLanguage == lang,
                                    colors = colors,
                                    onClick = { onLanguageChange(lang) },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                }
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth().shadow(4.dp, RoundedCornerShape(16.dp)),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = colors.surface)
                ) {
                    Column {
                        SettingsItem(
                            icon = Icons.Filled.Add,
                            title = strings.customPiecesManage,
                            subtitle = strings.customPiecesSubtitle,
                            colors = colors,
                            onClick = onOpenCustomPieces
                        )
                        HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), color = colors.border)
                        SettingsItem(
                            icon = Icons.Filled.Delete,
                            title = strings.deleteAll,
                            subtitle = strings.deleteAllSubtitle,
                            colors = colors,
                            isDestructive = true,
                            onClick = { showResetDialog = true }
                        )
                    }
                }
            }

            item {
                SectionLabel(strings.information, colors)
                Card(
                    modifier = Modifier.fillMaxWidth().shadow(4.dp, RoundedCornerShape(16.dp)),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = colors.surface)
                ) {
                    val versionName = runCatching {
                        context.packageManager.getPackageInfo(context.packageName, 0).versionName ?: "1.0.0"
                    }.getOrDefault("1.0.0")
                    Column {
                        SettingsItem(icon = Icons.Filled.Info, title = strings.version, subtitle = versionName, colors = colors)
                        HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), color = colors.border)
                        SettingsItem(icon = Icons.Filled.Edit, title = strings.developedBy, subtitle = "PLS", colors = colors)
                    }
                }
            }
        }
    }

    if (showResetDialog) {
        AlertDialog(
            modifier = Modifier.shadow(8.dp, RoundedCornerShape(24.dp)),
            onDismissRequest = { showResetDialog = false },
            containerColor = colors.surface,
            title = { Text(strings.deleteAllConfirmTitle, color = colors.onSurface, fontWeight = FontWeight.Bold) },
            text = { Text(strings.deleteAllConfirmMsg, color = colors.mutedForeground) },
            confirmButton = {
                TextButton(onClick = {
                    sessionManager.deleteAllSessions()
                    AchievementManager(context).clearAchievements()
                    showResetDialog = false
                    Toast.makeText(context, strings.dataDeleted, Toast.LENGTH_SHORT).show()
                }) { Text(strings.deleteAllConfirmBtn, color = MaterialTheme.colorScheme.error) }
            },
            dismissButton = {
                TextButton(onClick = { showResetDialog = false }) { Text(strings.cancel, color = colors.primary) }
            }
        )
    }
}

@Composable
private fun SectionLabel(text: String, colors: SushiColors) {
    Text(
        text = text.uppercase(),
        color = colors.mutedForeground,
        fontSize = 11.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 1.sp,
        modifier = Modifier.padding(bottom = 8.dp, start = 4.dp)
    )
}

@Composable
private fun ThemeOption(
    theme: AppTheme, isSelected: Boolean, colors: SushiColors, strings: AppStrings.Strings,
    onClick: () -> Unit, modifier: Modifier = Modifier
) {
    val themeColors = getColorsForTheme(theme)
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(if (isSelected) colors.primary.copy(alpha = 0.1f) else colors.secondary)
            .border(if (isSelected) 2.dp else 0.dp, if (isSelected) colors.primary else Color.Transparent, RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(10.dp))
        Box(
            modifier = Modifier.size(40.dp).clip(CircleShape).background(themeColors.background).border(1.dp, themeColors.border, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Box(modifier = Modifier.size(20.dp).clip(CircleShape).background(themeColors.primary))
        }
        Text(
            text = when (theme) { AppTheme.DARK -> strings.darkTheme; AppTheme.SALMON -> strings.salmonTheme; AppTheme.LIGHT -> strings.lightTheme },
            color = if (isSelected) colors.primary else colors.onSurface,
            fontSize = 12.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
        )
    }
}

@Composable
private fun LanguageOption(
    language: AppLanguage, isSelected: Boolean, colors: SushiColors,
    onClick: () -> Unit, modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(if (isSelected) colors.primary.copy(alpha = 0.1f) else colors.secondary)
            .border(if (isSelected) 2.dp else 0.dp, if (isSelected) colors.primary else Color.Transparent, RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(vertical = 10.dp, horizontal = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(language.flag, fontSize = 22.sp)
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = language.displayName,
            color = if (isSelected) colors.primary else colors.onSurface,
            fontSize = 11.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
        )
    }
}

@Composable
private fun SettingsItem(
    icon: ImageVector, title: String, subtitle: String, colors: SushiColors,
    isDestructive: Boolean = false, onClick: (() -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Icon(
            imageVector = icon, contentDescription = null,
            tint = if (isDestructive) MaterialTheme.colorScheme.error else colors.mutedForeground,
            modifier = Modifier.size(24.dp)
        )
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                color = if (isDestructive) MaterialTheme.colorScheme.error else colors.onSurface,
                fontSize = 16.sp, fontWeight = FontWeight.Medium
            )
            Text(text = subtitle, color = colors.mutedForeground, fontSize = 13.sp)
        }
        if (onClick != null) {
            Icon(Icons.Filled.KeyboardArrowRight, contentDescription = null, tint = colors.mutedForeground, modifier = Modifier.size(20.dp))
        }
    }
}