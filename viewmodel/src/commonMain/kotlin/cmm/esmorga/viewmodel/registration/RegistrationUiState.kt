package cmm.esmorga.viewmodel.registration

enum class RegistrationValidationError {
    NONE,
    EMPTY,
    INVALID_NAME,
    INVALID_LAST_NAME,
    INVALID_EMAIL,
    EMAIL_ALREADY_IN_USE,
    INVALID_PASSWORD,
    PASSWORD_MISMATCH
}

data class RegistrationUiState(
    val loading: Boolean = false,
    val nameError: RegistrationValidationError = RegistrationValidationError.NONE,
    val lastNameError: RegistrationValidationError = RegistrationValidationError.NONE,
    val emailError: RegistrationValidationError = RegistrationValidationError.NONE,
    val passError: RegistrationValidationError = RegistrationValidationError.NONE,
    val repeatPassError: RegistrationValidationError = RegistrationValidationError.NONE
) {
    fun hasAnyError() = nameError != RegistrationValidationError.NONE ||
            lastNameError != RegistrationValidationError.NONE ||
            emailError != RegistrationValidationError.NONE ||
            passError != RegistrationValidationError.NONE ||
            repeatPassError != RegistrationValidationError.NONE
}

sealed class RegistrationEffect {
    data object ShowNoNetworkSnackbar : RegistrationEffect()
    data object NavigateToEventList : RegistrationEffect()
    data object ShowFullScreenError : RegistrationEffect()
}