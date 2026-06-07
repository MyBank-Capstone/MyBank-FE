package com.example.mybank.ui.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.mybank.data.api.ApiConfig
import com.example.mybank.data.models.ReasonData
import com.example.mybank.data.models.Recommendation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RecommendationViewModel(application: Application) : AndroidViewModel(application) {
    private val _recommendations = MutableStateFlow<List<Recommendation>>(emptyList())
    val recommendations: StateFlow<List<Recommendation>> = _recommendations
    private val _aiReason = MutableStateFlow<ReasonData?>(null)
    val aiReason: StateFlow<ReasonData?> = _aiReason


    fun fetchRecommendations() {
        viewModelScope.launch {
            try {
                val response = ApiConfig.recommendationService.getRecommendations()
                if (response.isSuccessful) {
                    val data = response.body()?.data ?: emptyList()
                    _recommendations.value = data.sortedBy { it.priority ?: 99 }
                }
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Gagal mengambil rekomendasi", e)
            }
        }
    }

    fun trackPromoClick(recommendationId: Int) {
        viewModelScope.launch {
            try {
                ApiConfig.recommendationService.trackRecommendationClick(recommendationId)
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Gagal track promo click", e)
            }
        }
    }

    fun fetchPromoReason(recommendationId: Int) {
        viewModelScope.launch {
            try {
                val response = ApiConfig.recommendationService.getRecommendationReason(recommendationId)
                if (response.isSuccessful) {
                    _aiReason.value = response.body()?.data
                }
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Gagal mengambil alasan AI", e)
            }
        }
    }

    // Bersihkan pop-up setelah ditutup
    fun clearPromoReason() {
        _aiReason.value = null
    }
}