package com.example.androidfundamental.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.androidfundamental.dao.FavoriteEvent
import com.example.androidfundamental.model.Event
import com.example.androidfundamental.repository.FavoriteRepository
import kotlinx.coroutines.launch

class FavoriteViewModel(application: Application) : AndroidViewModel(application) {

    private val favoriteEventRepository = FavoriteRepository(application)
    private val _favoriteEvents = MutableLiveData<List<Event>>()
    val favoriteEvents: LiveData<List<Event>> get() = _favoriteEvents

    init {
        loadFavoriteEvents() // Memuat data favorit saat ViewModel dibuat
    }

    // Fungsi untuk load data favorit
    fun loadFavoriteEvents() {
        viewModelScope.launch {
            val favoriteEventList = favoriteEventRepository.getAllFavorites()
            _favoriteEvents.value = favoriteEventList.map { favoriteEvent ->
                Event(
                    id = favoriteEvent.id,
                    name = favoriteEvent.name,
                    summary = favoriteEvent.summary ?: "",
                    description = favoriteEvent.description ?: "",
                    imageLogo = favoriteEvent.imageLogo ?: "",
                    mediaCover = favoriteEvent.mediaCover ?: "",
                    category = favoriteEvent.category ?: "",
                    ownerName = favoriteEvent.ownerName ?: "",
                    cityName = favoriteEvent.cityName ?: "",
                    quota = favoriteEvent.quota,
                    registrants = favoriteEvent.registrants,
                    beginTime = favoriteEvent.beginTime ?: "",
                    endTime = favoriteEvent.endTime ?: "",
                    link = favoriteEvent.link ?: "",
                    isFavorite = true // Set bahwa event ini sudah favorit
                )
            }
        }
    }

    // Fungsi untuk menambahkan event favorit
    fun addFavorite(favoriteEvent: FavoriteEvent) {
        viewModelScope.launch {
            favoriteEventRepository.addFavorite(favoriteEvent)
            loadFavoriteEvents() // Memuat ulang data setelah penambahan
        }
    }

    fun removeFavorite(favoriteEvent: FavoriteEvent) {
        viewModelScope.launch {
            favoriteEventRepository.removeFavorite(favoriteEvent)
            loadFavoriteEvents() // Memuat ulang data setelah penghapusan
        }
    }

    // Fungsi untuk memeriksa apakah event sudah ada di favorit
    fun isFavorite(eventId: String): LiveData<Boolean> {
        val isFavoriteLiveData = MutableLiveData<Boolean>()
        viewModelScope.launch {
            val isFavorite = favoriteEventRepository.isFavorite(eventId)
            isFavoriteLiveData.postValue(isFavorite) // Menyampaikan hasil pengecekan favorit
        }
        return isFavoriteLiveData
    }
}

