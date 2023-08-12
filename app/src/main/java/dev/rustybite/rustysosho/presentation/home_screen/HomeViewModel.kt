package dev.rustybite.rustysosho.presentation.home_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.rustybite.rustysosho.utils.AppEvents
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    private val _appEvents = Channel<AppEvents>()
    val appEvents = _appEvents.receiveAsFlow()

    fun onFabClick() {
        viewModelScope.launch {
            _appEvents.send(AppEvents.Navigate("add_post_screen"))
        }
    }
}