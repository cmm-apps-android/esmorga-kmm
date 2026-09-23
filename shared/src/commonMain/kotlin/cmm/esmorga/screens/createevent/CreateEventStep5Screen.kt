package cmm.esmorga.screens.createevent

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import cmm.esmorga.designsystem.EsmorgaButton
import cmm.esmorga.designsystem.EsmorgaSnackbarHost
import cmm.esmorga.designsystem.EsmorgaText
import cmm.esmorga.designsystem.EsmorgaTextField
import cmm.esmorga.designsystem.EsmorgaTextStyle
import cmm.esmorga.shared.generated.resources.Res
import cmm.esmorga.shared.generated.resources.back_icon_description
import cmm.esmorga.shared.generated.resources.button_create_event
import cmm.esmorga.shared.generated.resources.button_delete
import cmm.esmorga.shared.generated.resources.button_preview
import cmm.esmorga.shared.generated.resources.field_title_event_image
import cmm.esmorga.shared.generated.resources.ic_arrow_back
import cmm.esmorga.shared.generated.resources.img_event_list_empty
import cmm.esmorga.shared.generated.resources.placeholder_event_image
import cmm.esmorga.shared.generated.resources.screen_create_event_title
import cmm.esmorga.shared.generated.resources.error_create_event
import cmm.esmorga.utils.screenContentInsets
import cmm.esmorga.utils.screenTopBarInsets
import cmm.esmorga.viewmodel.createevent.CreateEventStep5ViewModel
import cmm.esmorga.viewmodel.createevent.model.CreateEventStep5Effect
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateEventStep5Screen(
    viewModel: CreateEventStep5ViewModel = koinViewModel(),
    onNext: () -> Unit,
    onBackPressed: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val createEventError = stringResource(Res.string.error_create_event)

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                CreateEventStep5Effect.NavigateToSuccess -> onNext()
                CreateEventStep5Effect.NavigateBack -> onBackPressed()
                CreateEventStep5Effect.ShowError -> {
                    scope.launch {
                        snackbarHostState.showSnackbar(createEventError)
                    }
                }
            }
        }
    }

    Scaffold(
        contentWindowInsets = screenContentInsets(),
        snackbarHost = { EsmorgaSnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {},
                windowInsets = screenTopBarInsets(),
                navigationIcon = {
                    IconButton(onClick = { viewModel.onBackClicked() }) {
                        Icon(
                            painter = painterResource(Res.drawable.ic_arrow_back),
                            contentDescription = stringResource(Res.string.back_icon_description)
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(scrollState)
                .padding(horizontal = 16.dp)
        ) {
            EsmorgaText(
                text = stringResource(Res.string.screen_create_event_title),
                style = EsmorgaTextStyle.HEADING_1
            )
            Spacer(modifier = Modifier.height(20.dp))

            EsmorgaTextField(
                value = uiState.eventImageUrl,
                onValueChange = { viewModel.onImageUrlChanged(it) },
                title = stringResource(Res.string.field_title_event_image),
                placeholder = stringResource(Res.string.placeholder_event_image),
                imeAction = ImeAction.Done
            )

            Spacer(modifier = Modifier.height(16.dp))

            EsmorgaButton(
                text = stringResource(
                    if (uiState.isPreviewVisible) Res.string.button_delete else Res.string.button_preview
                ),
                primary = false,
                isEnabled = uiState.eventImageUrl.isNotBlank() || uiState.isPreviewVisible,
                onClick = { viewModel.onPreviewButtonClicked() }
            )

            if (uiState.isPreviewVisible && uiState.eventImageUrl.isNotBlank()) {
                Spacer(modifier = Modifier.height(16.dp))

                AsyncImage(
                    model = uiState.eventImageUrl,
                    placeholder = painterResource(Res.drawable.img_event_list_empty),
                    error = painterResource(Res.drawable.img_event_list_empty),
                    contentDescription = stringResource(Res.string.field_title_event_image),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(16.dp))
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            EsmorgaButton(
                text = stringResource(Res.string.button_create_event),
                isLoading = uiState.isLoading,
                onClick = { viewModel.onCreateEventClicked() }
            )

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
