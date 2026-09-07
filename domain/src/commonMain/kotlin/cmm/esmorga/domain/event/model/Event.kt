package cmm.esmorga.domain.event.model

import kotlin.time.Instant

enum class EventType {
    PARTY, SPORT, FOOD, CHARITY, GAMES
}

data class Event(
    val id: String,
    val name: String,
    val date: Instant,
    val description: String,
    val type: EventType,
    val imageUrl: String? = null,
    val location: EventLocation,
    val tags: List<String> = emptyList(),
    val maxCapacity: Int? = null,
    val joinDeadline: Instant? = null,
    val currentAttendeeCount: Int = 0,
)

data class EventLocation(
    val name: String,
    val lat: Double? = null,
    val long: Double? = null
)