package cmm.apps.data.event

import cmm.apps.data.CacheHelper
import cmm.apps.data.event.datasource.EventDatasource
import cmm.apps.data.event.mapper.toEvent
import cmm.apps.data.event.mapper.toEventList
import cmm.apps.domain.event.model.Event
import cmm.apps.domain.event.repository.EventRepository
import cmm.apps.domain.result.ErrorCodes
import cmm.apps.domain.result.EsmorgaException
import cmm.apps.domain.result.Success

class EventRepositoryImpl(private val localDs: EventDatasource, private val remoteDs: EventDatasource) : EventRepository {

    override suspend fun getEvents(forceRefresh: Boolean): Success<List<Event>> {
        val localList = localDs.getEvents()

        if (forceRefresh.not() && localList.isNotEmpty() && CacheHelper.shouldReturnCache(localList[0].dataCreationTime)) {
            return Success(localList.toEventList())
        }

        try {
            val remoteList = remoteDs.getEvents()
            localDs.cacheEvents(remoteList)

            return Success(remoteList.toEventList())
        } catch (esmorgaEx: EsmorgaException) {
            if (esmorgaEx.code == ErrorCodes.NO_CONNECTION) {
                return Success(localList.toEventList(), ErrorCodes.NO_CONNECTION)
            } else {
                throw esmorgaEx
            }
        }
    }

    override suspend fun getEventDetails(eventId: String): Success<Event> {
        return Success(localDs.getEventById(eventId).toEvent())
    }

}