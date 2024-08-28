package cmm.apps.datasource.local.user.mapper

import cmm.apps.data.user.model.UserDataModel
import cmm.apps.datasource.local.user.model.UserLocalModel

fun UserLocalModel.toUserDataModel(): UserDataModel = UserDataModel(
    dataEmail = localEmail,
    dataName = localName,
    dataLastName = localLastName
)

fun UserDataModel.toUserLocalModel(): UserLocalModel = UserLocalModel(
    localEmail = dataEmail,
    localName = dataName,
    localLastName = dataLastName
)