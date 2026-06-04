package com.example.mybank.data.models
import com.google.gson.annotations.SerializedName

data class FeatureClickRequest(
    @SerializedName("feature_name") val featureName: String
)

data class FeatureClickData(
    @SerializedName("feature_name") val featureName: String?,
    @SerializedName("click_count") val clickCount: Int?,
    @SerializedName("last_clicked") val lastClicked: String?
)

data class FeatureClicksResponse(
    @SerializedName("success") val success: Boolean?,
    @SerializedName("message") val message: String?,
    @SerializedName("data") val data: List<FeatureClickData>?,
    @SerializedName("meta") val meta: Meta? // Pakai class Meta yang sudah ada
)

// Response simpel untuk POST yang sukses
data class SimpleResponse(
    @SerializedName("success") val success: Boolean?,
    @SerializedName("message") val message: String?
)