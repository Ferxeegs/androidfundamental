package com.example.androidfundamental.api

import com.example.androidfundamental.model.EventResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface EventApiService {
    @GET("events")
    fun getEvents(@Query("active") active: Int): Call<EventResponse>
}