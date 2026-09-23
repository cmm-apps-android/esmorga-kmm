package cmm.esmorga.viewmodel.createevent.model

data class CreateEventStep5UiState(
    val eventImageUrl: String = "",
    val isPreviewVisible: Boolean = false,
    val isLoading: Boolean = false
)

sealed class CreateEventStep5Effect {
    data object NavigateToSuccess : CreateEventStep5Effect()
    data object NavigateBack : CreateEventStep5Effect()
    data object ShowError : CreateEventStep5Effect()
}
