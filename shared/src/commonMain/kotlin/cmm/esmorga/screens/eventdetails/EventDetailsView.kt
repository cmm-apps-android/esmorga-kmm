package cmm.esmorga.screens.eventdetails

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cmm.esmorga.designsystem.EsmorgaButton
import cmm.esmorga.designsystem.EsmorgaText
import cmm.esmorga.designsystem.EsmorgaTextStyle
import cmm.esmorga.shared.generated.resources.Res
import cmm.esmorga.shared.generated.resources.back_icon_description
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
import cmm.esmorga.shared.generated.resources.snackbar_event_full
import cmm.esmorga.utils.screenContentInsets
import cmm.esmorga.utils.screenTopBarInsets
import cmm.esmorga.view.theme.EsmorgaTheme
import cmm.esmorga.viewmodel.eventdetails.EventDetailsViewModel
import cmm.esmorga.viewmodel.eventdetails.model.EventDetailsEffect
import cmm.esmorga.viewmodel.eventdetails.model.EventDetailsUiState
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
    onJoinLeaveClicked: () -> Unit,
    onBackPressed: () -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = screenContentInsets(),
        snackbarHost = { SnackbarHost(snackbarHostState) },
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
            if (uiState.navigateButton) {
                Spacer(modifier = Modifier.height(32.dp))
                EsmorgaButton(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    text = stringResource(Res.string.navigate),
                    primary = false,
                    onClick = onNavigateClicked
                )
            }

            EsmorgaButton(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 32.dp),
                text = joinButtonLabel(uiState),
                primary = true,
                isEnabled = uiState.isJoinLeaveButtonEnabled,
                isLoading = uiState.isLoading,
                onClick = onJoinLeaveClicked
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



