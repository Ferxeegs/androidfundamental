package com.example.androidfundamental.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.androidfundamental.model.Event
import com.example.androidfundamental.api.EventApiService
import com.example.androidfundamental.model.EventResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ActiveEventsViewModel : ViewModel() {
    private val _activeEvents = MutableLiveData<List<Event>>()
    val activeEvents: LiveData<List<Event>> get() = _activeEvents

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    fun fetchActiveEvents(apiService: EventApiService) {
        // Jika data sudah ada, tidak perlu memanggil ulang
        if (_activeEvents.value != null) return

        // Menandakan bahwa loading sedang berlangsung
        _isLoading.value = true

        // Panggil API
        apiService.getEvents(1).enqueue(object : Callback<EventResponse> {
            override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                _isLoading.value = false // Loading selesai
                if (response.isSuccessful) {
                    val eventResponse = response.body()
                    if (eventResponse != null && eventResponse.listEvents.isNotEmpty()) {
                        _activeEvents.value = eventResponse.listEvents
                    } else {
                        _errorMessage.value = "No events available"
                    }
                } else {
                    _errorMessage.value = "Error: ${response.message()}"
                }
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                _isLoading.value = false // Loading selesai
                _errorMessage.value = "Failure: ${t.message}"
            }
        })
    }
}
