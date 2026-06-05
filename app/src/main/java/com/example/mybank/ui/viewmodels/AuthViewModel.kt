package com.example.mybank.ui.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.mybank.data.UserPreferencesManager
import com.example.mybank.data.api.ApiConfig // Sesuaikan dengan nama file konfigurasimu
import com.example.mybank.data.models.AuthResponse
import com.example.mybank.data.models.LoginRequest
import com.example.mybank.data.models.RegisterRequest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// 1. Definisikan kemungkinan status (State) layar
sealed class AuthState {
    object Idle : AuthState() // Posisi diam (belum ngapa-ngapain)
    object Loading : AuthState() // Saat tombol login ditekan (API berjalan)
    data class Success(val response: AuthResponse) : AuthState() // Kalau login berhasil
    data class Error(val message: String) : AuthState() // Kalau password salah/internet mati
}

class AuthViewModel(application: Application) : AndroidViewModel(application) {

    private val prefsManager = UserPreferencesManager(application)

    // 2. StateFlow untuk diamati oleh UI (LoginScreen)
    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState

    // 3. Fungsi yang dipanggil saat tombol Login diklik
    fun login(email: String, password: String) {
        viewModelScope.launch {
            // Ubah status UI jadi Loading (Bisa untuk memunculkan animasi muter)
            _authState.value = AuthState.Loading

            try {
                // Siapkan data yang mau dikirim
                val request = LoginRequest(email, password)

                // Tembak API (Ini berjalan di background, tidak bikin lag)
                val response = ApiConfig.authService.login(request)

                // Cek balasan dari backend
                if (response.isSuccessful && response.body() != null) {
                    val authResponse = response.body()!!

                    if (authResponse.success == true) {
                        // Simpan ID User ke SharedPreferences
                        authResponse.data?.user?.id?.let {
                            prefsManager.userId = it
                        }

                        // Simpan Nama User ke SharedPreferences
                        authResponse.data?.user?.name?.let {
                            prefsManager.userName = it
                        }

                        // TODO: Nanti simpan access_token ke SharedPreferences di sini
                        authResponse.data?.accessToken?.let { token ->
                            prefsManager.accessToken = token
                            ApiConfig.token = token
                        }

                        _authState.value = AuthState.Success(authResponse)
                    } else {
                        // Kalau format benar tapi backend bilang gagal (misal user diblokir)
                        _authState.value = AuthState.Error(authResponse.message)
                    }
                } else {
                    // Kalau HTTP Error (misal 401 Unauthorized / Password Salah)
                    _authState.value = AuthState.Error("Email atau Password salah.")
                }
            } catch (e: Exception) {
                // Tambahkan baris ini untuk mencetak error asli ke jendela Logcat di Android Studio
                e.printStackTrace()
                android.util.Log.e("API_ERROR", "Penyebab asli: ${e.message}")

                _authState.value = AuthState.Error("Periksa koneksi internet Anda.")
            }
        }
    }

    // Update parameter untuk menerima semua data dari UI
    fun register(
        name: String,
        email: String,
        phone: String,
        dateOfBirth: String,
        occupation: String,
        password: String
    ) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading

            try {
                // Masukkan data asli ke dalam cetakan Request
                val request = RegisterRequest(
                    name = name,
                    email = email,
                    password = password,
                    phone = phone,
                    dateOfBirth = dateOfBirth,
                    occupation = occupation
                )

                val response = ApiConfig.authService.register(request)

                if (response.isSuccessful && response.body() != null) {
                    val authResponse = response.body()!!

                    if (authResponse.success == true) {
                        // Simpan ID User ke SharedPreferences
                        authResponse.data?.user?.id?.let {
                            prefsManager.userId = it
                        }

                        // Simpan Nama User ke SharedPreferences
                        authResponse.data?.user?.name?.let {
                            prefsManager.userName = it
                        }

                        _authState.value = AuthState.Success(authResponse)
                    } else {
                        _authState.value = AuthState.Error(authResponse.message ?: "Registrasi gagal")
                    }
                } else {
                    // KUNCI DEBUGGING: Baca balasan error asli dari backend
                    val errorBodyString = response.errorBody()?.string()

                    // Cetak ke jendela Logcat dengan warna merah
                    android.util.Log.e("API_ERROR_BODY", "HTTP Code: ${response.code()}")
                    android.util.Log.e("API_ERROR_BODY", "Alasan Backend: $errorBodyString")

                    // Munculkan di Toast sementara agar kamu bisa langsung baca di layar HP
                    _authState.value = AuthState.Error("Ditolak BE: $errorBodyString")
                }
            } catch (e: Exception) {
                // Tambahkan baris ini untuk mencetak error asli ke jendela Logcat di Android Studio
                e.printStackTrace()
                android.util.Log.e("API_ERROR", "Penyebab asli: ${e.message}")

                _authState.value = AuthState.Error("Periksa koneksi internet Anda.")
            }
        }
    }

    // Fungsi untuk mereset state (penting saat pindah halaman agar toast tidak muncul terus)
    fun resetState() {
        _authState.value = AuthState.Idle
    }
}