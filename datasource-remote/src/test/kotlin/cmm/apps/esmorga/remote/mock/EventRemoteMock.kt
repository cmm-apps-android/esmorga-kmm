package cmm.apps.esmorga.remote.mock

import cmm.apps.datasource.remote.event.model.EventListWrapperRemoteModel
import cmm.apps.datasource.remote.event.model.EventLocationRemoteModel
import cmm.apps.datasource.remote.event.model.EventRemoteModel
import cmm.apps.domain.event.model.EventType


object EventRemoteMock {

    fun provideEventListWrapper(nameList: List<String>): EventListWrapperRemoteModel {
        val list = provideEventList(nameList)
        return EventListWrapperRemoteModel(list.size, list)
    }

    fun provideEventList(nameList: List<String>): List<EventRemoteModel> = nameList.map { name -> provideEvent(name) }

    fun provideEvent(name: String): EventRemoteModel = EventRemoteModel(
        remoteId = "$name-${System.currentTimeMillis()}",
        remoteName = name,
        remoteDate = "2030-12-31T23:59:59.000Z",
        remoteDescription = "description",
        remoteType = EventType.SPORT.name,
        remoteImageUrl = null,
        remoteLocation = EventLocationRemoteModel("Location"),
    )

}