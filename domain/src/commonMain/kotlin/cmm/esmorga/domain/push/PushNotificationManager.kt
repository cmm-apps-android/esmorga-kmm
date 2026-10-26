package cmm.esmorga.domain.push

import kotlinx.coroutines.flow.SharedFlow

interface PushNotificationManager {
    val notificationPayloadFlow: SharedFlow<NotificationPayload>

    fun initialize(appId: String)
    fun requestPermission(onResult: (Boolean) -> Unit = {})
    fun login(userId: String)
    fun logout()
    fun addTags(tags: Map<String, String>)
    fun onNotificationClicked(payload: NotificationPayload)
    fun resetNotificationPayload() {}
}
