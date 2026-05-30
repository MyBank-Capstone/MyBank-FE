package com.example.mybank.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.mybank.data.UserPreferencesManager
import com.example.mybank.data.api.ApiConfig
import com.example.mybank.data.models.PersonalizationState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// Menggunakan AndroidViewModel agar bisa mengakses 'application context' untuk SharedPreferences
class PersonalizationViewModel(application: Application) : AndroidViewModel(application) {

    private val prefsManager = UserPreferencesManager(application)

    // 1. State untuk status sakelar AI (ON/OFF)
    private val _isAiActive = MutableStateFlow(prefsManager.isAiPersonalizationEnabled)
    val isAiActive: StateFlow<Boolean> = _isAiActive

    // 2. State untuk mengontrol kemunculan pop-up di Home
    private val _showConsentDialog = MutableStateFlow(!prefsManager.hasAnsweredAiConsent)
    val showConsentDialog: StateFlow<Boolean> = _showConsentDialog

    // Fungsi untuk memperbarui status AI (dipanggil dari Pop-up maupun halaman Settings)
    fun updatePersonalizationStatus(isEnabled: Boolean) {
        viewModelScope.launch {
            // Update UI secara instan agar terasa responsif
            _isAiActive.value = isEnabled
            _showConsentDialog.value = false

            // 1. Simpan ke penyimpanan lokal HP
            prefsManager.isAiPersonalizationEnabled = isEnabled
            prefsManager.hasAnsweredAiConsent = true

            try {
                // 2. Tembak ke API Backend
                val request = PersonalizationState(enabled = isEnabled)
                val response = ApiConfig.userService.setPersonalization(request)

                if (!response.isSuccessful) {
                    // Jika API gagal, idealnya lakukan rollback atau tampilkan error
                }
            } catch (e: Exception) {
                // Handle jika tidak ada jaringan internet
            }
        }
    }
}