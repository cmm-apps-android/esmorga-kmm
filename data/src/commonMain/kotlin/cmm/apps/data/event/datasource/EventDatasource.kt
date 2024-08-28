package cmm.apps.data.event.datasource

import cmm.apps.data.event.model.EventDataModel
import cmm.apps.domain.result.ErrorCodes
import cmm.apps.domain.result.EsmorgaException
import cmm.apps.domain.result.Source


interface EventDatasource {
    suspend fun getEvents(): List<EventDataModel>

    suspend fun cacheEvents(events: List<EventDataModel>) = Unit

    suspend fun getEventById(eventId: String): EventDataModel {
        throw EsmorgaException(message = "Unsupported operation", source = Source.UNSUPPORTED, code = ErrorCodes.UNSUPPORTED_OPERATION)
    }
}