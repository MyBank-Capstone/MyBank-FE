package com.example.mybank.data.api.services

import com.example.mybank.data.models.AuthResponse
import com.example.mybank.data.models.PersonalizationState
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.PATCH

interface UserApiService {

    @PATCH("users/me/personalization")
    suspend fun setPersonalization(
        @Body request: PersonalizationState
    ): Response<AuthResponse>
}