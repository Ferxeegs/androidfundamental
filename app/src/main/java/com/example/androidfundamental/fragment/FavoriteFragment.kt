package com.example.androidfundamental.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.androidfundamental.model.Event
import com.example.androidfundamental.adapter.EventAdapter
import com.example.androidfundamental.databinding.FragmentFavoriteBinding

class FavoriteFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var favoriteAdapter: EventAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentFavoriteBinding.inflate(inflater, container, false)

        recyclerView = binding.recyclerViewFavorites

        // Set up RecyclerView adapter
        favoriteAdapter = EventAdapter(mutableListOf()) { favoriteEvent ->
            // Handle favorite button click
        }
        recyclerView.adapter = favoriteAdapter

        // Load data into the RecyclerView
        loadFavoriteEvents()

        return binding.root
    }

    private fun loadFavoriteEvents() {
        // Fetch data from the database or API and update the RecyclerView adapter
        val favoriteEvents = getFavoriteEventsFromDatabase() // Gantikan dengan metode untuk mengambil data
        favoriteAdapter.updateData(favoriteEvents)
    }

    private fun getFavoriteEventsFromDatabase(): List<Event> {
        // Implementasikan logika untuk mengambil event favorit dari database
        return listOf() // Return data favorite sebagai contoh
    }
}

