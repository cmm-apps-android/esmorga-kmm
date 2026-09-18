package cmm.esmorga.screens.eventattendees

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cmm.esmorga.designsystem.EsmorgaCheckboxRow
import cmm.esmorga.designsystem.EsmorgaHorizontalDivider
import cmm.esmorga.designsystem.EsmorgaLinearLoader
import cmm.esmorga.designsystem.EsmorgaText
import cmm.esmorga.designsystem.EsmorgaTextStyle
import cmm.esmorga.domain.event.model.Attendee
import cmm.esmorga.screens.errors.EsmorgaFullScreenError
import cmm.esmorga.shared.generated.resources.Res
import cmm.esmorga.shared.generated.resources.back_icon_description
import cmm.esmorga.shared.generated.resources.body_loader
import cmm.esmorga.shared.generated.resources.default_error_title
import cmm.esmorga.shared.generated.resources.ic_arrow_back
import cmm.esmorga.shared.generated.resources.title_event_attendees
import cmm.esmorga.shared.generated.resources.title_name
import cmm.esmorga.shared.generated.resources.title_payment_status
import cmm.esmorga.utils.screenContentInsets
import cmm.esmorga.utils.screenTopBarInsets
import cmm.esmorga.viewmodel.eventattendees.EventAttendeesViewModel
import cmm.esmorga.viewmodel.eventattendees.model.EventAttendeesUiState
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun EventAttendeesScreen(
    eventId: String,
    onBackPressed: () -> Unit,
    viewModel: EventAttendeesViewModel = koinViewModel(parameters = { parametersOf(eventId) })
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    if (uiState.hasError) {
        EsmorgaFullScreenError(
            esmorgaErrorScreenArguments = stringResource(Res.string.default_error_title),
            onButtonPressed = onBackPressed
        )
    } else {
        EventAttendeesView(
            uiState = uiState,
            onBackPressed = onBackPressed,
            onAttendeeChecked = viewModel::onAttendeeChecked
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EventAttendeesView(
    uiState: EventAttendeesUiState,
    onBackPressed: () -> Unit,
    onAttendeeChecked: (Int, Boolean) -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = screenContentInsets(),
        topBar = {
            TopAppBar(
                title = {},
                windowInsets = screenTopBarInsets(),
                navigationIcon = {
                    Icon(
                        painter = painterResource(Res.drawable.ic_arrow_back),
                        contentDescription = stringResource(Res.string.back_icon_description),
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .clickable { onBackPressed() }
                    )
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            EsmorgaText(
                text = stringResource(Res.string.title_event_attendees),
                style = EsmorgaTextStyle.TITLE,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(24.dp))

            if (uiState.isLoading) {
                EventAttendeesLoading()
            } else {
                EventAttendeesList(
                    attendees = uiState.attendees,
                    onAttendeeChecked = onAttendeeChecked
                )
            }
        }
    }
}

@Composable
private fun EventAttendeesLoading() {
    EsmorgaText(
        text = stringResource(Res.string.body_loader),
        style = EsmorgaTextStyle.HEADING_2,
        modifier = Modifier.padding(horizontal = 16.dp)
    )
    Spacer(modifier = Modifier.height(12.dp))
    EsmorgaLinearLoader(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    )
}

@Composable
private fun EventAttendeesList(attendees: List<Attendee>, onAttendeeChecked: (Int, Boolean) -> Unit) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, bottom = 32.dp, start = 16.dp, end = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                EsmorgaText(
                    text = stringResource(Res.string.title_name),
                    style = EsmorgaTextStyle.HEADING_2
                )

                EsmorgaText(
                    text = stringResource(Res.string.title_payment_status),
                    style = EsmorgaTextStyle.HEADING_2
                )
            }
        }

        item {
            EsmorgaHorizontalDivider()
        }

        itemsIndexed(attendees) { index, attendee ->
            EsmorgaCheckboxRow(
                text = "${index + 1}. ${attendee.name}",
                shouldShowChecked = true,
                checked = attendee.alreadyPaid,
                onCheckedChanged = { onAttendeeChecked(index, it) }
            )
        }
    }
}
