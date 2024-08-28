package cmm.apps.android.designsystem

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import cmm.apps.android.designsystem.EsmorgaText
import cmm.apps.android.designsystem.EsmorgaTextStyle

@Composable
fun EsmorgaTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: Int,
    modifier: Modifier = Modifier,
    singleLine: Boolean = true,
    isPassword: Boolean = false,
    imeAction: ImeAction = ImeAction.Done,
    errorText: String? = null,
    isEnabled: Boolean = true,
    onDonePressed: () -> Unit = {}
) {
    var passwordVisible by remember { mutableStateOf(false) }
    Column {
        EsmorgaText(text = stringResource(id = placeholder), style = EsmorgaTextStyle.BODY_1)
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = value,
            visualTransformation = if (isPassword && !passwordVisible) PasswordVisualTransformation() else VisualTransformation.None,
            onValueChange = onValueChange,
            placeholder = { Text(text = stringResource(id = placeholder, TextStyle(color = MaterialTheme.colorScheme.onSurface))) },
            singleLine = singleLine,
            enabled = isEnabled,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = imeAction),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.secondary,
                unfocusedBorderColor = MaterialTheme.colorScheme.secondary
            ),
            supportingText = {
                if (errorText != null) {
                    EsmorgaText(text = errorText, style = EsmorgaTextStyle.CAPTION, modifier = Modifier.padding(top = 4.dp))
                }
            },
            modifier = modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12),
            trailingIcon = {
                if (isPassword) {
                    val image = if (passwordVisible) painterResource(id = R.drawable.ic_visibility_off) else painterResource(id = R.drawable.ic_visibility)
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(image, "toggle password visibility")
                    }
                }
            },
            keyboardActions = KeyboardActions(onDone = { onDonePressed() })
        )

    }
}