package pls.dev.sushilog.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pls.dev.sushilog.ui.theme.*
import kotlinx.coroutines.delay

/**
 * Estilos disponibles para el título SUSHI LOG.
 * ────────────────────────────────────────────────
 * Cambia el valor de `style` en SushiLogTitle para probar cada uno.
 *
 * 1 ➜ LÍNEAS FINAS           ── LOG ──
 * 2 ➜ PILL / BADGE            SUSHI  [ LOG ]
 * 3 ➜ SUBRAYADO               SUSHI
 *                              LOG
 *                            ──────────
 * 4 ➜ PUNTO SEPARADOR         SUSHI · LOG  (una sola línea)
 * 5 ➜ LÍNEA VERTICAL          SUSHI | LOG  (una sola línea, barra gruesa)
 */

@Composable
fun SushiLogTitle(
    colors: SushiColors,
    sushiFontSize: Int = 56,
    logFontSize: Int = 32,
    // ──────────────────────────────────────
    // ⬇️  CAMBIA ESTE NÚMERO PARA PROBAR  ⬇️
    style: Int = 1
    // ──────────────────────────────────────
) {
    when (style) {

        // ═══════════════════════════════════════
        // ESTILO 1 — Líneas finas a los lados
        //        SUSHI
        //      ── LOG ──
        // ═══════════════════════════════════════
        1 -> {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "SUSHI",
                    color = colors.onBackground,
                    fontSize = sushiFontSize.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = (-2).sp
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.offset(y = (-8).dp)
                ) {
                    HorizontalDivider(
                        modifier = Modifier.width((sushiFontSize / 2).dp),
                        thickness = 2.dp,
                        color = colors.primary
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "LOG",
                        color = colors.onBackground,
                        fontSize = logFontSize.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 2.sp
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    HorizontalDivider(
                        modifier = Modifier.width((sushiFontSize / 2).dp),
                        thickness = 2.dp,
                        color = colors.primary
                    )
                }
            }
        }

        // ═══════════════════════════════════════
        // ESTILO 2 — LOG dentro de una pill/badge
        //        SUSHI
        //       ┌─────┐
        //       │ LOG │
        //       └─────┘
        // ═══════════════════════════════════════
        2 -> {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "SUSHI",
                    color = colors.onBackground,
                    fontSize = sushiFontSize.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = (-2).sp
                )
                Box(
                    modifier = Modifier
                        .offset(y = (-6).dp)
                        .border(2.dp, colors.primary, RoundedCornerShape(8.dp))
                        .padding(horizontal = 16.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "LOG",
                        color = colors.primary,
                        fontSize = logFontSize.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 4.sp
                    )
                }
            }
        }

        // ═══════════════════════════════════════
        // ESTILO 3 — Subrayado centrado
        //        SUSHI
        //         LOG
        //      ──────────
        // ═══════════════════════════════════════
        3 -> {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "SUSHI",
                    color = colors.onBackground,
                    fontSize = sushiFontSize.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = (-2).sp
                )
                Text(
                    text = "LOG",
                    color = colors.onBackground,
                    fontSize = logFontSize.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 6.sp,
                    modifier = Modifier.offset(y = (-8).dp)
                )
                HorizontalDivider(
                    modifier = Modifier
                        .width((sushiFontSize * 2.2f).dp)
                        .offset(y = (-6).dp),
                    thickness = 3.dp,
                    color = colors.primary
                )
            }
        }

        // ═══════════════════════════════════════
        // ESTILO 4 — Todo en línea con punto
        //        SUSHI · LOG
        // ═══════════════════════════════════════
        4 -> {
            Row(
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "SUSHI",
                    color = colors.onBackground,
                    fontSize = sushiFontSize.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = (-2).sp
                )
                Text(
                    text = " · ",
                    color = colors.primary,
                    fontSize = (sushiFontSize * 0.6f).toInt().sp,
                    fontWeight = FontWeight.Black,
                    modifier = Modifier.offset(y = (-4).dp)
                )
                Text(
                    text = "LOG",
                    color = colors.primary,
                    fontSize = (sushiFontSize * 0.65f).toInt().sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 2.sp,
                    modifier = Modifier.offset(y = (-4).dp)
                )
            }
        }

        // ═══════════════════════════════════════
        // ESTILO 5 — Línea vertical separadora
        //        SUSHI | LOG
        // ═══════════════════════════════════════
        5 -> {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "SUSHI",
                    color = colors.onBackground,
                    fontSize = sushiFontSize.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = (-2).sp
                )
                Spacer(modifier = Modifier.width(12.dp))
                Box(
                    modifier = Modifier
                        .width(3.dp)
                        .height((sushiFontSize * 0.7f).dp)
                        .background(colors.primary)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "LOG",
                    color = colors.primary,
                    fontSize = (sushiFontSize * 0.65f).toInt().sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 2.sp
                )
            }
        }
    }
}

@Composable
fun SplashScreen(
    colors: SushiColors,
    logoRes: Int = pls.dev.sushilog.R.drawable.logo,
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
            androidx.compose.material3.Icon(
                painter = androidx.compose.ui.res.painterResource(id = logoRes),
                contentDescription = null,
                tint = androidx.compose.ui.graphics.Color.Unspecified,
                modifier = Modifier.size(180.dp).padding(bottom = 16.dp)
            )

            SushiLogTitle(
                colors = colors,
                sushiFontSize = 56,
                logFontSize = 32,
                style = 1
            )
        }
    }
}