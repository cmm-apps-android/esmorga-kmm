package cmm.esmorga.datasource.remote.event.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EventListWrapperRemoteModel(
    @SerialName("totalEvents") val remoteTotalEvents: Int,
    @SerialName("events") val remoteEventList: List<EventRemoteModel>
)

@Serializable
data class EventRemoteModel(
    @SerialName("eventId") val remoteId: String,
    @SerialName("eventName") val remoteName: String,
    @SerialName("eventDate") val remoteDate: String,
    @SerialName("description") val remoteDescription: String,
    @SerialName("eventType") val remoteType: String,
    @SerialName("imageUrl") val remoteImageUrl: String? = null,
    @SerialName("location") val remoteLocation: EventLocationRemoteModel,
    @SerialName("tags") val remoteTags: List<String>?,
    @SerialName("maxCapacity") val remoteMaxCapacity: Int? = null,
    @SerialName("joinDeadline") val remoteJoinDeadline: String,
    @SerialName("currentAttendeeCount") val remoteCurrentAttendeeCount: Int,
    @SerialName("userJoined") val remoteUserJoined: Boolean = false
)

@Serializable
data class EventLocationRemoteModel(
    @SerialName("name") val remoteLocationName: String,
    @SerialName("lat") val remoteLat: Double? = null,
    @SerialName("long") val remoteLong: Double? = null
)
