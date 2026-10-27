package cmm.esmorga.push

import android.content.Context
import cmm.esmorga.domain.push.NotificationPayload
import cmm.esmorga.domain.push.PushNotificationManager
import com.onesignal.OneSignal
import com.onesignal.notifications.INotificationClickEvent
import com.onesignal.notifications.INotificationClickListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class AndroidPushNotificationManager(
    private val context: Context
) : PushNotificationManager {

    private val _notificationPayloadFlow = MutableSharedFlow<NotificationPayload>(
        replay = 1,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    override val notificationPayloadFlow: SharedFlow<NotificationPayload> = _notificationPayloadFlow.asSharedFlow()

    override fun initialize(appId: String) {
        OneSignal.initWithContext(context, appId)
        OneSignal.Notifications.addClickListener(object : INotificationClickListener {
            override fun onClick(event: INotificationClickEvent) {
                val additionalData = event.notification.additionalData
                val type = additionalData?.optString("type")?.takeIf { it.isNotEmpty() }
                onNotificationClicked(type)
            }
        })
    }

    override fun requestPermission(onResult: (Boolean) -> Unit) {
        CoroutineScope(Dispatchers.Main).launch {
            val granted = OneSignal.Notifications.requestPermission(true)
            onResult(granted)
        }
    }

    override fun login(userId: String) {
        OneSignal.login(userId)
    }

    override fun logout() {
        OneSignal.logout()
    }

    override fun addTags(tags: Map<String, String>) {
        OneSignal.User.addTags(tags)
    }

    override fun onNotificationClicked(type: String?) {
        _notificationPayloadFlow.tryEmit(NotificationPayload(type = type))
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun resetNotificationPayload() {
        _notificationPayloadFlow.resetReplayCache()
    }
}
