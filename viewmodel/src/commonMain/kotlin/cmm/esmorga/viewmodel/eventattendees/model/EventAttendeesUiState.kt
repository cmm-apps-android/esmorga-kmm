package cmm.esmorga.viewmodel.eventattendees.model

data class EventAttendeesUiState(
    val isLoading: Boolean = true,
    val attendees: List<String> = emptyList(),
    val hasError: Boolean = false
)