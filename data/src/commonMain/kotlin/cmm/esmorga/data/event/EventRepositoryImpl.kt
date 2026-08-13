package cmm.esmorga.data.event

import cmm.esmorga.data.CacheHelper
import cmm.esmorga.data.event.datasource.EventDatasource
import cmm.esmorga.data.event.mapper.toEvent
import cmm.esmorga.data.event.mapper.toEventList
import cmm.esmorga.domain.event.model.Event
import cmm.esmorga.domain.event.repository.EventRepository
import cmm.esmorga.domain.result.ErrorCodes
import cmm.esmorga.domain.result.EsmorgaException
import cmm.esmorga.domain.result.Success

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