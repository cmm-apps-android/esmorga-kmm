package cmm.esmorga.datasource.local.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import cmm.esmorga.datasource.local.event.model.AttendeeLocalModel

@Dao
interface AttendeeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAttendee(attendee: AttendeeLocalModel)

    @Query("SELECT localUserName FROM AttendeeLocalModel WHERE localEventId = :eventId AND localAlreadyPaid = 1")
    suspend fun getPaidAttendeesNamesByEvent(eventId: String): List<String>

    @Query("DELETE FROM AttendeeLocalModel")
    suspend fun deleteAll()
}