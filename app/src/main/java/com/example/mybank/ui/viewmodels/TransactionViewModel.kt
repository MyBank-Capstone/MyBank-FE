package com.example.mybank.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mybank.data.api.ApiConfig
import com.example.mybank.data.models.Transaction
import com.example.mybank.data.models.TransactionRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// 1. STATE UNTUK PROSES TRANSFER / TOPUP (POST)
sealed class TransferUiState {
    object Idle : TransferUiState()
    object Loading : TransferUiState()
    object Success : TransferUiState()
    data class Error(val message: String) : TransferUiState()
}

// 2. STATE UNTUK MENAMPILKAN HALAMAN MUTASI (GET)
sealed class HistoryUiState {
    object Loading : HistoryUiState()
    data class Success(val transactions: List<Transaction>) : HistoryUiState()
    object Empty : HistoryUiState()
    data class Error(val message: String) : HistoryUiState()
}

class TransactionViewModel : ViewModel() {

    // State untuk memantau proses pembuatan transaksi baru
    private val _transferState = MutableStateFlow<TransferUiState>(TransferUiState.Idle)
    val transferState: StateFlow<TransferUiState> = _transferState

    // State untuk mengatur tampilan layar riwayat mutasi
    private val _historyState = MutableStateFlow<HistoryUiState>(HistoryUiState.Loading)
    val historyState: StateFlow<HistoryUiState> = _historyState

    // State list mentah untuk kebutuhan pencarian data di halaman TransactionDetailScreen
    private val _transactionsList = MutableStateFlow<List<Transaction>>(emptyList())
    val transactions: StateFlow<List<Transaction>> = _transactionsList

    init {
        // Otomatis memuat riwayat mutasi saat pertama kali aplikasi memuat ViewModel ini
        fetchTransactions()
    }

    // --- FUNGSI 1: RIWAYAT MUTASI (GET /transactions) ---
    fun fetchTransactions() {
        viewModelScope.launch {
            _historyState.value = HistoryUiState.Loading
            try {
                val response = ApiConfig.transactionService.getTransactions()
                if (response.isSuccessful) {
                    val data = response.body()?.data
                    if (data.isNullOrEmpty()) {
                        _historyState.value = HistoryUiState.Empty
                        _transactionsList.value = emptyList()
                    } else {
                        _historyState.value = HistoryUiState.Success(data)
                        _transactionsList.value = data // Disimpan ke cache lokal untuk detail screen
                    }
                } else {
                    _historyState.value = HistoryUiState.Error("Gagal mengambil data: ${response.code()}")
                }
            } catch (e: Exception) {
                _historyState.value = HistoryUiState.Error("Periksa koneksi internet Anda.")
            }
        }
    }

    // --- FUNGSI 2: TRANSAKSI BARU (POST /transactions) ---
    fun submitTransaction(request: TransactionRequest) {
        viewModelScope.launch {
            _transferState.value = TransferUiState.Loading
            try {
                val response = ApiConfig.transactionService.createTransaction(request)

                if (response.isSuccessful) {
                    _transferState.value = TransferUiState.Success

                    // KUNCI UTAMA: Otomatis perbarui list riwayat mutasi begitu transaksi berhasil tembus!
                    fetchTransactions()
                } else {
                    val errorBody = response.errorBody()?.string() ?: "Unknown error"
                    android.util.Log.e("API_TRANSFER_ERROR", "Code: ${response.code()}, Body: $errorBody")

                    _transferState.value = TransferUiState.Error("Transaksi gagal: Cek Logcat!")
                }
            } catch (e: Exception) {
                _transferState.value = TransferUiState.Error("Periksa koneksi internet Anda.")
            }
        }
    }

    // Fungsi reset state pasca pop-up transaksi ditutup
    fun resetTransferState() {
        _transferState.value = TransferUiState.Idle
    }
}