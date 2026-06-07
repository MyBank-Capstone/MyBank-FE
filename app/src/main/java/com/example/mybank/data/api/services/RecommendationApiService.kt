package com.example.mybank.data.api.services

import com.example.mybank.data.models.RecommendationReasonResponse
import com.example.mybank.data.models.RecommendationResponse
import com.example.mybank.data.models.SimpleResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface RecommendationApiService {
    @GET("recommendations")
    suspend fun getRecommendations(): Response<RecommendationResponse>

    @POST("recommendations/{id}/click")
    suspend fun trackRecommendationClick(
        @Path("id") recommendationId: Int
    ): Response<SimpleResponse>

    @GET("recommendations/{id}/reason")
    suspend fun getRecommendationReason(
        @Path("id") recommendationId: Int
    ): Response<RecommendationReasonResponse>
}