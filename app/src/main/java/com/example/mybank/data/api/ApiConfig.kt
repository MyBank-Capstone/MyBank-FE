package com.example.mybank.data.api

import com.example.mybank.data.api.services.AuthApiService
import com.example.mybank.data.api.services.TransactionApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiConfig {
    // Ganti dengan URL dari Swagger BE kamu
    private const val BASE_URL = "https://api.mybank-staging.com/v1/"

    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    // 2. Pembuatan masing-masing service secara modular (Lazy initialization)
    val authService: AuthApiService by lazy {
        getRetrofit().create(AuthApiService::class.java)
    }

    val transactionService: TransactionApiService by lazy {
        getRetrofit().create(TransactionApiService::class.java)
    }
}