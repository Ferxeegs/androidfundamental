package com.example.androidfundamental.repository

import androidx.lifecycle.LiveData
import com.example.androidfundamental.dao.FavoriteEvent
import com.example.androidfundamental.dao.FavoriteEventDao

class FavoriteRepository(private val favoriteEventDao: FavoriteEventDao) {
    val allFavorites: LiveData<List<FavoriteEvent>> = favoriteEventDao.getAllFavorites()

    suspend fun addFavorite(event: FavoriteEvent) = favoriteEventDao.addFavorite(event)

    suspend fun removeFavorite(event: FavoriteEvent) = favoriteEventDao.removeFavorite(event)
}
