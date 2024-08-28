package cmm.apps.datasource.remote.event.mapper

import cmm.apps.datasource.remote.event.model.EventLocationRemoteModel
import cmm.apps.datasource.remote.event.model.EventRemoteModel
import cmm.apps.domain.event.model.EventType
import cmm.apps.domain.result.ErrorCodes
import cmm.apps.domain.result.EsmorgaException
import cmm.apps.domain.result.Source
import cmm.apps.data.event.model.EventDataModel
import cmm.apps.data.event.model.EventLocationDataModel
import io.ktor.http.parsing.ParseException
import kotlinx.datetime.Instant


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