package dev.rustybite.rustysosho.presentation.authentication

import dev.rustybite.rustysosho.utils.CountryCode

data class AuthUiState(
    val loading: Boolean = false,
    val isUserStored: Boolean = false,
    val userId: String? = null,
    val errorMessage: String = "",
    val successMessage: String = "",
    val phoneNumber: String = "",
    val otp: String = "",
    val countryCode: String = "Code",
    val query: String = "",
    val searchResult: List<CountryCode> = emptyList()
)
