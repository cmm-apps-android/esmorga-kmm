package cmm.esmorga.data.event.datasource

import cmm.esmorga.data.event.model.EventDataModel
import cmm.esmorga.domain.result.ErrorCodes
import cmm.esmorga.domain.result.EsmorgaException
import cmm.esmorga.domain.result.Source


interface EventDatasource {
    suspend fun getEvents(): List<EventDataModel>

    suspend fun getMyEvents(): List<EventDataModel> {
        throw EsmorgaException(message = "Unsupported operation", source = Source.UNSUPPORTED, code = ErrorCodes.UNSUPPORTED_OPERATION)
    }

    suspend fun joinEvent(eventId: String) {
        throw EsmorgaException(message = "Unsupported operation", source = Source.UNSUPPORTED, code = ErrorCodes.UNSUPPORTED_OPERATION)
    }

    suspend fun leaveEvent(eventId: String) {
        throw EsmorgaException(message = "Unsupported operation", source = Source.UNSUPPORTED, code = ErrorCodes.UNSUPPORTED_OPERATION)
    }

    suspend fun cacheEvents(events: List<EventDataModel>) = Unit

    suspend fun getEventById(eventId: String): EventDataModel {
        throw EsmorgaException(message = "Unsupported operation", source = Source.UNSUPPORTED, code = ErrorCodes.UNSUPPORTED_OPERATION)
    }

    suspend fun clearEvents() {
        throw EsmorgaException(message = "Unsupported operation", source = Source.UNSUPPORTED, code = ErrorCodes.UNSUPPORTED_OPERATION)
    }
}