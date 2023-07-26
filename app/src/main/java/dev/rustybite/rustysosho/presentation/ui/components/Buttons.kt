package dev.rustybite.rustysosho.presentation.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import dev.rustybite.rustysosho.R

@Composable
fun RSPrimaryButton(
    action: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean,
    backgroundColor: Color = MaterialTheme.colorScheme.primary,
    contentColor: Color = MaterialTheme.colorScheme.onPrimary
) {
    val colors = ButtonDefaults.buttonColors(
        containerColor = backgroundColor,
        contentColor = contentColor
    )

    Button(
        onClick  = onClick,
        modifier = modifier
            .fillMaxWidth(.5f),
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