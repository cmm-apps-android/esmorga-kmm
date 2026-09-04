package cmm.esmorga.viewmodel.changepassword

data class ChangePasswordUiState(
    val loading: Boolean = false,
    val currentPasswordError: ChangePasswordErrorRes? = null,
    val newPasswordError: ChangePasswordErrorRes? = null,
    val repeatPasswordError: ChangePasswordErrorRes? = null
) {
    fun hasAnyError() = currentPasswordError != null || newPasswordError != null || repeatPasswordError != null
}

enum class ChangePasswordErrorRes {
    EMPTY_FIELD,
    INVALID_PASSWORD,
    REUSED_PASSWORD,
    PASSWORD_MISMATCH
}

sealed class ChangePasswordEffect {
    data object NavigateToLogin : ChangePasswordEffect()
    data class ShowFullScreenError(val esmorgaErrorScreenArguments: String = "") : ChangePasswordEffect()
}