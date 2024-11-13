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
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

internal class MainActivity : AppCompatActivity() {
    private var imgView: ImageView? = null
    private var textView: TextView? = null

    private fun render(imgIndex: Int) {
        val imgView = checkNotNull(imgView)
        val textView = checkNotNull(textView)
        imgView.setImageDrawable(Drawable.createFromStream(assets.open("img_$imgIndex.jpg"), null))
        textView.text = "img: $imgIndex"
    }

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
            ImageView(context).also {
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
                imgView = it
                view.addView(it)
            }
            TextView(context).also {
                it.layoutParams = FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                )
                val imgIndex = App.injection.locals.imgIndex
                it.text = "img: $imgIndex"
                it.setTextColor(Color.BLACK)
                textView = it
                view.addView(it)
            }
            Button(context).also {
                it.layoutParams = FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                )
                it.text = "click"
                it.setOnClickListener { _ ->
                    Bus.click(context = context)
                }
                view.addView(it)
            }
            root.addView(view)
        }
        setContentView(root)
        Bus.events
            .flowWithLifecycle(lifecycle, minActiveState = Lifecycle.State.CREATED)
            .onEach { event ->
                when (event) {
                    Bus.Event.Update -> {
                        render(imgIndex = App.injection.locals.imgIndex)
                    }
                }
            }
            .launchIn(lifecycleScope)
    }

    companion object {
        private val logger = App.injection.loggers.create("[MainActivity]")
    }
}
