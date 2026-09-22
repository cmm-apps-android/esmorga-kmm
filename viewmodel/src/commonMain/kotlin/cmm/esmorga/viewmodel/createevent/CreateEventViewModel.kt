package cmm.esmorga.viewmodel.createevent

import androidx.lifecycle.viewModelScope
import cmm.esmorga.domain.event.model.EventType
import cmm.esmorga.viewmodel.BaseViewModel
import cmm.esmorga.viewmodel.createevent.model.CreateEventEffect
import cmm.esmorga.viewmodel.createevent.model.CreateEventUiState
import cmm.esmorga.viewmodel.createevent.model.DescriptionError
import cmm.esmorga.viewmodel.createevent.model.NameError
import kotlinx.datetime.*
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CreateEventViewModel : BaseViewModel() {

    private val _uiState = MutableStateFlow(CreateEventUiState())
    val uiState: StateFlow<CreateEventUiState> = _uiState.asStateFlow()

    private val _effect = MutableSharedFlow<CreateEventEffect>()
    val effect: SharedFlow<CreateEventEffect> = _effect.asSharedFlow()

    fun onEventNameChanged(name: String) {
        _uiState.update {
            it.copy(
                eventName = name,
                nameError = validateName(name)
            )
        }
        validateStep1()
    }

    fun onEventDescriptionChanged(description: String) {
        _uiState.update {
            it.copy(
                eventDescription = description,
                descriptionError = validateDescription(description)
            )
        }
        validateStep1()
    }

    fun onEventTypeSelected(type: EventType) {
        _uiState.update { it.copy(selectedType = type) }
    }

    fun onDateChanged(dateMillis: Long?) {
        _uiState.update {
            it.copy(selectedDateMillis = dateMillis)
        }
        validateStep3()
    }

    fun onTimeChanged(hour: Int, minute: Int) {
        _uiState.update {
            it.copy(
                selectedHour = hour,
                selectedMinute = minute
            )
        }
        validateStep3()
    }

    fun onDeadlineDateChanged(dateMillis: Long?) {
        _uiState.update {
            it.copy(selectedDeadlineDateMillis = dateMillis)
        }
        validateStep3()
    }

    fun onDeadlineTimeChanged(hour: Int, minute: Int) {
        _uiState.update {
            it.copy(
                selectedDeadlineHour = hour,
                selectedDeadlineMinute = minute
            )
        }
        validateStep3()
    }

    fun onToggleDeadlineSection(show: Boolean) {
        _uiState.update {
            it.copy(
                showDeadlineSection = show,
                selectedDeadlineDateMillis = if (show) it.selectedDeadlineDateMillis else null,
                selectedDeadlineHour = if (show) it.selectedDeadlineHour else null,
                selectedDeadlineMinute = if (show) it.selectedDeadlineMinute else null
            )
        }
        validateStep3()
    }

    private fun validateStep3() {
        val state = _uiState.value
        val selDate = state.selectedDateMillis
        val isEventDateSelected = selDate != null
        
        var isDeadlineValid = true
        var isDeadlineExceeded = false

        if (state.showDeadlineSection) {
            val selDeadlineDate = state.selectedDeadlineDateMillis
            val isDeadlineDateSelected = selDeadlineDate != null
            if (!isDeadlineDateSelected) {
                isDeadlineValid = false
            } else if (isEventDateSelected) {
                val eventDate = Instant.fromEpochMilliseconds(selDate).toLocalDateTime(TimeZone.currentSystemDefault()).date
                val deadlineDate = Instant.fromEpochMilliseconds(selDeadlineDate).toLocalDateTime(TimeZone.currentSystemDefault()).date

                val eventHour = state.selectedHour ?: 0
                val eventMinute = state.selectedMinute ?: 0
                val deadlineHour = state.selectedDeadlineHour ?: 0
                val deadlineMinute = state.selectedDeadlineMinute ?: 0

                val eventDateTime = LocalDateTime(eventDate.year, eventDate.month, eventDate.day, eventHour, eventMinute)
                val deadlineDateTime = LocalDateTime(deadlineDate.year, deadlineDate.month, deadlineDate.day, deadlineHour, deadlineMinute)

                if (deadlineDateTime > eventDateTime) {
                    isDeadlineExceeded = true
                    isDeadlineValid = false
                } else {
                    if (state.selectedDeadlineHour == null || state.selectedDeadlineMinute == null) {
                        isDeadlineValid = false
                    }
                }
            } else {
                isDeadlineValid = false
            }
        }

        _uiState.update {
            it.copy(
                isDeadlineExceeded = isDeadlineExceeded,
                isStep3Valid = isEventDateSelected && state.selectedHour != null && state.selectedMinute != null && isDeadlineValid
            )
        }
    }

    fun onContinueStep2() {
        if (_uiState.value.selectedType != null) {
            viewModelScope.launch {
                _effect.emit(CreateEventEffect.NavigateToStep3)
            }
        }
    }

    fun onContinueStep3() {
        if (_uiState.value.isStep3Valid) {
            viewModelScope.launch {
                _effect.emit(CreateEventEffect.NavigateToSuccess)
            }
        }
    }

    fun onContinueStep1() {
        if (_uiState.value.isStep1Valid) {
            viewModelScope.launch {
                _effect.emit(CreateEventEffect.NavigateToStep2)
            }
        }
    }

    fun onBackClicked() {
        viewModelScope.launch {
            _effect.emit(CreateEventEffect.NavigateBack)
        }
    }

    private fun validateName(name: String): NameError? {
        return when {
            name.isBlank() -> NameError.EMPTY
            name.length !in 3..100 -> NameError.INVALID_LENGTH
            else -> null
        }
    }

    private fun validateDescription(description: String): DescriptionError? {
        return when {
            description.isNotEmpty() && (description.length !in 20..5000) -> DescriptionError.INVALID_LENGTH
            else -> null
        }
    }

    private fun validateStep1() {
        val state = _uiState.value
        val isValid = state.nameError == null && 
                      state.descriptionError == null && 
                      state.eventName.isNotBlank()
        _uiState.update { it.copy(isStep1Valid = isValid) }
    }
}