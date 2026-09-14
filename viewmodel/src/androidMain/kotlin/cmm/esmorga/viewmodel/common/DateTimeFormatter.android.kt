package cmm.esmorga.viewmodel.common

import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale
import kotlin.time.Instant

actual fun formatDayOfWeekMediumDateShortTime(date: Instant): String {
    val locale = Locale.getDefault()
    val zonedDateTime = java.time.Instant
        .ofEpochMilli(date.toEpochMilliseconds())
        .atZone(ZoneId.systemDefault())

    val dayOfWeek = zonedDateTime.dayOfWeek.getDisplayName(TextStyle.SHORT, locale)
    val dayMonthDate = DateTimeFormatter.ofPattern("d MMM yyyy", locale)
        .format(zonedDateTime)
    val shortTime = DateTimeFormatter.ofPattern("h:mm a", locale)
        .withLocale(locale)
        .format(zonedDateTime)

    return "$dayOfWeek, $dayMonthDate, $shortTime"
}

