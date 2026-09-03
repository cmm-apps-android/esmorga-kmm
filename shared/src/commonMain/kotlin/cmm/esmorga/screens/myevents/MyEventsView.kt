package cmm.esmorga.screens.myevents

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cmm.esmorga.designsystem.EsmorgaEventCard
import cmm.esmorga.designsystem.EsmorgaFullScreenError
import cmm.esmorga.designsystem.EsmorgaLinearLoader
import cmm.esmorga.designsystem.EsmorgaText
import cmm.esmorga.designsystem.EsmorgaTextStyle
import cmm.esmorga.screens.errors.EsmorgaGuestError
import cmm.esmorga.shared.generated.resources.Res
import cmm.esmorga.shared.generated.resources.event_image_content_description
import cmm.esmorga.shared.generated.resources.event_list_error_button
import cmm.esmorga.shared.generated.resources.event_list_error_subtitle
import cmm.esmorga.shared.generated.resources.event_list_error_title
import cmm.esmorga.shared.generated.resources.event_list_loading
import cmm.esmorga.shared.generated.resources.img_event_list_empty
import cmm.esmorga.shared.generated.resources.login_button
import cmm.esmorga.shared.generated.resources.screen_my_events_empty_text
import cmm.esmorga.shared.generated.resources.unauthenticated_error_message
import cmm.esmorga.viewmodel.eventlist.model.EventListUiModel
import cmm.esmorga.viewmodel.myevents.MyEventsViewModel
import cmm.esmorga.viewmodel.myevents.model.MyEventsEffect
import cmm.esmorga.viewmodel.myevents.model.MyEventsUiState
import io.github.alexzhirkevich.compottie.Compottie
import io.github.alexzhirkevich.compottie.LottieCompositionSpec
import io.github.alexzhirkevich.compottie.animateLottieCompositionAsState
import io.github.alexzhirkevich.compottie.rememberLottieComposition
import io.github.alexzhirkevich.compottie.rememberLottiePainter
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MyEventsScreen(
    mevm: MyEventsViewModel = koinViewModel(),
    onNavigateToLogin: () -> Unit,
    onEventClick: (String) -> Unit
) {
    val uiState by mevm.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        mevm.checkLoginStatus()
    }

    LaunchedEffect(Unit) {
        mevm.effect.collect { effect ->
            when (effect) {
                is MyEventsEffect.NavigateToLogin -> onNavigateToLogin()
                is MyEventsEffect.NavigateToEventDetail -> onEventClick(effect.eventId)
            }
        }
    }

    MyEventsView(
        uiState = uiState,
        onLoginClicked = { mevm.onLoginClicked() },
        onRetryClick = { mevm.checkLoginStatus() },
        onEventClick = { mevm.onEventClick(it) }
    )
}

@Composable
private fun MyEventsView(
    uiState: MyEventsUiState,
    onLoginClicked: () -> Unit,
    onRetryClick: () -> Unit,
    onEventClick: (String) -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize()
            .background(colorScheme.background)
            .padding(horizontal = 16.dp)
    ) {
        when {
            uiState.loading -> MyEventListLoading()
            uiState.isLoggedIn -> {
                if (uiState.error != null) {
                    EsmorgaFullScreenError(
                        title = stringResource(Res.string.event_list_error_title),
                        subtitle = stringResource(Res.string.event_list_error_subtitle),
                        buttonText = stringResource(Res.string.event_list_error_button),
                        buttonAction = onRetryClick
                    )
                } else if (uiState.eventList.isEmpty()) {
                    MyEventsEmptyView()
                }else {
                    MyEventList(events = uiState.eventList, onEventClick = onEventClick)
                }
            }

            !uiState.isLoggedIn -> EsmorgaGuestError(
                errorMessage = stringResource(Res.string.unauthenticated_error_message),
                buttonText = stringResource(Res.string.login_button),
                onButtonClicked = onLoginClicked
            )
        }
    }
}

@Composable
private fun MyEventListLoading() {
    Column(modifier = Modifier.fillMaxSize()) {
        EsmorgaText(text = stringResource(Res.string.event_list_loading), style = EsmorgaTextStyle.HEADING_1, modifier = Modifier.padding(vertical = 16.dp))
        EsmorgaLinearLoader(modifier = Modifier.fillMaxWidth())
    }
}

@Composable
private fun MyEventList(events: List<EventListUiModel>, onEventClick: (eventId: String) -> Unit) {
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

@Composable
fun MyEventsEmptyView() {
    val composition by rememberLottieComposition {
        LottieCompositionSpec.JsonString(
            Res.readBytes("files/empty.json").decodeToString()
        )
    }
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = Compottie.IterateForever
    )

    Column(
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.Start,
        modifier = Modifier.fillMaxSize(),
    ) {
        EsmorgaText(
            text = stringResource(Res.string.screen_my_events_empty_text),
            style = EsmorgaTextStyle.HEADING_1,
        )
        Image(
            painter = rememberLottiePainter(
                composition = composition,
                progress = { progress }
            ),
            contentDescription = null,
            contentScale = ContentScale.FillWidth,
            modifier = Modifier.width(100.dp)
        )
    }

}
