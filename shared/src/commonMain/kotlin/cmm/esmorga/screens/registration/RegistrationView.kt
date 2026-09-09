package cmm.esmorga.screens.registration

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cmm.esmorga.designsystem.EsmorgaButton
import cmm.esmorga.designsystem.EsmorgaText
import cmm.esmorga.designsystem.EsmorgaTextField
import cmm.esmorga.designsystem.EsmorgaTextStyle
import cmm.esmorga.shared.generated.resources.Res
import cmm.esmorga.shared.generated.resources.back_icon_description
import cmm.esmorga.shared.generated.resources.field_title_email
import cmm.esmorga.shared.generated.resources.field_title_last_name
import cmm.esmorga.shared.generated.resources.field_title_name
import cmm.esmorga.shared.generated.resources.field_title_password
import cmm.esmorga.shared.generated.resources.field_title_repeat_password
import cmm.esmorga.shared.generated.resources.ic_arrow_back
import cmm.esmorga.shared.generated.resources.no_internet_snackbar
import cmm.esmorga.shared.generated.resources.placeholder_confirm_password
import cmm.esmorga.shared.generated.resources.placeholder_email
import cmm.esmorga.shared.generated.resources.placeholder_last_name
import cmm.esmorga.shared.generated.resources.placeholder_name
import cmm.esmorga.shared.generated.resources.placeholder_password
import cmm.esmorga.shared.generated.resources.registration_screen_title
import cmm.esmorga.shared.generated.resources.registration_submit_button
import cmm.esmorga.utils.screenContentInsets
import cmm.esmorga.utils.screenTopBarInsets
import cmm.esmorga.view.theme.EsmorgaTheme
import cmm.esmorga.viewmodel.registration.RegistrationEffect
import cmm.esmorga.viewmodel.registration.RegistrationField
import cmm.esmorga.viewmodel.registration.RegistrationUiState
import cmm.esmorga.viewmodel.registration.RegistrationViewModel
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel


@Composable
fun RegistrationScreen(
    rvm: RegistrationViewModel = koinViewModel(),
    onRegistrationSuccess: () -> Unit,
    onRegistrationError: (String) -> Unit,
    onBackClicked: () -> Unit
) {
    val uiState: RegistrationUiState by rvm.uiState.collectAsStateWithLifecycle()
    val message = stringResource(Res.string.no_internet_snackbar)
    val snackbarHostState = remember { SnackbarHostState() }
    val localCoroutineScope = rememberCoroutineScope()
    LaunchedEffect(Unit) {
        rvm.effect.collect { eff ->
            when (eff) {
                is RegistrationEffect.ShowNoNetworkSnackbar -> localCoroutineScope.launch { snackbarHostState.showSnackbar(message = message) }
                is RegistrationEffect.ShowFullScreenError -> onRegistrationError(eff.esmorgaErrorScreenArguments)
                is RegistrationEffect.NavigateToEventList -> onRegistrationSuccess()
            }
        }
    }

    EsmorgaTheme {
        RegistrationView(
            uiState = uiState,
            snackbarHostState = snackbarHostState,
            onBackClicked = onBackClicked,
            onRegisterClicked = { name, lastName, email, password, repeatedPassword -> rvm.onRegisterClicked(name, lastName, email, password, repeatedPassword) },
            validateField = { field, value, comparisonValue -> rvm.validateField(field, value, comparisonValue) },
            onFieldChanged = { field -> rvm.onFieldChanged(field) }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrationView(
    uiState: RegistrationUiState,
    snackbarHostState: SnackbarHostState,
    onBackClicked: () -> Unit,
    onRegisterClicked: (String, String, String, String, String) -> Unit,
    validateField: (RegistrationField, String, String?) -> Unit,
    onFieldChanged: (RegistrationField) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var repeatedPassword by remember { mutableStateOf("") }
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
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
        ) {
            EsmorgaText(
                text = stringResource(Res.string.registration_screen_title),
                style = EsmorgaTextStyle.HEADING_1
            )
            Spacer(modifier = Modifier.height(16.dp))
            EsmorgaTextField(
                value = name,
                isEnabled = !uiState.loading,
                onValueChange = {
                    name = it
                    onFieldChanged(RegistrationField.NAME)
                },
                errorText = uiState.nameError,
                title = stringResource(Res.string.field_title_name),
                placeholder = stringResource(Res.string.placeholder_name),
                modifier = Modifier.onFocusChanged { focusState ->
                    if (!focusState.isFocused) {
                        validateField(RegistrationField.NAME, name, null)
                    }
                },
                imeAction = ImeAction.Next
            )
            EsmorgaTextField(
                value = lastName,
                isEnabled = !uiState.loading,
                onValueChange = {
                    lastName = it
                    onFieldChanged(RegistrationField.LAST_NAME)
                },
                errorText = uiState.lastNameError,
                title = stringResource(Res.string.field_title_last_name),
                placeholder = stringResource(Res.string.placeholder_last_name),
                modifier = Modifier.onFocusChanged { focusState ->
                    if (!focusState.isFocused) {
                        validateField(RegistrationField.LAST_NAME, lastName, null)
                    }
                },
                imeAction = ImeAction.Next
            )
            EsmorgaTextField(
                value = email,
                isEnabled = !uiState.loading,
                onValueChange = {
                    email = it
                    onFieldChanged(RegistrationField.EMAIL)
                },
                errorText = uiState.emailError,
                title = stringResource(Res.string.field_title_email),
                placeholder = stringResource(Res.string.placeholder_email),
                modifier = Modifier.onFocusChanged { focusState ->
                    if (!focusState.isFocused) {
                        validateField(RegistrationField.EMAIL, email, null)
                    }
                },
                imeAction = ImeAction.Next
            )
            EsmorgaTextField(
                value = password,
                isEnabled = !uiState.loading,
                onValueChange = {
                    password = it
                    onFieldChanged(RegistrationField.PASS)
                },
                errorText = uiState.passError,
                isPassword = true,
                title = stringResource(Res.string.field_title_password),
                placeholder = stringResource(Res.string.placeholder_password),
                modifier = Modifier.onFocusChanged { focusState ->
                    if (!focusState.isFocused) {
                        validateField(RegistrationField.PASS, password, null)
                    }
                },
                imeAction = ImeAction.Next,
            )
            EsmorgaTextField(
                value = repeatedPassword,
                isEnabled = !uiState.loading,
                onValueChange = {
                    repeatedPassword = it
                    onFieldChanged(RegistrationField.REPEAT_PASS)
                },
                errorText = uiState.repeatPassError,
                isPassword = true,
                title = stringResource(Res.string.field_title_repeat_password),
                placeholder = stringResource(Res.string.placeholder_confirm_password),
                modifier = Modifier.onFocusChanged { focusState ->
                    if (!focusState.isFocused) {
                        validateField(RegistrationField.REPEAT_PASS, password, repeatedPassword)
                    }
                },
                imeAction = ImeAction.Done,
                onDonePressed = {
                    onRegisterClicked(name, lastName, email, password, repeatedPassword)
                },
            )
            Spacer(modifier = Modifier.height(16.dp))
            EsmorgaButton(text = stringResource(Res.string.registration_submit_button), isEnabled = !uiState.loading, primary = true) {
                onRegisterClicked(name, lastName, email, password, repeatedPassword)
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

    }
}
