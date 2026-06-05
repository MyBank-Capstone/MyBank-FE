package com.example.mybank.data.api

import com.example.mybank.data.models.*
import retrofit2.Response
import retrofit2.http.*

interface TransactionApiService {

    @GET("transactions")
    suspend fun getTransactions(): Response<TransactionListResponse>

    @POST("transactions")
    suspend fun createTransaction(
        @Body request: TransactionRequest
    ): Response<TransactionDetailResponse>

    @GET("transactions/summary")
    suspend fun getTransactionSummary(): Response<TransactionSummaryResponse>

    @GET("transactions/{id}")
    suspend fun getTransactionDetail(
        @Path("id") transactionId: Int
    ): Response<TransactionDetailResponse>
}