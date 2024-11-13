package test.android.widget

import android.content.Context
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
import androidx.appcompat.app.AppCompatActivity

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
                }
                view.addView(it)
            }
            root.addView(view)
        }
        setContentView(root)
    }
}
