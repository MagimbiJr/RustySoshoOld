package dev.rustybite.rustysosho.presentation.add_post_screen

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.google.accompanist.systemuicontroller.SystemUiController
import dev.rustybite.rustysosho.R
import dev.rustybite.rustysosho.presentation.ui.components.RSTopAppBar
import dev.rustybite.rustysosho.utils.AppEvents
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPostScreen(
    viewModal: AddPostViewModal,
    onPopBack: (AppEvents.PopBackStack) -> Unit,
    modifier: Modifier = Modifier,
    systemUiController: SystemUiController,
) {
    systemUiController.setSystemBarsColor(MaterialTheme.colorScheme.surfaceVariant)
    val uiState = viewModal.uiState.collectAsState().value
    val appEvents = viewModal.appEvents
    val context = LocalContext.current
    val focusRequester = remember { mutableStateOf(FocusRequester()) }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        viewModal.onUriChange(uri)
    }



    LaunchedEffect(key1 = appEvents) {
        appEvents.collectLatest { event ->
            when (event) {
                is AppEvents.Navigate -> Unit
                is AppEvents.ShowToast -> {
                    Toast.makeText(context, event.message, Toast.LENGTH_LONG).show()
                }

                is AppEvents.ShowSnackBar -> Unit
                is AppEvents.PopBackStack -> {
                    onPopBack(event)
                }
            }
        }
    }

    Scaffold(
        topBar = {
            RSTopAppBar(
                title = stringResource(id = R.string.add_post_top_bar),
                navigationIcon = {
                    if (uiState.uri != null) {
                        IconButton(onClick = { viewModal.onUriChange(uri = null) }) {
                            Icon(
                                painter = painterResource(id = R.drawable.cancel),
                                contentDescription = stringResource(id = R.string.cancel_btn),
                                modifier = modifier
                                    .size(dimensionResource(id = R.dimen.rs_icon_size_extra_small))
                            )
                        }
                    } else {
                        IconButton(
                            onClick = {
                                viewModal.onBackCancelClick()
                                viewModal.onPostCaptionChange(postCaption = "")
                            }
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.back_arrow_icon),
                                contentDescription = stringResource(id = R.string.back_button_content_description),
                                modifier = modifier
                                    .size(dimensionResource(id = R.dimen.rs_icon_size_small))
                            )
                        }
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            launcher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                        }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.gallery_icon),
                            contentDescription = stringResource(id = R.string.back_button_content_description),
                            modifier = modifier
                                .size(dimensionResource(id = R.dimen.rs_icon_size_small))
                        )
                    }
                },
                backgroundColor = MaterialTheme.colorScheme.surfaceVariant
            )
        },
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            AddPostContents(
                uiState = uiState,
                onPostImageChange = viewModal::onPostCaptionChange,
                onPostClick = {
                    viewModal.addPost(
                        postCaption = uiState.postCaption,
                        uri = uiState.uri,
                        privacyStatus = uiState.privacyStatus
                    )
                },
                modifier = modifier,
                context = context,
                onScreenClicked = {
                    focusRequester.value.requestFocus()
                },
                focusRequester = focusRequester
            )
        }
    }
}