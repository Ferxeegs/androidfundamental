package com.example.androidfundamental.fragment

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.androidfundamental.model.Event
import com.example.androidfundamental.adapter.EventAdapter
import com.example.androidfundamental.model.EventResponse
import com.example.androidfundamental.dao.FavoriteEvent
import com.example.androidfundamental.api.RetrofitClient
import com.example.androidfundamental.databinding.FragmentHomeBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var activeEventsAdapter: EventAdapter
    private lateinit var pastEventsAdapter: EventAdapter
    private lateinit var searchResultsAdapter: EventAdapter
    private val activeEvents = mutableListOf<Event>()
    private val pastEvents = mutableListOf<Event>()
    private val searchResults = mutableListOf<Event>()

    private var isActiveEventsLoaded = false
    private var isPastEventsLoaded = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerViews() // Menyiapkan RecyclerView untuk acara aktif dan acara yang telah berlalu
        loadEvents() // Memanggil fungsi untuk mengambil acara

        // Set up search button click listener
        binding.buttonSearch.setOnClickListener {
            val query = binding.editTextSearch.text.toString().trim()
            if (query.isNotEmpty()) {
                searchEvents(query) // Panggil fungsi pencarian
            } else {
                Toast.makeText(context, "Please enter a keyword to search", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupRecyclerViews() {
        // Define the onFavoriteClicked lambda function to handle favorites
        val onFavoriteClicked: (FavoriteEvent) -> Unit = { favoriteEvent ->
            // Handle the event of adding or removing a favorite here
            // You can save the favorite event in your local database or shared preferences
            Log.d("HomeFragment", "Favorite clicked: ${favoriteEvent.name}")
        }

        // RecyclerView untuk acara aktif
        activeEventsAdapter = EventAdapter(activeEvents, onFavoriteClicked)
        binding.recyclerViewActiveEvents.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerViewActiveEvents.adapter = activeEventsAdapter

        // RecyclerView untuk acara yang telah berlalu (ditampilkan secara horizontal)
        pastEventsAdapter = EventAdapter(pastEvents, onFavoriteClicked)
        binding.recyclerViewPastEvents.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.recyclerViewPastEvents.adapter = pastEventsAdapter

        // RecyclerView untuk hasil pencarian
        searchResultsAdapter = EventAdapter(searchResults, onFavoriteClicked)
        binding.recyclerViewSearchResults.layoutManager = LinearLayoutManager(context) // Menggunakan layout manager yang sesuai
        binding.recyclerViewSearchResults.adapter = searchResultsAdapter
    }

    private fun loadEvents() {
        binding.progressBar.visibility = View.VISIBLE // Menampilkan progress bar
        fetchActiveEvents() // Mengambil acara aktif
        fetchPastEvents()   // Mengambil acara yang telah berlalu
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkCapabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)

        return networkCapabilities != null && networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    private fun checkIfDataLoaded() {
        if (isActiveEventsLoaded && isPastEventsLoaded) {
            binding.progressBar.visibility = View.GONE // Sembunyikan ProgressBar ketika kedua data sudah selesai di-load
        }
    }

    private fun fetchActiveEvents() {
        if (!isNetworkAvailable()) {
            Toast.makeText(context, "No internet connection. Please check your connection.", Toast.LENGTH_SHORT).show()
            return
        }

        RetrofitClient.eventApiService.getEvents(1).enqueue(object : Callback<EventResponse> {
            override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                isActiveEventsLoaded = true
                if (response.isSuccessful) {
                    val eventResponse = response.body()
                    if (eventResponse != null && eventResponse.listEvents.isNotEmpty()) {
                        val limitedActiveEvents = eventResponse.listEvents.take(5)
                        activeEvents.clear()
                        activeEvents.addAll(limitedActiveEvents)
                        activeEventsAdapter.notifyDataSetChanged()
                    } else {
                        Toast.makeText(context, "No active events available", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(context, "Error: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
                checkIfDataLoaded()
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                isActiveEventsLoaded = true
                Toast.makeText(context, "Failure: ${t.message}", Toast.LENGTH_SHORT).show()
                Log.e("HomeFragment", "Error fetching active events", t)
                checkIfDataLoaded()
            }
        })
    }

    private fun fetchPastEvents() {
        if (!isNetworkAvailable()) {
            Toast.makeText(context, "No internet connection. Please check your connection.", Toast.LENGTH_SHORT).show()
            return
        }

        RetrofitClient.eventApiService.getEvents(0).enqueue(object : Callback<EventResponse> {
            override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                isPastEventsLoaded = true
                if (response.isSuccessful) {
                    val eventResponse = response.body()
                    if (eventResponse != null && eventResponse.listEvents.isNotEmpty()) {
                        val limitedPastEvents = eventResponse.listEvents.take(5)
                        pastEvents.clear()
                        pastEvents.addAll(limitedPastEvents)
                        pastEventsAdapter.notifyDataSetChanged()
                    } else {
                        Toast.makeText(context, "No past events available", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(context, "Error: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
                checkIfDataLoaded()
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                isPastEventsLoaded = true
                Toast.makeText(context, "Failure: ${t.message}", Toast.LENGTH_SHORT).show()
                Log.e("HomeFragment", "Error fetching past events", t)
                checkIfDataLoaded()
            }
        })
    }

    private fun searchEvents(keyword: String) {
        binding.progressBar.visibility = View.VISIBLE

        RetrofitClient.apiService.searchEvents(keyword = keyword).enqueue(object : Callback<EventResponse> {
            override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                binding.progressBar.visibility = View.GONE
                if (response.isSuccessful) {
                    val eventResponse = response.body()
                    if (eventResponse != null && eventResponse.listEvents.isNotEmpty()) {
                        binding.recyclerViewActiveEvents.visibility = View.GONE
                        binding.recyclerViewPastEvents.visibility = View.GONE
                        binding.textSearchResults.visibility = View.VISIBLE
                        binding.recyclerViewSearchResults.visibility = View.VISIBLE

                        searchResults.clear()
                        searchResults.addAll(eventResponse.listEvents.take(5))
                        searchResultsAdapter.notifyDataSetChanged()
                    } else {
                        Toast.makeText(context, "No events found for the keyword", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(context, "Error: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                binding.progressBar.visibility = View.GONE
                Toast.makeText(context, "Failure: ${t.message}", Toast.LENGTH_SHORT).show()
                Log.e("HomeFragment", "Error searching events", t)
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
