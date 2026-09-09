package cmm.esmorga.viewmodel.changepassword

data class ChangePasswordUiState(
    val loading: Boolean = false,
    val currentPasswordError: ChangePasswordErrorRes? = null,
    val newPasswordError: ChangePasswordErrorRes? = null,
    val repeatPasswordError: ChangePasswordErrorRes? = null
) {
    fun hasAnyError() = currentPasswordError != null || newPasswordError != null || repeatPasswordError != null
}

enum class ChangePasswordField {
    CURRENT_PASSWORD,
    NEW_PASSWORD,
    REPEAT_PASSWORD
}

enum class ChangePasswordErrorRes {
    INVALID_PASSWORD,
    REUSED_PASSWORD,
    PASSWORD_MISMATCH
}

sealed class ChangePasswordEffect {
    data object NavigateToHome : ChangePasswordEffect()
    data object ShowFullScreenError : ChangePasswordEffect()
}