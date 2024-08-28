package cmm.apps.domain.event.model

import kotlinx.datetime.Instant

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
)

data class EventLocation(
    val name: String,
    val lat: Double? = null,
    val long: Double? = null
)