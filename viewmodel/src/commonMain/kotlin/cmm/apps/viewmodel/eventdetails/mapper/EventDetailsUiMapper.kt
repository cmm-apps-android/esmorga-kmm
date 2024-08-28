package cmm.apps.viewmodel.eventdetails.mapper

import cmm.apps.domain.event.model.Event
import cmm.apps.viewmodel.eventlist.mapper.EventListUiMapper.formatDate
import cmm.apps.viewmodel.eventdetails.model.EventDetailsUiState

object EventDetailsUiMapper {

    fun Event.toEventUiDetails(): EventDetailsUiState = EventDetailsUiState(
        id = this.id,
        image = this.imageUrl,
        title = this.name,
        subtitle = formatDate(date),
        description = this.description,
        locationName = this.location.name,
        locationLat = this.location.lat,
        locationLng = this.location.long
    )
}