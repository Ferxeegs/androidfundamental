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
import com.example.androidfundamental.viewmodel.ActiveEventsViewModel
import com.example.androidfundamental.adapter.EventAdapter
import com.example.androidfundamental.api.RetrofitClient
import com.example.androidfundamental.databinding.FragmentActiveEventsBinding

class ActiveEventsFragment : Fragment() {

    private var _binding: FragmentActiveEventsBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: ActiveEventsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentActiveEventsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inisialisasi ViewModel
        viewModel = ViewModelProvider(this)[ActiveEventsViewModel::class.java]

        // Inisialisasi Adapter tanpa tombol Favorite
        val adapter = EventAdapter(mutableListOf())
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = adapter

        // Observe daftar event aktif
        viewModel.activeEvents.observe(viewLifecycleOwner) { events ->
            binding.progressBar.visibility = View.GONE
            if (events.isEmpty()) {
                binding.recyclerView.visibility = View.GONE
            } else {
                binding.recyclerView.visibility = View.VISIBLE
                adapter.updateData(events)
            }
        }

        // Observe error messages
        viewModel.errorMessage.observe(viewLifecycleOwner) { message ->
            message?.let {
                binding.progressBar.visibility = View.GONE
                binding.recyclerView.visibility = View.GONE
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            }
        }

        // Cek koneksi internet dan ambil data
        if (isNetworkAvailable()) {
            viewModel.fetchActiveEvents(RetrofitClient.eventApiService)
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
