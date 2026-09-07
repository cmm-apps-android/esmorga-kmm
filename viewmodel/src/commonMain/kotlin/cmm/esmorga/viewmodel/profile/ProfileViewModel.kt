package cmm.esmorga.viewmodel.profile

import androidx.lifecycle.viewModelScope
import cmm.esmorga.domain.user.GetSavedUserUseCase
import cmm.esmorga.domain.user.LogOutUseCase
import cmm.esmorga.viewmodel.BaseViewModel
import cmm.esmorga.viewmodel.profile.model.ProfileEffect
import cmm.esmorga.viewmodel.profile.model.ProfileUiState
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val getSavedUserUseCase: GetSavedUserUseCase,
    private val logOutUseCase: LogOutUseCase
) : BaseViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    private val _effect: MutableSharedFlow<ProfileEffect> = MutableSharedFlow(extraBufferCapacity = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    val effect: SharedFlow<ProfileEffect> = _effect.asSharedFlow()

    init {
        loadProfile()
    }

    fun loadProfile() {
        viewModelScope.launch {
            val result = getSavedUserUseCase()
            result.onSuccess { userSuccess ->
                _uiState.value = ProfileUiState(
                    isLoggedIn = true,
                    name = "${userSuccess.data.name} ${userSuccess.data.lastName}".trim(),
                    email = userSuccess.data.email
                )
            }.onFailure {
                _uiState.value = ProfileUiState(isLoggedIn = false)
            }
        }
    }

    fun onChangePasswordClicked() {
        _effect.tryEmit(ProfileEffect.NavigateToChangePassword)
    }

    fun onLogoutClicked() {
        viewModelScope.launch {
            val result = logOutUseCase()
            result.onSuccess {
                _uiState.value = ProfileUiState(isLoggedIn = false)
            }
        }
    }
}
