package pls.dev.sushilog.ui.screens

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pls.dev.sushilog.data.*
import pls.dev.sushilog.ui.theme.*

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
    val configuration = LocalConfiguration.current
    val isCompactWidth = configuration.screenWidthDp < 360
    val sessionManager = remember { SessionStorage(context) }
    val settingsManager = remember { AppSettingsManager(context) }
    val customPieces = remember { settingsManager.getCustomPieces() }
    var stats by remember { mutableStateOf(sessionManager.getStats(StatsFilter.ALL)) }

    Column(modifier = Modifier.fillMaxSize().background(colors.background)) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            IconButton(
                onClick = onBack,
                modifier = Modifier.size(40.dp).clip(CircleShape).background(colors.secondary)
            ) {
                Icon(
                    painter = androidx.compose.ui.res.painterResource(id = pls.dev.sushilog.R.drawable.back),
                    contentDescription = strings.back,
                    tint = androidx.compose.ui.graphics.Color.Unspecified,
                    modifier = Modifier.size(20.dp)
                )
            }
            Text(
                strings.statsTitle,
                color = colors.onBackground,
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
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
                        androidx.compose.ui.res.painterResource(id = pls.dev.sushilog.R.drawable.stats),
                        contentDescription = null,
                        tint = androidx.compose.ui.graphics.Color.Unspecified,
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
                    val fullList = SUSHI_PIECES + customPieces.map { SushiPiece(id = it.id, name = it.name, iconId = it.iconId, kcal = it.kcal, salmonCount = it.salmonCount, riceGrams = it.riceGrams) }
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
                            Icon(painter = androidx.compose.ui.res.painterResource(id = pls.dev.sushilog.R.drawable.all), contentDescription = null, tint = androidx.compose.ui.graphics.Color.Unspecified, modifier = Modifier.size(64.dp))
                            Text(
                                stats.total.toString(),
                                color = colors.primary,
                                fontSize = 56.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                            Text(
                                if (stats.total == 1) strings.totalPiecesLabelSingular else strings.totalPiecesLabel,
                                color = colors.mutedForeground,
                                fontSize = 16.sp
                            )
                        }
                    }
                }
                item {
                    if (isCompactWidth) {
                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                StatCard(
                                    iconRes = pls.dev.sushilog.R.drawable.calendar,
                                    value = stats.sessionCount.toString(),
                                    label = if (stats.sessionCount == 1) strings.session.replaceFirstChar { if (it.isLowerCase()) it.titlecase(java.util.Locale.getDefault()) else it.toString() } else strings.sessionCount,
                                    colors = colors,
                                    modifier = Modifier.weight(1f)
                                )
                                StatCard(
                                    iconRes = pls.dev.sushilog.R.drawable.average,
                                    value = String.format("%.0f", stats.avgPerSession),
                                    label = strings.average,
                                    colors = colors,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                            StatCard(
                                iconRes = pls.dev.sushilog.R.drawable.record,
                                value = stats.maxInSession.toString(),
                                label = strings.record,
                                colors = colors,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    } else {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            StatCard(
                                iconRes = pls.dev.sushilog.R.drawable.calendar,
                                value = stats.sessionCount.toString(),
                                label = if (stats.sessionCount == 1) strings.session.replaceFirstChar { if (it.isLowerCase()) it.titlecase(java.util.Locale.getDefault()) else it.toString() } else strings.sessionCount,
                                colors = colors,
                                modifier = Modifier.weight(1f)
                            )
                            StatCard(
                                iconRes = pls.dev.sushilog.R.drawable.average,
                                value = String.format("%.0f", stats.avgPerSession),
                                label = strings.average,
                                colors = colors,
                                modifier = Modifier.weight(1f)
                            )
                            StatCard(
                                iconRes = pls.dev.sushilog.R.drawable.record,
                                value = stats.maxInSession.toString(),
                                label = strings.record,
                                colors = colors,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
                item {
                    Text(
                        strings.breakdown.uppercase(),
                        color = colors.mutedForeground,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 2.sp,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth().shadow(4.dp, RoundedCornerShape(24.dp)),
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = colors.surface)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            val maxPieces = stats.pieceStats.values.maxOrNull() ?: 1
                            val sortedStats = stats.pieceStats.entries.sortedByDescending { it.value }
                            val top5 = sortedStats.take(5)
                            val others = sortedStats.drop(5)

                            top5.forEach { (id, count) ->
                                    PieceTypeRow(
                                        id = id,
                                        count = count,
                                        maxCount = maxPieces,
                                        colors = colors,
                                        strings = strings,
                                        customPieces = customPieces
                                    )
                                }

                            if (others.isNotEmpty()) {
                                val othersTotal = others.sumOf { it.value }
                                PieceTypeRowOthers(
                                    count = othersTotal,
                                    maxCount = maxPieces,
                                    colors = colors,
                                    label = strings.others
                                )
                            }
                        }
                    }
                }
                if (stats.sessionCount > 0) {
                    item {
                        Text(
                            strings.curiosities.uppercase(),
                            color = colors.mutedForeground,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 2.sp,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth().shadow(4.dp, RoundedCornerShape(16.dp)),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = colors.surface)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                val fullList = SUSHI_PIECES + customPieces.map { SushiPiece(id = it.id, name = it.name, iconId = it.iconId, kcal = it.kcal, salmonCount = it.salmonCount, riceGrams = it.riceGrams) }
                                
                                val totalCalories = stats.pieceStats.entries.sumOf { (id, count) ->
                                    val pieceOpt = fullList.find { p -> p.id == id }
                                    (pieceOpt?.kcal ?: 0) * count
                                }
                                if (totalCalories > 0) {
                                    CuriosityItem(
                                        pls.dev.sushilog.R.drawable.kcal,
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
                                        iconId = pls.dev.sushilog.R.drawable.rice,
                                        text = strings.riceApprox.format(riceGrams),
                                        colors = colors
                                    )
                                }
                                stats.pieceStats.maxByOrNull { it.value }?.let {
                                        CuriosityItem(
                                            getPieceIconId(it.key, customPieces),
                                            strings.favoritePiece.format(
                                                getPieceName(
                                                    it.key,
                                                    customPieces,
                                                    strings
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
                                        pls.dev.sushilog.R.drawable.salmon,
                                        strings.statsWholeSalmon.format(wholeSalmons, salmonEst),
                                        colors
                                    )
                                } else if (salmonEst > 0) {
                                    CuriosityItem(
                                        pls.dev.sushilog.R.drawable.salmon,
                                        if (salmonEst == 1) strings.statsSalmonPiecesSingular.format(salmonEst) else strings.statsSalmonPieces.format(salmonEst),
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
    iconRes: Int, value: String, label: String, colors: SushiColors,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier.shadow(4.dp, RoundedCornerShape(24.dp)), shape = RoundedCornerShape(24.dp), colors = CardDefaults.cardColors(containerColor = colors.surface)) {
        Column(modifier = Modifier.fillMaxWidth().padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(painter = androidx.compose.ui.res.painterResource(id = iconRes), contentDescription = null, tint = Color.Unspecified, modifier = Modifier.size(28.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text(value, color = colors.onSurface, fontSize = 24.sp, fontWeight = FontWeight.ExtraBold)
            Text(label, color = colors.mutedForeground, fontSize = 12.sp)
        }
    }
}

@Composable
private fun PieceTypeRow(
    id: String, count: Int, maxCount: Int, colors: SushiColors, strings: AppStrings.Strings,
    customPieces: List<CustomPiece> = emptyList()
) {
    Column {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Row(modifier = Modifier.weight(1f), horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(painter = androidx.compose.ui.res.painterResource(id = getPieceIconId(id, customPieces)), contentDescription = null, tint = androidx.compose.ui.graphics.Color.Unspecified, modifier = Modifier.size(28.dp))
                Text(getPieceName(id, customPieces, strings), color = colors.onSurface, fontSize = 14.sp, fontWeight = FontWeight.Medium, maxLines = 1, overflow = TextOverflow.Ellipsis)
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
private fun PieceTypeRowOthers(
    count: Int, maxCount: Int, colors: SushiColors, label: String
) {
    Column {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(painter = androidx.compose.ui.res.painterResource(id = pls.dev.sushilog.R.drawable.nigiri), contentDescription = null, tint = androidx.compose.ui.graphics.Color.Unspecified, modifier = Modifier.size(28.dp))
                Text(label, color = colors.onSurface, fontSize = 14.sp, fontWeight = FontWeight.Medium)
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
private fun CuriosityItem(iconId: Int, text: String, colors: SushiColors) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        Icon(painter = androidx.compose.ui.res.painterResource(id = iconId), contentDescription = null, tint = androidx.compose.ui.graphics.Color.Unspecified, modifier = Modifier.size(32.dp))
        Text(text, color = colors.onSurface, fontSize = 14.sp, maxLines = 2, overflow = TextOverflow.Ellipsis)
    }
}
