package cmm.esmorga.viewmodel.myevents

import androidx.lifecycle.viewModelScope
import cmm.esmorga.domain.event.GetMyEventListUseCase
import cmm.esmorga.domain.user.GetSavedUserUseCase
import cmm.esmorga.domain.user.model.RoleType
import cmm.esmorga.viewmodel.BaseViewModel
import cmm.esmorga.viewmodel.explore.mapper.EventListUiMapper.toEventUiList
import cmm.esmorga.viewmodel.myevents.model.MyEventsEffect
import cmm.esmorga.viewmodel.myevents.model.MyEventsUiState
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
                _uiState.value = MyEventsUiState(
                    isLoggedIn = true,
                    isAdmin = userResult.getOrNull()?.data?.role == RoleType.ADMIN
                )
                loadMyEvents()
            } else {
                _uiState.value = MyEventsUiState(
                    loading = false,
                    isLoggedIn = false,
                    isAdmin = false
                )
            }
        }
    }

    private fun loadMyEvents() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(loading = true, error = false)
            val result = getMyEventListUseCase()

            result.onSuccess { success ->
                _uiState.value = _uiState.value.copy(
                    loading = false,
                    eventList = success.data.toEventUiList(),
                )
            }.onFailure { _ ->
                _uiState.value = _uiState.value.copy(
                    isLoggedIn = true,
                    loading = false,
                    error = true
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

    fun onCreateEventClicked() {
        viewModelScope.launch {
            _effect.emit(MyEventsEffect.NavigateToCreateEvent)
        }
    }
}
