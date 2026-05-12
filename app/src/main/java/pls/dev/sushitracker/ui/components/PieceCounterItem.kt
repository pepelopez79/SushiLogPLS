package pls.dev.sushitracker.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pls.dev.sushitracker.data.SushiPiece
import pls.dev.sushitracker.ui.theme.*
import kotlinx.coroutines.*

@Composable
fun PieceCounterItem(
    piece: SushiPiece,
    count: Int,
    colors: SushiColors,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit
) {
    var isPressing by remember { mutableStateOf(false) }
    var progress by remember { mutableFloatStateOf(0f) }
    var bounceScale by remember { mutableFloatStateOf(1f) }
    val coroutineScope = rememberCoroutineScope()

    val animatedScale by animateFloatAsState(
        targetValue = bounceScale,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessHigh
        ),
        finishedListener = { bounceScale = 1f },
        label = "bounce"
    )

    val animatedProgress by animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(
            durationMillis = if (progress > 0f) 100 else 0
        ),
        label = "progress"
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(24.dp))
            .clip(RoundedCornerShape(24.dp))
            .background(if (isPressing) colors.secondary else colors.surface)
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        isPressing = true
                        var didDecrement = false

                        val pressJob = coroutineScope.launch {
                            val startTime = System.currentTimeMillis()
                            while (isActive) {
                                val elapsed = System.currentTimeMillis() - startTime
                                val pct = (elapsed / 5000f).coerceIn(0f, 1f)
                                progress = pct

                                if (pct >= 1f) {
                                    didDecrement = true
                                    onDecrement()
                                    bounceScale = 1.3f
                                    break
                                }
                                delay(50)
                            }
                        }

                        val released = tryAwaitRelease()
                        pressJob.cancel()
                        isPressing = false
                        progress = 0f

                        if (released && !didDecrement) {
                            onIncrement()
                            bounceScale = 1.3f
                        }
                    }
                )
            }
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Image(
            painter = painterResource(id = piece.imageRes),
            contentDescription = piece.name,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(56.dp)
                .clip(RoundedCornerShape(12.dp))
        )

        Text(
            text = piece.name.uppercase(),
            color = colors.onSurface,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f)
        )

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.size(48.dp)
        ) {
            if (isPressing && animatedProgress > 0f) {
                androidx.compose.foundation.Canvas(
                    modifier = Modifier.fillMaxSize()
                ) {
                    drawArc(
                        color = Color(0xFFEF4444),
                        startAngle = -90f,
                        sweepAngle = animatedProgress * 360f,
                        useCenter = false,
                        style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
                    )
                }
            }

            Box(
                modifier = Modifier
                    .size(40.dp)
                    .scale(animatedScale)
                    .clip(CircleShape)
                    .background(colors.primary.copy(alpha = 0.1f))
                    .then(
                        Modifier
                            .clip(CircleShape)
                    ),
                contentAlignment = Alignment.Center
            ) {
                androidx.compose.foundation.Canvas(
                    modifier = Modifier.fillMaxSize()
                ) {
                    drawCircle(
                        color = colors.primary,
                        style = Stroke(width = 2.dp.toPx())
                    )
                }
                Text(
                    text = "$count",
                    color = colors.primary,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 18.sp
                )
            }
        }
    }
}
