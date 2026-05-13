package pls.dev.sushilog.ui.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pls.dev.sushilog.data.*
import pls.dev.sushilog.ui.theme.*

@Composable
fun AchievementsScreen(
    colors: SushiColors,
    onBack: () -> Unit,
    strings: AppStrings.Strings
) {
    val context = LocalContext.current
    val achievementManager = remember { AchievementManager(context) }

    var achievements by remember { mutableStateOf(achievementManager.getAllAchievementsWithStatus()) }

    LaunchedEffect(Unit) {
        achievementManager.checkAndUnlockAll()
        achievements = achievementManager.getAllAchievementsWithStatus()
    }

    val unlockedCount = achievements.count { it.isUnlocked }
    val totalCount = achievements.size

    val categoryOrder = listOf(
        AchievementCategory.SESSIONS_COUNT,
        AchievementCategory.TOTAL_PIECES,
        AchievementCategory.SESSION_PIECES,
        AchievementCategory.SPECIFIC_PIECE,
        AchievementCategory.VARIETY
    )
    val grouped = achievements.groupBy { it.achievement.category }

    Column(modifier = Modifier.fillMaxSize().background(colors.background)) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            IconButton(onClick = onBack, modifier = Modifier.size(40.dp).clip(CircleShape).background(colors.secondary)) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = strings.back, tint = colors.onSecondary, modifier = Modifier.size(20.dp))
            }
            Text(strings.achievementsTitle, color = colors.onBackground, fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, modifier = Modifier.weight(1f))
        }

        Card(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).padding(bottom = 16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = colors.primary.copy(alpha = 0.1f))
        ) {
            Column(modifier = Modifier.fillMaxWidth().padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(painter = painterResource(id = pls.dev.sushilog.R.drawable.achievements), contentDescription = null, tint = Color.Unspecified, modifier = Modifier.size(48.dp))
                Spacer(modifier = Modifier.height(8.dp))
                Text("$unlockedCount / $totalCount", color = colors.primary, fontSize = 32.sp, fontWeight = FontWeight.ExtraBold)
                Text(strings.achievementsUnlocked, color = colors.mutedForeground, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(12.dp))
                Box(modifier = Modifier.fillMaxWidth().height(10.dp).clip(CircleShape).background(colors.secondary)) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(if (totalCount > 0) unlockedCount.toFloat() / totalCount else 0f)
                            .fillMaxHeight().clip(CircleShape).background(colors.primary)
                    )
                }
            }
        }

        LazyColumn(
            modifier = Modifier.padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            categoryOrder.forEach { category ->
                val itemsInCategory = grouped[category] ?: return@forEach
                val categoryUnlocked = itemsInCategory.count { it.isUnlocked }

                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp, bottom = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = getCategoryLabel(category, strings),
                            color = colors.mutedForeground,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 2.sp
                        )
                        Text(
                            text = "$categoryUnlocked/${itemsInCategory.size}",
                            color = colors.mutedForeground,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                items(itemsInCategory) { item ->
                    AchievementCard(item = item, colors = colors, strings = strings)
                }
            }
        }
    }
}

@Composable
fun AchievementCard(item: AchievementWithStatus, colors: SushiColors, strings: AppStrings.Strings) {
    val bgColor by animateColorAsState(
        targetValue = if (item.isUnlocked) colors.primary.copy(alpha = 0.15f) else colors.surface,
        label = "bgColorAnimation"
    )
    val contentColor = if (item.isUnlocked) colors.primary else colors.onSurface
    val iconBgColor = if (item.isUnlocked) colors.primary else colors.secondary

    Card(
        modifier = Modifier.fillMaxWidth().alpha(if (item.isUnlocked) 1f else 0.8f),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = bgColor)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Icon(
                painter = painterResource(id = item.achievement.iconRes),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier.size(48.dp)
            )

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = AppStrings.getAchievementTitle(item.achievement.id, strings),
                    color = contentColor, fontWeight = FontWeight.Bold, fontSize = 16.sp
                )
                Text(
                    text = AppStrings.getAchievementDescription(item.achievement.id, strings),
                    color = colors.mutedForeground, fontSize = 12.sp, lineHeight = 16.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Box(modifier = Modifier.weight(1f).height(6.dp).clip(CircleShape).background(colors.secondary)) {
                        Box(modifier = Modifier.fillMaxWidth(item.progress.percentage).fillMaxHeight().clip(CircleShape).background(colors.primary))
                    }
                    Text(
                        text = "${item.progress.displayCurrent}/${item.progress.target}",
                        color = if (item.isUnlocked) colors.primary else colors.mutedForeground,
                        fontSize = 11.sp,
                        fontWeight = if (item.isUnlocked) FontWeight.Bold else FontWeight.Normal
                    )
                }
            }
        }
    }
}

private fun getCategoryLabel(category: AchievementCategory, strings: AppStrings.Strings): String {
    return when (category) {
        AchievementCategory.SESSIONS_COUNT -> strings.catTrajectory.uppercase()
        AchievementCategory.TOTAL_PIECES   -> strings.catAccumulation.uppercase()
        AchievementCategory.SESSION_PIECES -> strings.catFeats.uppercase()
        AchievementCategory.SPECIFIC_PIECE -> strings.catSpecialist.uppercase()
        AchievementCategory.VARIETY        -> strings.catExplorer.uppercase()
    }
}
