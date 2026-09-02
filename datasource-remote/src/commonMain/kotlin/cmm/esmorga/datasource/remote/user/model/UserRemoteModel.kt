package cmm.esmorga.datasource.remote.user.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

interface TokenPayloadRemoteModel {
    val accessToken: String
    val refreshToken: String
    val ttl: Int
}

@Serializable
data class AccessTokenRemoteModel(
    override val accessToken: String,
    override val refreshToken: String,
    override val ttl: Int
) : TokenPayloadRemoteModel

@Serializable
data class UserRemoteModel(
    override val accessToken: String,
    override val refreshToken: String,
    override val ttl: Int,
    @SerialName("profile") val remoteProfile: ProfileRemoteModel
) : TokenPayloadRemoteModel

@Serializable
data class ProfileRemoteModel(
    @SerialName("name") val remoteName: String,
    @SerialName("lastName") val remoteLastName: String,
    @SerialName("email") val remoteEmail: String,
    @SerialName("role") val remoteRole: String
)
