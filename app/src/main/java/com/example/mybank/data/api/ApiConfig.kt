package com.example.mybank.data.api

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import com.example.mybank.data.api.services.*

object ApiConfig {

    private const val BASE_URL = "https://mybank-be.fly.dev/api/v1/"

    private fun getRetrofit(): Retrofit {
        val client = OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS) // nunggu connect
            .readTimeout(60, TimeUnit.SECONDS)    // nunggu balasan data
            .writeTimeout(60, TimeUnit.SECONDS)   // nunggu kiriman data
            .build()

        // 2. Pasang klien tersebut ke dalam Retrofit
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client) // KUNCI: Pasangkan OkHttpClient di sini
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val authService: AuthApiService by lazy {
        getRetrofit().create(AuthApiService::class.java)
    }
}