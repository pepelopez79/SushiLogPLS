package pls.dev.sushitracker.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pls.dev.sushitracker.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    colors: SushiColors,
    onFinished: () -> Unit
) {
    var visible by remember { mutableStateOf(false) }

    val scale by animateFloatAsState(
        targetValue = if (visible) 1f else 0.85f,
        animationSpec = tween(1000, easing = FastOutSlowInEasing),
        label = "scale"
    )
    val alpha by animateFloatAsState(
        targetValue = if (visible) 1f else 0f,
        animationSpec = tween(1000, easing = LinearEasing),
        label = "alpha"
    )

    LaunchedEffect(Unit) {
        visible = true
        delay(2200)
        onFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .scale(scale)
                .alpha(alpha)
        ) {
            Text(
                text = "🍣",
                fontSize = 100.sp,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Column(horizontalAlignment = Alignment.Start) {
                Text(
                    text = "SUSHI",
                    color = colors.onBackground,
                    fontSize = 56.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = (-2).sp
                )
                Text(
                    text = "TRACKER",
                    color = colors.primary,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 2.sp,
                    modifier = Modifier.offset(y = (-10).dp)
                )
            }
        }
    }
}