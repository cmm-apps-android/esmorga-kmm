package cmm.esmorga.viewmodel.createevent

import androidx.lifecycle.viewModelScope
import cmm.esmorga.domain.event.CreateEventUseCase
import cmm.esmorga.domain.event.model.CreateEventForm
import cmm.esmorga.domain.event.model.EventLocation
import cmm.esmorga.domain.event.model.EventType
import cmm.esmorga.viewmodel.BaseViewModel
import cmm.esmorga.viewmodel.common.DateUtils
import cmm.esmorga.viewmodel.common.parseCoordinates
import cmm.esmorga.viewmodel.createevent.model.CreateEventStep5Effect
import cmm.esmorga.viewmodel.createevent.model.CreateEventStep5UiState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CreateEventStep5ViewModel(
    private val session: CreateEventSession,
    private val createEventUseCase: CreateEventUseCase
) : BaseViewModel() {

    private val _uiState = MutableStateFlow(CreateEventStep5UiState())
    val uiState: StateFlow<CreateEventStep5UiState> = _uiState.asStateFlow()

    private val _effect = MutableSharedFlow<CreateEventStep5Effect>()
    val effect: SharedFlow<CreateEventStep5Effect> = _effect.asSharedFlow()

    init {
        val initialImageUrl = session.data.value.imageUrl.orEmpty()
        _uiState.update {
            it.copy(
                eventImageUrl = initialImageUrl,
                isPreviewVisible = initialImageUrl.isNotBlank()
            )
        }
    }

    fun onImageUrlChanged(url: String) {
        _uiState.update {
            it.copy(
                eventImageUrl = url,
                isPreviewVisible = if (url.isBlank()) false else it.isPreviewVisible
            )
        }
        session.updateData { it.copy(imageUrl = url) }
    }

    fun onPreviewButtonClicked() {
        val currentState = _uiState.value
        if (currentState.isPreviewVisible) {
            _uiState.update {
                it.copy(
                    eventImageUrl = "",
                    isPreviewVisible = false
                )
            }
            session.updateData { it.copy(imageUrl = null) }
        } else {
            if (currentState.eventImageUrl.isNotBlank()) {
                _uiState.update {
                    it.copy(isPreviewVisible = true)
                }
                session.updateData { it.copy(imageUrl = currentState.eventImageUrl) }
            }
        }
    }

    fun onCreateEventClicked() {
        val data = session.data.value
        val coordinates = data.eventCoordinates.parseCoordinates()
        val finalImageUrl = _uiState.value.eventImageUrl.takeIf { it.isNotBlank() && _uiState.value.isPreviewVisible }

        val eventForm = CreateEventForm(
            name = data.eventName.orEmpty(),
            description = data.eventDescription,
            type = data.selectedType ?: EventType.PARTY,
            date = DateUtils.formatToUtcIsoString(data.selectedDateMillis, data.selectedHour, data.selectedMinute).orEmpty(),
            joinDeadline = if (data.showDeadlineSection) {
                DateUtils.formatToUtcIsoString(data.selectedDeadlineDateMillis, data.selectedDeadlineHour, data.selectedDeadlineMinute)
            } else {
                null
            },
            location = EventLocation(
                name = data.eventLocation.orEmpty(),
                lat = coordinates?.first,
                long = coordinates?.second
            ),
            maxCapacity = data.eventMaxCapacity?.toIntOrNull(),
            imageUrl = finalImageUrl
        )

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            createEventUseCase(eventForm).onSuccess {
                    session.clear()
                    _uiState.update { it.copy(isLoading = false) }
                    _effect.emit(CreateEventStep5Effect.NavigateToSuccess)
                }.onFailure { _ ->
                    _uiState.update { it.copy(isLoading = false) }
                    _effect.emit(CreateEventStep5Effect.ShowError)
                }
        }
    }

    fun onBackClicked() {
        viewModelScope.launch {
            _effect.emit(CreateEventStep5Effect.NavigateBack)
        }
    }
}
