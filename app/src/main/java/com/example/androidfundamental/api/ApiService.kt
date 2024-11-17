package com.example.androidfundamental.api

import com.example.androidfundamental.model.EventResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("events")
    fun searchEvents(@Query("active") active: Int = -1, @Query("q") keyword: String): Call<EventResponse>
}