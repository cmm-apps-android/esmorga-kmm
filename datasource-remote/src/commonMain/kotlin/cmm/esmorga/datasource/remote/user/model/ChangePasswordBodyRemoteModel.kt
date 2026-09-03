package cmm.esmorga.datasource.remote.user.model

import kotlinx.serialization.Serializable

@Serializable
data class ChangePasswordBodyRemoteModel(
    val currentPassword: String,
    val newPassword: String
)