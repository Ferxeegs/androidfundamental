package com.example.androidfundamental.repository

import android.content.Context
import android.util.Log
import com.example.androidfundamental.dao.FavoriteEvent
import com.example.androidfundamental.dao.FavoriteEventDao
import com.example.androidfundamental.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FavoriteRepository(context: Context) {

    private val favoriteEventDao: FavoriteEventDao

    init {
        val database = AppDatabase.getDatabase(context) // Akses instance database
        favoriteEventDao = database.favoriteEventDao() // Dapatkan DAO
    }

    // Menggunakan coroutine untuk menjalankan query di background thread
    suspend fun getAllFavorites(): List<FavoriteEvent> {
        return withContext(Dispatchers.IO) {
            favoriteEventDao.getAllFavorites() // Panggil DAO untuk mendapatkan semua favorit di background thread
        }
    }

    // Fungsi untuk menambahkan event favorit
    suspend fun addFavorite(favoriteEvent: FavoriteEvent) {
        withContext(Dispatchers.IO) {
            favoriteEventDao.insertFavorite(favoriteEvent) // Tambahkan ke database di background thread
            Log.d("FavoriteRepository", "Added favorite: ${favoriteEvent.name}")
        }
    }

    // Fungsi untuk menghapus event favorit
    suspend fun removeFavorite(favoriteEvent: FavoriteEvent) {
        withContext(Dispatchers.IO) {
            favoriteEventDao.deleteFavorite(favoriteEvent)
            Log.d("FavoriteRepository", "Removed favorite: ${favoriteEvent.name}")
        }
    }

    // Fungsi untuk memeriksa apakah event sudah ada di favorit
    suspend fun isFavorite(eventId: String): Boolean {
        return withContext(Dispatchers.IO) {
            val favoriteEvent = favoriteEventDao.getFavoriteById(eventId)
            favoriteEvent != null
        }
    }
}
