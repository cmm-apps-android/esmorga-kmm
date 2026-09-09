package cmm.esmorga.viewmodel.registration

import cmm.esmorga.viewmodel.registration.RegistrationViewHelper.getEsmorgaErrorScreenArguments
import org.koin.core.component.KoinComponent


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
    data class ShowFullScreenError(val esmorgaErrorScreenArguments: String = getEsmorgaErrorScreenArguments()) : RegistrationEffect()
}

object RegistrationViewHelper : KoinComponent {
    fun getEsmorgaErrorScreenArguments() = ""

    fun getNameErrorText() = "registration_name_last_name_invalid"
    fun getLastNameErrorText() = "registration_name_last_name_invalid"
    fun getEmailErrorText() = "registration_email_invalid"
    fun getEmailAlreadyInUseErrorText() = "registration_email_already_used"
    fun getPasswordErrorText() = "registration_password_invalid"
    fun getRepeatPasswordErrorText() = "registration_password_mismatch_error"
    fun getEmptyFieldErrorText() = "registration_empty_field"
}