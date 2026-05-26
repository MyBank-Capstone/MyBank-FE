package com.example.mybank.data.models

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    @SerializedName("email")
    val email: String,
    @SerializedName("password")
    val password: String
)

data class RegisterRequest(
    @SerializedName("name")
    val name: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("password")
    val password: String,
    @SerializedName("phone")
    val phone: String,
    @SerializedName("date_of_birth")
    val dateOfBirth: String,
    @SerializedName("occupation")
    val occupation: String
)

// RESPONSE LOGIN
data class AuthResponse(
    @SerializedName("success")
    val success: Boolean?,
    @SerializedName("data")
    val data: AuthData?,
    @SerializedName("message")
    val message: String,
    @SerializedName("meta")
    val meta: Meta?
)

data class AuthData(
    @SerializedName("access_token")
    val accessToken: String,
    @SerializedName("expires_in")
    val expiresIn: Int,
    @SerializedName("refresh_token")
    val refreshToken: String,
    @SerializedName("user")
    val user: User
)

data class User(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("phone")
    val phone: String,
    @SerializedName("date_of_birth")
    val dateOfBirth: String?, // Diberi tanda '?' (nullable) untuk jaga-jaga jika BE mengirim null
    @SerializedName("occupation")
    val occupation: String?,
    @SerializedName("segment")
    val segment: String?,
    @SerializedName("is_active")
    val isActive: Boolean,
    @SerializedName("is_personalization_enabled")
    val isPersonalizationEnabled: Boolean,
    @SerializedName("created_at")
    val createdAt: String
)

data class Meta(
    @SerializedName("limit")
    val limit: Int,
    @SerializedName("page")
    val page: Int,
    @SerializedName("total_items")
    val totalItems: Int
)
