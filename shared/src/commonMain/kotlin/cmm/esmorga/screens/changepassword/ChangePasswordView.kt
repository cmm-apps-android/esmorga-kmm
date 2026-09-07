package cmm.esmorga.screens.changepassword

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
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import cmm.esmorga.shared.generated.resources.field_title_password
import cmm.esmorga.shared.generated.resources.ic_arrow_back
import cmm.esmorga.shared.generated.resources.my_profile_change_password
import cmm.esmorga.shared.generated.resources.placeholder_confirm_password
import cmm.esmorga.shared.generated.resources.placeholder_new_password
import cmm.esmorga.shared.generated.resources.placeholder_password
import cmm.esmorga.shared.generated.resources.registration_password_invalid
import cmm.esmorga.shared.generated.resources.registration_password_mismatch_error
import cmm.esmorga.shared.generated.resources.registration_reused_password_error
import cmm.esmorga.shared.generated.resources.reset_password_new_password_field
import cmm.esmorga.shared.generated.resources.reset_password_repeat_password_field
import cmm.esmorga.utils.screenContentInsets
import cmm.esmorga.utils.screenTopBarInsets
import cmm.esmorga.view.theme.EsmorgaTheme
import cmm.esmorga.viewmodel.changepassword.ChangePasswordEffect
import cmm.esmorga.viewmodel.changepassword.ChangePasswordField
import cmm.esmorga.viewmodel.changepassword.ChangePasswordErrorRes
import cmm.esmorga.viewmodel.changepassword.ChangePasswordUiState
import cmm.esmorga.viewmodel.changepassword.ChangePasswordViewModel
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ChangePasswordScreen(
    cvm: ChangePasswordViewModel = koinViewModel(),
    onBackClicked: () -> Unit,
    onChangePasswordSuccess: () -> Unit,
    onChangePasswordError: (String) -> Unit
) {
    val uiState: ChangePasswordUiState by cvm.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        cvm.effect.collect { effect ->
            when (effect) {
                ChangePasswordEffect.NavigateToHome -> onChangePasswordSuccess()
                is ChangePasswordEffect.ShowFullScreenError -> onChangePasswordError(effect.esmorgaErrorScreenArguments)
            }
        }
    }

    EsmorgaTheme {
        ChangePasswordView(
            uiState = uiState,
            onBackClicked = onBackClicked,
            validateField = cvm::validateField,
            clearFieldError = cvm::clearFieldError,
            onChangePasswordClicked = cvm::onChangePasswordClicked,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ChangePasswordView(
    uiState: ChangePasswordUiState,
    onBackClicked: () -> Unit,
    validateField: (ChangePasswordField, String, String, String) -> Unit,
    clearFieldError: (ChangePasswordField) -> Unit,
    onChangePasswordClicked: (String, String, String) -> Unit,
) {
    var currentPassword by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var repeatedPassword by remember { mutableStateOf("") }

    Scaffold(
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
                text = stringResource(Res.string.my_profile_change_password),
                style = EsmorgaTextStyle.HEADING_1
            )
            Spacer(modifier = Modifier.height(16.dp))
            EsmorgaTextField(
                modifier = Modifier.onFocusChanged { focusState ->
                    if (!focusState.isFocused && currentPassword.isNotEmpty()) {
                        validateField(ChangePasswordField.CURRENT_PASSWORD, currentPassword, newPassword, repeatedPassword)
                    }
                },
                value = currentPassword,
                isEnabled = !uiState.loading,
                onValueChange = {
                    currentPassword = it
                    clearFieldError(ChangePasswordField.CURRENT_PASSWORD)
                },
                errorText = resolveChangePasswordErrorText(uiState.currentPasswordError),
                isPassword = true,
                title = stringResource(Res.string.field_title_password),
                placeholder = stringResource(Res.string.placeholder_password),
                imeAction = ImeAction.Next
            )
            EsmorgaTextField(
                modifier = Modifier.onFocusChanged { focusState ->
                    if (!focusState.isFocused && newPassword.isNotEmpty()) {
                        validateField(ChangePasswordField.NEW_PASSWORD, currentPassword, newPassword, repeatedPassword)
                    }
                },
                value = newPassword,
                isEnabled = !uiState.loading,
                onValueChange = {
                    newPassword = it
                    clearFieldError(ChangePasswordField.NEW_PASSWORD)
                },
                errorText = resolveChangePasswordErrorText(uiState.newPasswordError),
                isPassword = true,
                title = stringResource(Res.string.reset_password_new_password_field),
                placeholder = stringResource(Res.string.placeholder_new_password),
                imeAction = ImeAction.Next
            )
            EsmorgaTextField(
                modifier = Modifier.onFocusChanged { focusState ->
                    if (!focusState.isFocused && repeatedPassword.isNotEmpty()) {
                        validateField(ChangePasswordField.REPEAT_PASSWORD, currentPassword, newPassword, repeatedPassword)
                    }
                },
                value = repeatedPassword,
                isEnabled = !uiState.loading,
                onValueChange = {
                    repeatedPassword = it
                    clearFieldError(ChangePasswordField.REPEAT_PASSWORD)
                },
                errorText = resolveChangePasswordErrorText(uiState.repeatPasswordError),
                isPassword = true,
                title = stringResource(Res.string.reset_password_repeat_password_field),
                placeholder = stringResource(Res.string.placeholder_confirm_password),
                imeAction = ImeAction.Done,
                onDonePressed = {
                    onChangePasswordClicked(currentPassword, newPassword, repeatedPassword)
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            EsmorgaButton(
                text = stringResource(Res.string.my_profile_change_password),
                isEnabled = currentPassword.isNotBlank() && newPassword.isNotBlank() && repeatedPassword.isNotBlank() && !uiState.loading && !uiState.hasAnyError(),
                isLoading = uiState.loading,
                primary = true
            ) {
                onChangePasswordClicked(currentPassword, newPassword, repeatedPassword)
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun resolveChangePasswordErrorText(errorRes: ChangePasswordErrorRes?): String? {
    return when (errorRes) {
        ChangePasswordErrorRes.INVALID_PASSWORD -> stringResource(Res.string.registration_password_invalid)
        ChangePasswordErrorRes.REUSED_PASSWORD -> stringResource(Res.string.registration_reused_password_error)
        ChangePasswordErrorRes.PASSWORD_MISMATCH -> stringResource(Res.string.registration_password_mismatch_error)
        else -> null
    }
}