package cmm.esmorga.viewmodel.eventattendees

import androidx.lifecycle.viewModelScope
import cmm.esmorga.domain.event.GetEventAttendeesUseCase
import cmm.esmorga.viewmodel.BaseViewModel
import cmm.esmorga.viewmodel.eventattendees.model.EventAttendeesUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EventAttendeesViewModel(
    private val getEventAttendeesUseCase: GetEventAttendeesUseCase,
    private val eventId: String
) : BaseViewModel() {

    private val _uiState = MutableStateFlow(EventAttendeesUiState())
    val uiState: StateFlow<EventAttendeesUiState> = _uiState.asStateFlow()

    init {
        loadAttendees()
    }

    fun loadAttendees() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, hasError = false) }
            val result = getEventAttendeesUseCase(eventId)
            result.onSuccess { success ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        attendees = success.data,
                        hasError = false
                    )
                }
            }.onFailure {
                _uiState.update { state -> state.copy(isLoading = false, hasError = true) }
            }
        }
    }

    fun onAttendeeChecked(position: Int, checked: Boolean) {

    }
}

