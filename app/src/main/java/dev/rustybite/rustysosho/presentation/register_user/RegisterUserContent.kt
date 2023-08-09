package dev.rustybite.rustysosho.presentation.register_user

import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.DatePickerDefaults
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import dev.rustybite.rustysosho.R
import dev.rustybite.rustysosho.presentation.ui.components.CameraView
import dev.rustybite.rustysosho.presentation.ui.components.RSOptionMenu
import dev.rustybite.rustysosho.presentation.ui.components.RSPickerButton
import dev.rustybite.rustysosho.presentation.ui.components.RSPrimaryButton
import dev.rustybite.rustysosho.presentation.ui.components.RSTextField
import dev.rustybite.rustysosho.presentation.ui_utils.requestCameraPermission
import dev.rustybite.rustysosho.utils.Gender
import java.io.File
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.concurrent.Executor

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun RegisterUserContent(
    uiState: RegisterUserUiState,
    onNameChange: (String) -> Unit,
    onGenderChange: (String) -> Unit,
    onUserNameChange: (String) -> Unit,
    onDateOfBirthChange: (String) -> Unit,
    onUriChange: (Uri?) -> Unit,
    onCreateClick: () -> Unit,
    shouldShowCamera: MutableState<Boolean>,
    isPermissionGranted: MutableState<Boolean>,
    requestPermissionLauncher: ActivityResultLauncher<String>,
    outputDirectory: File,
    executor: Executor,
    modifier: Modifier,
    scrollState: ScrollState,
    focusManager: FocusManager
) {
    val dialogState = rememberMaterialDialogState()
    val showUploadMenu = remember { mutableStateOf(false) }
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        onUriChange(uri)
    }

    MaterialDialog(
        dialogState = dialogState,
        backgroundColor = MaterialTheme.colorScheme.background,
        buttons = {
            positiveButton(stringResource(id = R.string.positive_dialog_btn))
            negativeButton(stringResource(id = R.string.negative_dialog_btn))
        }
    ) {
        datepicker(
            colors = DatePickerDefaults.colors(
                headerBackgroundColor = MaterialTheme.colorScheme.primary,
                headerTextColor = MaterialTheme.colorScheme.onPrimary,
                dateActiveBackgroundColor = MaterialTheme.colorScheme.primary,
                dateActiveTextColor = MaterialTheme.colorScheme.onPrimary,
                dateInactiveTextColor = MaterialTheme.colorScheme.onBackground,
                calendarHeaderTextColor = MaterialTheme.colorScheme.onBackground
            )
        ) { localDate ->
            val formattedDate = localDate.format(
                DateTimeFormatter.ofLocalizedDate(
                    FormatStyle.MEDIUM
                )
            )
            onDateOfBirthChange(formattedDate)
        }
    }

    if (shouldShowCamera.value) {
        CameraView(
            outputDirectory = outputDirectory,
            executor = executor,
            onImageCaptured = { uri ->
                onUriChange(uri)
                shouldShowCamera.value = false
            },
            onError = {}
        )
    }

    if (!shouldShowCamera.value) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(dimensionResource(id = R.dimen.rs_margin_medium))
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = modifier.height(dimensionResource(id = R.dimen.rs_margin_extra_large)))
            RSImageView(
                showMenu = showUploadMenu,
                uri = uiState.uri,
                onClick = { showUploadMenu.value = true },
                onOpenCameraClicked = {
                    if (isPermissionGranted.value) {
                        shouldShowCamera.value = true
                    } else {
                        requestCameraPermission(
                            context,
                            isPermissionGranted,
                            requestPermissionLauncher
                        )
                    }
                    shouldShowCamera.value = true
                },
                onOpenGalleryClicked = {
                    launcher.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                },
                onRemovePhotoClicked = {
                    onUriChange(null)
                },
                modifier = modifier
            )
            Spacer(modifier = modifier.height(dimensionResource(id = R.dimen.rs_margin_extra_large)))
            RSTextField(
                value = uiState.name,
                onValueChange = onNameChange,
                modifier = modifier,
                placeholder = stringResource(id = R.string.enter_name_placeholder),
                enabled = !uiState.loading,
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Words,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                )
            )
            Spacer(modifier = modifier.height(dimensionResource(id = R.dimen.rs_margin_medium)))
            RSTextField(
                value = uiState.username,
                onValueChange = onUserNameChange,
                modifier = modifier,
                placeholder = stringResource(id = R.string.enter_username_placeholder),
                enabled = !uiState.loading,
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Words,
                    imeAction = ImeAction.Next
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Down) }
                )
            )
            Spacer(modifier = modifier.height(dimensionResource(id = R.dimen.rs_margin_medium)))
            RSPickerButton(
                action = uiState.dateOfBirth,
                onClick = { dialogState.show() }
            )
            Spacer(modifier = modifier.height(dimensionResource(id = R.dimen.rs_margin_medium)))
            val selectedValue = remember { mutableStateOf("") }
            RSRadioButtons(
                selectedValue = selectedValue,
                onMaleSelected = {
                    selectedValue.value = Gender.Male.name
                    onGenderChange(Gender.Male.name)
                },
                onFemaleSelected = {
                    selectedValue.value = Gender.Female.name
                    onGenderChange(Gender.Female.name)
                },
                modifier = modifier
            )
            Spacer(modifier = modifier.height(dimensionResource(id = R.dimen.rs_margin_large)))
            if (uiState.loading) {
                LinearProgressIndicator()
                Spacer(
                    modifier = modifier
                        .height(dimensionResource(id = R.dimen.rs_margin_large))

                )
            }
            if (uiState.errorMessage.isNotBlank()) {
                Text(
                    text = uiState.errorMessage,
                    modifier = modifier
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.error
                )
                Spacer(
                    modifier = modifier
                        .height(dimensionResource(id = R.dimen.rs_margin_large))
                )
            }
            RSPrimaryButton(
                action = stringResource(id = R.string.register_user),
                onClick = onCreateClick,
                enabled = uiState.name.isNotBlank()
                        && uiState.gender.isNotBlank()
                        && !uiState.loading,
            )
        }
    }
}

