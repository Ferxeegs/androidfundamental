package com.example.androidfundamental.dao

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_events")
data class FavoriteEvent(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val eventId: String,
    val name: String,
    val summary: String?,
    val description: String?,
    val imageLogo: String?,
    val mediaCover: String?,
    val category: String?,
    val ownerName: String?,
    val cityName: String?,
    val quota: Int,
    val registrants: Int,
    val beginTime: String?,
    val endTime: String?,
    val link: String?
)
