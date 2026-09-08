package cmm.esmorga.viewmodel.eventdetails

import androidx.lifecycle.viewModelScope
import cmm.esmorga.domain.event.GetEventDetailsUseCase
import cmm.esmorga.domain.event.JoinEventUseCase
import cmm.esmorga.domain.event.LeaveEventUseCase
import cmm.esmorga.domain.user.GetSavedUserUseCase
import cmm.esmorga.viewmodel.BaseViewModel
import cmm.esmorga.viewmodel.eventdetails.mapper.EventDetailsUiMapper.toEventUiDetails
import cmm.esmorga.viewmodel.eventdetails.model.EventDetailsEffect
import cmm.esmorga.viewmodel.eventdetails.model.EventDetailsUiState
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class EventDetailsViewModel(
    private val getEventDetailsUseCase: GetEventDetailsUseCase,
    private val joinEventUseCase: JoinEventUseCase,
    private val leaveEventUseCase: LeaveEventUseCase,
    private val getSavedUserUseCase: GetSavedUserUseCase,
    private val eventId: String
) : BaseViewModel() {
    private val _uiState = MutableStateFlow(EventDetailsUiState())
    val uiState: StateFlow<EventDetailsUiState> = _uiState.asStateFlow()

    private val _effect: MutableSharedFlow<EventDetailsEffect> = MutableSharedFlow(extraBufferCapacity = 2, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    val effect: SharedFlow<EventDetailsEffect> = _effect.asSharedFlow()

    init {
        checkAuthenticationAndLoadDetails()
    }

    private fun checkAuthenticationAndLoadDetails() {
        viewModelScope.launch {
            val result = getSavedUserUseCase()
            result.onSuccess { _ ->
                _uiState.value = _uiState.value.copy(isAuthenticated = true)
            }.onFailure {
                _uiState.value = _uiState.value.copy(isAuthenticated = false)
            }
            loadEventDetails()
        }
    }

    private fun loadEventDetails() {
        viewModelScope.launch {
            val result = getEventDetailsUseCase(eventId)
            result.onSuccess {
                _uiState.value = it.data.toEventUiDetails().copy(
                    isAuthenticated = _uiState.value.isAuthenticated
                )
            }
            _uiState.value = _uiState.value.copy(isLoading = false)
        }
    }

    fun onNavigateClick() {
        _effect.tryEmit(
            EventDetailsEffect.NavigateToLocation(
                uiState.value.locationLat!!,
                uiState.value.locationLng!!
            )
        )
    }

    fun onBackPressed() {
        _effect.tryEmit(EventDetailsEffect.NavigateBack)
    }

    fun onJoinLeaveClick() {
        viewModelScope.launch {
            val currentState = _uiState.value

            // If not authenticated, navigate to login
            if (!currentState.isAuthenticated) {
                _effect.tryEmit(EventDetailsEffect.NavigateToLogin)
                return@launch
            }

            _uiState.value = _uiState.value.copy(isLoading = true)

            val result = if (currentState.userJoined) {
                leaveEventUseCase(eventId)
            } else {
                joinEventUseCase(eventId)
            }

            if (result.isSuccess) {
                // Reload event details to get updated state from cache
                loadEventDetails()
            } else {
                _uiState.value = _uiState.value.copy(isLoading = false)
                // Could emit error effect if needed
            }
        }
    }

}