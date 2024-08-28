package cmm.apps.data.event.mapper

import cmm.apps.data.event.model.EventDataModel
import cmm.apps.domain.event.model.Event
import cmm.apps.domain.event.model.EventLocation


fun EventDataModel.toEvent(): Event = Event(
    id = this.dataId,
    name = this.dataName,
    date = this.dataDate,
    description = this.dataDescription,
    type = this.dataType,
    imageUrl = this.dataImageUrl,
    location = EventLocation(this.dataLocation.name, this.dataLocation.lat, this.dataLocation.long),
)

fun List<EventDataModel>.toEventList(): List<Event> = map { edm -> edm.toEvent() }