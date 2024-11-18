package com.example.androidfundamental.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query


@Dao
interface FavoriteEventDao {
    @Query("SELECT * FROM favorite_events")
    suspend fun getAllFavorites(): List<FavoriteEvent>

    @Insert
    suspend fun insertFavorite(favoriteEvent: FavoriteEvent)

    @Delete
    suspend fun deleteFavorite(favoriteEvent: FavoriteEvent)

    @Query("SELECT * FROM favorite_events WHERE eventId = :eventId LIMIT 1")
    suspend fun getFavoriteById(eventId: String): FavoriteEvent?
}




