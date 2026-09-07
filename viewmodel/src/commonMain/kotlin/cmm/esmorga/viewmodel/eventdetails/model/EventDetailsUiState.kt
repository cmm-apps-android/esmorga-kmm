package cmm.esmorga.viewmodel.eventdetails.model

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
    val navigateButton: Boolean = locationLat != null && locationLng != null,
)

sealed class EventDetailsEffect {
    data object NavigateBack : EventDetailsEffect()

    data class NavigateToLocation(val lat: Double, val lng: Double) : EventDetailsEffect()

}
