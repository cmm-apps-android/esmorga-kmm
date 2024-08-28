package cmm.apps.esmorga.datasource_local.mock

import cmm.apps.datasource.local.event.model.EventLocalModel
import cmm.apps.domain.event.model.EventType
import kotlinx.datetime.Clock


object EventLocalMock {

    fun provideEventList(nameList: List<String>): List<EventLocalModel> = nameList.map { name -> provideEvent(name) }

    fun provideEvent(name: String): EventLocalModel = EventLocalModel(
        localId = "$name-${System.currentTimeMillis()}",
        localName = name,
        localDate = Clock.System.now().toString(),
        localDescription = "Description",
        localType = EventType.SPORT.name,
        localLocationName = "Location",
        localCreationTime = System.currentTimeMillis()
    )

}