package com.example.mybank.data.models

import com.google.gson.annotations.SerializedName

// --- REQUEST MODEL ---
data class TransactionRequest(
    @SerializedName("account_id") val accountId: Int,
    @SerializedName("amount") val amount: Double,
    @SerializedName("type") val type: String, // Misal: "TRANSFER", "TOP_UP"
    @SerializedName("channel") val channel: String = "mobile",
    @SerializedName("destination_account_number") val destinationAccountNumber: String,
    @SerializedName("destination_bank_code") val destinationBankCode: String,
    @SerializedName("destination_name") val destinationName: String,
    @SerializedName("description") val description: String,
    @SerializedName("note") val note: String,
    @SerializedName("merchant_category") val merchantCategory: String,
    @SerializedName("merchant_location") val merchantLocation: String,
    @SerializedName("merchant_name") val merchantName: String,
    @SerializedName("pin") val pin: String
)

// --- OBJEK UTAMA TRANSAKSI ---
// Digunakan di GET list, GET detail, dan POST response
data class Transaction(
    @SerializedName("id") val id: Int,
    @SerializedName("type") val type: String?,
    @SerializedName("amount") val amount: Double?,
    @SerializedName("fee") val fee: Double?,
    @SerializedName("balance_before") val balanceBefore: Double?,
    @SerializedName("balance_after") val balanceAfter: Double?,
    @SerializedName("channel") val channel: String?,
    @SerializedName("status") val status: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("destination_account_number") val destinationAccountNumber: String?,
    @SerializedName("destination_bank_code") val destinationBankCode: String?,
    @SerializedName("destination_name") val destinationName: String?,
    @SerializedName("merchant_name") val merchantName: String?,
    @SerializedName("merchant_category") val merchantCategory: String?,
    @SerializedName("merchant_location") val merchantLocation: String?,
    @SerializedName("note") val note: String?,
    @SerializedName("reference_number") val referenceNumber: String?,
    @SerializedName("is_recommended") val isRecommended: Boolean?,
    @SerializedName("recommendation_id") val recommendationId: Int?,
    @SerializedName("transacted_at") val transactedAt: String?,
    @SerializedName("created_at") val createdAt: String?
)

// --- RESPONSE MODELS ---
data class TransactionListResponse(
    @SerializedName("success") val success: Boolean?,
    @SerializedName("message") val message: String?,
    @SerializedName("data") val data: List<Transaction>?,
    @SerializedName("meta") val meta: Meta? // Menggunakan Meta yang sudah dibuat sebelumnya
)

data class TransactionDetailResponse(
    @SerializedName("success") val success: Boolean?,
    @SerializedName("message") val message: String?,
    @SerializedName("data") val data: Transaction?,
    @SerializedName("meta") val meta: Meta?
)

// --- SUMMARY MODELS ---
data class TransactionSummaryResponse(
    @SerializedName("success") val success: Boolean?,
    @SerializedName("message") val message: String?,
    @SerializedName("data") val data: TransactionSummary?,
    @SerializedName("meta") val meta: Meta?
)

data class TransactionSummary(
    @SerializedName("total_spend") val totalSpend: Double?,
    @SerializedName("total_transactions") val totalTransactions: Int?,
    @SerializedName("fav_category") val favCategory: String?,
    @SerializedName("fav_method") val favMethod: String?,
    @SerializedName("category_breakdown") val categoryBreakdown: List<CategoryBreakdown>?,
    @SerializedName("top_merchants") val topMerchants: List<TopMerchant>?,
    @SerializedName("weekly_comparison") val weeklyComparison: List<WeeklyComparison>?
)

data class CategoryBreakdown(
    @SerializedName("merchant_category") val merchantCategory: String?,
    @SerializedName("total_amount") val totalAmount: Double?,
    @SerializedName("transaction_count") val transactionCount: Int?
)

data class TopMerchant(
    @SerializedName("merchant_category") val merchantCategory: String?,
    @SerializedName("merchant_name") val merchantName: String?,
    @SerializedName("total_amount") val totalAmount: Double?,
    @SerializedName("transaction_count") val transactionCount: Int?
)

data class WeeklyComparison(
    @SerializedName("category") val category: String?,
    @SerializedName("current_week_spend") val currentWeekSpend: Double?,
    @SerializedName("previous_week_spend") val previousWeekSpend: Double?
)