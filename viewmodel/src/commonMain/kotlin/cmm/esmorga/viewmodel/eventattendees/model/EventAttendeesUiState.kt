package cmm.esmorga.viewmodel.eventattendees.model

import cmm.esmorga.domain.event.model.Attendee

data class EventAttendeesUiState(
    val isLoading: Boolean = true,
    val attendees: List<Attendee> = emptyList(),
    val isAdmin: Boolean = false,
    val hasError: Boolean = false
)