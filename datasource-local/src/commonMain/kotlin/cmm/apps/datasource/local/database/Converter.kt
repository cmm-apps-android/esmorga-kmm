package cmm.apps.datasource.local.database

import androidx.room.TypeConverter
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime


class ZonedDateTimeConverter {

    companion object {
        const val DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSVV"
        const val CSV_SEPARATOR = ";//;"
    }

    @TypeConverter
    fun fromTimestamp(dateString: String): Instant {
        return Instant.parse(dateString)
    }

    @TypeConverter
    fun toTimestamp(date: Instant): String {
        return date.toLocalDateTime(TimeZone.currentSystemDefault()).toString()
    }

    @TypeConverter
    fun toString(list: List<String>): String {
        return list.joinToString(separator = CSV_SEPARATOR)
    }

    @TypeConverter
    fun fromString(csv: String): List<String> {
        return csv.split(CSV_SEPARATOR)
    }
}
