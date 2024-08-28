package cmm.apps.data.user.mapper

import cmm.apps.data.user.model.UserDataModel
import cmm.apps.domain.user.model.User

fun UserDataModel.toUser() = User(
    name = dataName,
    lastName = dataLastName,
    email = dataEmail
)