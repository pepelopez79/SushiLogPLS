package pls.dev.sushitracker.ui.screens

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import pls.dev.sushitracker.data.*
import pls.dev.sushitracker.ui.theme.*

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("DefaultLocale")
@Composable
fun StatsScreen(
    colors: SushiColors,
    strings: AppStrings.Strings,
    currentLanguage: AppLanguage,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val sessionManager = remember { SessionStorage(context) }
    val settingsManager = remember { AppSettingsManager(context) }
    val customPieces = remember { settingsManager.getCustomPieces() }
    var stats by remember { mutableStateOf(sessionManager.getStats(StatsFilter.ALL)) }
    val RICE_WEIGHTS = mapOf(
        "nigiri" to 18,
        "maki" to 15,
        "onigiri" to 80,
        "uramaki" to 25,
        "gunkan" to 20,
        "temaki" to 45,
        "sashimi" to 0,
        "gyoza" to 0,
        "tempura" to 0,
        "edamame" to 0,
        "takoyaki" to 0
    )

    Column(modifier = Modifier.fillMaxSize().background(colors.background)) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 16.dp),
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
                strings.statsTitle,
                color = colors.onBackground,
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier.weight(1f)
            )
        }

        if (stats.sessionCount == 0) {
            Box(
                modifier = Modifier.fillMaxSize().padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Icon(
                        Icons.Filled.DateRange,
                        contentDescription = null,
                        tint = colors.mutedForeground,
                        modifier = Modifier.size(64.dp)
                    )
                    Text(
                        strings.noData,
                        color = colors.onBackground,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        strings.noDataDesc,
                        color = colors.mutedForeground,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                item {
                    val fullList = SUSHI_PIECES + customPieces.map { SushiPiece(it.id, it.name, 0, it.emoji, it.kcal, it.salmonCount, it.riceGrams) }
                    val totalCalories = stats.pieceStats.entries.sumOf { (id, count) ->
                        val pieceOpt = fullList.find { p -> p.id == id }
                        (pieceOpt?.kcal ?: 0) * count
                    }
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = colors.primary.copy(alpha = 0.15f))
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth().padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text("🍣", fontSize = 48.sp)
                            Text(
                                stats.total.toString(),
                                color = colors.primary,
                                fontSize = 56.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                            Text(
                                strings.totalPiecesLabel,
                                color = colors.mutedForeground,
                                fontSize = 16.sp
                            )
                            if (totalCalories > 0) {
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    "🔥 $totalCalories kcal",
                                    color = colors.primary.copy(alpha = 0.8f),
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        StatCard(
                            Icons.Filled.DateRange,
                            stats.sessionCount.toString(),
                            strings.sessionCount,
                            colors,
                            modifier = Modifier.weight(1f)
                        )
                        StatCard(
                            Icons.Filled.Add,
                            String.format("%.1f", stats.avgPerSession),
                            strings.average,
                            colors,
                            modifier = Modifier.weight(1f)
                        )
                        StatCard(
                            Icons.Filled.Add,
                            stats.maxInSession.toString(),
                            strings.record,
                            colors,
                            iconTint = colors.primary,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
                item {
                    Text(
                        strings.breakdown,
                        color = colors.onBackground,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = colors.surface)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            val maxPieces = stats.pieceStats.values.maxOrNull() ?: 1
                            stats.pieceStats.entries.sortedByDescending { it.value }
                                .forEach { (id, count) ->
                                    PieceTypeRow(
                                        id = id,
                                        count = count,
                                        maxCount = maxPieces,
                                        colors = colors,
                                        customPieces = customPieces
                                    )
                                }
                        }
                    }
                }
                if (stats.sessionCount > 0) {
                    item {
                        Text(
                            strings.curiosities,
                            color = colors.onBackground,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = colors.surface)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                val fullList = SUSHI_PIECES + customPieces.map { SushiPiece(it.id, it.name, 0, it.emoji, it.kcal, it.salmonCount, it.riceGrams) }
                                
                                val totalCalories = stats.pieceStats.entries.sumOf { (id, count) ->
                                    val pieceOpt = fullList.find { p -> p.id == id }
                                    (pieceOpt?.kcal ?: 0) * count
                                }
                                if (totalCalories > 0) {
                                    CuriosityItem(
                                        "🔥",
                                        strings.caloriesApprox.format(totalCalories),
                                        colors
                                    )
                                }
                                
                                val riceGrams = stats.pieceStats.entries.sumOf { (id, count) ->
                                    val pieceOpt = fullList.find { p -> p.id == id }
                                    (pieceOpt?.riceGrams ?: 0) * count
                                }
                                if (riceGrams > 0) {
                                    CuriosityItem(
                                        emoji = "🍚",
                                        text = strings.riceApprox.format(riceGrams),
                                        colors = colors
                                    )
                                }
                                stats.pieceStats.maxByOrNull { it.value }?.let {
                                    CuriosityItem(
                                        getPieceEmoji(it.key, customPieces),
                                        strings.favoritePiece.format(
                                            getPieceName(
                                                it.key,
                                                customPieces
                                            )
                                        ),
                                        colors
                                    )
                                }
                                val salmonEst = stats.pieceStats.entries.sumOf { (id, count) ->
                                    val pieceOpt = fullList.find { p -> p.id == id }
                                    (pieceOpt?.salmonCount ?: 0) * count
                                }
                                val wholeSalmons = salmonEst / 40.0
                                if (wholeSalmons >= 1.0) {
                                    CuriosityItem(
                                        "🐟",
                                        "Aprox. %.1f salmones enteros (%d cortes)".format(wholeSalmons, salmonEst),
                                        colors
                                    )
                                } else if (salmonEst > 0) {
                                    CuriosityItem(
                                        "🐟",
                                        "$salmonEst cortes de salmón",
                                        colors
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun StatCard(
    icon: ImageVector, value: String, label: String, colors: SushiColors,
    modifier: Modifier = Modifier, iconTint: Color = colors.primary
) {
    Card(modifier = modifier, shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = colors.surface)) {
        Column(modifier = Modifier.fillMaxWidth().padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(imageVector = icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text(value, color = colors.onSurface, fontSize = 24.sp, fontWeight = FontWeight.ExtraBold)
            Text(label, color = colors.mutedForeground, fontSize = 12.sp)
        }
    }
}

@Composable
private fun PieceTypeRow(
    id: String, count: Int, maxCount: Int, colors: SushiColors,
    customPieces: List<CustomPiece> = emptyList()
) {
    Column {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                Text(getPieceEmoji(id, customPieces), fontSize = 20.sp)
                Text(getPieceName(id, customPieces), color = colors.onSurface, fontSize = 14.sp, fontWeight = FontWeight.Medium)
            }
            Text(count.toString(), color = colors.primary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(4.dp))
        Box(modifier = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(3.dp)).background(colors.secondary)) {
            Box(modifier = Modifier.fillMaxWidth(count.toFloat() / maxCount).fillMaxHeight().clip(RoundedCornerShape(3.dp)).background(colors.primary))
        }
    }
}

@Composable
private fun CuriosityItem(emoji: String, text: String, colors: SushiColors) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        Box(modifier = Modifier.size(40.dp).clip(CircleShape).background(colors.secondary), contentAlignment = Alignment.Center) {
            Text(emoji, fontSize = 20.sp)
        }
        Text(text, color = colors.onSurface, fontSize = 14.sp)
    }
}
