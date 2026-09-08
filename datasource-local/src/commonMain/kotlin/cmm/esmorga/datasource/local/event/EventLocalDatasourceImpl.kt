package cmm.esmorga.datasource.local.event

import cmm.esmorga.data.event.datasource.EventDatasource
import cmm.esmorga.data.event.model.EventDataModel
import cmm.esmorga.datasource.local.database.dao.EventDao
import cmm.esmorga.datasource.local.event.mapper.toEventDataModel
import cmm.esmorga.datasource.local.event.mapper.toEventDataModelList
import cmm.esmorga.datasource.local.event.mapper.toEventLocalModelList
import cmm.esmorga.domain.result.ErrorCodes
import cmm.esmorga.domain.result.EsmorgaException
import cmm.esmorga.domain.result.Source


class EventLocalDatasourceImpl(private val eventDao: EventDao) : EventDatasource {

    override suspend fun getEvents(): List<EventDataModel> {
        return eventDao.getEvents().toEventDataModelList()
    }

    override suspend fun getMyEvents(): List<EventDataModel> {
        throw EsmorgaException(message = "Unsupported operation", source = Source.UNSUPPORTED, code = ErrorCodes.UNSUPPORTED_OPERATION)
    }

    override suspend fun cacheEvents(events: List<EventDataModel>) {
        eventDao.deleteAll()
        eventDao.insertEvent(events.toEventLocalModelList())
    }

    override suspend fun getEventById(eventId: String): EventDataModel {
        return eventDao.getEventById(eventId).toEventDataModel()
    }

}