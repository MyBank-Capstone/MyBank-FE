package com.example.mybank.data.models
import com.google.gson.annotations.SerializedName

data class AccountData(
    @SerializedName("account_number") val accountNumber: String?,
    @SerializedName("account_type") val accountType: String?,
    @SerializedName("balance") val balance: Double?
)

data class AccountResponse(
    @SerializedName("success") val success: Boolean?,
    @SerializedName("message") val message: String?,
    @SerializedName("data") val data: List<AccountData>?
)