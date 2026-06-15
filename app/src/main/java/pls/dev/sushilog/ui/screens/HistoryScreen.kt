package pls.dev.sushilog.ui.screens

import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.ExperimentalLayoutApi
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
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun HistoryScreen(
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

    var sessions by remember { mutableStateOf(sessionManager.getSessions()) }
    var expandedSessionId by remember { mutableStateOf<String?>(null) }
    var showDeleteDialog by remember { mutableStateOf<SessionRecord?>(null) }

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
                Icon(painter = androidx.compose.ui.res.painterResource(id = pls.dev.sushilog.R.drawable.back), contentDescription = strings.back, tint = androidx.compose.ui.graphics.Color.Unspecified, modifier = Modifier.size(20.dp))
            }
            Text(
                strings.historyTitle,
                color = colors.onBackground,
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f)
            )
            if (sessions.isNotEmpty()) {
                Box(modifier = Modifier.clip(RoundedCornerShape(20.dp)).background(colors.primary.copy(alpha = 0.2f)).padding(horizontal = 12.dp, vertical = 6.dp)) {
                    Text(
                        text = "${sessions.size} ${if (sessions.size == 1) strings.session else strings.sessions}",
                        color = colors.primary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }

        if (sessions.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(32.dp), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Icon(
                        painter = androidx.compose.ui.res.painterResource(id = pls.dev.sushilog.R.drawable.history),
                        contentDescription = null,
                        tint = androidx.compose.ui.graphics.Color.Unspecified,
                        modifier = Modifier.size(64.dp)
                    )
                    Text(strings.noHistory, color = colors.onBackground, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    Text(strings.noHistoryDesc, color = colors.mutedForeground, fontSize = 14.sp, textAlign = TextAlign.Center)
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                items(sessions, key = { it.id }) { session ->
                    SessionHistoryCard(
                        session = session,
                        isExpanded = expandedSessionId == session.id,
                        colors = colors,
                        strings = strings,
                        currentLanguage = currentLanguage,
                        customPieces = customPieces,
                        isCompactWidth = isCompactWidth,
                        onToggleExpand = { expandedSessionId = if (expandedSessionId == session.id) null else session.id },
                        onShare = { pls.dev.sushilog.ui.screens.shareSessionAsImage(context, session, strings, currentLanguage, colors, customPieces) },
                        onDelete = { showDeleteDialog = session }
                    )
                }
            }
        }
    }

    showDeleteDialog?.let { session ->
        AlertDialog(
            modifier = Modifier.shadow(8.dp, RoundedCornerShape(24.dp)),
            onDismissRequest = { showDeleteDialog = null },
            containerColor = colors.surface,
            title = { Text(strings.deleteSession, color = colors.onSurface, fontWeight = FontWeight.Bold) },
            text = { Text(strings.deleteSessionConfirm.format(session.totalPieces), color = colors.mutedForeground) },
            confirmButton = {
                TextButton(onClick = {
                    sessionManager.deleteSession(session.id)
                    AchievementManager(context).syncAchievements()
                    sessions = sessionManager.getSessions().sortedByDescending { it.date }
                    showDeleteDialog = null
                }) { Text(strings.delete, color = MaterialTheme.colorScheme.error) }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = null }) { Text(strings.cancel, color = colors.primary) }
            }
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun SessionHistoryCard(
    session: SessionRecord,
    isExpanded: Boolean,
    colors: SushiColors,
    strings: AppStrings.Strings,
    currentLanguage: AppLanguage,
    customPieces: List<CustomPiece>,
    isCompactWidth: Boolean,
    onToggleExpand: () -> Unit,
    onShare: () -> Unit,
    onDelete: () -> Unit
) {
    val totalPieces = session.totalPieces

    val totalKcal = session.pieces.entries.sumOf { getPieceKcal(it.key, customPieces) * it.value }
    val totalRice = session.pieces.entries.sumOf { getPieceRiceGrams(it.key, customPieces) * it.value }
    val totalSalmon = session.pieces.entries.sumOf { getPieceSalmonCount(it.key, customPieces) * it.value }

    val mostConsumedPieceId = session.pieces.filter { it.value > 0 }.maxByOrNull { it.value }?.key
    val mainEmoji = mostConsumedPieceId?.let { getPieceIconId(it, customPieces) } ?: pls.dev.sushilog.R.drawable.nigiri

    val cardColor = colors.surface

    Card(
        modifier = Modifier.fillMaxWidth().shadow(4.dp, RoundedCornerShape(24.dp)),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth().clickable(onClick = onToggleExpand).padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Icon(painter = androidx.compose.ui.res.painterResource(id = mainEmoji), contentDescription = null, tint = androidx.compose.ui.graphics.Color.Unspecified, modifier = Modifier.size(40.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(formatDateLocalized(session.date, currentLanguage), color = colors.onSurface, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        Icon(painter = androidx.compose.ui.res.painterResource(id = pls.dev.sushilog.R.drawable.restaurant), contentDescription = null, tint = androidx.compose.ui.graphics.Color.Unspecified, modifier = Modifier.size(18.dp))
                        Text(session.restaurant, color = colors.mutedForeground, fontSize = 12.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    }
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        HistoryMetricChip(iconRes = pls.dev.sushilog.R.drawable.kcal, text = strings.historyKcalLabel.format(totalKcal), colors = colors)
                        if (totalRice > 0) {
                            HistoryMetricChip(iconRes = pls.dev.sushilog.R.drawable.rice, text = strings.historyRiceLabel.format(totalRice), colors = colors)
                        }
                        if (totalSalmon > 0) {
                            HistoryMetricChip(iconRes = pls.dev.sushilog.R.drawable.salmon, text = if (totalSalmon == 1) strings.historySalmonLabelSingular.format(totalSalmon) else strings.historySalmonLabel.format(totalSalmon), colors = colors)
                        }
                    }
                }

                Column(horizontalAlignment = Alignment.End, modifier = Modifier.padding(end = 4.dp)) {
                    Text(
                        text = totalPieces.toString(),
                        color = colors.onSurface,
                        fontSize = if (isCompactWidth) 20.sp else 24.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Text(if (totalPieces == 1) strings.piece else strings.pieces, color = colors.mutedForeground, fontSize = 11.sp)
                }

                Icon(
                    painter = androidx.compose.ui.res.painterResource(id = if (isExpanded) pls.dev.sushilog.R.drawable.up else pls.dev.sushilog.R.drawable.down),
                    contentDescription = null, tint = androidx.compose.ui.graphics.Color.Unspecified, modifier = Modifier.padding(end = 4.dp).size(24.dp)
                )
            }

            AnimatedVisibility(visible = isExpanded, enter = expandVertically(), exit = shrinkVertically()) {
                Column {
                    HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), color = colors.border)
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        session.pieces.filter { it.value > 0 }.forEach { (id, count) ->
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                                Row(modifier = Modifier.weight(1f), horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                                    Icon(painter = androidx.compose.ui.res.painterResource(id = getPieceIconId(id, customPieces)), contentDescription = null, tint = androidx.compose.ui.graphics.Color.Unspecified, modifier = Modifier.size(24.dp))
                                    Text(getPieceName(id, customPieces, strings), color = colors.onSurface, fontSize = 14.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
                                }
                                Text("$count", color = colors.primary, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                    Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).padding(bottom = 16.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        Button(
                            onClick = onDelete,
                            modifier = Modifier.weight(1f).height(48.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error, contentColor = MaterialTheme.colorScheme.onError)
                        ) {
                            Text(strings.delete, fontWeight = FontWeight.Bold)
                        }
                        Button(
                            onClick = onShare,
                            modifier = Modifier.weight(1f).height(48.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = colors.primary, contentColor = colors.onPrimary)
                        ) {
                            Text(strings.share, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun HistoryMetricChip(iconRes: Int, text: String, colors: SushiColors) {
    Row(
        modifier = Modifier.padding(end = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Icon(
            painter = androidx.compose.ui.res.painterResource(id = iconRes),
            contentDescription = null,
            tint = Color.Unspecified,
            modifier = Modifier.size(14.dp)
        )
        Text(text = text, color = colors.mutedForeground, fontSize = 11.sp, fontWeight = FontWeight.Medium)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
internal fun formatDateLocalized(dateString: String, language: AppLanguage): String {
    return try {
        val date = try {
            LocalDateTime.parse(dateString, DateTimeFormatter.ISO_DATE_TIME).toLocalDate()
        } catch (e: Exception) { LocalDate.parse(dateString) }

        val months = AppStrings.monthNames[language] ?: AppStrings.monthNames[AppLanguage.ENGLISH]!!
        val month = months[date.monthValue - 1]

        when (language) {
            AppLanguage.SPANISH -> "${date.dayOfMonth} de $month, ${date.year}"
            AppLanguage.ENGLISH -> "${date.dayOfMonth} $month ${date.year}"
            AppLanguage.FRENCH  -> "${date.dayOfMonth} $month ${date.year}"
            AppLanguage.ITALIAN -> "${date.dayOfMonth} $month ${date.year}"
        }
    } catch (e: Exception) { dateString }
}
