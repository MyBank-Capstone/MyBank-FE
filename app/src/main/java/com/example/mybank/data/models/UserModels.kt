package com.example.mybank.data.models

import com.google.gson.annotations.SerializedName

class UserModels {
}

data class PersonalizationState(
    @SerializedName("enabled")
    val enabled: Boolean
)

data class User(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String?,
    @SerializedName("email")
    val email: String,
    @SerializedName("phone")
    val phone: String?,
    @SerializedName("date_of_birth")
    val dateOfBirth: String?,
    @SerializedName("occupation")
    val occupation: String?,
    @SerializedName("segment")
    val segment: String?,
    @SerializedName("is_active")
    val isActive: Boolean,
    @SerializedName("is_personalization_enabled")
    val isPersonalizationEnabled: Boolean,
    @SerializedName("created_at")
    val createdAt: String?,
    @SerializedName("gender")
    val gender: String?,
    @SerializedName("last_login_at")
    val lastLoginAt: String?,
    @SerializedName("marital_status")
    val maritalStatus: String?,
    @SerializedName("monthly_income_range")
    val monthlyIncomeRange: String?
)

data class Meta(
    @SerializedName("limit")
    val limit: Int,
    @SerializedName("page")
    val page: Int,
    @SerializedName("total_items")
    val totalItems: Int,
    @SerializedName("total_pages")
    val totalPages: Int
)

// GET /users/me
data class UserProfileResponse(
    @SerializedName("success") val success: Boolean?,
    @SerializedName("message") val message: String?,
    @SerializedName("data") val data: User?,
    @SerializedName("meta") val meta: Meta?
)