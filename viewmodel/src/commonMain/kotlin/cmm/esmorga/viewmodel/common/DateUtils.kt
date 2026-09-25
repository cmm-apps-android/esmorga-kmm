package cmm.esmorga.viewmodel.common

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format.char
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

object DateUtils {

    private val isoUtcFormat = LocalDateTime.Format {
        year()
        char('-')
        monthNumber()
        char('-')
        day()
        char('T')
        hour()
        char(':')
        minute()
        char(':')
        second()
        char('.')
        secondFraction(3)
        char('Z')
    }

    fun formatDayOfWeekMediumDateShortTime(date: kotlin.time.Instant): String {
        return cmm.esmorga.viewmodel.common.formatDayOfWeekMediumDateShortTime(date)
    }

    fun formatTime(hour: Int?, minute: Int?): String {
        return if (hour != null && minute != null) {
            "${hour.toString().padStart(2, '0')}:${minute.toString().padStart(2, '0')}"
        } else {
            ""
        }
    }

    fun formatToUtcIsoString(dateMillis: Long?, hour: Int?, minute: Int?): String? {
        if (dateMillis == null || hour == null || minute == null) return null
        val localDate = kotlin.time.Instant.fromEpochMilliseconds(dateMillis).toLocalDateTime(TimeZone.currentSystemDefault()).date
        val localDateTime = LocalDateTime(localDate.year, localDate.month, localDate.day, hour, minute)
        val instant = localDateTime.toInstant(TimeZone.currentSystemDefault())
        val utcDateTime = instant.toLocalDateTime(TimeZone.UTC)
        return isoUtcFormat.format(utcDateTime)
    }
}
