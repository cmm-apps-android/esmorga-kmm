package cmm.esmorga.datasource.local.user.mapper

import cmm.esmorga.data.user.model.UserDataModel
import cmm.esmorga.datasource.local.user.model.UserLocalModel

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