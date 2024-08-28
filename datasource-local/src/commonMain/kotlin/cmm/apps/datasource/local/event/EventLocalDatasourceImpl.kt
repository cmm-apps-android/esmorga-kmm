package cmm.apps.datasource.local.event

import cmm.apps.data.event.datasource.EventDatasource
import cmm.apps.data.event.model.EventDataModel
import cmm.apps.datasource.local.database.dao.EventDao
import cmm.apps.datasource.local.event.mapper.toEventDataModel
import cmm.apps.datasource.local.event.mapper.toEventDataModelList
import cmm.apps.datasource.local.event.mapper.toEventLocalModelList


class EventLocalDatasourceImpl(private val eventDao: EventDao) : EventDatasource {

    override suspend fun getEvents(): List<EventDataModel> {
        return eventDao.getEvents().toEventDataModelList()
    }

    override suspend fun cacheEvents(events: List<EventDataModel>) {
        eventDao.deleteAll()
        eventDao.insertEvent(events.toEventLocalModelList())
    }

    override suspend fun getEventById(eventId: String): EventDataModel {
        return eventDao.getEventById(eventId).toEventDataModel()
    }

}