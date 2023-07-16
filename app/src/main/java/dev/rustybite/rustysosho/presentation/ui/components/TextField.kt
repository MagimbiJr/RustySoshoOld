package dev.rustybite.rustysosho.presentation.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.rustybite.rustysosho.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PhoneNumberField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,

    ) {
    val colors = TextFieldDefaults.textFieldColors(
        cursorColor = MaterialTheme.colorScheme.primary,
        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(.4f),
        focusedIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent
    )

    TextField(
        value = value,
        onValueChange = onValueChange,
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        placeholder = {
            Text(text = stringResource(id = R.string.enter_phone_placeholder))
        },
        colors = colors,
        modifier = modifier
            .fillMaxWidth()
            .heightIn(max = 50.dp)
            .clip(RoundedCornerShape(dimensionResource(id = R.dimen.rs_shape_extra_small))),
        maxLines = 1,

        )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RSSearchBar(
    value: String,
    onValueChange: (String) -> Unit,
    onBackClicked: () -> Unit,
    modifier: Modifier,
    focusRequester: FocusRequester,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
) {
    val colors = TextFieldDefaults.textFieldColors(
        cursorColor = MaterialTheme.colorScheme.onSurfaceVariant,
        containerColor = MaterialTheme.colorScheme.surfaceVariant,
        focusedIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent
    )

    TextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = {
            Text(text = stringResource(id = R.string.search_country_placeholder))
        },
        leadingIcon = {
            IconButton(onClick = onBackClicked) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = stringResource(id = R.string.back_button_content_description)
                )
            }
        },
        colors = colors,
        modifier = modifier
            .fillMaxWidth()
            .heightIn(max = 50.dp)
            .focusRequester(focusRequester),
        maxLines = 1,
        keyboardActions = keyboardActions,
        keyboardOptions = keyboardOptions
    )
}



