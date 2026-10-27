
package cmm.esmorga.android.push

import com.onesignal.notifications.INotificationReceivedEvent
import com.onesignal.notifications.INotificationServiceExtension
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

class NotificationServiceExtension : INotificationServiceExtension {

    override fun onNotificationReceived(event: INotificationReceivedEvent) {
        val notification = event.notification
        val additionalData = notification.additionalData ?: return

        if (additionalData.optString(KEY_TYPE) != TYPE_EVENT_CREATED) return

        val eventDate = additionalData.optString(KEY_EVENT_DATE)
            .takeIf(String::isNotBlank)
            ?.let(::formatEventDate)
            ?: return

        val body = notification.body.orEmpty()
        val newBody = listOf(eventDate, body)
            .filter(String::isNotBlank)
            .joinToString(SEPARATOR)

        notification.setExtender { builder ->
            builder.setContentText(newBody)
        }
    }

    private fun formatEventDate(rawDate: String): String? =
        runCatching {
            val instant = Instant.parse(rawDate)
            DATE_FORMATTER.format(instant)
        }.getOrNull()

    private companion object {
        const val KEY_TYPE = "type"
        const val KEY_EVENT_DATE = "eventDate"
        const val TYPE_EVENT_CREATED = "event-created"
        const val SEPARATOR = " · "

        val DATE_FORMATTER: DateTimeFormatter =
            DateTimeFormatter
                .ofPattern("d MMM, HH:mm", Locale.getDefault())
                .withZone(ZoneId.systemDefault())
    }
}
