package cmm.esmorga.viewmodel.createevent.model

data class CreateEventStep1UiState(
    val eventName: String? = null,
    val eventDescription: String? = null,
    val nameError: NameError? = null,
    val descriptionError: DescriptionError? = null,
    val isStep1Valid: Boolean = false
)

enum class NameError {
    EMPTY, INVALID_LENGTH
}

enum class DescriptionError {
    INVALID_LENGTH
}

sealed class CreateEventStep1Effect {
    data object NavigateToStep2 : CreateEventStep1Effect()
    data object NavigateBack : CreateEventStep1Effect()
}