package dev.rustybite.rustysosho.presentation.authentication

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import dev.rustybite.rustysosho.R
import dev.rustybite.rustysosho.presentation.ui.components.PhoneNumberField
import dev.rustybite.rustysosho.presentation.ui.components.RSPrimaryButton

@Composable
fun EnterNumberContent(
    modifier: Modifier,
    onPhoneNumberChange: (String) -> Unit,
    onNavigateToCodeSelection: () -> Unit,
    onVerifyNumber: () -> Unit,
    uiState: AuthUiState,
    scrollState: ScrollState
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(12.dp)
            .verticalScroll(scrollState),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        Icon(
            painter = painterResource(id = R.drawable.rustysosho_logo_black),
            contentDescription = stringResource(id = R.string.logo_content_description),
            modifier = modifier
                .size(dimensionResource(id = R.dimen.rs_logo_size_large)),
            tint = MaterialTheme.colorScheme.primary
        )

        Text(
            text = stringResource(id = R.string.lets_verify_text),
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = modifier.height(dimensionResource(id = R.dimen.rs_margin_large)))
        Row(
            modifier = modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Card(
                modifier = modifier
                    .fillMaxWidth()
                    .weight(.35f)
                    .height(50.dp)
                    .clip(RoundedCornerShape(dimensionResource(id = R.dimen.rs_shape_extra_small)))
                    .clickable { onNavigateToCodeSelection() },
                shape = RoundedCornerShape(dimensionResource(id = R.dimen.rs_shape_extra_small)),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(.4f)
                )
            ) {
                Column(
                    modifier = modifier
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(text = uiState.countryCode)
                }
            }
            Spacer(modifier = modifier.width(dimensionResource(id = R.dimen.rs_padding_small)))
            PhoneNumberField(
                value = uiState.phoneNumber,
                onValueChange = onPhoneNumberChange,
                modifier = modifier
                    .fillMaxWidth()
                    .weight(.65f),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
            )
        }
        if (uiState.errorMessage.isNotBlank()) {
            Spacer(modifier = modifier.height(dimensionResource(id = R.dimen.rs_margin_large)))
            Text(text = uiState.errorMessage, color = MaterialTheme.colorScheme.error)
        }
        Spacer(
            modifier = modifier
                .height(dimensionResource(id = R.dimen.rs_margin_large))

        )
        if (uiState.loading) {
            LinearProgressIndicator()
            Spacer(
                modifier = modifier
                    .height(dimensionResource(id = R.dimen.rs_margin_large))

            )
        }
        RSPrimaryButton(
            action = stringResource(id = R.string.verify_number),
            onClick = onVerifyNumber,
            enabled = !uiState.loading && uiState.phoneNumber.isNotBlank()
                    && uiState.phoneNumber.length == 9
                    && uiState.countryCode != "Code"
        )
    }
}