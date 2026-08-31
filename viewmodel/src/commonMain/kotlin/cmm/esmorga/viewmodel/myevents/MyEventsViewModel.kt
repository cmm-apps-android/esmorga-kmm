package cmm.esmorga.viewmodel.myevents

import androidx.lifecycle.viewModelScope
import cmm.esmorga.domain.event.GetMyEventListUseCase
import cmm.esmorga.domain.result.ErrorCodes
import cmm.esmorga.domain.result.EsmorgaException
import cmm.esmorga.domain.user.GetSavedUserUseCase
import cmm.esmorga.viewmodel.eventlist.mapper.EventListUiMapper.toEventUiList
import cmm.esmorga.viewmodel.myevents.model.MyEventsEffect
import cmm.esmorga.viewmodel.myevents.model.MyEventsUiState
import cmm.esmorga.viewmodel.BaseViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MyEventsViewModel(
    private val getSavedUserUseCase: GetSavedUserUseCase,
    private val getMyEventListUseCase: GetMyEventListUseCase
) : BaseViewModel() {

    private val _uiState = MutableStateFlow(MyEventsUiState())
    val uiState: StateFlow<MyEventsUiState> = _uiState.asStateFlow()

    private val _effect = MutableSharedFlow<MyEventsEffect>()
    val effect: SharedFlow<MyEventsEffect> = _effect.asSharedFlow()

    fun checkLoginStatus() {
        viewModelScope.launch {
            val userResult = getSavedUserUseCase()
            if (userResult.isSuccess) {
                loadMyEvents()
            } else {
                _uiState.value = MyEventsUiState(
                    loading = false
                )
            }
        }
    }

    private fun loadMyEvents() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(loading = true)
            val result = getMyEventListUseCase()

            result.onSuccess { success ->
                _uiState.value = _uiState.value.copy(
                    isLoggedIn = true,
                    loading = false,
                    eventList = success.data.toEventUiList(),
                    error = if (success.hasError() && success.nonBlockingError == ErrorCodes.NO_CONNECTION && success.data.isEmpty()) "No Connection" else null
                )
            }.onFailure { error ->
                val errorMessage = if (error is EsmorgaException) {
                    "${error.source} error: ${error.message}"
                } else {
                    "Unknown error: ${error.message}"
                }
                _uiState.value = _uiState.value.copy(
                    isLoggedIn = true,
                    loading = false,
                    error = errorMessage
                )
            }
        }
    }

    fun onLoginClicked() {
        viewModelScope.launch {
            _effect.emit(MyEventsEffect.NavigateToLogin)
        }
    }

    fun onEventClick(eventId: String) {
        viewModelScope.launch {
            _effect.emit(MyEventsEffect.NavigateToEventDetail(eventId))
        }
    }
}
