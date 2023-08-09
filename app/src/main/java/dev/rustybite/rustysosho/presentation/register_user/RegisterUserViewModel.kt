package dev.rustybite.rustysosho.presentation.register_user

import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.rustybite.rustysosho.R
import dev.rustybite.rustysosho.data.repository.UserRepository
import dev.rustybite.rustysosho.utils.AppEvents
import dev.rustybite.rustysosho.utils.Resource
import dev.rustybite.rustysosho.utils.ResourceProvider
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime

@RequiresApi(Build.VERSION_CODES.O)
class RegisterUserViewModel(
    private val repository: UserRepository,
    private val resProvider: ResourceProvider
) : ViewModel() {
    private val _uiState = MutableStateFlow(RegisterUserUiState(dateOfBirth = resProvider.getString(R.string.select_birth_date)))
    val uiState = _uiState.asStateFlow()
    private val _appEvents = Channel<AppEvents>()
    val appEvents = _appEvents.receiveAsFlow()

    fun registerUser(
        name: String,
        gender: String,
        birthDate: String,
        username: String? = null,
        uri: Uri? = null,
    ) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(loading = true)
            repository.registerUser(
                name = name, username = username, uri = uri, birthDate = birthDate, gender = gender
            ).collectLatest { response ->
                when(response) {
                    is Resource.Success -> {
                        _uiState.value = _uiState.value.copy(
                            loading = false
                        )
                        _appEvents.send(AppEvents.ShowToast(response.data?.message ?: ""))
                        _appEvents.send(AppEvents.Navigate("home"))
                    }
                    is Resource.Failure -> {
                        _uiState.value = _uiState.value.copy(
                            loading = true,
                            errorMessage = response.message ?: ""
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
    fun onNameChange(name: String) {
        _uiState.value = _uiState.value.copy(
            name = name
        )
    }

    fun onGenderChange(gender: String) {
        _uiState.value = _uiState.value.copy(
            gender = gender
        )
    }

    fun onUsernameChange(username: String) {
        _uiState.value = _uiState.value.copy(
            username = username
        )
    }

    fun onUriChange(uri: Uri?) {
        _uiState.value = _uiState.value.copy(
            uri = uri
        )
    }
    fun onDateOfBirthChange(date: String) {
        _uiState.value = _uiState.value.copy(
            dateOfBirth = date
        )
    }
}