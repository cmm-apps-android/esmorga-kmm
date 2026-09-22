package cmm.esmorga.viewmodel.createevent

import androidx.lifecycle.viewModelScope
import cmm.esmorga.viewmodel.BaseViewModel
import cmm.esmorga.viewmodel.createevent.model.CreateEventStep1Effect
import cmm.esmorga.viewmodel.createevent.model.CreateEventStep1UiState
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

class CreateEventStep1ViewModel(
    private val session: CreateEventSession
) : BaseViewModel() {

    private val _uiState = MutableStateFlow(CreateEventStep1UiState())
    val uiState: StateFlow<CreateEventStep1UiState> = _uiState.asStateFlow()

    private val _effect = MutableSharedFlow<CreateEventStep1Effect>()
    val effect: SharedFlow<CreateEventStep1Effect> = _effect.asSharedFlow()

    init {
        val initialData = session.data.value
        _uiState.update {
            it.copy(
                eventName = initialData.eventName,
                eventDescription = initialData.eventDescription,
                nameError = initialData.eventName?.let { validateName(it) },
                descriptionError = initialData.eventDescription?.let { validateDescription(it) }
            )
        }
        validateStep1()
    }

    fun onEventNameChanged(name: String) {
        _uiState.update {
            it.copy(
                eventName = name,
                nameError = validateName(name)
            )
        }
        session.updateData { it.copy(eventName = name) }
        validateStep1()
    }

    fun onEventDescriptionChanged(description: String) {
        _uiState.update {
            it.copy(
                eventDescription = description,
                descriptionError = validateDescription(description)
            )
        }
        session.updateData { it.copy(eventDescription = description) }
        validateStep1()
    }

    fun onContinueStep1() {
        if (_uiState.value.isStep1Valid) {
            viewModelScope.launch {
                _effect.emit(CreateEventStep1Effect.NavigateToStep2)
            }
        }
    }

    fun onBackClicked() {
        viewModelScope.launch {
            _effect.emit(CreateEventStep1Effect.NavigateBack)
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
                      state.eventName.orEmpty().isNotBlank()
        _uiState.update { it.copy(isStep1Valid = isValid) }
    }
}
