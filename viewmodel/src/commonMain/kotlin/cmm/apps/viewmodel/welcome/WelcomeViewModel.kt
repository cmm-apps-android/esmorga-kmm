package cmm.apps.viewmodel.welcome

import cmm.apps.domain.user.GetSavedUserUseCase
import cmm.apps.viewmodel.BaseViewModel
import cmm.apps.viewmodel.welcome.model.WelcomeEffect
import cmm.apps.viewmodel.welcome.model.WelcomeUiState
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class WelcomeViewModel(private val getSavedUserUseCase: GetSavedUserUseCase) : BaseViewModel() {

    private val _uiState = MutableStateFlow(WelcomeUiState().createDefaultWelcomeUiState())
    val uiState: StateFlow<WelcomeUiState> = _uiState.asStateFlow()

    private val _effect: MutableSharedFlow<WelcomeEffect> = MutableSharedFlow(extraBufferCapacity = 2, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    val effect: SharedFlow<WelcomeEffect> = _effect.asSharedFlow()

    fun onPrimaryButtonClicked() {
        _effect.tryEmit(WelcomeEffect.NavigateToLogin)
    }

    fun onSecondaryButtonClicked() {
        _effect.tryEmit(WelcomeEffect.NavigateToEventList)
    }

    fun init() {
        scope.launch {
            val result = getSavedUserUseCase()
            if (result.isSuccess) {
                _effect.tryEmit(WelcomeEffect.NavigateToEventList)
            }
        }
    }

}