package test.android.widget.provider

import android.content.Context
import test.android.widget.BuildConfig

internal class FinalLocals(
    context: Context,
) : Locals {
    private val prefs = context.getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE)

    init {
//        prefs.edit().putInt("imgIndex", 0).commit() // todo
    }

    override var imgIndex: Int
        get() {
            return prefs.getInt("imgIndex", 0)
        }
        set(value) {
            prefs.edit().putInt("imgIndex", value).commit()
        }
}
