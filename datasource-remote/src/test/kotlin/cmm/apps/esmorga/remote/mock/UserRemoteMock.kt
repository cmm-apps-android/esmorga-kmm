package cmm.apps.esmorga.remote.mock

import cmm.apps.datasource.remote.user.model.ProfileRemoteModel
import cmm.apps.datasource.remote.user.model.UserRemoteModel

object UserRemoteMock {

    fun provideUser(name: String): UserRemoteModel = UserRemoteModel(
        remoteProfile = ProfileRemoteModel(
            remoteName = name,
            remoteEmail = "$",
            remoteLastName = "Doe"
        ),
        remoteRefreshToken = "refreshToken",
        remoteAccessToken = "token",
    )
}