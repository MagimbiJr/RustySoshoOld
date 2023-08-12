package dev.rustybite.rustysosho.presentation.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import dev.rustybite.rustysosho.R

@Composable
fun RSBottomNav(
    navItems: List<BottomNavScreen>,
    onClick: (String) -> Unit,
    currentRoute: String?,
    modifier: Modifier
) {


    Row(
        modifier = modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        navItems.forEach { item ->
            RSBottomNavItem(
                selected = currentRoute == item.route,
                onClick = onClick,
                item = item
            )
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
            .border(
                width = dimensionResource(id = R.dimen.rs_border_width_small),
                color = if (selected) MaterialTheme.colorScheme.primary else Color.Transparent,
                shape = CircleShape
            )
            .clip(CircleShape)
            .background(color = backgroundColor)
            .clickable { (onClick(item.route)) }
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.rs_padding_extra_small)),
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .padding(
                    horizontal = dimensionResource(id = R.dimen.rs_padding_larger),
                    vertical = dimensionResource(id = R.dimen.rs_padding_small)
                )
        ) {
            Icon(
                imageVector = item.icon,
                contentDescription = item.name,
                modifier = modifier,
                tint = contentColor
            )
            AnimatedVisibility(visible = selected) {
                Text(text = item.name, color = contentColor)
            }
        }
    }
}