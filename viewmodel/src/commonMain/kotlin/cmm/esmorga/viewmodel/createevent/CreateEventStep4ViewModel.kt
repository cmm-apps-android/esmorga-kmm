package cmm.esmorga.viewmodel.createevent

import androidx.lifecycle.viewModelScope
import cmm.esmorga.viewmodel.BaseViewModel
import cmm.esmorga.viewmodel.common.isValidCoordinates
import cmm.esmorga.viewmodel.createevent.model.CreateEventStep4Effect
import cmm.esmorga.viewmodel.createevent.model.CreateEventStep4UiState
import cmm.esmorga.viewmodel.createevent.model.LocationError
import cmm.esmorga.viewmodel.createevent.model.CoordinatesError
import cmm.esmorga.viewmodel.createevent.model.MaxCapacityError
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CreateEventStep4ViewModel(
    private val session: CreateEventSession
) : BaseViewModel() {

    private val _uiState = MutableStateFlow(CreateEventStep4UiState())
    val uiState: StateFlow<CreateEventStep4UiState> = _uiState.asStateFlow()

    private val _effect = MutableSharedFlow<CreateEventStep4Effect>()
    val effect: SharedFlow<CreateEventStep4Effect> = _effect.asSharedFlow()

    init {
        val initialData = session.data.value
        _uiState.update {
            it.copy(
                eventLocation = initialData.eventLocation,
                eventCoordinates = initialData.eventCoordinates,
                eventMaxCapacity = initialData.eventMaxCapacity,
                locationError = initialData.eventLocation?.let { validateLocation(it) },
                coordinatesError = initialData.eventCoordinates?.let { validateCoordinates(it) },
                maxCapacityError = initialData.eventMaxCapacity?.let { validateMaxCapacity(it) }
            )
        }
        validateStep4()
    }

    fun onEventLocationChanged(location: String) {
        _uiState.update {
            it.copy(
                eventLocation = location,
                locationError = validateLocation(location)
            )
        }
        session.updateData { it.copy(eventLocation = location) }
        validateStep4()
    }

    fun onEventCoordinatesChanged(coordinates: String) {
        _uiState.update {
            it.copy(
                eventCoordinates = coordinates,
                coordinatesError = validateCoordinates(coordinates)
            )
        }
        session.updateData { it.copy(eventCoordinates = coordinates) }
        validateStep4()
    }

    fun onEventMaxCapacityChanged(maxCapacity: String) {
        _uiState.update {
            it.copy(
                eventMaxCapacity = maxCapacity,
                maxCapacityError = validateMaxCapacity(maxCapacity)
            )
        }
        session.updateData { it.copy(eventMaxCapacity = maxCapacity) }
        validateStep4()
    }

    fun onContinueStep4() {
        if (_uiState.value.isStep4Valid) {
            viewModelScope.launch {
                _effect.emit(CreateEventStep4Effect.NavigateToStep5)
            }
        }
    }

    fun onBackClicked() {
        viewModelScope.launch {
            _effect.emit(CreateEventStep4Effect.NavigateBack)
        }
    }

    private fun validateLocation(location: String): LocationError? {
        return when {
            location.isBlank() -> LocationError.EMPTY
            location.length > LOCATION_NAME_MAX_LENGTH -> LocationError.INVALID_LENGTH
            else -> null
        }
    }

    private fun validateCoordinates(coordinates: String): CoordinatesError? {
        return if (coordinates.isValidCoordinates()) null else CoordinatesError.INVALID_FORMAT
    }

    private fun validateMaxCapacity(capacity: String): MaxCapacityError? {
        if (capacity.isBlank()) return null
        val value = capacity.toIntOrNull()
        if (value == null || value !in 1..MAX_CAPACITY_LIMIT) return MaxCapacityError.INVALID_VALUE
        return null
    }

    private fun validateStep4() {
        val state = _uiState.value
        val isValid = state.locationError == null &&
                state.coordinatesError == null &&
                state.maxCapacityError == null &&
                state.eventLocation.orEmpty().isNotBlank()
        _uiState.update { it.copy(isStep4Valid = isValid) }
    }

    companion object {
        const val LOCATION_NAME_MAX_LENGTH = 100
        const val MAX_CAPACITY_LIMIT = 5000
    }
}
