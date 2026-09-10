package cmm.esmorga.viewmodel.login

enum class ValidationError {
    NONE,
    EMPTY,
    INVALID_EMAIL,
    INVALID_PASSWORD
}

data class LoginUiState(
    val loading: Boolean = false,
    val emailError: ValidationError = ValidationError.NONE,
    val passwordError: ValidationError = ValidationError.NONE
) {
    fun hasAnyError() = emailError != ValidationError.NONE || passwordError != ValidationError.NONE
}

sealed class LoginEffect {
    data object NavigateToRegistration : LoginEffect()
    data object ShowNoNetworkSnackbar : LoginEffect()
    data object NavigateToEventList : LoginEffect()
    object ShowFullScreenError : LoginEffect()
}