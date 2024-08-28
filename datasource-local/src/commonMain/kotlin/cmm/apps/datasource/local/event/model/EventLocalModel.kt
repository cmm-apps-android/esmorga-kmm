package cmm.apps.datasource.local.event.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.Instant

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
    val localCreationTime: Long
)