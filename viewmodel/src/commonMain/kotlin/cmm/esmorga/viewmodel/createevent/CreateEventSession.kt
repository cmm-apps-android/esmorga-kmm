package cmm.esmorga.viewmodel.createevent

import cmm.esmorga.domain.event.model.EventType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class CreateEventData(
    val eventName: String? = null,
    val eventDescription: String? = null,
    val selectedType: EventType? = EventType.entries.firstOrNull(),
    val selectedDateMillis: Long? = null,
    val selectedHour: Int? = null,
    val selectedMinute: Int? = null,
    val selectedDeadlineDateMillis: Long? = null,
    val selectedDeadlineHour: Int? = null,
    val selectedDeadlineMinute: Int? = null,
    val showDeadlineSection: Boolean = false,
    val eventLocation: String? = null,
    val eventCoordinates: String? = null,
    val eventMaxCapacity: String? = null,
)

class CreateEventSession {
    private val _data = MutableStateFlow(CreateEventData())
    val data: StateFlow<CreateEventData> = _data.asStateFlow()

    fun updateData(transform: (CreateEventData) -> CreateEventData) {
        _data.update(transform)
    }

    fun clear() {
        _data.value = CreateEventData()
    }
}
