package cmm.esmorga.datasource.remote.event

import cmm.esmorga.data.event.datasource.EventDatasource
import cmm.esmorga.data.event.model.EventDataModel
import cmm.esmorga.datasource.remote.api.EsmorgaApi
import cmm.esmorga.datasource.remote.api.EsmorgaPublicApi
import cmm.esmorga.datasource.remote.api.ExceptionHandler.manageApiException
import cmm.esmorga.datasource.remote.event.mapper.toEventDataModelList


class EventRemoteDatasourceImpl(
    private val publicApi: EsmorgaPublicApi,
    private val authenticatedApi: EsmorgaApi
) : EventDatasource {

    override suspend fun getEvents(): List<EventDataModel> {
        try {
            val eventList = publicApi.getEvents()
            return eventList.remoteEventList.toEventDataModelList()
        } catch (e: Throwable) {
            throw manageApiException(e)
        }
    }

    override suspend fun getMyEvents(): List<EventDataModel> {
        try {
            val eventList = authenticatedApi.getMyEvents()
            return eventList.remoteEventList.toEventDataModelList()
        } catch (e: Throwable) {
            throw manageApiException(e)
        }
    }

    override suspend fun joinEvent(eventId: String) {
        try {
            authenticatedApi.joinEvent(eventId)
        } catch (e: Throwable) {
            throw manageApiException(e)
        }
    }

    override suspend fun leaveEvent(eventId: String) {
        try {
            authenticatedApi.leaveEvent(eventId)
        } catch (e: Throwable) {
            throw manageApiException(e)
        }
    }

    override suspend fun getEventAttendees(eventId: String): List<String> {
        return try {
            authenticatedApi.getEventAttendees(eventId).remoteEventAttendeeList
        } catch (e: Throwable) {
            throw manageApiException(e)
        }
    }
}