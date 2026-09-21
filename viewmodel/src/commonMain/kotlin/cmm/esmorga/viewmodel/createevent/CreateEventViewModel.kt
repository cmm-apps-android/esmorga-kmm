package cmm.esmorga.viewmodel.createevent

import androidx.lifecycle.viewModelScope
import cmm.esmorga.domain.event.model.EventType
import cmm.esmorga.viewmodel.BaseViewModel
import cmm.esmorga.viewmodel.createevent.model.CreateEventEffect
import cmm.esmorga.viewmodel.createevent.model.CreateEventUiState
import cmm.esmorga.viewmodel.createevent.model.DescriptionError
import cmm.esmorga.viewmodel.createevent.model.NameError
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
            it.copy(
                selectedDateMillis = dateMillis,
                isStep3Valid = dateMillis != null
            )
        }
    }

    fun onTimeChanged(hour: Int, minute: Int) {
        _uiState.update {
            it.copy(
                selectedHour = hour,
                selectedMinute = minute
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