@Composable
fun RSRadioButtons(
    selectedValue: MutableState<String>,
    onMaleSelected: () -> Unit,
    onFemaleSelected: () -> Unit,
    modifier: Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth(),
    ) {
        Text(
            text = stringResource(id = R.string.seletc_gender_label),
            color = MaterialTheme.colorScheme.onBackground.copy(.8f)
        )

        Row(
            modifier = modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                selected = selectedValue.value == Gender.Male.name,
                onClick = onMaleSelected
            )
            Text(
                text = Gender.Male.name,
                color = MaterialTheme.colorScheme.onBackground.copy(.8f)
            )
        }
        Row(
            modifier = modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                selected = selectedValue.value == Gender.Female.name,
                onClick = onFemaleSelected
            )
            Text(
                text = Gender.Female.name,
                color = MaterialTheme.colorScheme.onBackground.copy(.8f)
            )
        }
    }
}

@Composable
fun RSImageView(
    showMenu: MutableState<Boolean>,
    uri: Uri?,
    onClick: () -> Unit,
    onOpenCameraClicked: () -> Unit,
    onOpenGalleryClicked: () -> Unit,
    onRemovePhotoClicked: () -> Unit,
    modifier: Modifier
) {
    val cameraMenuItems = listOf(
        stringResource(id = R.string.upload_camera_photo),
        stringResource(id = R.string.upload_gallery_photo),
        stringResource(id = R.string.remove_photo)
    )
    Column() {
        if (uri == null) {
            NoUriImageView(modifier = modifier, onClick = onClick)
        } else {
            ImageView(uri = uri, onClick = onClick, modifier = modifier)
        }
        Spacer(modifier = modifier.height(dimensionResource(id = R.dimen.rs_margin_small)))
        RSOptionMenu(
            items = cameraMenuItems,
            expanded = showMenu,
            onMenuItemClicked = { index ->
                when (index) {
                    0 -> {
                        onOpenCameraClicked()
                        showMenu.value = false
                    }

                    1 -> {
                        onOpenGalleryClicked()
                        showMenu.value = false
                    }

                    2 -> {
                        onRemovePhotoClicked()
                        showMenu.value = false
                    }
                }
            },
            onDismissRequest = { showMenu.value = false },
            modifier = modifier
        )
    }
}

@Composable
private fun ImageView(uri: Uri, onClick: () -> Unit, modifier: Modifier) {
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current).data(uri.toString()).build(),
        contentDescription = stringResource(id = R.string.image_profile_to_be_uploaded),
        modifier = modifier
            .size(dimensionResource(id = R.dimen.rs_profile_to_upload_size))
            .clip(CircleShape)
            .clickable { onClick() },
        contentScale = ContentScale.FillWidth
    )
}

@Composable
private fun NoUriImageView(modifier: Modifier, onClick: () -> Unit) {
    Box {
        Box(
            modifier = modifier
                .clip(CircleShape)
                .border(
                    width = dimensionResource(id = R.dimen.rs_border_width_small),
                    color = MaterialTheme.colorScheme.primary,
                    shape = CircleShape
                )
                .clickable { onClick() }
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = stringResource(id = R.string.no_user_icon_content_description),
                modifier = modifier
                    .size(dimensionResource(id = R.dimen.rs_icon_size_medium))
                    .padding(dimensionResource(id = R.dimen.rs_padding_medium)),
                tint = MaterialTheme.colorScheme.onBackground
            )
        }
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = stringResource(id = R.string.add_icon_content_description),
            modifier = modifier
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary)
                .align(Alignment.BottomEnd),
            tint = MaterialTheme.colorScheme.onPrimary
        )
    }
}