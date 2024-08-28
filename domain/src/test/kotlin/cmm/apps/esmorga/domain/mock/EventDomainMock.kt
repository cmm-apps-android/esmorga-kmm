package cmm.apps.esmorga.domain.mock

import cmm.apps.domain.event.model.Event
import cmm.apps.domain.event.model.EventLocation
import cmm.apps.domain.event.model.EventType
import kotlinx.datetime.Clock


object EventDomainMock {

    fun provideEventList(nameList: List<String>): List<Event> = nameList.map { name -> provideEvent(name) }

    fun provideEvent(name: String): Event = Event(
        id = "$name-${System.currentTimeMillis()}",
        name = name,
        date = Clock.System.now(),
        description = "description",
        type = EventType.SPORT,
        location = EventLocation("Location")
    )

}