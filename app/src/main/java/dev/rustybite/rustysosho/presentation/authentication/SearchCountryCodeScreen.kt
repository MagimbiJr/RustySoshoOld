package dev.rustybite.rustysosho.presentation.authentication

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import com.google.accompanist.systemuicontroller.SystemUiController
import dev.rustybite.rustysosho.R
import dev.rustybite.rustysosho.presentation.ui.components.RSSearchBar
import dev.rustybite.rustysosho.utils.AppEvents
import dev.rustybite.rustysosho.utils.codes
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchCountryCodeScreen(
    onNavigate: (AppEvents.Navigate) -> Unit,
    onPopBackClicked: (AppEvents.PopBackStack) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AuthViewModel,
    systemUiController: SystemUiController
) {
    systemUiController.setStatusBarColor(MaterialTheme.colorScheme.surfaceVariant)
    val appEvents = viewModel.appEvents
    val focusRequest = remember { FocusRequester() }
    val uiState = viewModel.uiState.collectAsState().value

    LaunchedEffect(key1 = uiState.query) {
        if (uiState.query.isBlank()) {
            focusRequest.requestFocus()
        }
    }

    LaunchedEffect(key1 = appEvents) {
        appEvents.collectLatest { event ->
            when (event) {
                is AppEvents.Navigate -> {
                    onNavigate(event)
                }
                is AppEvents.ShowSnackBar -> Unit
                is AppEvents.ShowToast -> Unit
                is AppEvents.PopBackStack -> {
                    onPopBackClicked(event)
                }
            }
        }
    }

    Scaffold(
        topBar = {
            RSSearchBar(
                value = uiState.query,
                onValueChange = viewModel::onQueryChange,
                onBackClicked = { viewModel.onPopBackFromSearchCode() },
                modifier = modifier,
                focusRequester = focusRequest,
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = modifier
                .padding(paddingValues)
                .padding(
                    horizontal = dimensionResource(id = R.dimen.rs_padding_small),
                    vertical = dimensionResource(id = R.dimen.rs_padding_medium)
                )
        ) {
            val items = if (uiState.query.isBlank()) {
                codes
            } else {
                uiState.searchResult
            }
            items(items) { code ->
                Row(
                    modifier = modifier
                        .fillMaxWidth()
                        .clickable {
                            viewModel.onCountryCodeChange(code.code)
                            viewModel.onNavigateToHome()
                        }
                        .padding(
                            horizontal = dimensionResource(id = R.dimen.rs_padding_medium),
                            vertical = dimensionResource(id = R.dimen.rs_padding_small)
                        ),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.rs_padding_medium))
                ) {
                    Column(
                        modifier = modifier
                            .weight(.1f)
                    ) {
                        Text(text = code.isoCode)
                    }
                    Column(
                        modifier = modifier
                            .weight(.7f)
                    ) {
                        Text(
                            text = code.name,
                        )
                    }
                    Column(
                        modifier = modifier
                            .weight(.2f),
                        horizontalAlignment = Alignment.End
                    ) {
                        Text(text = code.code)
                    }
                }
            }
        }
    }
}
