package com.example.mybank.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mybank.data.api.ApiConfig
import com.example.mybank.data.models.TransactionRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// State untuk memantau proses pengiriman
sealed class TransferUiState {
    object Idle : TransferUiState()
    object Loading : TransferUiState()
    object Success : TransferUiState()
    data class Error(val message: String) : TransferUiState()
}

class TransactionViewModel : ViewModel() {
    private val _uiState = MutableStateFlow<TransferUiState>(TransferUiState.Idle)
    val uiState: StateFlow<TransferUiState> = _uiState

    fun submitTransaction(request: TransactionRequest) {
        viewModelScope.launch {
            _uiState.value = TransferUiState.Loading
            try {
                // Tembak API POST
                val response = ApiConfig.transactionService.createTransaction(request)

                if (response.isSuccessful) {
                    _uiState.value = TransferUiState.Success
                } else {
                    // KUNCI: Ekstrak isi pesan error dari backend
                    val errorBody = response.errorBody()?.string() ?: "Unknown error"
                    android.util.Log.e("API_TRANSFER_ERROR", "Code: ${response.code()}, Body: $errorBody")

                    _uiState.value = TransferUiState.Error("Transaksi gagal: Cek Logcat!")
                }
            } catch (e: Exception) {
                _uiState.value = TransferUiState.Error("Periksa koneksi internet Anda.")
            }
        }
    }

    // Fungsi untuk mereset state setelah pop-up sukses/error ditutup
    fun resetState() {
        _uiState.value = TransferUiState.Idle
    }
}