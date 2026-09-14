package cmm.esmorga.screens.eventdetails

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cmm.esmorga.designsystem.EsmorgaButton
import cmm.esmorga.designsystem.EsmorgaSnackbarHost
import cmm.esmorga.designsystem.EsmorgaText
import cmm.esmorga.designsystem.EsmorgaTextStyle
import cmm.esmorga.shared.generated.resources.Res
import cmm.esmorga.shared.generated.resources.back_icon_description
import cmm.esmorga.shared.generated.resources.button_view_attendees
import cmm.esmorga.shared.generated.resources.button_deadline_passed
import cmm.esmorga.shared.generated.resources.button_join_event
import cmm.esmorga.shared.generated.resources.button_join_event_disabled
import cmm.esmorga.shared.generated.resources.button_leave_event
import cmm.esmorga.shared.generated.resources.button_login_to_join
import cmm.esmorga.shared.generated.resources.event_details_description
import cmm.esmorga.shared.generated.resources.event_details_location
import cmm.esmorga.shared.generated.resources.event_image_content_description
import cmm.esmorga.shared.generated.resources.ic_arrow_back
import cmm.esmorga.shared.generated.resources.img_event_list_empty
import cmm.esmorga.shared.generated.resources.navigate
import cmm.esmorga.shared.generated.resources.screen_event_details_capacity
import cmm.esmorga.shared.generated.resources.screen_event_details_join_deadline
import cmm.esmorga.shared.generated.resources.snackbar_event_full
import cmm.esmorga.utils.screenContentInsets
import cmm.esmorga.utils.screenTopBarInsets
import cmm.esmorga.view.theme.DarkClaret
import cmm.esmorga.view.theme.EsmorgaTheme
import cmm.esmorga.viewmodel.eventdetails.EventDetailsViewModel
import cmm.esmorga.viewmodel.eventdetails.model.EventDetailsEffect
import cmm.esmorga.viewmodel.eventdetails.model.EventDetailsUiState
import cmm.esmorga.viewmodel.explore.mapper.EventListUiMapper.formatDate
import cmm.esmorga.design_system.generated.resources.Res as DsRes
import cmm.esmorga.design_system.generated.resources.group
import coil3.compose.AsyncImage
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun EventDetailsScreen(
    eventId: String,
    onBackPressed: () -> Unit,
    onNavigateToLocation: (lat: Double, lng: Double) -> Unit,
    onNavigateToAttendees: (eventId: String) -> Unit,
    onNavigateToLogin: () -> Unit,
    onNavigateToError: () -> Unit,
    edvm: EventDetailsViewModel = koinViewModel(parameters = { parametersOf(eventId) })
) {
    val uiState: EventDetailsUiState by edvm.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val eventFullMessage = stringResource(Res.string.snackbar_event_full)
    LaunchedEffect(Unit) {
        edvm.effect.collect { eff ->
            when (eff) {
                is EventDetailsEffect.NavigateToLocation -> onNavigateToLocation(eff.lat, eff.lng)
                is EventDetailsEffect.NavigateToAttendees -> onNavigateToAttendees(eff.eventId)
                is EventDetailsEffect.NavigateBack -> onBackPressed()
                is EventDetailsEffect.NavigateToLogin -> onNavigateToLogin()
                is EventDetailsEffect.NavigateToError -> onNavigateToError()
                is EventDetailsEffect.ShowEventFullSnackbar -> scope.launch {
                    snackbarHostState.showSnackbar(eventFullMessage)
                }
            }
        }
    }
    EsmorgaTheme {
        EventDetailsView(
            uiState = uiState,
            snackbarHostState = snackbarHostState,
            onNavigateClicked = { edvm.onNavigateClick() },
            onViewAttendeesClicked = { edvm.onViewAttendeesClick() },
            onJoinLeaveClicked = { edvm.onJoinLeaveClick() },
            onBackPressed = { edvm.onBackPressed() }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventDetailsView(
    uiState: EventDetailsUiState,
    snackbarHostState: SnackbarHostState,
    onNavigateClicked: () -> Unit,
    onViewAttendeesClicked: () -> Unit,
    onJoinLeaveClicked: () -> Unit,
    onBackPressed: () -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = screenContentInsets(),
        snackbarHost = { EsmorgaSnackbarHost(snackbarHostState) },
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
                .padding(
                    top = innerPadding.calculateTopPadding(),
                    bottom = innerPadding.calculateBottomPadding()
                )
                .verticalScroll(state = rememberScrollState())
        ) {
            AsyncImage(
                model = uiState.image,
                placeholder = painterResource(Res.drawable.img_event_list_empty),
                error = painterResource(Res.drawable.img_event_list_empty),
                contentDescription = stringResource(Res.string.event_image_content_description, uiState.title),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16 / 9f)
            )
            Spacer(modifier = Modifier.height(24.dp))
            EsmorgaText(
                text = uiState.title,
                style = EsmorgaTextStyle.HEADING_1,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
            EsmorgaText(text = uiState.subtitle, style = EsmorgaTextStyle.BODY_1_ACCENT, modifier = Modifier.padding(horizontal = 16.dp))
            uiState.maxCapacity?.let { maxCapacity ->
                Spacer(modifier = Modifier.height(20.dp))
                EventAttendeesLabel(
                    currentAttendeeCount = uiState.currentAttendeeCount,
                    maxCapacity = maxCapacity,
                    isAuthenticated = uiState.isAuthenticated,
                    onViewAttendeesClicked = onViewAttendeesClicked,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                )
            }
            uiState.joinDeadline?.let { joinDeadline ->
                Spacer(modifier = Modifier.height(16.dp))
                EsmorgaText(
                    text = stringResource(
                        Res.string.screen_event_details_join_deadline,
                        formatDate(joinDeadline)
                    ),
                    style = EsmorgaTextStyle.BUTTON_PRIMARY,
                    color = DarkClaret,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
            Spacer(modifier = Modifier.height(32.dp))
            EsmorgaText(
                text = stringResource(Res.string.event_details_description),
                style = EsmorgaTextStyle.HEADING_2,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
            EsmorgaText(
                text = uiState.description,
                style = EsmorgaTextStyle.BODY_1,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(32.dp))
            EsmorgaText(
                text = stringResource(Res.string.event_details_location),
                style = EsmorgaTextStyle.HEADING_2,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
            EsmorgaText(
                text = uiState.locationName,
                style = EsmorgaTextStyle.BODY_1,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(32.dp))
            if (uiState.navigateButton) {
                EsmorgaButton(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    text = stringResource(Res.string.navigate),
                    primary = false,
                    onClick = onNavigateClicked
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            EsmorgaButton(
                modifier = Modifier.padding(horizontal = 16.dp),
                text = joinButtonLabel(uiState),
                primary = true,
                isEnabled = uiState.isJoinLeaveButtonEnabled,
                isLoading = uiState.isLoading,
                onClick = onJoinLeaveClicked
            )
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun EventAttendeesLabel(
    currentAttendeeCount: Int,
    maxCapacity: Int,
    isAuthenticated: Boolean,
    onViewAttendeesClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
            Icon(
                painter = painterResource(DsRes.drawable.group),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(width = 15.dp, height = 15.dp)
            )
            EsmorgaText(
                text = stringResource(
                    Res.string.screen_event_details_capacity,
                    currentAttendeeCount,
                    maxCapacity
                ),
                style = EsmorgaTextStyle.CAPTION,
                color = DarkClaret
            )
        }

        if (currentAttendeeCount > 0 && isAuthenticated) {
            EsmorgaText(
                text = stringResource(Res.string.button_view_attendees),
                style = EsmorgaTextStyle.CAPTION,
                color = MaterialTheme.colorScheme.onSurface,
                textDecoration = TextDecoration.Underline,
                modifier = Modifier.clickable { onViewAttendeesClicked() }
            )
        }
    }
}

@Composable
private fun joinButtonLabel(uiState: EventDetailsUiState): String = with(uiState) {
    when {
        !isAuthenticated -> stringResource(Res.string.button_login_to_join)
        userJoined -> stringResource(Res.string.button_leave_event)
        isDeadlinePassed -> stringResource(Res.string.button_deadline_passed)
        isEventFull -> stringResource(Res.string.button_join_event_disabled)
        else -> stringResource(Res.string.button_join_event)
    }
}



