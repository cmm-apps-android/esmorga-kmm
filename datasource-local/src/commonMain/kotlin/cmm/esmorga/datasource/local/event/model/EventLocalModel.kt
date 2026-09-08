package cmm.esmorga.datasource.local.event.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class EventLocalModel(
    @PrimaryKey val localId: String,
    val localName: String,
    val localDate: String,
    val localDescription: String,
    val localType: String,
    val localImageUrl: String? = null,
    val localLocationName: String,
    val localLocationLat: Double? = null,
    val localLocationLong: Double? = null,
    val localTags: List<String> = listOf(),
    val localMaxCapacity: Int? = null,
    val localJoinDeadline: String? = null,
    val localCurrentAttendeeCount: Int = 0,
    val localIsMyEvent: Boolean = false,
    val localCreationTime: Long
)