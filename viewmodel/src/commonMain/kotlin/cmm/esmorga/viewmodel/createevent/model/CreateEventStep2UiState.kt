package cmm.esmorga.viewmodel.createevent.model

import cmm.esmorga.domain.event.model.EventType

data class CreateEventStep2UiState(
    val selectedType: EventType? = EventType.entries.firstOrNull()
)

sealed class CreateEventStep2Effect {
    data object NavigateToStep3 : CreateEventStep2Effect()
    data object NavigateBack : CreateEventStep2Effect()
}