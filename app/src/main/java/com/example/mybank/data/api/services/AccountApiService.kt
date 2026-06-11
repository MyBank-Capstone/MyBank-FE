package com.example.mybank.data.api
import com.example.mybank.data.models.AccountResponse
import retrofit2.Response
import retrofit2.http.GET

interface AccountApiService {
    @GET("accounts")
    suspend fun getAccounts(): Response<AccountResponse>
}