package cmm.esmorga.viewmodel.eventdetails

import androidx.lifecycle.viewModelScope
import cmm.esmorga.domain.event.GetEventDetailsUseCase
import cmm.esmorga.domain.event.JoinEventUseCase
import cmm.esmorga.domain.event.LeaveEventUseCase
import cmm.esmorga.domain.result.ErrorCodes
import cmm.esmorga.domain.result.EsmorgaException
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
import kotlinx.coroutines.flow.update
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

    private val _effect: MutableSharedFlow<EventDetailsEffect> = MutableSharedFlow(extraBufferCapacity = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    val effect: SharedFlow<EventDetailsEffect> = _effect.asSharedFlow()

    init {
        loadEventDetails()
    }
    
    private fun loadEventDetails() {
        viewModelScope.launch {
            val result = getEventDetailsUseCase(eventId)
            val userResult = getSavedUserUseCase()
            result.onSuccess {
                _uiState.value = it.data.toEventUiDetails().copy(
                    isAuthenticated = userResult.getOrNull()?.data != null
                )
            }
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

            _uiState.update { it.copy(isLoading = true) }
            val result = if (currentState.userJoined) leaveEventUseCase(eventId) else joinEventUseCase(eventId)
            _uiState.update { it.copy(isLoading = false) }

            if (result.isSuccess) {
                loadEventDetails()
            } else {
                val error = result.exceptionOrNull()
                if (error is EsmorgaException && error.code == ErrorCodes.EVENT_FULL) {
                    _effect.tryEmit(EventDetailsEffect.ShowEventFullSnackbar)
                } else {
                    _effect.tryEmit(EventDetailsEffect.NavigateToError)
                }
            }
        }
    }

}