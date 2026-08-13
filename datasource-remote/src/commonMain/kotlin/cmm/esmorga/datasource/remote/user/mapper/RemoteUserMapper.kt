package cmm.esmorga.datasource.remote.user.mapper

import cmm.esmorga.data.user.model.UserDataModel
import cmm.esmorga.datasource.remote.user.model.UserRemoteModel

fun UserRemoteModel.toUserDataModel(): UserDataModel {
    return UserDataModel(
        dataName = remoteProfile.remoteName,
        dataLastName = remoteProfile.remoteLastName,
        dataEmail = remoteProfile.remoteEmail
    )
}