package cmm.esmorga.data.user.mapper

import cmm.esmorga.data.user.model.UserDataModel
import cmm.esmorga.domain.user.model.User

fun UserDataModel.toUser() = User(
    name = dataName,
    lastName = dataLastName,
    email = dataEmail,
    accessToken = dataAccessToken,
    refreshToken = dataRefreshToken,
    expiresAt = dataExpiresAt
)