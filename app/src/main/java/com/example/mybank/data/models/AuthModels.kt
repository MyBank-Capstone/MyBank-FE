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