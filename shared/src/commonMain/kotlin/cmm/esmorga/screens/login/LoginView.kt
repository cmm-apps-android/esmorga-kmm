package cmm.esmorga.screens.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
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
import cmm.esmorga.designsystem.EsmorgaButton
import cmm.esmorga.designsystem.EsmorgaText
import cmm.esmorga.designsystem.EsmorgaTextField
import cmm.esmorga.designsystem.EsmorgaTextStyle
import cmm.esmorga.domain.user.model.User.Companion.EMAIL_REGEX
import cmm.esmorga.domain.user.model.User.Companion.PASSWORD_REGEX
import cmm.esmorga.shared.generated.resources.Res
import cmm.esmorga.shared.generated.resources.back_icon_description
import cmm.esmorga.shared.generated.resources.field_title_email
import cmm.esmorga.shared.generated.resources.field_title_password
import cmm.esmorga.shared.generated.resources.ic_arrow_back
import cmm.esmorga.shared.generated.resources.img_login_header
import cmm.esmorga.shared.generated.resources.inline_error_email
import cmm.esmorga.shared.generated.resources.inline_error_empty_field
import cmm.esmorga.shared.generated.resources.inline_error_password
import cmm.esmorga.shared.generated.resources.login_button
import cmm.esmorga.shared.generated.resources.login_screen_create_account_button
import cmm.esmorga.shared.generated.resources.login_screen_title
import cmm.esmorga.shared.generated.resources.no_internet_snackbar
import cmm.esmorga.shared.generated.resources.placeholder_email
import cmm.esmorga.shared.generated.resources.placeholder_password
import cmm.esmorga.utils.screenContentInsets
import cmm.esmorga.utils.screenTopBarInsets
import cmm.esmorga.view.theme.EsmorgaTheme
import cmm.esmorga.viewmodel.login.LoginEffect
import cmm.esmorga.viewmodel.login.LoginUiState
import cmm.esmorga.viewmodel.login.LoginViewModel
import cmm.esmorga.viewmodel.login.ValidationError
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
            validateEmail = { email -> lvm.validateField(email, EMAIL_REGEX, ValidationError.INVALID_EMAIL) },
            validatePass = { password -> lvm.validateField(password, PASSWORD_REGEX, ValidationError.INVALID_PASSWORD) }
        )
    }
}

@Composable
private fun getErrorMessage(error: ValidationError): String? {
    return when (error) {
        ValidationError.EMPTY -> stringResource(Res.string.inline_error_empty_field)
        ValidationError.INVALID_EMAIL -> stringResource(Res.string.inline_error_email)
        ValidationError.INVALID_PASSWORD -> stringResource(Res.string.inline_error_password)
        ValidationError.NONE -> null
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
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .clickable { onBackClicked() }
                    )
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding)
                .verticalScroll(state = rememberScrollState())
        ) {
            Image(
                painter = painterResource(Res.drawable.img_login_header),
                contentDescription = "Login header",
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f),
                contentScale = ContentScale.FillWidth
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                EsmorgaText(text = stringResource(Res.string.login_screen_title), style = EsmorgaTextStyle.HEADING_1, modifier = Modifier.padding(vertical = 16.dp))
                EsmorgaTextField(
                    value = email,
                    isEnabled = !uiState.loading,
                    onValueChange = {
                        email = it
                        onEmailChanged()
                    },
                    errorText = getErrorMessage(uiState.emailError),
                    title = stringResource(Res.string.field_title_email),
                    placeholder = stringResource(Res.string.placeholder_email),
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
                    errorText = getErrorMessage(uiState.passwordError),
                    isPassword = true,
                    title = stringResource(Res.string.field_title_password),
                    placeholder = stringResource(Res.string.placeholder_password),
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
                Spacer(modifier = Modifier.height(16.dp))
            }
        }

    }
}
