package cmm.esmorga.screens.createevent

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cmm.esmorga.designsystem.DeadlineSelectableDates
import cmm.esmorga.designsystem.EsmorgaButton
import cmm.esmorga.designsystem.EsmorgaDatePicker
import cmm.esmorga.designsystem.EsmorgaRow
import cmm.esmorga.designsystem.EsmorgaSwitchRow
import cmm.esmorga.designsystem.EsmorgaText
import cmm.esmorga.designsystem.EsmorgaTextStyle
import cmm.esmorga.designsystem.EsmorgaTimePickerDialog
import cmm.esmorga.designsystem.PossibleSelectableDates
import cmm.esmorga.shared.generated.resources.Res
import cmm.esmorga.shared.generated.resources.back_icon_description
import cmm.esmorga.shared.generated.resources.field_title_join_deadline
import cmm.esmorga.shared.generated.resources.field_title_join_deadline_time
import cmm.esmorga.shared.generated.resources.ic_arrow_back
import cmm.esmorga.shared.generated.resources.inline_error_event_date_deadline_exceeded
import cmm.esmorga.shared.generated.resources.screen_create_event_title
import cmm.esmorga.shared.generated.resources.step_3_screen_row_time
import cmm.esmorga.shared.generated.resources.step_3_screen_title
import cmm.esmorga.shared.generated.resources.step_continue_button
import cmm.esmorga.shared.generated.resources.time_picker_dialog_title
import cmm.esmorga.viewmodel.common.DateUtils
import cmm.esmorga.utils.screenContentInsets
import cmm.esmorga.utils.screenTopBarInsets
import cmm.esmorga.viewmodel.createevent.CreateEventViewModel
import cmm.esmorga.viewmodel.createevent.model.CreateEventEffect
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.todayIn
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import kotlin.time.Clock

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateEventStep3Screen(
    cevm: CreateEventViewModel,
    onNext: () -> Unit,
    onBackPressed: () -> Unit
) {
    val uiState by cevm.uiState.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()

    var showTimePicker by remember { mutableStateOf(false) }
    var showDeadlineTimePicker by remember { mutableStateOf(false) }


    val startOfToday = remember {
        val today = Clock.System.todayIn(TimeZone.currentSystemDefault())
        today.atStartOfDayIn(TimeZone.currentSystemDefault()).toEpochMilliseconds()
    }

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = Clock.System.now().toEpochMilliseconds(),
        selectableDates = PossibleSelectableDates(startOfToday)
    )

    val deadlineDatePickerState = rememberDatePickerState(
        initialSelectedDateMillis = datePickerState.selectedDateMillis ?: Clock.System.now().toEpochMilliseconds(),
        selectableDates = DeadlineSelectableDates(
            startOfToday = startOfToday,
            eventDateMidnightMillis = datePickerState.selectedDateMillis ?: Long.MAX_VALUE
        )
    )


    val currentTime = Clock.System.now()
        .toLocalDateTime(TimeZone.currentSystemDefault())

    val timePickerState = rememberTimePickerState(
        initialHour = currentTime.hour,
        initialMinute = currentTime.minute,
        is24Hour = true
    )

    val deadlineTimePickerState = rememberTimePickerState(
        initialHour = currentTime.hour,
        initialMinute = currentTime.minute,
        is24Hour = true
    )

    LaunchedEffect(datePickerState.selectedDateMillis) {
        cevm.onDateChanged(datePickerState.selectedDateMillis)
    }

    LaunchedEffect(deadlineDatePickerState.selectedDateMillis) {
        cevm.onDeadlineDateChanged(deadlineDatePickerState.selectedDateMillis)
    }

    LaunchedEffect(Unit) {
        cevm.effect.collect { effect ->
            when (effect) {
                CreateEventEffect.NavigateToStep4 -> onNext()
                CreateEventEffect.NavigateBack -> onBackPressed()
                else -> {}
            }
        }
    }

    if (showTimePicker) {
        EsmorgaTimePickerDialog(
            state = timePickerState,
            title = stringResource(Res.string.time_picker_dialog_title),
            onCancel = { showTimePicker = false },
            onConfirm = {
                cevm.onTimeChanged(timePickerState.hour, timePickerState.minute)
                showTimePicker = false
            }
        )
    }

    if (showDeadlineTimePicker) {
        EsmorgaTimePickerDialog(
            state = deadlineTimePickerState,
            title = stringResource(Res.string.time_picker_dialog_title),
            onCancel = { showDeadlineTimePicker = false },
            onConfirm = {
                cevm.onDeadlineTimeChanged(deadlineTimePickerState.hour, deadlineTimePickerState.minute)
                showDeadlineTimePicker = false
            }
        )
    }

    Scaffold(
        contentWindowInsets = screenContentInsets(),
        topBar = {
            TopAppBar(
                title = {},
                windowInsets = screenTopBarInsets(),
                navigationIcon = {
                    IconButton(onClick = { cevm.onBackClicked() }) {
                        Icon(
                            painter = painterResource(Res.drawable.ic_arrow_back),
                            contentDescription = stringResource(Res.string.back_icon_description)
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(scrollState)
        ) {
            EsmorgaText(
                text = stringResource(Res.string.screen_create_event_title),
                style = EsmorgaTextStyle.HEADING_1,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(20.dp))
            EsmorgaText(
                text = stringResource(Res.string.step_3_screen_title),
                style = EsmorgaTextStyle.BODY_1,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(20.dp))

            EsmorgaDateTimePickerSection(
                datePickerState = datePickerState,
                timeRowTitle = stringResource(Res.string.step_3_screen_row_time),
                formattedTime = DateUtils.formatTime(uiState.selectedHour, uiState.selectedMinute),
                onTimeClick = { showTimePicker = true }
            )

            Spacer(modifier = Modifier.height(20.dp))

            EsmorgaSwitchRow(
                text = stringResource(Res.string.field_title_join_deadline),
                checked = uiState.showDeadlineSection,
                shouldShowDivider = false,
                onCheckedChanged = {
                    cevm.onToggleDeadlineSection(it)
                }
            )

            if (uiState.showDeadlineSection) {
                EsmorgaDateTimePickerSection(
                    datePickerState = deadlineDatePickerState,
                    timeRowTitle = stringResource(Res.string.field_title_join_deadline_time),
                    formattedTime = DateUtils.formatTime(uiState.selectedDeadlineHour, uiState.selectedDeadlineMinute),
                    onTimeClick = { showDeadlineTimePicker = true },
                    errorContent = if (uiState.isDeadlineExceeded) {
                        {
                            EsmorgaText(
                                text = stringResource(Res.string.inline_error_event_date_deadline_exceeded),
                                style = EsmorgaTextStyle.CAPTION,
                                color = MaterialTheme.colorScheme.error,
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                            )
                        }
                    } else null
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            EsmorgaButton(
                text = stringResource(Res.string.step_continue_button),
                isEnabled = uiState.isStep3Valid,
                modifier = Modifier.padding(horizontal = 16.dp),
                onClick = { cevm.onContinueStep3() }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EsmorgaDateTimePickerSection(
    datePickerState: DatePickerState,
    timeRowTitle: String,
    formattedTime: String,
    onTimeClick: () -> Unit,
    errorContent: @Composable (() -> Unit)? = null
) {
    EsmorgaDatePicker(state = datePickerState)
    if (errorContent != null) {
        errorContent()
    }
    EsmorgaRow(
        title = timeRowTitle,
        caption = formattedTime,
        onClick = onTimeClick
    )
}
