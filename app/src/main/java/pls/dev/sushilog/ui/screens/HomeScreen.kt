package pls.dev.sushilog.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pls.dev.sushilog.data.AppStrings
import pls.dev.sushilog.ui.theme.*

@Composable
fun HomeScreen(
    colors: SushiColors,
    strings: AppStrings.Strings,
    onStartCounter: () -> Unit,
    onOpenHistory: () -> Unit,
    onOpenStats: () -> Unit,
    onOpenAchievements: () -> Unit,
    onOpenSettings: () -> Unit
) {
    val configuration = LocalConfiguration.current
    val isCompactWidth = configuration.screenWidthDp < 360
    val isCompactHeight = configuration.screenHeightDp < 700

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background)
    ) {
        
        Icon(
            painter = painterResource(id = pls.dev.sushilog.R.drawable.all),
            contentDescription = null,
            tint = androidx.compose.ui.graphics.Color.Unspecified,
            modifier = Modifier
                .size(if (isCompactWidth) 290.dp else 350.dp)
                .align(Alignment.BottomStart)
                .offset(x = (-60).dp, y = 100.dp)
                .alpha(0.07f)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(top = if (isCompactHeight) 28.dp else 48.dp, start = 20.dp, end = 20.dp, bottom = 24.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(
                        painter = painterResource(id = pls.dev.sushilog.R.drawable.logo),
                        contentDescription = null,
                        tint = androidx.compose.ui.graphics.Color.Unspecified,
                        modifier = Modifier.size(if (isCompactWidth) 54.dp else 66.dp)
                    )
                    SushiLogTitle(
                        colors = colors,
                        sushiFontSize = if (isCompactWidth) 30 else 38,
                        logFontSize = if (isCompactWidth) 13 else 16,
                        style = 1
                    )
                }
                Box(
                    modifier = Modifier
                        .size(if (isCompactWidth) 52.dp else 60.dp)
                        .shadow(4.dp, CircleShape)
                        .clip(CircleShape)
                        .background(colors.surface)
                        .clickable(onClick = onOpenSettings),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = pls.dev.sushilog.R.drawable.settings),
                        contentDescription = strings.settings,
                        tint = androidx.compose.ui.graphics.Color.Unspecified,
                        modifier = Modifier.size(40.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(if (isCompactHeight) 24.dp else 40.dp))

            
            Card(
                onClick = onStartCounter,
                colors = CardDefaults.cardColors(containerColor = colors.primary),
                shape = RoundedCornerShape(32.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(if (isCompactHeight) 150.dp else 180.dp)
                    .shadow(12.dp, RoundedCornerShape(32.dp))
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .offset(x = 20.dp, y = 20.dp)
                            .size(140.dp)
                            .clip(CircleShape)
                            .background(colors.onPrimary.copy(alpha = 0.1f))
                    )

                    Column(
                        modifier = Modifier
                            .align(Alignment.CenterStart)
                            .padding(32.dp)
                    ) {
                        Text(
                            strings.begin.uppercase(),
                            color = colors.onPrimary,
                            fontSize = if (isCompactWidth) 26.sp else 32.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = if (isCompactWidth) 0.sp else (-1).sp,
                            maxLines = 1
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            strings.newSession,
                            color = colors.onPrimary.copy(alpha = 0.8f),
                            fontSize = if (isCompactWidth) 14.sp else 16.sp,
                            fontWeight = FontWeight.Medium,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    Box(
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .padding(end = 24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = pls.dev.sushilog.R.drawable.start),
                            contentDescription = null,
                            tint = androidx.compose.ui.graphics.Color.Unspecified,
                            modifier = Modifier.size(if (isCompactWidth) 56.dp else 70.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(if (isCompactHeight) 24.dp else 32.dp))
            Text(
                strings.explore.uppercase(),
                color = colors.mutedForeground,
                fontSize = 13.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 2.sp
            )
            Spacer(modifier = Modifier.height(16.dp))

            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                DashboardCard(
                    title = strings.history,
                    icon = pls.dev.sushilog.R.drawable.history,
                    color = colors.surface,
                    iconColor = colors.onSurface,
                    textColor = colors.onSurface,
                    onClick = onOpenHistory,
                    isMain = true,
                    modifier = Modifier.weight(0.45f).height(if (isCompactHeight) 140.dp else 160.dp)
                )
                Column(
                    modifier = Modifier.weight(0.55f).height(if (isCompactHeight) 140.dp else 160.dp),
                    verticalArrangement = Arrangement.spacedBy(if (isCompactHeight) 10.dp else 16.dp)
                ) {
                    DashboardCard(
                        title = strings.stats,
                        icon = pls.dev.sushilog.R.drawable.stats,
                        color = colors.surface,
                        iconColor = colors.onSurface,
                        textColor = colors.onSurface,
                        onClick = onOpenStats,
                        isMain = false,
                        modifier = Modifier.weight(1f).fillMaxWidth()
                    )
                    DashboardCard(
                        title = strings.achievements,
                        icon = pls.dev.sushilog.R.drawable.achievements,
                        color = colors.surface,
                        iconColor = colors.onSurface,
                        textColor = colors.onSurface,
                        onClick = onOpenAchievements,
                        isMain = false,
                        modifier = Modifier.weight(1f).fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Composable
fun DashboardCard(
    title: String,
    icon: Int,
    color: androidx.compose.ui.graphics.Color,
    iconColor: androidx.compose.ui.graphics.Color,
    textColor: androidx.compose.ui.graphics.Color,
    onClick: () -> Unit,
    isMain: Boolean,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        colors = CardDefaults.cardColors(containerColor = color),
        shape = RoundedCornerShape(28.dp),
        modifier = modifier.shadow(4.dp, RoundedCornerShape(28.dp))
    ) {
        if (isMain) {
            Column(
                modifier = Modifier.fillMaxSize().padding(24.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Icon(
                    painter = painterResource(id = icon),
                    contentDescription = null,
                    tint = androidx.compose.ui.graphics.Color.Unspecified,
                    modifier = Modifier.size(40.dp)
                )
                Text(
                    title,
                    color = textColor,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraBold,
                    lineHeight = 22.sp,
                    maxLines = 2
                )
            }
        } else {
            Row(
                modifier = Modifier.fillMaxSize().padding(horizontal = 20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Icon(
                    painter = painterResource(id = icon),
                    contentDescription = null,
                    tint = androidx.compose.ui.graphics.Color.Unspecified,
                    modifier = Modifier.size(32.dp)
                )
                Text(
                    title,
                    color = textColor,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Black,
                    maxLines = 1,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}
