package cmm.esmorga.viewmodel.createevent

import androidx.lifecycle.viewModelScope
import cmm.esmorga.domain.event.model.EventType
import cmm.esmorga.viewmodel.BaseViewModel
import cmm.esmorga.viewmodel.createevent.model.CreateEventStep2Effect
import cmm.esmorga.viewmodel.createevent.model.CreateEventStep2UiState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CreateEventStep2ViewModel(
    private val session: CreateEventSession
) : BaseViewModel() {

    private val _uiState = MutableStateFlow(CreateEventStep2UiState())
    val uiState: StateFlow<CreateEventStep2UiState> = _uiState.asStateFlow()

    private val _effect = MutableSharedFlow<CreateEventStep2Effect>()
    val effect: SharedFlow<CreateEventStep2Effect> = _effect.asSharedFlow()

    init {
        val initialData = session.data.value
        _uiState.update {
            it.copy(selectedType = initialData.selectedType)
        }
    }

    fun onEventTypeSelected(type: EventType) {
        _uiState.update { it.copy(selectedType = type) }
        session.updateData { it.copy(selectedType = type) }
    }

    fun onContinueStep2() {
        if (_uiState.value.selectedType != null) {
            viewModelScope.launch {
                _effect.emit(CreateEventStep2Effect.NavigateToStep3)
            }
        }
    }

    fun onBackClicked() {
        viewModelScope.launch {
            _effect.emit(CreateEventStep2Effect.NavigateBack)
        }
    }
}
