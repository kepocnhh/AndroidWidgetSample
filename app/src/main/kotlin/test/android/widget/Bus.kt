package test.android.widget

import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

internal object Bus {
    sealed interface Event {
        data object Update : Event
    }

    private val _events = MutableSharedFlow<Event>()
    val events = _events.asSharedFlow()

    private val scope = CoroutineScope(Dispatchers.Main + Job())

    fun click(context: Context) {
        val imgIndex = App.injection.locals.imgIndex.plus(1) % 5
        App.injection.locals.imgIndex = imgIndex
        scope.launch {
            _events.emit(Event.Update)
        }
        val intent = Intent(context, WidgetProvider::class.java)
        intent.action = AppWidgetManager.ACTION_APPWIDGET_UPDATE
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, intArrayOf(0))
        context.sendBroadcast(intent)
    }
}
