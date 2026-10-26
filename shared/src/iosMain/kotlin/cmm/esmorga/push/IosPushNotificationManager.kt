package cmm.esmorga.push

import cmm.esmorga.domain.push.NotificationPayload
import cmm.esmorga.domain.push.PushNotificationManager
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

interface IosPushNotificationDelegate {
    fun initialize(appId: String)
    fun requestPermission(onResult: (Boolean) -> Unit)
    fun login(userId: String)
    fun logout()
    fun addTags(tags: Map<String, String>)
}

class IosPushNotificationManager(
    private var delegate: IosPushNotificationDelegate? = null
) : PushNotificationManager {

    private val _notificationPayloadFlow = MutableSharedFlow<NotificationPayload>(
        replay = 1,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    override val notificationPayloadFlow: SharedFlow<NotificationPayload> = _notificationPayloadFlow.asSharedFlow()

    fun setDelegate(delegate: IosPushNotificationDelegate) {
        this.delegate = delegate
    }

    override fun initialize(appId: String) {
        delegate?.initialize(appId)
    }

    override fun requestPermission(onResult: (Boolean) -> Unit) {
        delegate?.requestPermission(onResult)
    }

    override fun login(userId: String) {
        delegate?.login(userId)
    }

    override fun logout() {
        delegate?.logout()
    }

    override fun addTags(tags: Map<String, String>) {
        delegate?.addTags(tags)
    }

    override fun onNotificationClicked(payload: NotificationPayload) {
        _notificationPayloadFlow.tryEmit(payload)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun resetNotificationPayload() {
        _notificationPayloadFlow.resetReplayCache()
    }
}
