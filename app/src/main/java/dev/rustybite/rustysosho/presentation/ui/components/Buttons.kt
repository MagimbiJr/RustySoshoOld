package dev.rustybite.rustysosho.presentation.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import dev.rustybite.rustysosho.R

@Composable
fun RSPrimaryButton(
    action: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean,
    fraction: Float = .5f,
    backgroundColor: Color = MaterialTheme.colorScheme.primary,
    contentColor: Color = MaterialTheme.colorScheme.onPrimary
) {
    val colors = ButtonDefaults.buttonColors(
        containerColor = backgroundColor,
        contentColor = contentColor
    )

    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth(fraction),
        enabled = enabled,
        colors = colors
    ) {
        Text(
            text = action,
            modifier = modifier
                .padding(
                    vertical = dimensionResource(id = R.dimen.rs_padding_small)
                )
        )
    }
}

@Composable
fun RSPickerButton(
    action: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    backgroundColor: Color = MaterialTheme.colorScheme.surfaceVariant.copy(.4f),
    contentColor: Color = MaterialTheme.colorScheme.onBackground.copy(.8f)
) {
    Surface(
        modifier = modifier
            .height(dimensionResource(id = R.dimen.rs_button_height))
            .clip(RoundedCornerShape(dimensionResource(id = R.dimen.rs_shape_small)))
            .clickable(enabled = enabled) { onClick() },
        shape = RoundedCornerShape(dimensionResource(id = R.dimen.rs_shape_small)),
        color = backgroundColor,
        contentColor = contentColor
    ) {
        Row(
            modifier = modifier
                .padding(horizontal = dimensionResource(id = R.dimen.rs_padding_large)),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = action,
                modifier = modifier
                    .fillMaxWidth(),
            )
        }
    }
}

@Composable
fun RustySoshoFab(
    icon: ImageVector,
    text: String? = null,
    onClick: () -> Unit,
    expanded: Boolean = false,
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colorScheme.primary,
    contentColor: Color = MaterialTheme.colorScheme.onPrimary
) {
    Card(
        modifier = modifier
            .clip(RoundedCornerShape(dimensionResource(id = R.dimen.rs_shape_small)))
            .clickable { onClick() },
        shape = RoundedCornerShape(dimensionResource(id = R.dimen.rs_shape_small)),
        colors = CardDefaults.cardColors(
            containerColor = backgroundColor,
            contentColor = contentColor
        )
    ) {
        Row(
            modifier = modifier
                .padding(dimensionResource(id = R.dimen.rs_padding_large)),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(
                if (text != null) dimensionResource(id = R.dimen.rs_padding_small)
                else dimensionResource(id = R.dimen.rs_padding_none)
            )
        ) {
            AnimatedVisibility(visible = expanded) {
                text?.let {
                    Text(text = it, style = MaterialTheme.typography.labelLarge)
                }
            }
            Icon(
                imageVector = icon,
                contentDescription = stringResource(id = R.string.fab_icon_content_description)
            )
        }
    }
}