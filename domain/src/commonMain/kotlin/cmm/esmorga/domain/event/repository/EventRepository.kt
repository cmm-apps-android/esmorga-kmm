package cmm.esmorga.domain.event.repository

import cmm.esmorga.domain.event.model.Event
import cmm.esmorga.domain.result.Success


interface EventRepository {
    suspend fun getEvents(forceRefresh: Boolean = false): Success<List<Event>>
    suspend fun getEventDetails(eventId: String): Success<Event>
}