package cmm.esmorga.viewmodel.createevent.model

import cmm.esmorga.domain.event.model.EventType

data class CreateEventUiState(
    val eventName: String = "",
    val eventDescription: String = "",
    val selectedType: EventType? = EventType.entries.firstOrNull(),
    val nameError: NameError? = null,
    val descriptionError: DescriptionError? = null,
    val isStep1Valid: Boolean = false,
    val selectedDateMillis: Long? = null,
    val selectedHour: Int? = null,
    val selectedMinute: Int? = null,
    val selectedDeadlineDateMillis: Long? = null,
    val selectedDeadlineHour: Int? = null,
    val selectedDeadlineMinute: Int? = null,
    val showDeadlineSection: Boolean = false,
    val isDeadlineExceeded: Boolean = false,
    val isStep3Valid: Boolean = false
)

enum class NameError {
    EMPTY, INVALID_LENGTH
}

enum class DescriptionError {
    INVALID_LENGTH
}

sealed class CreateEventEffect {
    data object NavigateToStep2 : CreateEventEffect()
    data object NavigateToStep3 : CreateEventEffect()
    data object NavigateBack : CreateEventEffect()
    data object NavigateToSuccess : CreateEventEffect()
}