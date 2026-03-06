package com.falahw.bantucepat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(private val dataStoreManager: DataStoreManager) : ViewModel() {

    val licenseKey: StateFlow<String?> = dataStoreManager.licenseKey
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val isActivated: StateFlow<Boolean> = dataStoreManager.isActivated
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    fun saveLicense(key: String) {
        viewModelScope.launch {
            dataStoreManager.saveLicense(key)
        }
    }

    fun activateApp() {
        viewModelScope.launch {
            dataStoreManager.setActivated(true)
        }
    }
}
