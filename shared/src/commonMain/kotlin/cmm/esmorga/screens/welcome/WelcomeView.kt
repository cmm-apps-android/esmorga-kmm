package cmm.esmorga.screens.welcome

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cmm.esmorga.designsystem.EsmorgaButton
import cmm.esmorga.view.theme.EsmorgaTheme
import cmm.esmorga.viewmodel.welcome.WelcomeViewModel
import cmm.esmorga.viewmodel.welcome.model.WelcomeEffect
import cmm.esmorga.viewmodel.welcome.model.WelcomeUiState
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun WelcomeScreen(wvm: WelcomeViewModel = koinViewModel(), onLoginRegisterClicked: () -> Unit, onEnterAsGuestClicked: () -> Unit) {

    val uiState: WelcomeUiState by wvm.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) {
        wvm.init()
        wvm.effect.collect { eff ->
            when (eff) {
                is WelcomeEffect.NavigateToEventList -> onEnterAsGuestClicked()
                is WelcomeEffect.NavigateToLogin -> onLoginRegisterClicked()
            }
        }
    }
    EsmorgaTheme {
        WelcomeView(uiState = uiState, onPrimaryButtonClicked = { wvm.onPrimaryButtonClicked() }, onSecondaryButtonClicked = { wvm.onSecondaryButtonClicked() })
    }
}

@Composable
fun WelcomeView(uiState: WelcomeUiState, onPrimaryButtonClicked: () -> Unit, onSecondaryButtonClicked: () -> Unit) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {}
    ) { innerPadding ->
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = innerPadding.calculateTopPadding(),
                    bottom = innerPadding.calculateBottomPadding()
                )
                .fillMaxSize()
        ) {
            EsmorgaButton(text = uiState.primaryButtonText) {
                onPrimaryButtonClicked()
            }
            Spacer(modifier = Modifier.height(32.dp))
            EsmorgaButton(text = uiState.secondaryButtonText, primary = false) {
                onSecondaryButtonClicked()
            }
        }
    }
}
