package cmm.esmorga.viewmodel.common

import kotlin.time.Instant
import platform.Foundation.NSDate
import platform.Foundation.NSDateFormatter

actual fun formatDayOfWeekMediumDateShortTime(date: Instant): String {
    val epochSeconds = date.toEpochMilliseconds().toDouble() / 1000.0
    val secondsBetween1970AndReferenceDate = 978307200.0
    val nsDate = NSDate(timeIntervalSinceReferenceDate = epochSeconds - secondsBetween1970AndReferenceDate)

    val dayFormatter = NSDateFormatter().apply {
        setDateFormat("EEE")
    }
    val dayMonthDateFormatter = NSDateFormatter().apply {
        setDateFormat("d MMM yyyy")
    }
    val shortTimeFormatter = NSDateFormatter().apply {
        setDateFormat("h:mm a")
    }

    val dayOfWeek = dayFormatter.stringFromDate(nsDate)
    val dayMonthDate = dayMonthDateFormatter.stringFromDate(nsDate)
    val shortTime = shortTimeFormatter.stringFromDate(nsDate)

    return "$dayOfWeek, $dayMonthDate, $shortTime"
}

