package cmm.apps.datasource.remote.user.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserRemoteModel(
    @SerialName("accessToken") val remoteAccessToken: String,
    @SerialName("refreshToken") val remoteRefreshToken: String,
    @SerialName("profile") val remoteProfile: ProfileRemoteModel
)

@Serializable
data class ProfileRemoteModel(
    @SerialName("name") val remoteName: String,
    @SerialName("lastName") val remoteLastName: String,
    @SerialName("email") val remoteEmail: String
)
