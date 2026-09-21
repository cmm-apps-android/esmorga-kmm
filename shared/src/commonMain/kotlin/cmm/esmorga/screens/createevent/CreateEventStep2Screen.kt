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
import cmm.esmorga.designsystem.EsmorgaRadioButtonRow
import cmm.esmorga.designsystem.EsmorgaText
import cmm.esmorga.designsystem.EsmorgaTextStyle
import cmm.esmorga.domain.event.model.EventType
import cmm.esmorga.shared.generated.resources.Res
import cmm.esmorga.shared.generated.resources.back_icon_description
import cmm.esmorga.shared.generated.resources.ic_arrow_back
import cmm.esmorga.shared.generated.resources.screen_create_event_title
import cmm.esmorga.shared.generated.resources.step_2_option_charity
import cmm.esmorga.shared.generated.resources.step_2_option_food
import cmm.esmorga.shared.generated.resources.step_2_option_games
import cmm.esmorga.shared.generated.resources.step_2_option_party
import cmm.esmorga.shared.generated.resources.step_2_option_sport
import cmm.esmorga.shared.generated.resources.step_2_screen_title
import cmm.esmorga.shared.generated.resources.step_continue_button
import cmm.esmorga.utils.screenContentInsets
import cmm.esmorga.utils.screenTopBarInsets
import cmm.esmorga.viewmodel.createevent.CreateEventViewModel
import cmm.esmorga.viewmodel.createevent.model.CreateEventEffect
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateEventStep2Screen(
    cevm: CreateEventViewModel,
    onNext: () -> Unit,
    onBackPressed: () -> Unit
) {
    val uiState by cevm.uiState.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()

    LaunchedEffect(Unit) {
        cevm.effect.collect { effect ->
            when (effect) {
                CreateEventEffect.NavigateToSuccess -> onNext()
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
            EsmorgaText(
                text = stringResource(Res.string.step_2_screen_title),
                style = EsmorgaTextStyle.BODY_1
            )
            Spacer(modifier = Modifier.height(32.dp))

            EventType.entries.forEach { type ->
                val label = when (type) {
                    EventType.PARTY -> stringResource(Res.string.step_2_option_party)
                    EventType.SPORT -> stringResource(Res.string.step_2_option_sport)
                    EventType.FOOD -> stringResource(Res.string.step_2_option_food)
                    EventType.CHARITY -> stringResource(Res.string.step_2_option_charity)
                    EventType.GAMES -> stringResource(Res.string.step_2_option_games)
                }
                EsmorgaRadioButtonRow(
                    text = label,
                    selected = uiState.selectedType == type,
                    onClick = { cevm.onEventTypeSelected(type) }
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            EsmorgaButton(
                text = stringResource(Res.string.step_continue_button),
                isEnabled = uiState.selectedType != null,
                onClick = { cevm.onContinueStep2() }
            )
        }
    }
}
