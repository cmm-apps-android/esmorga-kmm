package cmm.esmorga.screens.myevents

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cmm.esmorga.designsystem.EsmorgaButton
import cmm.esmorga.designsystem.EsmorgaLinearLoader
import cmm.esmorga.screens.eventlist.EventList
import cmm.esmorga.screens.eventlist.EventListEmpty
import cmm.esmorga.screens.eventlist.EventListError
import cmm.esmorga.shared.generated.resources.Res
import cmm.esmorga.shared.generated.resources.login_button
import cmm.esmorga.viewmodel.myevents.MyEventsViewModel
import cmm.esmorga.viewmodel.myevents.model.MyEventsEffect
import cmm.esmorga.viewmodel.myevents.model.MyEventsUiState
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
fun MyEventsView(
    uiState: MyEventsUiState,
    onLoginClicked: () -> Unit,
    onRetryClick: () -> Unit,
    onEventClick: (String) -> Unit
) {
    Box(modifier = Modifier.fillMaxSize().background(Color.White)) {

        when {
            uiState.loading -> EsmorgaLinearLoader(modifier = Modifier.fillMaxWidth().align(Alignment.TopCenter))
            uiState.isLoggedIn -> {
                if (uiState.error != null) {
                    EventListError(onRetryClick = onRetryClick)
                } else if (uiState.eventList.isEmpty()) {
                    EventListEmpty()
                } else {
                    Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                        EventList(events = uiState.eventList, onEventClick = onEventClick)
                    }
                }
            }
            !uiState.isLoggedIn -> {
                Column(
                    modifier = Modifier.fillMaxSize().padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.weight(1f))
                    EsmorgaButton(
                        text = stringResource(Res.string.login_button)
                    ) {
                        onLoginClicked()
                    }
                }
            }
        }
    }
}
