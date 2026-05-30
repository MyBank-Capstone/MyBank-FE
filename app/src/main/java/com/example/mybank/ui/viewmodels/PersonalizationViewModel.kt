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

class PersonalizationViewModel(application: Application) : AndroidViewModel(application) {

    private val prefsManager = UserPreferencesManager(application)

    // 1. State untuk status sakelar AI (ON/OFF)
    private val _isAiActive = MutableStateFlow(prefsManager.isAiPersonalizationEnabled)
    val isAiActive: StateFlow<Boolean> = _isAiActive

    // 2. State untuk mengontrol kemunculan pop-up di Home
    private val _showConsentDialog = MutableStateFlow(!prefsManager.hasAnsweredAiConsent)
    val showConsentDialog: StateFlow<Boolean> = _showConsentDialog

    // Fungsi untuk memperbarui status AI (dipanggil dari Pop-up maupun layar Pengaturan)
    fun updatePersonalizationStatus(isEnabled: Boolean) {
        viewModelScope.launch {
            // Update UI secara instan agar terasa responsif
            _isAiActive.value = isEnabled
            _showConsentDialog.value = false

            // Simpan ke penyimpanan lokal HP
            prefsManager.isAiPersonalizationEnabled = isEnabled
            prefsManager.hasAnsweredAiConsent = true

            try {
                // Tembak ke API Backend
                val request = PersonalizationState(enabled = isEnabled)
                val response = ApiConfig.userService.setPersonalization(request)

                if (response.isSuccessful) {
                    android.util.Log.d("API_CONSENT", "Data AI berhasil dikirim: $isEnabled")
                } else {
                    android.util.Log.e("API_CONSENT", "Server Error: ${response.code()}")
                }
            } catch (e: Exception) {
                android.util.Log.e("API_CONSENT", "Koneksi Gagal: ${e.message}")
            }
        }
    }
}