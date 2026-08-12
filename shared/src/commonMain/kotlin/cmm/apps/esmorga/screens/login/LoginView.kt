package cmm.apps.esmorga.screens.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cmm.apps.designsystem.EsmorgaButton
import cmm.apps.designsystem.EsmorgaText
import cmm.apps.designsystem.EsmorgaTextField
import cmm.apps.designsystem.EsmorgaTextStyle
import cmm.apps.esmorga.utils.screenContentInsets
import cmm.apps.esmorga.utils.screenTopBarInsets
import cmm.apps.esmorga.view.theme.EsmorgaTheme
import cmm.apps.viewmodel.login.LoginEffect
import cmm.apps.viewmodel.login.LoginUiState
import cmm.apps.viewmodel.login.LoginViewModel
import esmorga.shared.generated.resources.Res
import esmorga.shared.generated.resources.back_icon_description
import esmorga.shared.generated.resources.ic_arrow_back
import esmorga.shared.generated.resources.img_login_header
import esmorga.shared.generated.resources.login_button
import esmorga.shared.generated.resources.login_screen_create_account_button
import esmorga.shared.generated.resources.login_screen_email
import esmorga.shared.generated.resources.login_screen_password
import esmorga.shared.generated.resources.login_screen_title
import esmorga.shared.generated.resources.no_internet_snackbar
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun LoginScreen(
    lvm: LoginViewModel = koinViewModel(),
    onRegisterClicked: () -> Unit,
    onLoginSuccess: () -> Unit,
    onLoginError: (String) -> Unit,
    onBackClicked: () -> Unit
) {
    val uiState: LoginUiState by lvm.uiState.collectAsStateWithLifecycle()
    val message = stringResource(Res.string.no_internet_snackbar)
    val snackbarHostState = remember { SnackbarHostState() }
    val localCoroutineScope = rememberCoroutineScope()
    LaunchedEffect(Unit) {
        lvm.effect.collect { eff ->
            when (eff) {
                is LoginEffect.ShowNoNetworkSnackbar -> localCoroutineScope.launch { snackbarHostState.showSnackbar(message = message) }
                is LoginEffect.NavigateToRegistration -> onRegisterClicked()
                is LoginEffect.ShowFullScreenError -> onLoginError(eff.error)
                is LoginEffect.NavigateToEventList -> onLoginSuccess()
            }
        }
    }

    EsmorgaTheme {
        LoginView(
            uiState = uiState,
            snackbarHostState = snackbarHostState,
            onBackClicked = onBackClicked,
            onLoginClicked = { email, password -> lvm.onLoginClicked(email, password) },
            onRegisterClicked = { lvm.onRegisterClicked() },
            onEmailChanged = { lvm.onEmailChanged() },
            onPassChanged = { lvm.onPassChanged() },
            validateEmail = { email -> lvm.validateEmail(email) },
            validatePass = { password -> lvm.validatePass(password) }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginView(
    uiState: LoginUiState,
    snackbarHostState: SnackbarHostState,
    onBackClicked: () -> Unit,
    onLoginClicked: (String, String) -> Unit,
    onRegisterClicked: () -> Unit,
    onEmailChanged: () -> Unit,
    onPassChanged: () -> Unit,
    validateEmail: (String) -> Unit,
    validatePass: (String) -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        contentWindowInsets = screenContentInsets(),
        topBar = {
            TopAppBar(
                title = {},
                windowInsets = screenTopBarInsets(),
                navigationIcon = {
                    Icon(
                        painter = painterResource(Res.drawable.ic_arrow_back),
                        contentDescription = stringResource(Res.string.back_icon_description),
                        modifier = Modifier.clickable { onBackClicked() }
                    )
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding)
        ) {
            Image(
                painter = painterResource(Res.drawable.img_login_header),
                contentDescription = "Login header",
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(0.3f),
                contentScale = ContentScale.FillWidth
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .verticalScroll(state = rememberScrollState())
            ) {
                EsmorgaText(text = stringResource(Res.string.login_screen_title), style = EsmorgaTextStyle.HEADING_1, modifier = Modifier.padding(vertical = 16.dp))
                EsmorgaTextField(
                    value = email,
                    isEnabled = !uiState.loading,
                    onValueChange = {
                        email = it
                        onEmailChanged()
                    },
                    errorText = uiState.emailError,
                    placeholder = stringResource(Res.string.login_screen_email),
                    modifier = Modifier.onFocusChanged { focusState ->
                        if (!focusState.isFocused) {
                            validateEmail(email)
                        }
                    },
                    imeAction = ImeAction.Next
                )
                EsmorgaTextField(
                    value = password,
                    isEnabled = !uiState.loading,
                    onValueChange = {
                        password = it
                        onPassChanged()
                    },
                    errorText = uiState.passwordError,
                    isPassword = true,
                    placeholder = stringResource(Res.string.login_screen_password),
                    modifier = Modifier.onFocusChanged { focusState ->
                        if (!focusState.isFocused) {
                            validatePass(password)
                        }
                    },
                    imeAction = ImeAction.Done,
                    onDonePressed = {
                        onLoginClicked(email, password)
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))
                EsmorgaButton(text = stringResource(Res.string.login_button), isLoading = uiState.loading) {
                    onLoginClicked(email, password)
                }
                EsmorgaButton(text = stringResource(Res.string.login_screen_create_account_button), isEnabled = !uiState.loading, primary = false) {
                    onRegisterClicked()
                }
            }
        }

    }
}
