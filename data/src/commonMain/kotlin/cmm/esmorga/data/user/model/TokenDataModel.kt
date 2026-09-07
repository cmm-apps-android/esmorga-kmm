package cmm.esmorga.data.user.model

data class TokenDataModel(
    val dataAccessToken: String,
    val dataRefreshToken: String,
    val dataExpiresAt: Long
)

