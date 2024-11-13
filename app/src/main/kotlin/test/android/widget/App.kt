package test.android.widget

import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import test.android.widget.module.app.Injection
import test.android.widget.provider.FinalLocals
import test.android.widget.provider.FinalLoggers
import java.io.ByteArrayOutputStream

internal class App : Application() {
    override fun onCreate() {
        super.onCreate()
        _injection = Injection(
            loggers = FinalLoggers,
            locals = FinalLocals(this),
        )
        (0..4).forEach { index ->
            assets.open("img_$index.jpg").use { stream ->
                val file = cacheDir.resolve("img_${index}_tiny.jpg")
//                file.delete() // todo
                if (!file.exists()) {
                    val bitmap = BitmapFactory.decodeStream(stream)
//                    file.writeBytes(compress(bitmap = bitmap, quality = 0))
                    file.writeBytes(compress(bitmap = bitmap, quality = 10))
//                    file.writeBytes(compress(bitmap = bitmap, quality = 64))
//                    file.writeBytes(compress(bitmap = bitmap, quality = 100))
                }
            }
        }
    }

    companion object {
        private var _injection: Injection? = null
        val injection: Injection get() = checkNotNull(_injection)

        private fun compress(bitmap: Bitmap, quality: Int): ByteArray {
            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, stream)
            return stream.toByteArray()
        }
    }
}
