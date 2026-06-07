package com.example.mybank.data.models

import com.google.gson.annotations.SerializedName

data class Recommendation(
    @SerializedName("id") val id: Int,
    @SerializedName("type") val type: String?,
    @SerializedName("title") val title: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("reason") val reason: String?,
    @SerializedName("priority") val priority: Int?,
    @SerializedName("image_url") val imageUrl: String?
)

data class RecommendationResponse(
    @SerializedName("success") val success: Boolean?,
    @SerializedName("message") val message: String?,
    @SerializedName("data") val data: List<Recommendation>?
)

data class ReasonData(
    @SerializedName("consent_status") val consentStatus: Boolean?,
    @SerializedName("data_used") val dataUsed: String?,
    @SerializedName("reason") val reason: String?,
    @SerializedName("recommendation_id") val recommendationId: Int?,
    @SerializedName("title") val title: String?
)

data class RecommendationReasonResponse(
    @SerializedName("success") val success: Boolean?,
    @SerializedName("message") val message: String?,
    @SerializedName("data") val data: ReasonData?
)