package cmm.esmorga.datasource.remote.user.mapper

import cmm.esmorga.data.user.model.TokenDataModel
import cmm.esmorga.data.user.model.UserDataModel
import cmm.esmorga.datasource.remote.user.model.AccessTokenRemoteModel
import cmm.esmorga.datasource.remote.user.model.UserRemoteModel
import kotlin.time.Clock

fun UserRemoteModel.toUserDataModel(): UserDataModel {
    return UserDataModel(
        dataName = remoteProfile.remoteName,
        dataLastName = remoteProfile.remoteLastName,
        dataEmail = remoteProfile.remoteEmail,
        role = remoteProfile.remoteRole,
        dataAccessToken = accessToken,
        dataRefreshToken = refreshToken,
        dataExpiresAt = Clock.System.now().toEpochMilliseconds() + (ttl * 1000L)
    )
}

fun AccessTokenRemoteModel.toTokenDataModel(): TokenDataModel {
    return TokenDataModel(
        dataAccessToken = accessToken,
        dataRefreshToken = refreshToken,
        dataExpiresAt = Clock.System.now().toEpochMilliseconds() + (ttl * 1000L)
    )
}
