package com.example.mybank.data.api.services

import com.example.mybank.data.models.AuthResponse
import com.example.mybank.data.models.LoginRequest
import com.example.mybank.data.models.RegisterRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {

    @POST("login")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<AuthResponse>

    @POST("register")
    suspend fun register(
        @Body request: RegisterRequest
    ): Response<AuthResponse>
}