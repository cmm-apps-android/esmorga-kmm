package cmm.esmorga.data.event.model

import cmm.esmorga.domain.event.model.EventType
import kotlin.time.Clock
import kotlin.time.Instant


data class EventDataModel(
    val dataId: String,
    val dataName: String,
    val dataDate: Instant,
    val dataDescription: String,
    val dataType: EventType,
    val dataImageUrl: String? = null,
    val dataLocation: EventLocationDataModel,
    val dataTags: List<String> = emptyList(),
    val dataMaxCapacity: Int? = null,
    val dataJoinDeadline: Instant? = null,
    val dataCurrentAttendeeCount: Int = 0,
    val dataUserJoined: Boolean = false,
    val dataCreationTime: Long = Clock.System.now().toEpochMilliseconds(),
)

data class EventLocationDataModel(
    val name: String,
    val lat: Double? = null,
    val long: Double? = null
)
