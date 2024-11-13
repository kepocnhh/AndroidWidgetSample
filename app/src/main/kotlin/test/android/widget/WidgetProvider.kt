package test.android.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.util.TypedValue
import android.widget.RemoteViews
import androidx.core.content.FileProvider

internal class WidgetProvider : AppWidgetProvider() {
    override fun onUpdate(
        context: Context?,
        appWidgetManager: AppWidgetManager?,
        appWidgetIds: IntArray?,
    ) {
        logger.debug("on update...")
        if (context == null) return
        if (appWidgetManager == null) return
        val remoteViews = RemoteViews(context.packageName, R.layout.widget)
        val imgIndex = App.injection.locals.imgIndex
        remoteViews.setTextViewText(R.id.text, "img: $imgIndex")
        val uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.provider",
            context.cacheDir.resolve("img_${imgIndex}_tiny.jpg"),
        )
        val source = ImageDecoder.createSource(context.contentResolver, uri)
        val bitmap = ImageDecoder.decodeBitmap(source)
        val size = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            128f,
            context.resources.displayMetrics,
        ).toInt()
        remoteViews.setImageViewBitmap(R.id.img, Bitmap.createScaledBitmap(bitmap, size, size, false))
        val intent = Intent(context, WidgetProvider::class.java)
        intent.action = "click"
        val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
        remoteViews.setOnClickPendingIntent(R.id.clicker, pendingIntent)
        appWidgetManager.updateAppWidget(ComponentName(context, this::class.java), remoteViews)
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        logger.debug("on receive...")
        super.onReceive(context, intent)
        if (context == null) return
        if (intent == null) return
        when (intent.action) {
            "click" -> {
                Bus.click(context = context)
            }
        }
    }

    companion object {
        private val logger = App.injection.loggers.create("[WidgetProvider]")
    }
}
