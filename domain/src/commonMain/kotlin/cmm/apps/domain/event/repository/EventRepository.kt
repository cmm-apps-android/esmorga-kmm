package cmm.apps.domain.event.repository

import cmm.apps.domain.event.model.Event
import cmm.apps.domain.result.Success


interface EventRepository {
    suspend fun getEvents(forceRefresh: Boolean = false): Success<List<Event>>
    suspend fun getEventDetails(eventId: String): Success<Event>
}