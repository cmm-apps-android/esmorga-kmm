package cmm.esmorga.datasource.local.event

import cmm.esmorga.data.event.datasource.EventDatasource
import cmm.esmorga.data.event.model.EventDataModel
import cmm.esmorga.datasource.local.database.dao.AttendeeDao
import cmm.esmorga.datasource.local.database.dao.EventDao
import cmm.esmorga.datasource.local.event.mapper.toEventDataModel
import cmm.esmorga.datasource.local.event.mapper.toEventDataModelList
import cmm.esmorga.datasource.local.event.mapper.toEventLocalModelList
import cmm.esmorga.datasource.local.event.model.AttendeeLocalModel
import cmm.esmorga.domain.result.ErrorCodes
import cmm.esmorga.domain.result.EsmorgaException
import cmm.esmorga.domain.result.Source


class EventLocalDatasourceImpl(
    private val eventDao: EventDao,
    private val attendeeDao: AttendeeDao
) : EventDatasource {

    override suspend fun getEvents(): List<EventDataModel> {
        return eventDao.getEvents().toEventDataModelList()
    }

    override suspend fun getMyEvents(): List<EventDataModel> {
        throw EsmorgaException(message = "Unsupported operation", source = Source.UNSUPPORTED, code = ErrorCodes.UNSUPPORTED_OPERATION)
    }

    override suspend fun cacheEvents(events: List<EventDataModel>) {
        clearEvents()
        eventDao.insertEvent(events.toEventLocalModelList())
    }

    override suspend fun getEventById(eventId: String): EventDataModel {
        return eventDao.getEventById(eventId).toEventDataModel()
    }

    override suspend fun clearEvents() {
        eventDao.deleteAll()
    }

    override suspend fun resetUserEvents() {
        eventDao.insertEvent(eventDao.getEvents().map { it.copy(localUserJoined = false) })
    }

    override suspend fun getPaidAttendeesNames(eventId: String): List<String> {
        return attendeeDao.getPaidAttendeesNamesByEvent(eventId)
    }

    override suspend fun saveAttendeePayment(eventId: String, userName: String, paid: Boolean) {
        attendeeDao.insertAttendee(
            AttendeeLocalModel(
                localEventId = eventId,
                localUserName = userName,
                localAlreadyPaid = paid
            )
        )
    }

    override suspend fun clearAttendeesPayments() {
        attendeeDao.deleteAll()
    }

}
