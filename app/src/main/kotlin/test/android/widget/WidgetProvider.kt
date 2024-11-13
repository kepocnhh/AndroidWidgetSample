package test.android.widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.provider.MediaStore
import android.util.TypedValue
import android.widget.RemoteViews
import androidx.core.content.FileProvider
import java.io.ByteArrayOutputStream

internal class WidgetProvider : AppWidgetProvider() {
    private fun getBytes(bitmap: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        return stream.toByteArray()
    }

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
        val authority = "${context.packageName}.provider"
        logger.debug("authority: $authority")
        val uri = FileProvider.getUriForFile(
            context,
            authority,
            context.cacheDir.resolve("img_${imgIndex}_tiny.jpg"),
        )
        logger.debug("uri: $uri")
//        remoteViews.setImageViewUri(R.id.img, uri)
//        val source = ImageDecoder.createSource(context.assets, "img_${imgIndex}.jpg")
        val source = ImageDecoder.createSource(context.contentResolver, uri)
        val bitmap = ImageDecoder.decodeBitmap(source)
        logger.debug("$uri: ${getBytes(bitmap = bitmap).size} bytes")
//        remoteViews.setImageViewBitmap(R.id.img, bitmap)
        val size = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            128f,
            context.resources.displayMetrics,
        ).toInt()
        remoteViews.setImageViewBitmap(R.id.img, Bitmap.createScaledBitmap(bitmap, size, size, false))
//        remoteViews.setImageViewResource(R.id.img, android.R.drawable.ic_menu_view)
//        appWidgetManager.updateAppWidget(appWidgetIds, remoteViews)
        appWidgetManager.updateAppWidget(ComponentName(context, this::class.java), remoteViews)
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        logger.debug("on receive...")
        super.onReceive(context, intent)
    }

    companion object {
        private val logger = App.injection.loggers.create("[WidgetProvider]")
    }
}
