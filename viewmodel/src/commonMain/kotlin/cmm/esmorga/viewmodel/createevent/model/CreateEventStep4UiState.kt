package cmm.esmorga.viewmodel.createevent.model

data class CreateEventStep4UiState(
    val eventLocation: String? = null,
    val eventCoordinates: String? = null,
    val eventMaxCapacity: String? = null,
    val locationError: LocationError? = null,
    val coordinatesError: CoordinatesError? = null,
    val maxCapacityError: MaxCapacityError? = null,
    val isStep4Valid: Boolean = false
)

enum class LocationError {
    EMPTY, INVALID_LENGTH
}

enum class CoordinatesError {
    INVALID_FORMAT
}

enum class MaxCapacityError {
    INVALID_VALUE
}

sealed class CreateEventStep4Effect {
    data object NavigateToSuccess : CreateEventStep4Effect()
    data object NavigateBack : CreateEventStep4Effect()
}
