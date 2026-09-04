package cmm.esmorga.viewmodel.changepassword

import androidx.lifecycle.viewModelScope
import cmm.esmorga.domain.result.EsmorgaException
import cmm.esmorga.domain.user.ChangePasswordUseCase
import cmm.esmorga.domain.user.LogOutUseCase
import cmm.esmorga.domain.user.model.User.Companion.PASSWORD_REGEX
import cmm.esmorga.viewmodel.BaseViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ChangePasswordViewModel(
    private val changePasswordUseCase: ChangePasswordUseCase,
    private val logOutUseCase: LogOutUseCase
) : BaseViewModel() {

    private val _uiState = MutableStateFlow(ChangePasswordUiState())
    val uiState: StateFlow<ChangePasswordUiState> = _uiState.asStateFlow()

    private val _effect: MutableSharedFlow<ChangePasswordEffect> = MutableSharedFlow(extraBufferCapacity = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    val effect: SharedFlow<ChangePasswordEffect> = _effect.asSharedFlow()

    fun onCurrentPasswordChanged(value: String) {
        _uiState.value = _uiState.value.copy(
            currentPasswordError = getPassFieldErrorText(
                value = value,
                isValidCondition = value.matches(PASSWORD_REGEX.toRegex())
            )
        )
    }

    fun onNewPasswordChanged(value: String) {
        _uiState.value = _uiState.value.copy(
            newPasswordError = getPassFieldErrorText(
                value = value,
                isValidCondition = value.matches(PASSWORD_REGEX.toRegex())
            )
        )
    }

    fun onRepeatPasswordChanged(newPassword: String, repeatedPassword: String) {
        _uiState.value = _uiState.value.copy(
            repeatPasswordError = getPassFieldErrorText(
                value = repeatedPassword,
                isValidCondition = repeatedPassword.matches(PASSWORD_REGEX.toRegex()),
                mismatchError = repeatedPassword.isNotEmpty() && newPassword.isNotEmpty() && repeatedPassword != newPassword
            )
        )
    }

    fun onChangePasswordClicked(currentPassword: String, newPassword: String, repeatedPassword: String) {
        onCurrentPasswordChanged(currentPassword)
        onNewPasswordChanged(newPassword)
        onRepeatPasswordChanged(newPassword = newPassword, repeatedPassword = repeatedPassword)

        if (_uiState.value.hasAnyError()) return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(loading = true)
            val result = changePasswordUseCase(currentPassword.trim(), newPassword.trim())
            result.onSuccess {
                // Even if local cleanup fails, force a fresh login flow after password change.
                logOutUseCase()
                _effect.tryEmit(ChangePasswordEffect.NavigateToLogin)
            }.onFailure { error ->
                _uiState.value = _uiState.value.copy(loading = false)
                if (error is EsmorgaException && error.code == 409) {
                    _uiState.value = _uiState.value.copy(
                        newPasswordError = getPassFieldErrorText(
                            value = newPassword,
                            isValidCondition = true,
                            reusedError = true
                        )
                    )
                } else {
                    _effect.tryEmit(ChangePasswordEffect.ShowFullScreenError())
                }
            }
        }
    }

    private fun getPassFieldErrorText(
        value: String,
        isValidCondition: Boolean,
        reusedError: Boolean = false,
        mismatchError: Boolean = false,
    ): ChangePasswordErrorRes? {
        val isBlank = value.isBlank()
        val isValid = value.isEmpty() || isValidCondition
        return when {
            isBlank -> ChangePasswordErrorRes.EMPTY_FIELD
            !isValid -> ChangePasswordErrorRes.INVALID_PASSWORD
            reusedError -> ChangePasswordErrorRes.REUSED_PASSWORD
            mismatchError -> ChangePasswordErrorRes.PASSWORD_MISMATCH
            else -> null
        }
    }
}
