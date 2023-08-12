package dev.rustybite.rustysosho.presentation.add_post_screen

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.rustybite.rustysosho.data.repository.PostsRepository
import dev.rustybite.rustysosho.utils.AppEvents
import dev.rustybite.rustysosho.utils.Resource
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class AddPostViewModal(
    private val repository: PostsRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(AddPostUiState())
    val uiState = _uiState.asStateFlow()
    private val _appState = Channel<AppEvents>()
    val appEvents = _appState.receiveAsFlow()


    init {
        viewModelScope.launch {
            repository.getUser().collectLatest { response ->
                when(response) {
                    is Resource.Success -> {
                        _uiState.value = _uiState.value.copy(
                            user = response.data
                        )
                    }
                    is Resource.Failure -> {
                        _uiState.value = _uiState.value.copy(
                            errorMessage = response.message ?: ""
                        )
                    }
                    is Resource.Loading -> Unit
                }
            }
        }
    }

    fun addPost(
        postCaption: String,
        uri: Uri?,
        privacyStatus: String
    ) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(loading = true)
            repository.addPost(
                postCaption = postCaption,
                uri = uri,
                privacyStatus = privacyStatus
            ).collectLatest { response ->
                when(response) {
                    is Resource.Success -> {
                        _uiState.value = _uiState.value.copy(
                            loading = false
                        )
                        _appState.send(AppEvents.ShowToast(response.data?.message ?: ""))
                        _appState.send(AppEvents.PopBackStack)
                    }
                    is Resource.Failure -> {
                        _uiState.value = _uiState.value.copy(
                            loading = false,
                            errorMessage = response.message ?: ""
                        )
                    }
                    is Resource.Loading -> {
                        _uiState.value = _uiState.value.copy(loading = true)
                    }
                }
            }
        }
    }

    fun onUriChange(uri: Uri?) {
        _uiState.value = _uiState.value.copy(
            uri = uri
        )
    }

    fun onPostCaptionChange(postCaption: String) {
        _uiState.value = _uiState.value.copy(
            postCaption = postCaption
        )
    }

    fun onBackCancelClick() {
        viewModelScope.launch {
            _appState.send(AppEvents.PopBackStack)
        }
    }

    fun onPrivacyStatusChange(privacyStatus: String) {
        _uiState.value = _uiState.value.copy(
            privacyStatus = privacyStatus
        )
    }
}