package pls.dev.sushilog.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
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
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background)
    ) {
        
        Icon(
            painter = painterResource(id = pls.dev.sushilog.R.drawable.nigiri),
            contentDescription = null,
            tint = androidx.compose.ui.graphics.Color.Unspecified,
            modifier = Modifier
                .size(300.dp)
                .align(Alignment.TopEnd)
                .offset(x = 100.dp, y = (-80).dp)
                .alpha(0.04f)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 64.dp, start = 24.dp, end = 24.dp, bottom = 24.dp)
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
                        modifier = Modifier.size(55.dp)
                    )
                    SushiLogTitle(
                        colors = colors,
                        sushiFontSize = 35,
                        logFontSize = 16,
                        style = 1
                    )
                }
                Box(
                    modifier = Modifier
                        .size(52.dp)
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
                        modifier = Modifier.size(36.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(48.dp))

            
            Card(
                onClick = onStartCounter,
                colors = CardDefaults.cardColors(containerColor = colors.primary),
                shape = RoundedCornerShape(32.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
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
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = (-1).sp
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            strings.newSession,
                            color = colors.onPrimary.copy(alpha = 0.8f),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    Box(
                        modifier = Modifier
                            .align(Alignment.CenterEnd)
                            .padding(end = 24.dp)
                            .size(64.dp)
                            .clip(CircleShape)
                            .background(colors.onPrimary.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.AutoMirrored.Filled.KeyboardArrowRight,
                            contentDescription = null,
                            tint = colors.onPrimary,
                            modifier = Modifier.size(36.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(40.dp))
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
                    modifier = Modifier.weight(0.45f).height(160.dp)
                )
                Column(
                    modifier = Modifier.weight(0.55f).height(160.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
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
            
            Spacer(modifier = Modifier.weight(1f))
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
