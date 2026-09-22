package cmm.esmorga.viewmodel.common

import kotlin.time.Instant

object DateUtils {
    fun formatDayOfWeekMediumDateShortTime(date: Instant): String {
        return cmm.esmorga.viewmodel.common.formatDayOfWeekMediumDateShortTime(date)
    }

    fun formatTime(hour: Int?, minute: Int?): String {
        return if (hour != null && minute != null) {
            "${hour.toString().padStart(2, '0')}:${minute.toString().padStart(2, '0')}"
        } else {
            ""
        }
    }
}
