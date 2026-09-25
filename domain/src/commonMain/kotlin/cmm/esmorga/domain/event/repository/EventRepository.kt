package cmm.esmorga.domain.event.repository

import cmm.esmorga.domain.event.model.Attendee
import cmm.esmorga.domain.event.model.CreateEventForm
import cmm.esmorga.domain.event.model.Event
import cmm.esmorga.domain.result.Success


interface EventRepository {
    suspend fun getEvents(forceRefresh: Boolean = false): Success<List<Event>>
    suspend fun getMyEvents(forceRefresh: Boolean = false): Success<List<Event>>
    suspend fun joinEvent(eventId: String): Success<Unit>
    suspend fun leaveEvent(eventId: String): Success<Unit>
    suspend fun getEventDetails(eventId: String): Success<Event>
    suspend fun getEventAttendees(eventId: String): Success<List<Attendee>>
    suspend fun saveAttendeePayment(eventId: String, userName: String, paid: Boolean): Success<Unit>
    suspend fun createEvent(eventForm: CreateEventForm)
}