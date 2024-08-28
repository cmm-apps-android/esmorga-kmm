package cmm.apps.datasource.remote.user.mapper

import cmm.apps.data.user.model.UserDataModel
import cmm.apps.datasource.remote.user.model.UserRemoteModel

fun UserRemoteModel.toUserDataModel(): UserDataModel {
    return UserDataModel(
        dataName = remoteProfile.remoteName,
        dataLastName = remoteProfile.remoteLastName,
        dataEmail = remoteProfile.remoteEmail
    )
}