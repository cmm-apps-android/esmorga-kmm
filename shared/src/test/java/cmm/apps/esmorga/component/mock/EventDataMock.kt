package cmm.apps.esmorga.component.mock

import cmm.apps.data.event.model.EventDataModel
import cmm.apps.data.event.model.EventLocationDataModel
import cmm.apps.domain.event.model.EventType
import kotlinx.datetime.Clock


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