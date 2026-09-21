package cmm.esmorga.screens.createevent

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cmm.esmorga.designsystem.*
import cmm.esmorga.shared.generated.resources.Res
import cmm.esmorga.shared.generated.resources.back_icon_description
import cmm.esmorga.shared.generated.resources.ic_arrow_back
import cmm.esmorga.shared.generated.resources.screen_create_event_title
import cmm.esmorga.shared.generated.resources.step_3_screen_row_time
import cmm.esmorga.shared.generated.resources.step_3_screen_title
import cmm.esmorga.shared.generated.resources.step_continue_button
import cmm.esmorga.shared.generated.resources.time_picker_dialog_title
import cmm.esmorga.utils.screenContentInsets
import cmm.esmorga.utils.screenTopBarInsets
import cmm.esmorga.viewmodel.createevent.CreateEventViewModel
import cmm.esmorga.viewmodel.createevent.model.CreateEventEffect
import kotlinx.datetime.*
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

    val startOfToday = remember {
        val today = Clock.System.todayIn(TimeZone.currentSystemDefault())
        today.atStartOfDayIn(TimeZone.currentSystemDefault()).toEpochMilliseconds()
    }

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = Clock.System.now().toEpochMilliseconds(),
        selectableDates = PossibleSelectableDates(startOfToday)
    )

    val currentTime = Clock.System.now()
        .toLocalDateTime(TimeZone.currentSystemDefault())

    val timePickerState = rememberTimePickerState(
        initialHour = currentTime.hour,
        initialMinute = currentTime.minute,
        is24Hour = true
    )

    LaunchedEffect(datePickerState.selectedDateMillis) {
        cevm.onDateChanged(datePickerState.selectedDateMillis)
    }

    LaunchedEffect(Unit) {
        cevm.effect.collect { effect ->
            when (effect) {
                CreateEventEffect.NavigateToSuccess -> onNext()
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

            EsmorgaDatePicker(state = datePickerState)

            val formattedTime = if (uiState.selectedHour != null && uiState.selectedMinute != null) {
                "${uiState.selectedHour.toString().padStart(2, '0')}:${uiState.selectedMinute.toString().padStart(2, '0')}"
            } else {
                ""
            }

            EsmorgaRow(
                title = stringResource(Res.string.step_3_screen_row_time),
                caption = formattedTime,
                onClick = { showTimePicker = true }
            )

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
