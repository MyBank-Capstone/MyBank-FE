package com.example.mybank.ui.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.mybank.data.UserPreferencesManager
import com.example.mybank.data.api.ApiConfig
import com.example.mybank.data.models.FeatureClickRequest
import com.example.mybank.data.models.MenuFeature
import com.example.mybank.data.models.ReasonData
import com.example.mybank.data.models.Recommendation
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
    private val _balance = MutableStateFlow(0.0)
    val balance: StateFlow<Double> = _balance

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

    fun fetchAccountInfo() {
        viewModelScope.launch {
            try {
                val response = ApiConfig.accountService.getAccounts()
                if (response.isSuccessful) {
                    val accounts = response.body()?.data

                    // CCTV 1: Cek apakah data list-nya masuk atau kosong
                    android.util.Log.d("DEBUG_SALDO", "List Account: $accounts")

                    if (!accounts.isNullOrEmpty()) {
                        val mainAccount = accounts[0]

                        // CCTV 2: Cek nominal saldonya
                        android.util.Log.d("DEBUG_SALDO", "Nominal Saldo: ${mainAccount.balance}")

                        _balance.value = mainAccount.balance ?: 0.0
                        prefsManager.accountNumber = mainAccount.accountNumber ?: ""
                    } else {
                        android.util.Log.e("DEBUG_SALDO", "List account kosong [] ! Berarti user ini belum punya rekening.")
                    }
                } else {
                    // CCTV 3: Cek kalau API nolak (misal 401 Unauthorized)
                    val errorBody = response.errorBody()?.string()
                    android.util.Log.e("DEBUG_SALDO", "Error API: ${response.code()} - $errorBody")
                }
            } catch (e: Exception) {
                android.util.Log.e("DEBUG_SALDO", "Gagal fetch, mungkin koneksi atau model JSON salah", e)
            }
        }
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