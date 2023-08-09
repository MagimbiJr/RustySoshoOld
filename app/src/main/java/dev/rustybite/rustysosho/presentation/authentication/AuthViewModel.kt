package dev.rustybite.rustysosho.presentation.authentication

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.rustybite.rustysosho.R
import dev.rustybite.rustysosho.data.repository.AuthRepository
import dev.rustybite.rustysosho.utils.AppEvents
import dev.rustybite.rustysosho.utils.Resource
import dev.rustybite.rustysosho.utils.ResourceProvider
import dev.rustybite.rustysosho.utils.codes
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    //private val authenticateUseCase: AuthenticateUseCase,
    //private val verifyTokenUseCase: VerifyTokenUseCase,
    private val repository: AuthRepository,
    private val resProvider: ResourceProvider
) : ViewModel() {
    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState = _uiState.asStateFlow()
    private val _appEvents = Channel<AppEvents>()
    val appEvents = _appEvents.receiveAsFlow()

    //private val signUpState = repository.signUpState
    private val message = repository.message
    private val otp = repository.verificationOtp
    var isUserStored: Boolean = false
    //private val route = if (isUserStored) "home" else "register_user"

    init {
        viewModelScope.launch {
            repository.getUserId().collectLatest { userId ->
                _uiState.value = _uiState.value.copy(userId = userId)
            }
        }
    }
    init {
        viewModelScope.launch {
            repository.isUserStored().collectLatest { isUserStored ->
                _uiState.value = _uiState.value.copy(isUserStored = isUserStored)
                Log.d("TaNa", "isStored in init: $isUserStored")
            }
        }
    }

    fun onPhoneNumberChange(phoneNumber: String) {
        _uiState.value = _uiState.value.copy(
            phoneNumber = phoneNumber
        )
    }

    fun onOtpChange(otp: String) {
        _uiState.value = _uiState.value.copy(
            otp = otp
        )
    }

    fun onCountryCodeChange(code: String) {
        _uiState.value = _uiState.value.copy(
            countryCode = code
        )
    }

    val route = if (_uiState.value.isUserStored) "home" else "register_user"
    fun authenticate(phoneNumber: String) {
        viewModelScope.launch {
            repository.authenticate(phoneNumber)
            message.collectLatest { message ->
                _uiState.value = _uiState.value.copy(loading = message.isBlank())
                when (message) {
                    "Verification complete" -> {
                        _appEvents.send(AppEvents.Navigate("home"))
                    }

                    "Verification failed. Try again" -> {
                        _uiState.value = _uiState.value.copy(errorMessage = message)
                    }

                    "Too many request. Try again after some time" -> {
                        _uiState.value = _uiState.value.copy(errorMessage = message)
                    }

                    "Code sent" -> {
                        otp.collectLatest { _ ->
                            // _uiState.value = _uiState.value.copy(otp = otp)
                            if (message.isNotBlank()) {
                                _appEvents.send(AppEvents.Navigate("enter_code_screen"))
                            }
                        }

                    }

                    else -> {
                        _uiState.value = _uiState.value.copy(errorMessage = message)
                    }
                }
            }
        }
    }

    fun onVerifyOtp(code: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                loading = true
            )
            repository.onVerifyOtp(code).collectLatest { response ->
                when (response) {
                    is Resource.Success -> {
                        _uiState.value = _uiState.value.copy(
                            loading = false
                        )
                        _appEvents.send(AppEvents.ShowToast(response.data?.message ?: ""))
                        _appEvents.send(AppEvents.Navigate("home"))
                    }

                    is Resource.Failure -> {
                        _uiState.value = _uiState.value.copy(
                            loading = false,
                            errorMessage = response.message
                                ?: resProvider.getString(R.string.unknown_error)
                        )
                    }

                    is Resource.Loading -> {
                        _uiState.value = _uiState.value.copy(
                            loading = true
                        )
                    }
                }
            }
        }
    }

    fun onQueryChange(query: String) {
        _uiState.value = _uiState.value.copy(query = query)
        val result = codes.filter { code ->
            code.name.lowercase().contains(query)
        }
        _uiState.value = _uiState.value.copy(searchResult = result)
    }

    fun onNavigateToCodeSelection() {
        viewModelScope.launch {
            _appEvents.send(AppEvents.Navigate("select_code_screen"))
        }
    }

    fun onNavigateToSearchCode() {
        viewModelScope.launch {
            _appEvents.send(AppEvents.Navigate("search_code_screen"))
        }
    }

    fun onNavigateToHome() {
        viewModelScope.launch {
            _appEvents.send(AppEvents.Navigate("verify_number_screen"))
            _uiState.value = _uiState.value.copy(query = "")
        }
    }

    fun onPopBackFromSelectCode() {
        viewModelScope.launch {
            _appEvents.send(AppEvents.PopBackStack)
        }
    }

    fun onPopBackFromSearchCode() {
        viewModelScope.launch {
            _appEvents.send(AppEvents.PopBackStack)
        }
    }

    fun onRemoveZero(): String {
        return _uiState.value.phoneNumber.drop(0)
    }
}