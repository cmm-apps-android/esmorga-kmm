package cmm.esmorga.android.push

import com.onesignal.notifications.INotificationReceivedEvent
import com.onesignal.notifications.INotificationServiceExtension
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class NotificationServiceExtension : INotificationServiceExtension {
    override fun onNotificationReceived(event: INotificationReceivedEvent) {
        val notification = event.notification
        val additionalData = notification.additionalData ?: return

        if (additionalData.optString("type") == "event-created") {
            val rawDate = additionalData.optString("eventDate", "")
            if (rawDate.isNotEmpty()) {
                val prefix = localDatePrefix(rawDate)
                if (prefix != null) {
                    val currentBody = notification.body ?: ""
                    val newBody = if (currentBody.isEmpty()) prefix else "$prefix · $currentBody"
                    notification.setExtender { builder ->
                        builder.setContentText(newBody)
                    }
                }
            }
        }
    }

    private fun localDatePrefix(rawDate: String): String? {
        val date = parseISO8601(rawDate) ?: return null
        val dateFormatter = SimpleDateFormat("d MMM", Locale.getDefault()).apply {
            timeZone = TimeZone.getDefault()
        }
        val timeFormatter = SimpleDateFormat("HH:mm", Locale.getDefault()).apply {
            timeZone = TimeZone.getDefault()
        }
        return "${dateFormatter.format(date)}, ${timeFormatter.format(date)}"
    }

    private fun parseISO8601(value: String): Date? {
        return try {
            val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US).apply {
                timeZone = TimeZone.getTimeZone("UTC")
            }
            sdf.parse(value)
        } catch (_: Exception) {
            try {
                val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US).apply {
                    timeZone = TimeZone.getTimeZone("UTC")
                }
                sdf.parse(value)
            } catch (_: Exception) {
                null
            }
        }
    }
}
