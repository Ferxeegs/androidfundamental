package com.example.androidfundamental.dao

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_events")
data class FavoriteEvent(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val eventId: Int,
    val name: String,
    val summary: String?,
    val imageLogo: String?,
    val beginTime: String?,
    val endTime: String?
)
