package cmm.esmorga.domain.event.model

data class CreateEventForm(
    val name: String,
    val description: String? = null,
    val type: EventType,
    val date: String,
    val joinDeadline: String? = null,
    val location: EventLocation,
    val maxCapacity: Int? = null,
    val imageUrl: String? = null
)