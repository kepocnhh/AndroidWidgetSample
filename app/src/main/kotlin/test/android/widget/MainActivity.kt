package test.android.widget

import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.net.toFile

internal class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val context: Context = this
        val root = FrameLayout(context).also {
            it.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT,
            )
            it.background = ColorDrawable(Color.WHITE)
        }
        LinearLayout(context).also { view ->
            view.layoutParams = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                Gravity.CENTER,
            )
            view.orientation = LinearLayout.VERTICAL
            view.gravity = Gravity.CENTER_HORIZONTAL
            val imgView = ImageView(context).also {
                it.layoutParams = FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        256f,
                        resources.displayMetrics,
                    ).toInt(),
                )
                val imgIndex = App.injection.locals.imgIndex
                it.setImageDrawable(Drawable.createFromStream(assets.open("img_$imgIndex.jpg"), null))
                view.addView(it)
            }
            val textView = TextView(context).also {
                it.layoutParams = FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                )
                val imgIndex = App.injection.locals.imgIndex
                it.text = "img: $imgIndex"
                it.setTextColor(Color.BLACK)
                view.addView(it)
            }
            Button(context).also {
                it.layoutParams = FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                )
                it.text = "click"
                it.setOnClickListener { _ ->
                    val imgIndex = App.injection.locals.imgIndex.plus(1) % 4
                    App.injection.locals.imgIndex = imgIndex
                    imgView.setImageDrawable(Drawable.createFromStream(assets.open("img_$imgIndex.jpg"), null))
//                    imgView.setImageDrawable(Drawable.createFromPath(context.cacheDir.resolve("img_${imgIndex}_tiny.jpg").absolutePath))
//                    val uri = FileProvider.getUriForFile(
//                        context,
//                        "${context.packageName}.provider",
//                        context.cacheDir.resolve("img_${imgIndex}_tiny.jpg"),
//                    )
//                    imgView.setImageURI(uri)
                    textView.text = "img: $imgIndex"
                    val intent = Intent(context, WidgetProvider::class.java)
                    intent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
//                    val awm = AppWidgetManager.getInstance(context)
//                    val ids = awm.getAppWidgetIds(ComponentName(context, WidgetProvider::class.java))
//                    intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids)
                    intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, intArrayOf(0))
//                    intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_PROVIDER, ComponentName(context, WidgetProvider::class.java))
                    sendBroadcast(intent)
                }
                view.addView(it)
            }
            root.addView(view)
        }
        setContentView(root)
    }
}
