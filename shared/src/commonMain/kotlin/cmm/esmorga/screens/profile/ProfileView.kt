package cmm.esmorga.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cmm.esmorga.designsystem.EsmorgaRow
import cmm.esmorga.designsystem.EsmorgaText
import cmm.esmorga.designsystem.EsmorgaTextStyle
import cmm.esmorga.screens.errors.EsmorgaGuestError
import cmm.esmorga.shared.generated.resources.Res
import cmm.esmorga.shared.generated.resources.login_button
import cmm.esmorga.shared.generated.resources.my_profile_change_password
import cmm.esmorga.shared.generated.resources.my_profile_email
import cmm.esmorga.shared.generated.resources.my_profile_logout
import cmm.esmorga.shared.generated.resources.my_profile_name
import cmm.esmorga.shared.generated.resources.my_profile_options
import cmm.esmorga.shared.generated.resources.unauthenticated_error_message
import cmm.esmorga.viewmodel.profile.ProfileViewModel
import cmm.esmorga.viewmodel.profile.model.ProfileUiState
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ProfileScreen(
    pvm: ProfileViewModel = koinViewModel(),
    onNavigateToLogin: () -> Unit
) {
    val uiState: ProfileUiState by pvm.uiState.collectAsStateWithLifecycle()

    when {
        uiState.isLoggedIn -> ProfileView(
            uiState = uiState,
            onChangePasswordClicked = { pvm.onChangePasswordClicked() },
            onLogoutClicked = { pvm.onLogoutClicked() }
        )

        else -> EsmorgaGuestError(
            errorMessage = stringResource(Res.string.unauthenticated_error_message),
            buttonText = stringResource(Res.string.login_button),
            onButtonClicked = onNavigateToLogin
        )
    }
}

@Composable
private fun ProfileView(
    uiState: ProfileUiState,
    onChangePasswordClicked: () -> Unit,
    onLogoutClicked: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorScheme.background)
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        EsmorgaText(text = stringResource(Res.string.my_profile_name), style = EsmorgaTextStyle.HEADING_1, Modifier.padding(horizontal = 16.dp))
        Spacer(modifier = Modifier.height(8.dp))
        EsmorgaText(
            text = uiState.name,
            style = EsmorgaTextStyle.BODY_1,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(36.dp))
        EsmorgaText(text = stringResource(Res.string.my_profile_email), style = EsmorgaTextStyle.HEADING_1, Modifier.padding(horizontal = 16.dp))
        Spacer(modifier = Modifier.height(8.dp))
        EsmorgaText(
            text = uiState.email,
            style = EsmorgaTextStyle.BODY_1,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(36.dp))
        EsmorgaText(text = stringResource(Res.string.my_profile_options), style = EsmorgaTextStyle.HEADING_1, Modifier.padding(horizontal = 16.dp))
        Spacer(modifier = Modifier.height(8.dp))
        EsmorgaRow(
            title = stringResource(Res.string.my_profile_change_password),
            onClick = onChangePasswordClicked
        )
        EsmorgaRow(
            title = stringResource(Res.string.my_profile_logout),
            onClick = onLogoutClicked
        )
    }
}