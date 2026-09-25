package cmm.esmorga.viewmodel.createevent

import androidx.lifecycle.viewModelScope
import cmm.esmorga.viewmodel.BaseViewModel
import cmm.esmorga.viewmodel.createevent.model.CreateEventStep3Effect
import cmm.esmorga.viewmodel.createevent.model.CreateEventStep3UiState
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CreateEventStep3ViewModel(
    private val session: CreateEventSession
) : BaseViewModel() {

    private val _uiState = MutableStateFlow(CreateEventStep3UiState())
    val uiState: StateFlow<CreateEventStep3UiState> = _uiState.asStateFlow()

    private val _effect = MutableSharedFlow<CreateEventStep3Effect>()
    val effect: SharedFlow<CreateEventStep3Effect> = _effect.asSharedFlow()

    init {
        val initialData = session.data.value
        _uiState.update {
            it.copy(
                selectedDateMillis = initialData.selectedDateMillis,
                selectedHour = initialData.selectedHour,
                selectedMinute = initialData.selectedMinute,
                selectedDeadlineDateMillis = initialData.selectedDeadlineDateMillis,
                selectedDeadlineHour = initialData.selectedDeadlineHour,
                selectedDeadlineMinute = initialData.selectedDeadlineMinute,
                showDeadlineSection = initialData.showDeadlineSection
            )
        }
        validateStep3()
    }

    fun onDateChanged(dateMillis: Long?) {
        _uiState.update {
            it.copy(selectedDateMillis = dateMillis)
        }
        session.updateData { it.copy(selectedDateMillis = dateMillis) }
        validateStep3()
    }

    fun onTimeChanged(hour: Int, minute: Int) {
        _uiState.update {
            it.copy(
                selectedHour = hour,
                selectedMinute = minute
            )
        }
        session.updateData {
            it.copy(
                selectedHour = hour,
                selectedMinute = minute
            )
        }
        validateStep3()
    }

    fun onDeadlineDateChanged(dateMillis: Long?) {
        _uiState.update {
            it.copy(selectedDeadlineDateMillis = dateMillis)
        }
        session.updateData { it.copy(selectedDeadlineDateMillis = dateMillis) }
        validateStep3()
    }

    fun onDeadlineTimeChanged(hour: Int, minute: Int) {
        _uiState.update {
            it.copy(
                selectedDeadlineHour = hour,
                selectedDeadlineMinute = minute
            )
        }
        session.updateData {
            it.copy(
                selectedDeadlineHour = hour,
                selectedDeadlineMinute = minute
            )
        }
        validateStep3()
    }

    fun onToggleDeadlineSection(show: Boolean) {
        _uiState.update {
            it.copy(
                showDeadlineSection = show,
                selectedDeadlineDateMillis = if (show) it.selectedDeadlineDateMillis else null,
                selectedDeadlineHour = if (show) it.selectedDeadlineHour else null,
                selectedDeadlineMinute = if (show) it.selectedDeadlineMinute else null
            )
        }
        session.updateData {
            it.copy(
                showDeadlineSection = show,
                selectedDeadlineDateMillis = if (show) it.selectedDeadlineDateMillis else null,
                selectedDeadlineHour = if (show) it.selectedDeadlineHour else null,
                selectedDeadlineMinute = if (show) it.selectedDeadlineMinute else null
            )
        }
        validateStep3()
    }

    private fun validateStep3() {
        val state = _uiState.value
        val selDate = state.selectedDateMillis
        val isEventDateSelected = selDate != null
        
        var isDeadlineValid = true
        var isDeadlineExceeded = false

        if (state.showDeadlineSection) {
            val selDeadlineDate = state.selectedDeadlineDateMillis
            val isDeadlineDateSelected = selDeadlineDate != null
            if (!isDeadlineDateSelected) {
                isDeadlineValid = false
            } else if (isEventDateSelected) {
                val eventDate = Instant.fromEpochMilliseconds(selDate).toLocalDateTime(TimeZone.currentSystemDefault()).date
                val deadlineDate = Instant.fromEpochMilliseconds(selDeadlineDate).toLocalDateTime(TimeZone.currentSystemDefault()).date

                val eventHour = state.selectedHour ?: 0
                val eventMinute = state.selectedMinute ?: 0
                val deadlineHour = state.selectedDeadlineHour ?: 0
                val deadlineMinute = state.selectedDeadlineMinute ?: 0

                val eventDateTime = LocalDateTime(eventDate.year, eventDate.month, eventDate.day, eventHour, eventMinute)
                val deadlineDateTime = LocalDateTime(deadlineDate.year, deadlineDate.month, deadlineDate.day, deadlineHour, deadlineMinute)

                if (deadlineDateTime > eventDateTime) {
                    isDeadlineExceeded = true
                    isDeadlineValid = false
                } else {
                    if (state.selectedDeadlineHour == null || state.selectedDeadlineMinute == null) {
                        isDeadlineValid = false
                    }
                }
            } else {
                isDeadlineValid = false
            }
        }

        _uiState.update {
            it.copy(
                isDeadlineExceeded = isDeadlineExceeded,
                isStep3Valid = isEventDateSelected && state.selectedHour != null && state.selectedMinute != null && isDeadlineValid
            )
        }
    }

    fun onContinueStep3() {
        if (_uiState.value.isStep3Valid) {
            viewModelScope.launch {
                _effect.emit(CreateEventStep3Effect.NavigateToStep4)
            }
        }
    }

    fun onBackClicked() {
        viewModelScope.launch {
            _effect.emit(CreateEventStep3Effect.NavigateBack)
        }
    }
}
