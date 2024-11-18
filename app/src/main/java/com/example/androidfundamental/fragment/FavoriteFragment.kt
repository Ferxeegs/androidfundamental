package com.example.androidfundamental.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidfundamental.adapter.EventAdapter
import com.example.androidfundamental.viewmodel.FavoriteViewModel
import com.example.androidfundamental.databinding.FragmentFavoriteBinding

class FavoriteFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var eventAdapter: EventAdapter
    private lateinit var favoriteViewModel: FavoriteViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentFavoriteBinding.inflate(inflater, container, false)

        recyclerView = binding.recyclerViewFavorites
        favoriteViewModel = ViewModelProvider(this).get(FavoriteViewModel::class.java)

        // Setup RecyclerView dengan EventAdapter dan LayoutManager
        eventAdapter = EventAdapter(mutableListOf())
        recyclerView.adapter = eventAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Observe perubahan data event favorit
        favoriteViewModel.favoriteEvents.observe(viewLifecycleOwner) { events ->
            Log.d("FavoriteFragment", "Received events: $events")
            // Pastikan data event favorit tidak duplikat
            val distinctEvents = events.distinctBy { it.id }
            eventAdapter.updateData(distinctEvents)  // Update dengan data yang unik
        }

        // Load event favorit
        favoriteViewModel.loadFavoriteEvents()

        return binding.root
    }
}
