package com.example.androidfundamental.fragment

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.androidfundamental.adapter.EventAdapter
import com.example.androidfundamental.viewmodel.FavoriteViewModel
import com.example.androidfundamental.viewmodel.PastEventsViewModel
import com.example.androidfundamental.api.RetrofitClient
import com.example.androidfundamental.databinding.FragmentPastEventsBinding

class PastEventsFragment : Fragment() {

    private var _binding: FragmentPastEventsBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: PastEventsViewModel
    private lateinit var favoriteViewModel: FavoriteViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPastEventsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inisialisasi ViewModel
        viewModel = ViewModelProvider(this)[PastEventsViewModel::class.java]
        favoriteViewModel = ViewModelProvider(requireActivity())[FavoriteViewModel::class.java]

        val adapter = EventAdapter(mutableListOf()) { favoriteEvent ->
            // Menambahkan atau menghapus event dari favorit
            favoriteViewModel.addFavorite(favoriteEvent)
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = adapter

        // Observe loading status
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        // Observe LiveData dari ViewModel
        viewModel.pastEvents.observe(viewLifecycleOwner) { events ->
            binding.progressBar.visibility = View.GONE // Menyembunyikan progress bar
            adapter.updateData(events)
        }

        // Observe error messages
        viewModel.errorMessage.observe(viewLifecycleOwner) { message ->
            message?.let {
                binding.progressBar.visibility = View.GONE // Menyembunyikan progress bar
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            }
        }

        // Cek koneksi internet dan ambil data
        if (isNetworkAvailable()) {
            viewModel.fetchPastEvents(RetrofitClient.eventApiService)
        } else {
            Toast.makeText(context, "No internet connection. Please check your connection.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkCapabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        return networkCapabilities != null && networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Menghindari kebocoran memori
    }
}

