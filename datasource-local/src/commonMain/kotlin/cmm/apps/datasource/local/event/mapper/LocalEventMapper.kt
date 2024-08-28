package cmm.apps.datasource.local.event.mapper

import cmm.apps.data.event.model.EventDataModel
import cmm.apps.data.event.model.EventLocationDataModel
import cmm.apps.datasource.local.event.model.EventLocalModel
import cmm.apps.domain.event.model.EventType
import cmm.apps.domain.result.ErrorCodes
import cmm.apps.domain.result.EsmorgaException
import cmm.apps.domain.result.Source
import kotlinx.datetime.Instant


fun EventLocalModel.toEventDataModel(): EventDataModel {
    val parsedType = try {
        EventType.valueOf(this.localType)
    } catch (e: Exception) {
        throw EsmorgaException(message = "Error parsing type [${this.localType.uppercase()}] in EventRemoteModel", source = Source.LOCAL, code = ErrorCodes.PARSE_ERROR)
    }

    return EventDataModel(
        dataId = this.localId,
        dataName = this.localName,
        dataDate = Instant.parse(this.localDate),
        dataDescription = this.localDescription,
        dataType = parsedType,
        dataImageUrl = this.localImageUrl,
        dataLocation = EventLocationDataModel(this.localLocationName, this.localLocationLat, this.localLocationLong),
        dataCreationTime = localCreationTime
    )
}

fun List<EventLocalModel>.toEventDataModelList(): List<EventDataModel> = this.map { erm -> erm.toEventDataModel() }

fun EventDataModel.toEventLocalModel(): EventLocalModel {
    return EventLocalModel(
        localId = this.dataId,
        localName = this.dataName,
        localDate = this.dataDate.toString(),
        localDescription = this.dataDescription,
        localType = this.dataType.name,
        localImageUrl = this.dataImageUrl,
        localLocationName = this.dataLocation.name,
        localLocationLat = this.dataLocation.lat,
        localLocationLong = this.dataLocation.long,
        localCreationTime = dataCreationTime
    )
}

fun List<EventDataModel>.toEventLocalModelList(): List<EventLocalModel> = this.map { elm -> elm.toEventLocalModel() }