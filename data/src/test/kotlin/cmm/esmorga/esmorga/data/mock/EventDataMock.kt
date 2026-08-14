package cmm.esmorga.data.mock

import cmm.esmorga.data.event.model.EventDataModel
import cmm.esmorga.data.event.model.EventLocationDataModel
import cmm.esmorga.domain.event.model.EventType
import kotlin.time.Clock


object EventDataMock {

    fun provideEventDataModelList(nameList: List<String>): List<EventDataModel> = nameList.map { name -> provideEventDataModel(name) }

    fun provideEventDataModel(name: String): EventDataModel = EventDataModel(
        dataId = "$name-${System.currentTimeMillis()}",
        dataName = name,
        dataDate = Clock.System.now(),
        dataDescription = "description",
        dataType = EventType.SPORT,
        dataLocation = EventLocationDataModel("Location")
    )

}