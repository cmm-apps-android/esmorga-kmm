package cmm.esmorga.datasource.remote.event.mapper

import cmm.esmorga.datasource.remote.event.model.EventLocationRemoteModel
import cmm.esmorga.datasource.remote.event.model.EventRemoteModel
import cmm.esmorga.domain.event.model.EventType
import cmm.esmorga.domain.result.ErrorCodes
import cmm.esmorga.domain.result.EsmorgaException
import cmm.esmorga.domain.result.Source
import cmm.esmorga.data.event.model.EventDataModel
import cmm.esmorga.data.event.model.EventLocationDataModel
import io.ktor.http.parsing.ParseException
import kotlin.time.Instant


fun EventRemoteModel.toEventDataModel(): EventDataModel {
    val parsedDate = try {
        Instant.parse(remoteDate)
    } catch (e: Exception) {
        throw ParseException("Error parsing date in EventRemoteModel")
    }

    val parsedType = try {
        EventType.valueOf(this.remoteType.uppercase())
    } catch (e: Exception) {
        throw EsmorgaException(message = "Error parsing type [${this.remoteType.uppercase()}] in EventRemoteModel", source = Source.REMOTE, code = ErrorCodes.PARSE_ERROR)
    }

    return EventDataModel(
        dataId = this.remoteId,
        dataName = this.remoteName,
        dataDate = parsedDate,
        dataDescription = this.remoteDescription,
        dataType = parsedType,
        dataImageUrl = this.remoteImageUrl,
        dataLocation = this.remoteLocation.toEventLocationDataModel(),
    )
}

fun List<EventRemoteModel>.toEventDataModelList(): List<EventDataModel> = this.map { erm -> erm.toEventDataModel() }

fun EventLocationRemoteModel.toEventLocationDataModel(): EventLocationDataModel = EventLocationDataModel(
    name = this.remoteLocationName,
    lat = this.remoteLat,
    long = this.remoteLong
)