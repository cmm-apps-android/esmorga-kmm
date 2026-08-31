package cmm.esmorga.datasource.remote.event

import cmm.esmorga.data.event.datasource.EventDatasource
import cmm.esmorga.data.event.model.EventDataModel
import cmm.esmorga.datasource.remote.api.EsmorgaApi
import cmm.esmorga.datasource.remote.api.ExceptionHandler.manageApiException
import cmm.esmorga.datasource.remote.event.mapper.toEventDataModelList


class EventRemoteDatasourceImpl(private val eventApi: EsmorgaApi) : EventDatasource {

    override suspend fun getEvents(): List<EventDataModel> {
        try {
            val eventList = eventApi.getEvents()
            return eventList.remoteEventList.toEventDataModelList()
        } catch (e: Throwable) {
            throw manageApiException(e)
        }
    }

    override suspend fun getMyEvents(): List<EventDataModel> {
        try {
            val eventList = eventApi.getMyEvents()
            return eventList.remoteEventList.toEventDataModelList()
        } catch (e: Throwable) {
            throw manageApiException(e)
        }
    }
}