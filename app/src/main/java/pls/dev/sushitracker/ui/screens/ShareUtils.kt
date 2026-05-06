package pls.dev.sushitracker.ui.screens

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Typeface
import android.text.Layout
import android.text.StaticLayout
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

fun shareSessionAsImage(
    context: Context,
    session: SessionRecord,
    strings: AppStrings.Strings,
    language: AppLanguage,
    colors: SushiColors,
    customPieces: List<CustomPiece>
) {
    val bitmap = Bitmap.createBitmap(1080, 1920, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    
    canvas.drawColor(colors.background.toArgb())

    val paint = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
        color = colors.onBackground.toArgb()
        textSize = 80f
        typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        textAlign = Paint.Align.CENTER
    }

    canvas.drawText(strings.newSession, 540f, 300f, paint)

    paint.textSize = 50f
    paint.typeface = Typeface.DEFAULT
    paint.color = colors.mutedForeground.toArgb()
    
    val dateText = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
        formatDateLocalized(session.date, language)
    } else {
        session.date
    }
    
    canvas.drawText("📅 $dateText", 540f, 420f, paint)
    canvas.drawText("🏠 ${session.restaurant}", 540f, 500f, paint)

    paint.textSize = 200f
    paint.color = colors.primary.toArgb()
    paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
    canvas.drawText("${session.totalPieces}", 540f, 850f, paint)
    
    paint.textSize = 60f
    paint.color = colors.mutedForeground.toArgb()
    paint.typeface = Typeface.DEFAULT
    canvas.drawText(strings.pieces.uppercase(), 540f, 950f, paint)
    
    paint.textSize = 50f
    paint.textAlign = Paint.Align.LEFT
    paint.color = colors.onSurface.toArgb()
    
    val activePieces = session.pieces.filter { it.value > 0 }.toList()
    var currentY = 1150f
    val padding = 150f
    
    val cardPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = colors.surface.toArgb()
    }
    
    val listRect = RectF(padding - 50f, currentY - 80f, 1080f - padding + 50f, currentY + (activePieces.size * 100f) + 20f)
    canvas.drawRoundRect(listRect, 50f, 50f, cardPaint)
    
    for ((id, count) in activePieces) {
        val emoji = getPieceEmoji(id, customPieces)
        val name = getPieceName(id, customPieces)
        
        paint.textAlign = Paint.Align.LEFT
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        canvas.drawText("$emoji  $name", padding, currentY, paint)
        
        paint.textAlign = Paint.Align.RIGHT
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        paint.color = colors.primary.toArgb()
        canvas.drawText("$count", 1080f - padding, currentY, paint)
        
        paint.color = colors.onSurface.toArgb()
        currentY += 100f
    }
    
    paint.textAlign = Paint.Align.CENTER
    paint.textSize = 40f
    paint.color = colors.mutedForeground.toArgb()
    canvas.drawText("Sushi Tracker 🍣", 540f, 1800f, paint)

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
                putExtra(Intent.EXTRA_TEXT, "🍣 Mi sesión de Sushi en ${session.restaurant}! \n#SushiTracker")
            }
            context.startActivity(Intent.createChooser(shareIntent, "Compartir sesión"))
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}



