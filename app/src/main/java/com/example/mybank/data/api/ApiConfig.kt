package com.example.mybank.data.api

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import com.example.mybank.data.api.services.*
import com.example.mybank.data.models.PersonalizationState
import com.example.mybank.ui.components.TransactionItem

object ApiConfig {

    // BASE URL API
    private const val BASE_URL = "https://mybank-be.fly.dev/api/v1/"

    // Variabel untuk nampung token saat aplikasi jalan
    var token: String = ""

    private fun getRetrofit(): Retrofit {
        // Interceptor
        val authInterceptor = Interceptor { chain ->
            val req = chain.request()
            val requestHeaders = req.newBuilder()
                .apply {
                    if (token.isNotEmpty()) {
                        addHeader("Authorization", "Bearer $token")
                    }
                }
                .build()
            chain.proceed(requestHeaders)
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
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

    val userService: UserApiService by lazy {
        getRetrofit().create(UserApiService::class.java)
    }

    val transactionService: TransactionApiService by lazy {
        getRetrofit().create(TransactionApiService::class.java)
    }

    val featureService: FeatureApiService by lazy {
        getRetrofit().create(FeatureApiService::class.java)
    }
}