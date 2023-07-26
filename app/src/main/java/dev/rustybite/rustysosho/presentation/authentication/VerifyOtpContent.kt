package dev.rustybite.rustysosho.presentation.authentication

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import dev.rustybite.rustysosho.R
import dev.rustybite.rustysosho.presentation.ui.components.RSOtpTextField
import dev.rustybite.rustysosho.presentation.ui.components.RSPrimaryButton

@Composable
fun VerifyOtpContent(
    modifier: Modifier,
    onOtpChange: (String) -> Unit,
    onVerifyOtp: () -> Unit,
    uiState: AuthUiState,
    scrollState: ScrollState,
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
            text = stringResource(id = R.string.enter_otp_text),
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = modifier.height(dimensionResource(id = R.dimen.rs_margin_large)))
        RSOtpTextField(
            value = uiState.otp,
            onValueChange = onOtpChange,
            modifier = modifier,
        )
        if (uiState.errorMessage.isNotBlank()) {
            Spacer(modifier = modifier.height(dimensionResource(id = R.dimen.rs_margin_large)))
            Text(text = uiState.errorMessage, color = MaterialTheme.colorScheme.error)
        }
        if (uiState.successMessage.isNotBlank()) {
            Spacer(modifier = modifier.height(dimensionResource(id = R.dimen.rs_margin_large)))
            Text(text = uiState.successMessage)
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
            action = stringResource(id = R.string.verify_otp),
            onClick = onVerifyOtp,
            enabled = !uiState.loading && uiState.otp.isNotBlank()
                    && uiState.otp.length == 6
        )
    }
}