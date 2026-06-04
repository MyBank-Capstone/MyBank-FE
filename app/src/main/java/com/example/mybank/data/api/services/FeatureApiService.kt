package com.example.mybank.data.api.services
import com.example.mybank.data.models.*
import retrofit2.Response
import retrofit2.http.*

interface FeatureApiService {
    @POST("features/click")
    suspend fun trackFeatureClick(@Body request: FeatureClickRequest): Response<SimpleResponse>

    @GET("features/clicks")
    suspend fun getFeatureClicks(): Response<FeatureClicksResponse>
}