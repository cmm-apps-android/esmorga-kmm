package cmm.esmorga.viewmodel.login

import androidx.lifecycle.viewModelScope
import cmm.esmorga.domain.result.ErrorCodes
import cmm.esmorga.domain.result.EsmorgaException
import cmm.esmorga.domain.user.PerformLoginUseCase
import cmm.esmorga.domain.user.model.User.Companion.EMAIL_REGEX
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

class LoginViewModel(private val performLoginUseCase: PerformLoginUseCase) : BaseViewModel() {
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    private val _effect: MutableSharedFlow<LoginEffect> = MutableSharedFlow(extraBufferCapacity = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    val effect: SharedFlow<LoginEffect> = _effect.asSharedFlow()

    fun onLoginClicked(email: String, password: String) {
        validateField(email, EMAIL_REGEX, ValidationError.INVALID_EMAIL, false)
        validateField(password, PASSWORD_REGEX, ValidationError.INVALID_PASSWORD, false)
        if (!_uiState.value.hasAnyError()) {
            viewModelScope.launch {
                _uiState.update { it.copy(loading = true) }
                val result = performLoginUseCase(email.trim(), password.trim())
                result.onSuccess {
                    _effect.tryEmit(LoginEffect.NavigateToEventList)
                }.onFailure { error ->
                    _uiState.update { it.copy(loading = false) }
                    if (error is EsmorgaException && error.code == ErrorCodes.NO_CONNECTION) {
                        _effect.tryEmit(LoginEffect.ShowNoNetworkSnackbar)
                    } else {
                        _effect.tryEmit(LoginEffect.ShowFullScreenError())
                    }
                }
            }
        }
    }

    fun onRegisterClicked() {
        _effect.tryEmit(LoginEffect.NavigateToRegistration)
    }

    fun validateField(
        value: String,
        regex: String,
        invalidError: ValidationError,
        allowEmpty: Boolean = true
    ) {
        val error = when {
            value.isEmpty() && !allowEmpty -> ValidationError.EMPTY
            value.isNotEmpty() && !value.matches(regex.toRegex()) -> invalidError
            else -> ValidationError.NONE
        }
        _uiState.update { state ->
            when (invalidError) {
                ValidationError.INVALID_EMAIL -> state.copy(emailError = error)
                ValidationError.INVALID_PASSWORD -> state.copy(passwordError = error)
                else -> state
            }
        }
    }

    fun onEmailChanged() {
        _uiState.update { it.copy(emailError = ValidationError.NONE) }
    }

    fun onPassChanged() {
        _uiState.update { it.copy(passwordError = ValidationError.NONE) }
    }
}