package com.example.mybank.ui.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.mybank.data.UserPreferencesManager
import com.example.mybank.data.api.ApiConfig
import com.example.mybank.data.models.FeatureClickRequest
import com.example.mybank.data.models.MenuFeature
import com.example.mybank.ui.screens.allMyBankFeatures
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    private val prefsManager = UserPreferencesManager(application)

    // 1. STATE NAMA USER
    private val _userName = MutableStateFlow(prefsManager.userName ?: "User")
    val userName: StateFlow<String> = _userName
    private val _dynamicMenus = MutableStateFlow<List<MenuFeature>>(emptyList())
    val dynamicMenus: StateFlow<List<MenuFeature>> = _dynamicMenus

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

    fun fetchTopFeatures() {
        viewModelScope.launch {
            try {
                val response = ApiConfig.featureService.getFeatureClicks()
                if (response.isSuccessful) {
                    val clickData = response.body()?.data ?: emptyList()

                    // 1. Urutkan berdasarkan klik terbanyak, ambil 4 teratas
                    val topFeatureNames = clickData
                        .sortedByDescending { it.clickCount ?: 0 }
                        .take(4)
                        .mapNotNull { it.featureName }

                    // 2. Cocokkan string nama fitur dari BE dengan daftar ikon lokal kita
                    val matchedMenus = topFeatureNames.mapNotNull { name ->
                        allMyBankFeatures.find { it.title.equals(name, ignoreCase = true) }
                    }

                    _dynamicMenus.value = matchedMenus
                }
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Gagal mengambil fitur favorit", e)
            }
        }
    }

    fun trackFeatureClick(featureName: String) {
        viewModelScope.launch {
            try {
                // Tembak API secara diam-diam (Fire and Forget)
                ApiConfig.featureService.trackFeatureClick(FeatureClickRequest(featureName))

                // Opsional: Refresh daftar menu setiap ada klik baru
                fetchTopFeatures()
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Gagal track klik", e)
            }
        }
    }
}