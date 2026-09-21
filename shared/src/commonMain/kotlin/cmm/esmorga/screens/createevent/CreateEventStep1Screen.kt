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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cmm.esmorga.designsystem.EsmorgaButton
import cmm.esmorga.designsystem.EsmorgaText
import cmm.esmorga.designsystem.EsmorgaTextField
import cmm.esmorga.designsystem.EsmorgaTextStyle
import cmm.esmorga.shared.generated.resources.Res
import cmm.esmorga.shared.generated.resources.back_icon_description
import cmm.esmorga.shared.generated.resources.field_title_event_description
import cmm.esmorga.shared.generated.resources.field_title_event_name
import cmm.esmorga.shared.generated.resources.ic_arrow_back
import cmm.esmorga.shared.generated.resources.inline_error_empty_field
import cmm.esmorga.shared.generated.resources.inline_error_invalid_length_description
import cmm.esmorga.shared.generated.resources.inline_error_invalid_length_name
import cmm.esmorga.shared.generated.resources.placeholder_event_name
import cmm.esmorga.shared.generated.resources.screen_create_event_title
import cmm.esmorga.shared.generated.resources.step_continue_button
import cmm.esmorga.utils.screenContentInsets
import cmm.esmorga.utils.screenTopBarInsets
import cmm.esmorga.viewmodel.createevent.CreateEventViewModel
import cmm.esmorga.viewmodel.createevent.model.CreateEventEffect
import cmm.esmorga.viewmodel.createevent.model.DescriptionError
import cmm.esmorga.viewmodel.createevent.model.NameError
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateEventStep1Screen(
    cevm: CreateEventViewModel,
    onNavigateToStep2: () -> Unit,
    onBackPressed: () -> Unit
) {
    val uiState by cevm.uiState.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()

    LaunchedEffect(Unit) {
        cevm.effect.collect { effect ->
            when (effect) {
                CreateEventEffect.NavigateToStep2 -> onNavigateToStep2()
                CreateEventEffect.NavigateBack -> onBackPressed()
                else -> {}
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
                .padding(horizontal = 16.dp)
        ) {
            EsmorgaText(
                text = stringResource(Res.string.screen_create_event_title),
                style = EsmorgaTextStyle.HEADING_1
            )
            Spacer(modifier = Modifier.height(20.dp))

            val nameErrorText = when (uiState.nameError) {
                NameError.EMPTY -> stringResource(Res.string.inline_error_empty_field)
                NameError.INVALID_LENGTH -> stringResource(Res.string.inline_error_invalid_length_name)
                null -> null
            }

            EsmorgaTextField(
                value = uiState.eventName,
                onValueChange = { cevm.onEventNameChanged(it) },
                title = stringResource(Res.string.field_title_event_name),
                placeholder = stringResource(Res.string.placeholder_event_name),
                errorText = nameErrorText
            )

            Spacer(modifier = Modifier.height(16.dp))

            val descriptionErrorText = when (uiState.descriptionError) {
                DescriptionError.INVALID_LENGTH -> stringResource(Res.string.inline_error_invalid_length_description)
                null -> null
            }

            EsmorgaTextField(
                value = uiState.eventDescription,
                onValueChange = { cevm.onEventDescriptionChanged(it) },
                title = stringResource(Res.string.field_title_event_description),
                placeholder = "",
                errorText = descriptionErrorText,
                singleLine = false,
                maxChars = 5000,
                minHeight = 120.dp
            )

            Spacer(modifier = Modifier.height(32.dp))

            EsmorgaButton(
                text = stringResource(Res.string.step_continue_button),
                isEnabled = uiState.isStep1Valid,
                onClick = { cevm.onContinueStep1() }
            )
        }
    }
}
