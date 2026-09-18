package cmm.esmorga.datasource.local.event.model

import androidx.room.Entity

@Entity(primaryKeys = ["localEventId", "localUserName"])
data class AttendeeLocalModel(
    val localEventId: String,
    val localUserName: String,
    val localAlreadyPaid: Boolean
)