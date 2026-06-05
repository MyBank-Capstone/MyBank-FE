package com.example.mybank.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mybank.data.api.ApiConfig
import com.example.mybank.data.models.Transaction
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// State untuk mengatur tampilan UI Mutasi
sealed class HistoryUiState {
    object Loading : HistoryUiState()
    data class Success(val transactions: List<Transaction>) : HistoryUiState()
    object Empty : HistoryUiState()
    data class Error(val message: String) : HistoryUiState()
}

class HistoryViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<HistoryUiState>(HistoryUiState.Loading)
    val uiState: StateFlow<HistoryUiState> = _uiState

    init {
        fetchTransactions()
    }

    fun fetchTransactions() {
        viewModelScope.launch {
            _uiState.value = HistoryUiState.Loading
            try {
                val response = ApiConfig.transactionService.getTransactions()
                if (response.isSuccessful) {
                    val data = response.body()?.data
                    if (data.isNullOrEmpty()) {
                        _uiState.value = HistoryUiState.Empty
                    } else {
                        _uiState.value = HistoryUiState.Success(data)
                    }
                } else {
                    _uiState.value = HistoryUiState.Error("Gagal mengambil data: ${response.code()}")
                }
            } catch (e: Exception) {
                _uiState.value = HistoryUiState.Error("Periksa koneksi internet Anda.")
            }
        }
    }
}