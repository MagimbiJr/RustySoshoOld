package dev.rustybite.rustysosho.presentation.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import dev.rustybite.rustysosho.R
import dev.rustybite.rustysosho.presentation.navigation.BottomNavScreen

@Composable
fun RSBottomNav(
    navItems: List<BottomNavScreen>,
    onClick: (String) -> Unit,
    currentRoute: String?,
    modifier: Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(dimensionResource(id = R.dimen.rs_bottom_app_bar_height)),
        color = MaterialTheme.colorScheme.surfaceVariant
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround,
        ) {
            navItems.forEach { item ->
                RSBottomNavItem(
                    item = item,
                    onClick = { onClick(item.route) },
                    selected = item.route == currentRoute
                )
            }
        }
    }
}


@Composable
private fun RSBottomNavItem(
    item: BottomNavScreen,
    onClick: (String) -> Unit,
    selected: Boolean,
    modifier: Modifier = Modifier,
    backgroundColor: Color = if (selected) MaterialTheme.colorScheme.primary.copy(.1f) else Color.Transparent,
    contentColor: Color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
) {
    Box(
        modifier = modifier
            .clickable { (onClick(item.route)) }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement =  Arrangement.spacedBy(dimensionResource(id = R.dimen.rs_padding_extra_small)),
            modifier = modifier
                .padding(
                    horizontal = dimensionResource(id = R.dimen.rs_padding_larger),
                    vertical = dimensionResource(id = R.dimen.rs_padding_small)
                )
        ) {
            Icon(
                painter = painterResource(id = item.icon),
                contentDescription = item.name,
                modifier = modifier
                    .size(dimensionResource(id = R.dimen.rs_icon_size_small)),
                tint = contentColor
            )
            Text(
                text = item.name,
                color = contentColor,
                style = MaterialTheme.typography.labelMedium
            )
        }
    }
}