package cmm.esmorga.viewmodel.createevent.model


data class CreateEventStep3UiState(
    val selectedDateMillis: Long? = null,
    val selectedHour: Int? = null,
    val selectedMinute: Int? = null,
    val selectedDeadlineDateMillis: Long? = null,
    val selectedDeadlineHour: Int? = null,
    val selectedDeadlineMinute: Int? = null,
    val showDeadlineSection: Boolean = false,
    val isDeadlineExceeded: Boolean = false,
    val isStep3Valid: Boolean = false
)

sealed class CreateEventStep3Effect {
    data object NavigateToStep4 : CreateEventStep3Effect()
    data object NavigateBack : CreateEventStep3Effect()
}