package cmm.apps.viewmodel.registration

import cmm.apps.viewmodel.registration.RegistrationViewHelper.getEsmorgaErrorScreenArguments
import org.koin.core.component.KoinComponent


data class RegistrationUiState(
    val loading: Boolean = false,
    val nameError: String? = null,
    val lastNameError: String? = null,
    val emailError: String? = null,
    val passError: String? = null,
    val repeatPassError: String? = null
) {
    fun hasAnyError() = nameError != null || lastNameError != null || emailError != null || passError != null || repeatPassError != null
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