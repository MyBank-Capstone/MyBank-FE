package com.example.mybank.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.mybank.data.UserPreferencesManager
import com.example.mybank.data.api.ApiConfig
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    private val prefsManager = UserPreferencesManager(application)

    // 1. STATE NAMA USER
    private val _userName = MutableStateFlow(prefsManager.userName ?: "User")
    val userName: StateFlow<String> = _userName

    fun refreshName() {
        _userName.value = prefsManager.userName ?: "User"
    }

    init {
        // Tetap di sini agar saat aplikasi buka, Retrofit langsung pegang token
        ApiConfig.token = prefsManager.accessToken ?: ""
    }

    // Fungsi Logout (Menghapus data brankas)
    fun logout() {
        prefsManager.clearSession()
        ApiConfig.token = ""
    }
}