package cmm.apps.datasource.remote.event

import cmm.apps.data.event.datasource.EventDatasource
import cmm.apps.data.event.model.EventDataModel
import cmm.apps.datasource.remote.api.EsmorgaApi
import cmm.apps.datasource.remote.api.ExceptionHandler.manageApiException
import cmm.apps.datasource.remote.event.mapper.toEventDataModelList


class EventRemoteDatasourceImpl(private val eventApi: EsmorgaApi) : EventDatasource {

    override suspend fun getEvents(): List<EventDataModel> {
        try {
            val eventList = eventApi.getEvents()
            return eventList.remoteEventList.toEventDataModelList()
        } catch (e: Throwable) {
            throw manageApiException(e)
        }
    }
}