package cmm.esmorga.datasource.remote.event.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EventAttendeeWrapperRemoteModel(
    @SerialName("totalUsers") val remoteTotalAttendees: Int,
    @SerialName("users") val remoteEventAttendeeList: List<String>
)
