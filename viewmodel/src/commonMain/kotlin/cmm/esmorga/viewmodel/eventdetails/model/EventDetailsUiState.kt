package cmm.esmorga.viewmodel.eventdetails.model

import kotlin.time.Clock
import kotlin.time.Instant

data class EventDetailsUiState(
    val id: String = "",
    val title: String = "",
    val subtitle: String = "",
    val description: String = "",
    val image: String? = null,
    val locationName: String = "",
    val locationLat: Double? = null,
    val locationLng: Double? = null,
    val tags: List<String> = emptyList(),
    val maxCapacity: Int? = null,
    val joinDeadline: Instant? = null,
    val currentAttendeeCount: Int = 0,
    val userJoined: Boolean = false,
    val isAuthenticated: Boolean = false,
    val isLoading: Boolean = false,
    val navigateButton: Boolean = locationLat != null && locationLng != null,
) {
    val isEventFull: Boolean
        get() = maxCapacity != null && currentAttendeeCount >= maxCapacity

    val isDeadlinePassed: Boolean
        get() = joinDeadline != null && joinDeadline < Clock.System.now()

    val isJoinLeaveButtonEnabled: Boolean
        get() = !isAuthenticated || (!isDeadlinePassed && (!isEventFull || userJoined))
}

sealed class EventDetailsEffect {
    data object NavigateBack : EventDetailsEffect()

    data class NavigateToLocation(val lat: Double, val lng: Double) : EventDetailsEffect()

    data object NavigateToLogin : EventDetailsEffect()

    data object ShowEventFullSnackbar : EventDetailsEffect()

    data object NavigateToError : EventDetailsEffect()
}
