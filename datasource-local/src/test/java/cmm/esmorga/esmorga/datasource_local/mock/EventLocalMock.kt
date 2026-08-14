package cmm.esmorga.datasource_local.mock

import cmm.esmorga.datasource.local.event.model.EventLocalModel
import cmm.esmorga.domain.event.model.EventType
import kotlin.time.Clock


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