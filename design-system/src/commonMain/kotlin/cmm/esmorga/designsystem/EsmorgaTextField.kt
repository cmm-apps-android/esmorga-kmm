package cmm.esmorga.designsystem

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import cmm.esmorga.design_system.generated.resources.Res
import cmm.esmorga.design_system.generated.resources.ic_visibility
import cmm.esmorga.design_system.generated.resources.ic_visibility_off
import org.jetbrains.compose.resources.painterResource

@Composable
fun EsmorgaTextField(
    value: String,
    onValueChange: (String) -> Unit,
    title: String,
    placeholder: String,
    modifier: Modifier = Modifier,
    singleLine: Boolean = true,
    isPassword: Boolean = false,
    imeAction: ImeAction = ImeAction.Done,
    errorText: String? = null,
    isEnabled: Boolean = true,
    maxChars: Int? = null,
    minHeight: Dp? = null,
    onDonePressed: () -> Unit = {}
) {
    var passwordVisible by remember { mutableStateOf(false) }
    Column(modifier = modifier) {
        EsmorgaText(text = title, style = EsmorgaTextStyle.BODY_1)
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = value,
            visualTransformation = if (isPassword && !passwordVisible) PasswordVisualTransformation() else VisualTransformation.None,
            onValueChange = {
                if (maxChars == null || it.length <= maxChars) {
                    onValueChange(it)
                }
            },
            placeholder = {
                Text(
                    text = placeholder,
                    style = TextStyle(color = MaterialTheme.colorScheme.onSurface)
                )
            },
            singleLine = singleLine,
            enabled = isEnabled,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = imeAction),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.secondary,
                unfocusedBorderColor = MaterialTheme.colorScheme.secondary
            ),
            modifier = Modifier.fillMaxWidth().then(
                if (minHeight != null) Modifier.heightIn(min = minHeight) else Modifier
            ),
            shape = RoundedCornerShape(12),
            trailingIcon = {
                if (isPassword) {
                    val image = if (passwordVisible) painterResource(Res.drawable.ic_visibility) else painterResource(Res.drawable.ic_visibility_off)
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(painter = image, contentDescription = "toggle password visibility")
                    }
                }
            },
            keyboardActions = KeyboardActions(onDone = { onDonePressed() })
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp)
        ) {
            if (errorText != null) {
                EsmorgaText(
                    text = errorText,
                    style = EsmorgaTextStyle.CAPTION,
                    modifier = Modifier.weight(1f),
                    color = MaterialTheme.colorScheme.error
                )
            } else {
                Spacer(modifier = Modifier.weight(1f))
            }

            if (maxChars != null) {
                EsmorgaText(
                    text = "${value.length}/$maxChars",
                    style = EsmorgaTextStyle.CAPTION,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }
    }
}
