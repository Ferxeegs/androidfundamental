package com.example.androidfundamental.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.androidfundamental.AppDatabase
import com.example.androidfundamental.dao.FavoriteEvent
import com.example.androidfundamental.repository.FavoriteRepository
import kotlinx.coroutines.launch

class FavoriteViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: FavoriteRepository
    val allFavorites: LiveData<List<FavoriteEvent>>

    init {
        val dao = AppDatabase.getInstance(application).favoriteEventDao()
        repository = FavoriteRepository(dao)
        allFavorites = repository.allFavorites
    }

    fun addFavorite(event: FavoriteEvent) = viewModelScope.launch {
        repository.addFavorite(event)
    }

    fun removeFavorite(event: FavoriteEvent) = viewModelScope.launch {
        repository.removeFavorite(event)
    }
}
