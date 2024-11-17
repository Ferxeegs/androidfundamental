package com.example.androidfundamental.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FavoriteEventDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFavorite(event: FavoriteEvent)

    @Delete
    suspend fun removeFavorite(event: FavoriteEvent): Int // Mengembalikan jumlah baris yang dihapus

    @Query("SELECT * FROM favorite_events")
    fun getAllFavorites(): LiveData<List<FavoriteEvent>>
}



