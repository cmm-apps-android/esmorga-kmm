package cmm.apps.esmorga.eventlist

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cmm.apps.designsystem.EsmorgaButton
import cmm.apps.designsystem.EsmorgaLinearLoader
import cmm.apps.designsystem.EsmorgaText
import cmm.apps.designsystem.EsmorgaTextStyle
import cmm.apps.esmorga.view.eventlist.model.EventListEffect
import cmm.apps.esmorga.view.eventlist.model.EventListUiModel
import cmm.apps.esmorga.view.eventlist.model.EventListUiState
import cmm.apps.esmorga.view.theme.EsmorgaTheme
import cmm.apps.viewmodel.eventlist.EventListViewModel
import coil3.compose.AsyncImage
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import esmorga.shared.generated.resources.Res
import esmorga.shared.generated.resources.event_image_content_description
import esmorga.shared.generated.resources.event_list_empty_text
import esmorga.shared.generated.resources.event_list_error_button
import esmorga.shared.generated.resources.event_list_error_subtitle
import esmorga.shared.generated.resources.event_list_error_title
import esmorga.shared.generated.resources.event_list_loading
import esmorga.shared.generated.resources.event_list_title
import esmorga.shared.generated.resources.ic_error
import esmorga.shared.generated.resources.img_event_list_empty
import esmorga.shared.generated.resources.no_internet_snackbar

@Composable
fun EventListScreen(elvm: EventListViewModel = koinViewModel(), onEventClick: (eventId: String) -> Unit) {
    val uiState: EventListUiState by elvm.uiState.collectAsStateWithLifecycle()

    val message = stringResource(Res.string.no_internet_snackbar)
    val snackbarHostState = remember { SnackbarHostState() }
    val localCoroutineScope = rememberCoroutineScope()
    LaunchedEffect(Unit) {
        elvm.effect.collect { eff ->
            when (eff) {
                is EventListEffect.ShowNoNetworkPrompt -> {
                    localCoroutineScope.launch {
                        snackbarHostState.showSnackbar(message = message)
                    }
                }

                is EventListEffect.NavigateToEventDetail -> onEventClick(eff.eventId)
            }
        }
    }

    EsmorgaTheme {
        EventListView(
            uiState = uiState,
            snackbarHostState = snackbarHostState,
            onRetryClick = { elvm.loadEvents() },
            onEventClick = { elvm.onEventClick(it) }
        )
    }
}

@Composable
fun EventListView(uiState: EventListUiState, snackbarHostState: SnackbarHostState, onRetryClick: () -> Unit, onEventClick: (eventId: String) -> Unit) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(
                top = innerPadding.calculateTopPadding(),
                bottom = innerPadding.calculateBottomPadding(),
                start = 16.dp,
                end = 16.dp
            )
        ) {
            EsmorgaText(text = stringResource(Res.string.event_list_title), style = EsmorgaTextStyle.HEADING_1, modifier = Modifier.padding(vertical = 32.dp))
            if (uiState.loading) {
                EventListLoading()
            } else {
                if (uiState.error.isNullOrBlank().not()) {
                    EventListError(onRetryClick)
                } else if (uiState.eventList.isEmpty()) {
                    EventListEmpty()
                } else {
                    EventList(uiState.eventList, onEventClick)
                }
            }
        }
    }
}

@Composable
fun EventListLoading() {
    Column(modifier = Modifier.fillMaxSize()) {
        EsmorgaText(text = stringResource(Res.string.event_list_loading), style = EsmorgaTextStyle.HEADING_1, modifier = Modifier.padding(vertical = 16.dp))
        EsmorgaLinearLoader(modifier = Modifier.fillMaxWidth())
    }
}

@Composable
fun EventListEmpty() {
    Column(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(Res.drawable.img_event_list_empty),
            contentDescription = stringResource(Res.string.event_list_empty_text),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(16.dp))
        )

        EsmorgaText(
            text = stringResource(Res.string.event_list_empty_text),
            style = EsmorgaTextStyle.HEADING_2,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(vertical = 32.dp, horizontal = 16.dp)
        )
    }
}

@Composable
fun EventListError(onRetryClick: () -> Unit) {
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.surfaceContainerLowest)
                .height(72.dp)
                .padding(12.dp)
                .fillMaxSize()
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.surfaceContainerLow)
                    .size(48.dp)
            ) {
                Image(
                    painter = painterResource(Res.drawable.ic_error),
                    contentDescription = stringResource(Res.string.event_list_error_title),
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                EsmorgaText(text = stringResource(Res.string.event_list_error_title), style = EsmorgaTextStyle.HEADING_2, modifier = Modifier.padding(vertical = 4.dp))
                EsmorgaText(text = stringResource(Res.string.event_list_error_subtitle), style = EsmorgaTextStyle.BODY_1)
            }
        }

        Box(modifier = Modifier.height(32.dp))

        EsmorgaButton(text = stringResource(Res.string.event_list_error_button)) {
            onRetryClick()
        }
    }
}

@Composable
fun EventList(events: List<EventListUiModel>, onEventClick: (eventId: String) -> Unit) {
    LazyColumn {
        items(events.size) { pos ->
            val event = events[pos]

            Column(modifier = Modifier
                .padding(bottom = 32.dp)
                .clickable {
                    onEventClick(event.id)
                }) {
                AsyncImage(
                    model = event.imageUrl,
                    placeholder = painterResource(Res.drawable.img_event_list_empty),
                    error = painterResource(Res.drawable.img_event_list_empty),
                    contentDescription = stringResource(Res.string.event_image_content_description, event.cardTitle),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(16.dp))
                )

                EsmorgaText(text = event.cardTitle, style = EsmorgaTextStyle.HEADING_2, modifier = Modifier.padding(vertical = 4.dp))
                EsmorgaText(text = event.cardSubtitle1, style = EsmorgaTextStyle.BODY_1_ACCENT, modifier = Modifier.padding(vertical = 4.dp))
                EsmorgaText(text = event.cardSubtitle2, style = EsmorgaTextStyle.BODY_1_ACCENT, modifier = Modifier.padding(vertical = 4.dp))
            }
        }
    }
}
