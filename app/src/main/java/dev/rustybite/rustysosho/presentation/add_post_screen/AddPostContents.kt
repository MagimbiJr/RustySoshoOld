package dev.rustybite.rustysosho.presentation.add_post_screen

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import coil.compose.AsyncImage
import coil.request.ImageRequest
import dev.rustybite.rustysosho.R
import dev.rustybite.rustysosho.presentation.ui.components.RSNoStyleTextField
import dev.rustybite.rustysosho.presentation.ui.components.RSPrimaryButton
import org.jetbrains.annotations.Nls.Capitalization

@Composable
fun AddPostContents(
    uiState: AddPostUiState,
    onPostImageChange: (String) -> Unit,
    onPostClick: () -> Unit,
    modifier: Modifier,
    context: Context,
    onScreenClicked: () -> Unit,
    focusRequester: MutableState<FocusRequester>
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = dimensionResource(id = R.dimen.rs_padding_medium))
            .clickable { onScreenClicked() },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.rs_padding_small))
        ) {
            uiState.user?.let { user ->
                AsyncImage(
                    model = ImageRequest.Builder(context).data(user.userPhoto),
                    contentDescription = "",
                    modifier = modifier
                        .size(dimensionResource(id = R.dimen.rs_icon_size_medium))
                        .clip(CircleShape)
                )
            }
            RSNoStyleTextField(
                value = uiState.postCaption,
                onValueChange = onPostImageChange,
                modifier = modifier,
                placeholder = stringResource(id = R.string.say_something),
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences
                ),
                focusRequester = focusRequester
            )
        }
        AnimatedVisibility(visible = uiState.uri != null) {
            Spacer(modifier = modifier.height(dimensionResource(id = R.dimen.rs_margin_large)))
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(uiState.uri.toString())
                    .build(),
                contentDescription = stringResource(id = R.string.image_to_be_uploaded),
                modifier = modifier
                    .fillMaxWidth()
                    .fillMaxHeight(.5f)
                    .clip(RoundedCornerShape(dimensionResource(id = R.dimen.rs_shape_small))),
                contentScale = ContentScale.Crop,
            )
        }
        Spacer(modifier = modifier.height(dimensionResource(id = R.dimen.rs_margin_large)))
        AnimatedVisibility(uiState.loading) {
            LinearProgressIndicator()
        }
        Spacer(modifier = modifier.height(dimensionResource(id = R.dimen.rs_margin_large)))
        AnimatedVisibility(visible = uiState.uri != null || uiState.postCaption.isNotBlank()) {
            Spacer(modifier = modifier.height(dimensionResource(id = R.dimen.rs_margin_large)))
            RSPrimaryButton(
                action = stringResource(id = R.string.post),
                onClick = onPostClick,
                enabled = !uiState.loading,
                fraction = .7f
            )
        }
    }
}