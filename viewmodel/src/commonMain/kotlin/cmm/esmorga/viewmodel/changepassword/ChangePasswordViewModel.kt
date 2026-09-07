package cmm.esmorga.viewmodel.changepassword

import androidx.lifecycle.viewModelScope
import cmm.esmorga.domain.user.ChangePasswordUseCase
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
    private val changePasswordUseCase: ChangePasswordUseCase
) : BaseViewModel() {

    private val _uiState = MutableStateFlow(ChangePasswordUiState())
    val uiState: StateFlow<ChangePasswordUiState> = _uiState.asStateFlow()

    private val _effect: MutableSharedFlow<ChangePasswordEffect> = MutableSharedFlow(extraBufferCapacity = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    val effect: SharedFlow<ChangePasswordEffect> = _effect.asSharedFlow()

    fun validateField(
        field: ChangePasswordField,
        currentPassword: String,
        newPassword: String,
        repeatedPassword: String
    ) {
        _uiState.value = when (field) {
            ChangePasswordField.CURRENT_PASSWORD -> _uiState.value.copy(
                currentPasswordError = getPassFieldErrorText(
                    value = currentPassword,
                    isValidCondition = currentPassword.matches(PASSWORD_REGEX.toRegex())
                )
            )

            ChangePasswordField.NEW_PASSWORD -> _uiState.value.copy(
                newPasswordError = getPassFieldErrorText(
                    value = newPassword,
                    isValidCondition = newPassword.matches(PASSWORD_REGEX.toRegex()),
                    reusedError = newPassword.isNotEmpty() && currentPassword.isNotEmpty() && newPassword == currentPassword
                )
            )

            ChangePasswordField.REPEAT_PASSWORD -> _uiState.value.copy(
                repeatPasswordError = getPassFieldErrorText(
                    value = repeatedPassword,
                    isValidCondition = repeatedPassword.matches(PASSWORD_REGEX.toRegex()),
                    mismatchError = repeatedPassword.isNotEmpty() && newPassword.isNotEmpty() && repeatedPassword != newPassword
                )
            )
        }
    }

    fun clearFieldError(field: ChangePasswordField) {
        _uiState.value = when (field) {
            ChangePasswordField.CURRENT_PASSWORD -> _uiState.value.copy(currentPasswordError = null)
            ChangePasswordField.NEW_PASSWORD -> _uiState.value.copy(newPasswordError = null)
            ChangePasswordField.REPEAT_PASSWORD -> _uiState.value.copy(repeatPasswordError = null)
        }
    }

    fun onChangePasswordClicked(currentPassword: String, newPassword: String, repeatedPassword: String) {
        validateField(ChangePasswordField.CURRENT_PASSWORD, currentPassword, newPassword, repeatedPassword)
        validateField(ChangePasswordField.NEW_PASSWORD, currentPassword, newPassword, repeatedPassword)
        validateField(ChangePasswordField.REPEAT_PASSWORD, currentPassword, newPassword, repeatedPassword)

        if (_uiState.value.hasAnyError()) return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(loading = true)
            val result = changePasswordUseCase(currentPassword.trim(), newPassword.trim())
            result.onSuccess {
                _effect.tryEmit(ChangePasswordEffect.NavigateToHome)
            }.onFailure {
                _uiState.value = _uiState.value.copy(loading = false)
                _effect.tryEmit(ChangePasswordEffect.ShowFullScreenError())
            }
        }
    }

    private fun getPassFieldErrorText(
        value: String,
        isValidCondition: Boolean,
        reusedError: Boolean = false,
        mismatchError: Boolean = false,
    ): ChangePasswordErrorRes? {
        val isValid = value.isEmpty() || isValidCondition
        return when {
            !isValid -> ChangePasswordErrorRes.INVALID_PASSWORD
            reusedError -> ChangePasswordErrorRes.REUSED_PASSWORD
            mismatchError -> ChangePasswordErrorRes.PASSWORD_MISMATCH
            else -> null
        }
    }
}
