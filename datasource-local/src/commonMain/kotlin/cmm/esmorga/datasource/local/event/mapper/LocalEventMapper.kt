package cmm.esmorga.datasource.local.event.mapper

import cmm.esmorga.data.event.model.EventDataModel
import cmm.esmorga.data.event.model.EventLocationDataModel
import cmm.esmorga.datasource.local.event.model.EventLocalModel
import cmm.esmorga.domain.event.model.EventType
import cmm.esmorga.domain.result.ErrorCodes
import cmm.esmorga.domain.result.EsmorgaException
import cmm.esmorga.domain.result.Source
import kotlin.time.Instant


fun EventLocalModel.toEventDataModel(): EventDataModel {
    val parsedType = try {
        EventType.valueOf(this.localType)
    } catch (_: Exception) {
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
        dataTags = this.localTags,
        dataMaxCapacity = this.localMaxCapacity,
        dataJoinDeadline = this.localJoinDeadline?.let { Instant.parse(it) },
        dataCurrentAttendeeCount = this.localCurrentAttendeeCount,
        dataCreationTime = localCreationTime
    )
}

fun List<EventLocalModel>.toEventDataModelList(): List<EventDataModel> = this.map { erm -> erm.toEventDataModel() }

fun EventDataModel.toEventLocalModel(isMyEvent: Boolean = false): EventLocalModel {
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
        localTags = this.dataTags,
        localMaxCapacity = this.dataMaxCapacity,
        localJoinDeadline = this.dataJoinDeadline?.toString(),
        localCurrentAttendeeCount = this.dataCurrentAttendeeCount,
        localIsMyEvent = isMyEvent,
        localCreationTime = dataCreationTime
    )
}

fun List<EventDataModel>.toEventLocalModelList(isMyEvent: Boolean = false): List<EventLocalModel> = this.map { elm -> elm.toEventLocalModel(isMyEvent) }