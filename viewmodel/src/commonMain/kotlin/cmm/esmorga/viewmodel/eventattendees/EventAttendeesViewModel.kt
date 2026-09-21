package cmm.esmorga.viewmodel.eventattendees

import androidx.lifecycle.viewModelScope
import cmm.esmorga.domain.event.GetEventAttendeesUseCase
import cmm.esmorga.domain.event.SaveAttendeePaymentUseCase
import cmm.esmorga.domain.user.GetSavedUserUseCase
import cmm.esmorga.domain.user.model.RoleType
import cmm.esmorga.viewmodel.BaseViewModel
import cmm.esmorga.viewmodel.eventattendees.model.EventAttendeesUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EventAttendeesViewModel(
    private val getSavedUserUseCase: GetSavedUserUseCase,
    private val getEventAttendeesUseCase: GetEventAttendeesUseCase,
    private val saveAttendeePaymentUseCase: SaveAttendeePaymentUseCase,
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
            val userResult = getSavedUserUseCase()
            result.onSuccess { success ->
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        attendees = success.data,
                        isAdmin = userResult.getOrNull()?.data?.role == RoleType.ADMIN,
                        hasError = false
                    )
                }
            }.onFailure {
                _uiState.update { state -> state.copy(isLoading = false, hasError = true) }
            }
        }
    }

    fun onAttendeeChecked(position: Int, checked: Boolean) {
        val attendee = _uiState.value.attendees.getOrNull(position) ?: return
        viewModelScope.launch {
            saveAttendeePaymentUseCase(eventId, attendee.name, checked)
            _uiState.update { state ->
                val updatedAttendees = state.attendees.mapIndexed { index, item ->
                    if (index == position) item.copy(alreadyPaid = checked) else item
                }
                state.copy(attendees = updatedAttendees)
            }
        }
    }
}
