package dev.rustybite.rustysosho.presentation.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import dev.rustybite.rustysosho.R

@Composable
fun RSOptionMenu(
    items: List<String>,
    expanded: MutableState<Boolean>,
    onMenuItemClicked: (Int) -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier
) {
    Box(
        modifier = modifier
            .background(MaterialTheme.colorScheme.surfaceVariant)
    ) {
        DropdownMenu(
            expanded = expanded.value,
            onDismissRequest = onDismissRequest
        ) {
            items.forEachIndexed { index, item ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = item,
                            modifier = modifier
                                .fillMaxWidth()
                                .padding(
                                    horizontal = dimensionResource(id = R.dimen.rs_padding_medium),
                                )
                        )
                    },
                    onClick = { onMenuItemClicked(index) },
                )
            }
        }
    }
}