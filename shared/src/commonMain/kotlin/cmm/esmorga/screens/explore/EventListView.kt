package cmm.esmorga.screens.explore

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cmm.esmorga.designsystem.EsmorgaEventCard
import cmm.esmorga.designsystem.ErrorScreen
import cmm.esmorga.designsystem.EsmorgaLinearLoader
import cmm.esmorga.designsystem.EsmorgaText
import cmm.esmorga.designsystem.EsmorgaTextStyle
import cmm.esmorga.shared.generated.resources.Res
import cmm.esmorga.shared.generated.resources.event_image_content_description
import cmm.esmorga.shared.generated.resources.event_list_empty_text
import cmm.esmorga.shared.generated.resources.event_list_error_button
import cmm.esmorga.shared.generated.resources.event_list_error_subtitle
import cmm.esmorga.shared.generated.resources.event_list_error_title
import cmm.esmorga.shared.generated.resources.event_list_loading
import cmm.esmorga.shared.generated.resources.img_event_list_empty
import cmm.esmorga.shared.generated.resources.no_internet_snackbar
import cmm.esmorga.view.theme.EsmorgaTheme
import cmm.esmorga.viewmodel.explore.ExploreViewModel
import cmm.esmorga.viewmodel.explore.model.EventListEffect
import cmm.esmorga.viewmodel.explore.model.EventListUiModel
import cmm.esmorga.viewmodel.explore.model.ExploreUiState
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ExploreScreen(
    elvm: ExploreViewModel = koinViewModel(),
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    onEventClick: (eventId: String) -> Unit
) {
    val uiState: ExploreUiState by elvm.uiState.collectAsStateWithLifecycle()

    val message = stringResource(Res.string.no_internet_snackbar)
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
            onRefresh = { elvm.loadEvents(forceRefresh = true) },
            onRetryClick = { elvm.loadEvents() },
            onEventClick = { elvm.onEventClick(it) }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EventListView(
    uiState: ExploreUiState,
    onRefresh: () -> Unit,
    onRetryClick: () -> Unit,
    onEventClick: (eventId: String) -> Unit
) {
    PullToRefreshBox(
        isRefreshing = uiState.loading && uiState.eventList.isNotEmpty(),
        onRefresh = onRefresh,
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            if (uiState.loading && uiState.eventList.isEmpty()) {
                EventListLoading()
            } else {
                if (uiState.error.isNullOrBlank().not()) {
                    ErrorScreen(
                        title = stringResource(Res.string.event_list_error_title),
                        subtitle = stringResource(Res.string.event_list_error_subtitle),
                        buttonText = stringResource(Res.string.event_list_error_button),
                        buttonAction = onRetryClick
                    )
                } else if (uiState.eventList.isEmpty() && !uiState.loading) {
                    EventListEmpty()
                } else {
                    EventList(uiState.eventList, onEventClick)
                }
            }
        }
    }
}

@Composable
private fun EventListLoading() {
    Column(modifier = Modifier.fillMaxSize()) {
        EsmorgaText(text = stringResource(Res.string.event_list_loading), style = EsmorgaTextStyle.HEADING_1, modifier = Modifier.padding(vertical = 16.dp))
        EsmorgaLinearLoader(modifier = Modifier.fillMaxWidth())
    }
}

@Composable
private fun EventListEmpty() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
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
private fun EventList(events: List<EventListUiModel>, onEventClick: (eventId: String) -> Unit) {
    LazyColumn {
        items(events.size) { pos ->
            val event = events[pos]

            EsmorgaEventCard(
                imageUrl = event.imageUrl,
                title = event.cardTitle,
                subtitle1 = event.cardSubtitle1,
                subtitle2 = event.cardSubtitle2,
                placeholder = painterResource(Res.drawable.img_event_list_empty),
                contentDescription = stringResource(Res.string.event_image_content_description, event.cardTitle),
                onClick = { onEventClick(event.id) },
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }
    }
}
