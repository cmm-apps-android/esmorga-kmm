package cmm.apps.viewmodel.login

import org.koin.core.component.KoinComponent

data class LoginUiState(
    val loading: Boolean = false,
    val emailError: String? = null,
    val passwordError: String? = null
) {
    fun hasAnyError() = emailError != null || passwordError != null
}

sealed class LoginEffect {
    data object NavigateToRegistration : LoginEffect()
    data object ShowNoNetworkSnackbar : LoginEffect()
    data object NavigateToEventList : LoginEffect()
    data class ShowFullScreenError(val error: String = "Error") : LoginEffect()
}

object LoginViewHelper : KoinComponent {
    fun getEsmorgaErrorScreenArguments() = "title"
    fun getEmailErrorText() = "error"
    fun getPasswordErrorText() = "error"
}