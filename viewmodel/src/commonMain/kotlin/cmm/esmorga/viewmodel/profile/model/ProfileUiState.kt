package cmm.esmorga.viewmodel.profile.model

data class ProfileUiState(
    val isLoggedIn: Boolean = false,
    val name: String = "",
    val email: String = ""
)