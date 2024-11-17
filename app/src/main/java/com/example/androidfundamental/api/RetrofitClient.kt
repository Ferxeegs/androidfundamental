package com.example.androidfundamental.api

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "https://event-api.dicoding.dev/"

    private val client = OkHttpClient.Builder().build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val eventApiService: EventApiService = retrofit.create(EventApiService::class.java)
    val apiService: ApiService = retrofit.create(ApiService::class.java)
}