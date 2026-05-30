package com.example.mybank.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.mybank.data.UserPreferencesManager
import com.example.mybank.data.api.ApiConfig
import com.example.mybank.data.models.PersonalizationState // Pastikan nama model ini sesuai di kodemu
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    private val prefsManager = UserPreferencesManager(application)

    // 1. STATE NAMA USER
    private val _userName = MutableStateFlow(prefsManager.userName ?: "User")
    val userName: StateFlow<String> = _userName

    fun refreshName() {
        _userName.value = prefsManager.userName ?: "User"
    }

    // 2. STATE AI ACTIVE (Digabung ke sini agar UI Home mudah membaca datanya)
    private val _isAiActive = MutableStateFlow(prefsManager.isAiPersonalizationEnabled)
    val isAiActive: StateFlow<Boolean> = _isAiActive

    // 3. STATE KEMUNCULAN POP-UP CONSENT
    private val _showConsentDialog = MutableStateFlow(false)
    val showConsentDialog: StateFlow<Boolean> = _showConsentDialog

    init {
        ApiConfig.token = prefsManager.accessToken ?: ""

        checkConsentStatus()
    }

    private fun checkConsentStatus() {
        // KUNCI: Cek brankas. Kalau belum pernah jawab (false), maka dialog muncul (!false = true)
        _showConsentDialog.value = !prefsManager.hasAnsweredAiConsent
    }

    fun submitPersonalizationConsent(isEnabled: Boolean) {
        viewModelScope.launch {
            // 1. Update UI secara instan
            _showConsentDialog.value = false
            _isAiActive.value = isEnabled

            // 2. SIMPAN KE BRANKAS
            prefsManager.hasAnsweredAiConsent = true
            prefsManager.isAiPersonalizationEnabled = isEnabled

            try {
                // 3. Tembak API
                val request = PersonalizationState(enabled = isEnabled)
                val response = ApiConfig.userService.setPersonalization(request)

                if (response.isSuccessful) {
                    android.util.Log.d("API_CONSENT_SUCCESS", "Data berhasil dikirim")
                } else {
                    val errorBody = response.errorBody()?.string()
                    android.util.Log.e("API_CONSENT_ERROR", "Code: ${response.code()}")
                    android.util.Log.e("API_CONSENT_ERROR", "Server: $errorBody")
                }
            } catch (e: Exception) {
                android.util.Log.e("API_CONSENT_CRASH", "Gagal mengirim data: ${e.message}")
                e.printStackTrace()
            }
        }
    }
}