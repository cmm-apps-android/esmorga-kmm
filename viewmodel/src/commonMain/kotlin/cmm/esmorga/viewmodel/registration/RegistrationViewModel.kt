package cmm.esmorga.viewmodel.registration

import androidx.lifecycle.viewModelScope
import cmm.esmorga.domain.result.ErrorCodes
import cmm.esmorga.domain.result.EsmorgaException
import cmm.esmorga.domain.user.PerformRegistrationUserCase
import cmm.esmorga.domain.user.model.User.Companion.EMAIL_REGEX
import cmm.esmorga.domain.user.model.User.Companion.NAME_REGEX
import cmm.esmorga.domain.user.model.User.Companion.PASSWORD_REGEX
import cmm.esmorga.viewmodel.BaseViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class RegistrationViewModel(private val performRegistrationUserCase: PerformRegistrationUserCase) : BaseViewModel() {

    private val _uiState = MutableStateFlow(RegistrationUiState())
    val uiState: StateFlow<RegistrationUiState> = _uiState.asStateFlow()

    private val _effect: MutableSharedFlow<RegistrationEffect> = MutableSharedFlow(extraBufferCapacity = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    val effect: SharedFlow<RegistrationEffect> = _effect.asSharedFlow()

    fun onRegisterClicked(name: String, lastName: String, email: String, password: String, repeatedPassword: String) {
        validateField(RegistrationField.NAME, name, NAME_REGEX, RegistrationValidationError.INVALID_NAME, false)
        validateField(RegistrationField.LAST_NAME, lastName, NAME_REGEX, RegistrationValidationError.INVALID_LAST_NAME, false)
        validateField(RegistrationField.EMAIL, email, EMAIL_REGEX, RegistrationValidationError.INVALID_EMAIL, false)
        validateField(RegistrationField.PASS, password, PASSWORD_REGEX, RegistrationValidationError.INVALID_PASSWORD, false)
        validateField(RegistrationField.REPEAT_PASS, repeatedPassword, comparisonValue = password, acceptsEmpty = false)

        if (!_uiState.value.hasAnyError()) {
            viewModelScope.launch {
                _uiState.update { it.copy(loading = true) }
                val result = performRegistrationUserCase(name.trim(), lastName.trim(), email.trim(), password.trim())
                result.onSuccess {
                    _effect.tryEmit(RegistrationEffect.NavigateToEventList)
                }.onFailure { error ->
                    _uiState.update { it.copy(loading = false) }
                    when {
                        error is EsmorgaException && error.code == ErrorCodes.NO_CONNECTION -> _effect.tryEmit(RegistrationEffect.ShowNoNetworkSnackbar)
                        error is EsmorgaException && error.code == 409 -> _uiState.update { it.copy(emailError = RegistrationValidationError.EMAIL_ALREADY_IN_USE) }
                        else -> _effect.tryEmit(RegistrationEffect.ShowFullScreenError)
                    }
                }
            }
        }
    }

    fun validateField(
        field: RegistrationField,
        value: String,
        regex: String? = null,
        invalidError: RegistrationValidationError? = null,
        acceptsEmpty: Boolean = true,
        comparisonValue: String? = null
    ) {
        val error = when {
            value.isEmpty() && !acceptsEmpty -> RegistrationValidationError.EMPTY
            field == RegistrationField.REPEAT_PASS && value != comparisonValue -> RegistrationValidationError.PASSWORD_MISMATCH
            regex != null && value.isNotEmpty() && !value.matches(regex.toRegex()) -> invalidError ?: RegistrationValidationError.NONE
            else -> RegistrationValidationError.NONE
        }

        _uiState.update { state ->
            when (field) {
                RegistrationField.NAME -> state.copy(nameError = error)
                RegistrationField.LAST_NAME -> state.copy(lastNameError = error)
                RegistrationField.EMAIL -> state.copy(emailError = error)
                RegistrationField.PASS -> state.copy(passError = error)
                RegistrationField.REPEAT_PASS -> state.copy(repeatPassError = error)
            }
        }
    }

    fun onFieldChanged(field: RegistrationField) {
        _uiState.update { state ->
            when (field) {
                RegistrationField.NAME -> state.copy(nameError = RegistrationValidationError.NONE)
                RegistrationField.LAST_NAME -> state.copy(lastNameError = RegistrationValidationError.NONE)
                RegistrationField.EMAIL -> state.copy(emailError = RegistrationValidationError.NONE)
                RegistrationField.PASS -> state.copy(passError = RegistrationValidationError.NONE)
                RegistrationField.REPEAT_PASS -> state.copy(repeatPassError = RegistrationValidationError.NONE)
            }
        }
    }

}

enum class RegistrationField {
    NAME,
    LAST_NAME,
    EMAIL,
    PASS,
    REPEAT_PASS
}
