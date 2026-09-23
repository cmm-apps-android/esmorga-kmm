package cmm.esmorga.screens.createevent

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cmm.esmorga.designsystem.EsmorgaButton
import cmm.esmorga.designsystem.EsmorgaText
import cmm.esmorga.designsystem.EsmorgaTextField
import cmm.esmorga.designsystem.EsmorgaTextStyle
import cmm.esmorga.shared.generated.resources.Res
import cmm.esmorga.shared.generated.resources.back_icon_description
import cmm.esmorga.shared.generated.resources.field_title_event_location
import cmm.esmorga.shared.generated.resources.placeholder_event_location
import cmm.esmorga.shared.generated.resources.field_title_event_coordinates
import cmm.esmorga.shared.generated.resources.placeholder_event_coordinates
import cmm.esmorga.shared.generated.resources.field_title_event_max_capacity
import cmm.esmorga.shared.generated.resources.placeholder_event_max_capacity
import cmm.esmorga.shared.generated.resources.inline_error_location_required
import cmm.esmorga.shared.generated.resources.inline_error_coordinates_invalid
import cmm.esmorga.shared.generated.resources.inline_error_max_capacity_invalid
import cmm.esmorga.shared.generated.resources.ic_arrow_back
import cmm.esmorga.shared.generated.resources.screen_create_event_title
import cmm.esmorga.shared.generated.resources.step_continue_button
import cmm.esmorga.utils.screenContentInsets
import cmm.esmorga.utils.screenTopBarInsets
import cmm.esmorga.viewmodel.createevent.CreateEventStep4ViewModel
import cmm.esmorga.viewmodel.createevent.model.CreateEventStep4Effect
import cmm.esmorga.viewmodel.createevent.model.LocationError
import cmm.esmorga.viewmodel.createevent.model.CoordinatesError
import cmm.esmorga.viewmodel.createevent.model.MaxCapacityError
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateEventStep4Screen(
    viewModel: CreateEventStep4ViewModel = koinViewModel(),
    onNext: () -> Unit,
    onBackPressed: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                CreateEventStep4Effect.NavigateToStep5 -> onNext()
                CreateEventStep4Effect.NavigateBack -> onBackPressed()
            }
        }
    }

    Scaffold(
        contentWindowInsets = screenContentInsets(),
        topBar = {
            TopAppBar(
                title = {},
                windowInsets = screenTopBarInsets(),
                navigationIcon = {
                    IconButton(onClick = { viewModel.onBackClicked() }) {
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
                .padding(horizontal = 16.dp)
        ) {
            EsmorgaText(
                text = stringResource(Res.string.screen_create_event_title),
                style = EsmorgaTextStyle.HEADING_1
            )
            Spacer(modifier = Modifier.height(20.dp))

            val locationErrorText = when (uiState.locationError) {
                LocationError.EMPTY -> stringResource(Res.string.inline_error_location_required, CreateEventStep4ViewModel.LOCATION_NAME_MAX_LENGTH)
                LocationError.INVALID_LENGTH -> stringResource(Res.string.inline_error_location_required, CreateEventStep4ViewModel.LOCATION_NAME_MAX_LENGTH)
                null -> null
            }

            EsmorgaTextField(
                value = uiState.eventLocation,
                onValueChange = { viewModel.onEventLocationChanged(it) },
                title = stringResource(Res.string.field_title_event_location),
                placeholder = stringResource(Res.string.placeholder_event_location),
                imeAction = ImeAction.Next,
                errorText = locationErrorText
            )

            Spacer(modifier = Modifier.height(16.dp))

            val coordinatesErrorText = when (uiState.coordinatesError) {
                CoordinatesError.INVALID_FORMAT -> stringResource(Res.string.inline_error_coordinates_invalid)
                null -> null
            }

            EsmorgaTextField(
                value = uiState.eventCoordinates,
                onValueChange = { viewModel.onEventCoordinatesChanged(it) },
                title = stringResource(Res.string.field_title_event_coordinates),
                placeholder = stringResource(Res.string.placeholder_event_coordinates),
                imeAction = ImeAction.Next,
                errorText = coordinatesErrorText
            )

            Spacer(modifier = Modifier.height(16.dp))

            val maxCapacityErrorText = when (uiState.maxCapacityError) {
                MaxCapacityError.INVALID_VALUE -> stringResource(Res.string.inline_error_max_capacity_invalid)
                null -> null
            }

            EsmorgaTextField(
                value = uiState.eventMaxCapacity,
                onValueChange = { viewModel.onEventMaxCapacityChanged(it) },
                title = stringResource(Res.string.field_title_event_max_capacity),
                placeholder = stringResource(Res.string.placeholder_event_max_capacity),
                imeAction = ImeAction.Done,
                errorText = maxCapacityErrorText,
                onDonePressed = {
                    if (uiState.isStep4Valid) {
                        viewModel.onContinueStep4()
                    }
                }
            )

            Spacer(modifier = Modifier.height(32.dp))

            EsmorgaButton(
                text = stringResource(Res.string.step_continue_button),
                isEnabled = uiState.isStep4Valid,
                onClick = { viewModel.onContinueStep4() }
            )
        }
    }
}
