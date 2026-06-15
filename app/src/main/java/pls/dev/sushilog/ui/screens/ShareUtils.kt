package pls.dev.sushilog.ui.screens

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Typeface
import android.text.TextPaint
import androidx.core.content.FileProvider
import androidx.compose.ui.graphics.toArgb
import pls.dev.sushilog.data.AppLanguage
import pls.dev.sushilog.data.AppStrings
import pls.dev.sushilog.data.CustomPiece
import pls.dev.sushilog.data.SessionRecord
import pls.dev.sushilog.data.getPieceIconId
import pls.dev.sushilog.data.getPieceName
import pls.dev.sushilog.ui.theme.SushiColors
import java.io.File
import java.io.FileOutputStream
import androidx.core.graphics.createBitmap
import kotlin.math.max

fun shareSessionAsImage(
    context: Context,
    session: SessionRecord,
    strings: AppStrings.Strings,
    language: AppLanguage,
    colors: SushiColors,
    customPieces: List<CustomPiece>
) {
    val bitmap = createBitmap(1080, 1920)
    val canvas = Canvas(bitmap)
    
    val bgPaint = Paint().apply { color = colors.background.toArgb() }
    canvas.drawRect(0f, 0f, 1080f, 1920f, bgPaint)

    val decorPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = colors.primary.toArgb()
        alpha = 15
    }
    canvas.drawCircle(80f, 200f, 400f, decorPaint)
    canvas.drawCircle(950f, 1700f, 500f, decorPaint)

    // --- Header: Logo + SUSHI / ── LOG ── (centrado, como el Home) ---
    val logoDrawable = androidx.core.content.ContextCompat.getDrawable(context, pls.dev.sushilog.R.drawable.logo)
    val logoSize = 150
    val centerX = 540f

    val paint = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
        color = colors.onBackground.toArgb()
        textSize = 90f
        typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        textAlign = Paint.Align.LEFT
        letterSpacing = -0.03f
    }
    val sushiWidth = paint.measureText("SUSHI")
    val totalHeaderWidth = logoSize + 24f + sushiWidth
    val headerStartX = centerX - totalHeaderWidth / 2f

    // Logo
    val logoTop = 110
    logoDrawable?.setBounds(
        headerStartX.toInt(), logoTop,
        headerStartX.toInt() + logoSize, logoTop + logoSize
    )
    logoDrawable?.draw(canvas)

    // "SUSHI"
    val textStartX = headerStartX + logoSize + 24f
    canvas.drawText("SUSHI", textStartX, 200f, paint)

    // "── LOG ──" con líneas a AMBOS lados
    paint.apply {
        textSize = 50f
        letterSpacing = 0.08f
        typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        color = colors.onBackground.toArgb()
    }
    val logWidth = paint.measureText("LOG")
    val lineLength = 60f
    val lineGap = 16f
    val logBlockWidth = lineLength + lineGap + logWidth + lineGap + lineLength
    val logStartX = textStartX + (sushiWidth - logBlockWidth) / 2f

    val linePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = colors.primary.toArgb()
        strokeWidth = 4f
    }
    val logY = 255f
    // Línea izquierda
    canvas.drawLine(logStartX, logY - 12f, logStartX + lineLength, logY - 12f, linePaint)
    // "LOG"
    canvas.drawText("LOG", logStartX + lineLength + lineGap, logY, paint)
    // Línea derecha
    val rightLineX = logStartX + lineLength + lineGap + logWidth + lineGap
    canvas.drawLine(rightLineX, logY - 12f, rightLineX + lineLength, logY - 12f, linePaint)

    // --- Fecha con icono calendar (centrado) ---
    paint.letterSpacing = 0f
    paint.textSize = 40f
    paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
    paint.color = colors.mutedForeground.toArgb()
    paint.textAlign = Paint.Align.LEFT

    val dateText = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
        formatDateLocalized(session.date, language)
    } else {
        session.date
    }

    val infoBoxSize = 44

    val calendarDrawable = androidx.core.content.ContextCompat.getDrawable(context, pls.dev.sushilog.R.drawable.calendar)
    val calIw = calendarDrawable?.intrinsicWidth ?: infoBoxSize
    val calIh = calendarDrawable?.intrinsicHeight ?: infoBoxSize
    val calScale = minOf(infoBoxSize.toFloat() / calIw, infoBoxSize.toFloat() / calIh)
    val calW = (calIw * calScale).toInt()
    val calH = (calIh * calScale).toInt()

    val dateWidth = paint.measureText(dateText)
    val dateBlockWidth = infoBoxSize + 12f + dateWidth
    val dateStartX = centerX - dateBlockWidth / 2f
    val calLeft = dateStartX.toInt() + (infoBoxSize - calW) / 2
    val calTop = 330 + (infoBoxSize - calH) / 2

    calendarDrawable?.setBounds(calLeft, calTop, calLeft + calW, calTop + calH)
    calendarDrawable?.draw(canvas)
    canvas.drawText(dateText, dateStartX + infoBoxSize + 12f, 367f, paint)

    // --- Restaurante con icono restaurant (centrado) ---
    paint.color = colors.onBackground.toArgb()
    paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
    val restaurantDrawable = androidx.core.content.ContextCompat.getDrawable(context, pls.dev.sushilog.R.drawable.restaurant)
    val restIw = restaurantDrawable?.intrinsicWidth ?: infoBoxSize
    val restIh = restaurantDrawable?.intrinsicHeight ?: infoBoxSize
    val restScale = minOf(infoBoxSize.toFloat() / restIw, infoBoxSize.toFloat() / restIh)
    val restW = (restIw * restScale).toInt()
    val restH = (restIh * restScale).toInt()

    val maxRestaurantTextWidth = 1080f - 280f
    val restaurantText = ellipsizeForWidth(session.restaurant, paint, maxRestaurantTextWidth)
    val restaurantWidth = paint.measureText(restaurantText)
    val restaurantBlockWidth = infoBoxSize + 12f + restaurantWidth
    val restaurantStartX = centerX - restaurantBlockWidth / 2f
    val restLeft = restaurantStartX.toInt() + (infoBoxSize - restW) / 2
    val restTop = 405 + (infoBoxSize - restH) / 2

    restaurantDrawable?.setBounds(restLeft, restTop, restLeft + restW, restTop + restH)
    restaurantDrawable?.draw(canvas)
    canvas.drawText(restaurantText, restaurantStartX + infoBoxSize + 12f, 442f, paint)

    val cardPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = colors.surface.toArgb()
        setShadowLayer(40f, 0f, 20f, android.graphics.Color.argb(40, 0, 0, 0))
    }
    
    val sortedPieces = session.pieces.filter { it.value > 0 }.toList().sortedByDescending { it.second }
    val displayPieces = mutableListOf<Pair<String, Int>>()

    if (sortedPieces.size > 5) {
        displayPieces.addAll(sortedPieces.take(5))
        val restSum = sortedPieces.drop(5).sumOf { it.second }
        if (restSum > 0) {
            displayPieces.add(Pair("rest_others", restSum))
        }
    } else {
        displayPieces.addAll(sortedPieces)
    }

    val headerBottom = 470f
    val watermarkY = 1820f
    val watermarkAreaTop = watermarkY - 60f

    val listHeight = displayPieces.size * 110f
    val cardContentHeight = 450f + listHeight + 80f
    val availableSpace = watermarkAreaTop - headerBottom
    val topContentY = headerBottom + (availableSpace - cardContentHeight) / 2f
    val mainCardBottom = topContentY + 450f + listHeight + 80f

    val mainCardRect = RectF(100f, topContentY, 980f, mainCardBottom)
    canvas.drawRoundRect(mainCardRect, 80f, 80f, cardPaint)

    paint.textSize = 240f
    paint.textAlign = Paint.Align.CENTER
    paint.color = colors.primary.toArgb()
    paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
    canvas.drawText("${session.totalPieces}", 540f, topContentY + 280f, paint)
    
    paint.textSize = 50f
    paint.color = colors.mutedForeground.toArgb()
    paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
    canvas.drawText(strings.totalPiecesLabel.uppercase(), 540f, topContentY + 360f, paint)
    
    val borderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = colors.border.toArgb()
        strokeWidth = 4f
    }
    canvas.drawLine(180f, topContentY + 440f, 900f, topContentY + 440f, borderPaint)
    
    paint.textSize = 55f
    paint.color = colors.onSurface.toArgb()
    
    var currentY = topContentY + 560f
    val listPadding = 200f
    
    for ((id, count) in displayPieces) {
        val iconId = if (id == "rest_others") pls.dev.sushilog.R.drawable.all else getPieceIconId(id, customPieces)
        val name = if (id == "rest_others") strings.others else getPieceName(id, customPieces, strings)

        val drawable = androidx.core.content.ContextCompat.getDrawable(context, iconId)
        val boxSize = 64
        val iw = drawable?.intrinsicWidth ?: boxSize
        val ih = drawable?.intrinsicHeight ?: boxSize
        val scale = minOf(boxSize.toFloat() / iw, boxSize.toFloat() / ih)
        val drawW = (iw * scale).toInt()
        val drawH = (ih * scale).toInt()
        val iconLeft = listPadding.toInt() + (boxSize - drawW) / 2
        val iconTop = (currentY - 50).toInt() + (boxSize - drawH) / 2
        drawable?.setBounds(iconLeft, iconTop, iconLeft + drawW, iconTop + drawH)
        drawable?.draw(canvas)

        paint.textAlign = Paint.Align.LEFT
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        val nameStartX = listPadding + 100f
        val maxNameWidth = max(0f, (1080f - listPadding) - 120f - nameStartX)
        canvas.drawText(ellipsizeForWidth(name, paint, maxNameWidth), nameStartX, currentY, paint)

        paint.textAlign = Paint.Align.RIGHT
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        paint.color = colors.primary.toArgb()
        canvas.drawText("${count}x", 1080f - listPadding, currentY, paint)
        
        paint.color = colors.onSurface.toArgb()
        currentY += 110f
    }
    
    paint.textAlign = Paint.Align.CENTER
    paint.textSize = 45f
    paint.color = colors.mutedForeground.toArgb()
    paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
    canvas.drawText(strings.shareWatermark, 540f, 1820f, paint)

    try {
        val cachePath = File(context.cacheDir, "images")
        cachePath.mkdirs()
        val stream = FileOutputStream("$cachePath/session_summary.jpeg")
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        stream.close()

        val imagePath = File(context.cacheDir, "images")
        val newFile = File(imagePath, "session_summary.jpeg")
        val contentUri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", newFile)

        if (contentUri != null) {
            val shareIntent = Intent().apply {
                action = Intent.ACTION_SEND
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                setDataAndType(contentUri, context.contentResolver.getType(contentUri))
                putExtra(Intent.EXTRA_STREAM, contentUri)
                putExtra(Intent.EXTRA_TEXT, strings.shareIntentText.format(session.restaurant))
            }
            context.startActivity(Intent.createChooser(shareIntent, strings.shareActionTitle))
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

private fun ellipsizeForWidth(text: String, paint: TextPaint, maxWidth: Float): String {
    if (maxWidth <= 0f || paint.measureText(text) <= maxWidth) return text
    val ellipsis = "..."
    var end = text.length
    while (end > 0 && paint.measureText(text.substring(0, end) + ellipsis) > maxWidth) {
        end--
    }
    return if (end <= 0) ellipsis else text.substring(0, end) + ellipsis
}
