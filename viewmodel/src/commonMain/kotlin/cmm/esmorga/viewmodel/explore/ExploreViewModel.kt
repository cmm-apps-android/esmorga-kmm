package cmm.esmorga.viewmodel.explore

import androidx.lifecycle.viewModelScope
import cmm.esmorga.domain.event.GetEventListUseCase
import cmm.esmorga.domain.result.ErrorCodes
import cmm.esmorga.domain.result.EsmorgaException
import cmm.esmorga.viewmodel.explore.mapper.EventListUiMapper.toEventUiList
import cmm.esmorga.viewmodel.BaseViewModel
import cmm.esmorga.viewmodel.explore.model.EventListEffect
import cmm.esmorga.viewmodel.explore.model.ExploreUiState
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ExploreViewModel(private val getEventListUseCase: GetEventListUseCase) : BaseViewModel() {

    private val _uiState = MutableStateFlow(ExploreUiState())
    val uiState: StateFlow<ExploreUiState> = _uiState.asStateFlow()

    private val _effect: MutableSharedFlow<EventListEffect> = MutableSharedFlow(extraBufferCapacity = 2, onBufferOverflow = BufferOverflow.DROP_OLDEST)
    val effect: SharedFlow<EventListEffect> = _effect.asSharedFlow()

    init {
        loadEvents()
    }

    fun loadEvents() {
        _uiState.value = ExploreUiState(loading = true)
        viewModelScope.launch {
            val result = getEventListUseCase()

            result.onSuccess { success ->
                if (success.hasError() && success.nonBlockingError == ErrorCodes.NO_CONNECTION) {
                    _effect.tryEmit(EventListEffect.ShowNoNetworkPrompt)
                }
                _uiState.value = ExploreUiState(
                    eventList = success.data.toEventUiList(),
                    error = if (success.hasError() && success.nonBlockingError == ErrorCodes.NO_CONNECTION && success.data.isEmpty()) "No Connection" else null
                )
            }.onFailure { error ->
                if (error is EsmorgaException) {
                    _uiState.value = ExploreUiState(error = "${error.source} error: ${error.message}")
                } else {
                    _uiState.value = ExploreUiState(error = "Unknown error: ${error.message}")
                }
            }
        }
    }

    fun onEventClick(eventId: String) {
        _effect.tryEmit(EventListEffect.NavigateToEventDetail(eventId))
    }

}