package com.example.androidfundamental.model

data class EventResponse(
    val error: Boolean,
    val message: String,
    val listEvents: List<Event>
)