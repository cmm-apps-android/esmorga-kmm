package cmm.esmorga.data.event

import cmm.esmorga.data.CacheHelper
import cmm.esmorga.data.event.datasource.EventDatasource
import cmm.esmorga.data.event.mapper.toEvent
import cmm.esmorga.data.event.mapper.toEventList
import cmm.esmorga.data.event.model.EventDataModel
import cmm.esmorga.data.user.datasource.UserDatasource
import cmm.esmorga.domain.event.model.Event
import cmm.esmorga.domain.event.repository.EventRepository
import cmm.esmorga.domain.result.ErrorCodes
import cmm.esmorga.domain.result.EsmorgaException
import cmm.esmorga.domain.result.Success
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class EventRepositoryImpl(
    private val localUserDs: UserDatasource,
    private val localEventDs: EventDatasource,
    private val remoteEventDs: EventDatasource
) : EventRepository {

    override suspend fun getEvents(forceRefresh: Boolean): Success<List<Event>> {
        val localList = localEventDs.getEvents()

        if (forceRefresh.not() && localList.isNotEmpty() && CacheHelper.shouldReturnCache(localList[0].dataCreationTime)) {
            return Success(localList.toEventList())
        }

        return try {
            val eventsFromRemote = getEventsFromRemote()
            localEventDs.cacheEvents(eventsFromRemote)
            Success(eventsFromRemote.toEventList())
        } catch (esmorgaEx: EsmorgaException) {
            if (esmorgaEx.code == ErrorCodes.NO_CONNECTION) {
                Success(localList.toEventList(), ErrorCodes.NO_CONNECTION)
            } else {
                throw esmorgaEx
            }
        }
    }

    override suspend fun getMyEvents(forceRefresh: Boolean): Success<List<Event>> {
        val eventsResult = getEvents(forceRefresh)
        return Success(eventsResult.data.filter { it.userJoined }, eventsResult.nonBlockingError)
    }

    override suspend fun joinEvent(eventId: String): Success<Unit> {
        remoteEventDs.joinEvent(eventId)
        return Success(Unit)
    }

    override suspend fun leaveEvent(eventId: String): Success<Unit> {
        remoteEventDs.leaveEvent(eventId)
        return Success(Unit)
    }

    override suspend fun getEventDetails(eventId: String): Success<Event> {
        return Success(localEventDs.getEventById(eventId).toEvent())
    }


    private suspend fun getEventsFromRemote(): List<EventDataModel> = coroutineScope {
        val user = runCatching { localUserDs.getUser() }.getOrNull()

        if (user == null) {
            return@coroutineScope remoteEventDs.getEvents()
        }

        val remoteEventsDeferred = async { remoteEventDs.getEvents() }
        val remoteMyEventsDeferred = async { remoteEventDs.getMyEvents() }

        val remoteEvents = remoteEventsDeferred.await()
        val myEventIds = remoteMyEventsDeferred.await().map { it.dataId }.toSet()

        remoteEvents.map { event ->
            event.copy(dataUserJoined = event.dataId in myEventIds)
        }
    }

}