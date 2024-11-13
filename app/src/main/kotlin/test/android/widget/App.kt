package test.android.widget

import android.app.Application
import test.android.widget.module.app.Injection
import test.android.widget.provider.FinalLocals

internal class App : Application() {
    override fun onCreate() {
        super.onCreate()
        _injection = Injection(
            locals = FinalLocals(this),
        )
    }

    companion object {
        private var _injection: Injection? = null
        val injection: Injection get() = checkNotNull(_injection)
    }
}
