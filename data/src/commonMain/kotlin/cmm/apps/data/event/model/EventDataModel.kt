package cmm.apps.data.event.model

import cmm.apps.domain.event.model.EventType
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant


data class EventDataModel(
    val dataId: String,
    val dataName: String,
    val dataDate: Instant,
    val dataDescription: String,
    val dataType: EventType,
    val dataImageUrl: String? = null,
    val dataLocation: EventLocationDataModel,
    val dataCreationTime: Long = Clock.System.now().toEpochMilliseconds()
)

data class EventLocationDataModel(
    val name: String,
    val lat: Double? = null,
    val long: Double? = null
)
