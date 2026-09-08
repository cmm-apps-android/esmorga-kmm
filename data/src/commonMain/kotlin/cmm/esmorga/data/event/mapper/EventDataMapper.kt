package cmm.esmorga.data.event.mapper

import cmm.esmorga.data.event.model.EventDataModel
import cmm.esmorga.domain.event.model.Event
import cmm.esmorga.domain.event.model.EventLocation


fun EventDataModel.toEvent(): Event = Event(
    id = this.dataId,
    name = this.dataName,
    date = this.dataDate,
    description = this.dataDescription,
    type = this.dataType,
    imageUrl = this.dataImageUrl,
    location = EventLocation(this.dataLocation.name, this.dataLocation.lat, this.dataLocation.long),
    tags = this.dataTags,
    maxCapacity = this.dataMaxCapacity,
    joinDeadline = this.dataJoinDeadline,
    currentAttendeeCount = this.dataCurrentAttendeeCount,
    userJoined = this.dataUserJoined,
)

fun List<EventDataModel>.toEventList(): List<Event> = map { edm -> edm.toEvent() }