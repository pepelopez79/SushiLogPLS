package pls.dev.sushitracker.ui.screens

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
import pls.dev.sushitracker.data.AppLanguage
import pls.dev.sushitracker.data.AppStrings
import pls.dev.sushitracker.data.CustomPiece
import pls.dev.sushitracker.data.SessionRecord
import pls.dev.sushitracker.data.getPieceEmoji
import pls.dev.sushitracker.data.getPieceName
import pls.dev.sushitracker.ui.theme.SushiColors
import java.io.File
import java.io.FileOutputStream
import androidx.core.graphics.createBitmap

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
    
    canvas.drawColor(colors.secondary.toArgb())

    val paint = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
        color = colors.onBackground.toArgb()
        textSize = 90f
        typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        textAlign = Paint.Align.CENTER
    }

    paint.textSize = 45f
    paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
    paint.color = colors.mutedForeground.toArgb()
    
    val dateText = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
        formatDateLocalized(session.date, language)
    } else {
        session.date
    }
    
    canvas.drawText("📅 $dateText", 540f, 380f, paint)
    canvas.drawText("🏠 ${session.restaurant}", 540f, 460f, paint)

    val cardPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = colors.surface.toArgb()
        setShadowLayer(40f, 0f, 20f, android.graphics.Color.argb(40, 0, 0, 0))
    }
    
    val activePieces = session.pieces.filter { it.value > 0 }.toList()
    val topContentY = 560f
    
    val listHeight = activePieces.size * 110f
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
    
    for ((id, count) in activePieces) {
        val emoji = getPieceEmoji(id, customPieces)
        val name = getPieceName(id, customPieces, strings)
        
        paint.textAlign = Paint.Align.LEFT
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        canvas.drawText("$emoji  $name", listPadding, currentY, paint)
        
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