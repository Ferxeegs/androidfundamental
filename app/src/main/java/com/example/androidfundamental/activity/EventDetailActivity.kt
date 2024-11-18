package com.example.androidfundamental.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.androidfundamental.model.Event
import com.example.androidfundamental.dao.FavoriteEvent
import com.example.androidfundamental.viewmodel.FavoriteViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.androidfundamental.databinding.ActivityEventDetailBinding
import android.widget.Toast
import com.example.androidfundamental.R

@Suppress("DEPRECATION")
class EventDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEventDetailBinding
    private var isFavorite: Boolean = false
    private lateinit var favoriteViewModel: FavoriteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEventDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inisialisasi ViewModel untuk mengelola status favorit
        favoriteViewModel = ViewModelProvider(this)[FavoriteViewModel::class.java]

        // Tampilkan indikator loading saat memuat data
        binding.progressBar.visibility = View.VISIBLE

        // Ambil data dari intent
        val event = intent.getSerializableExtra("event") as? Event
        event?.let {
            displayEventDetails(it)
            setupLinkButton(it.link) // Pastikan Anda memiliki link dalam objek Event

            // Cek apakah event ini sudah menjadi favorit
            checkIfFavorite(it)
        }

        // Sembunyikan indikator loading setelah menampilkan data
        binding.progressBar.visibility = View.GONE
    }

    @SuppressLint("SetTextI18n")
    private fun displayEventDetails(event: Event) {
        // Tampilkan detail event
        Glide.with(this)
            .load(event.imageLogo) // Menggunakan URL gambar
            .into(binding.imageLogo)

        binding.eventName.text = event.name
        binding.ownerName.text = "Penyelenggara: ${event.ownerName}"
        binding.beginTime.text = "Waktu: ${event.beginTime}"
        binding.quota.text = "Sisa Kuota: ${event.quota - event.registrants}"
        binding.description.text = Html.fromHtml(event.description, Html.FROM_HTML_MODE_LEGACY)

        // Sembunyikan indikator loading setelah menampilkan data
        binding.progressBar.visibility = View.GONE
    }

    private fun setupLinkButton(link: String?) {
        // Menyiapkan aksi untuk tombol yang membuka link
        binding.buttonLink.setOnClickListener {
            link?.let {
                // Membuka link di browser
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it))
                startActivity(intent)
            }
        }
    }

    private fun checkIfFavorite(event: Event) {
        // Cek apakah event sudah ada dalam daftar favorit
        favoriteViewModel.isFavorite(event.id.toString()).observe(this) { isFav ->
            isFavorite = isFav
            updateFavoriteButton()
        }
    }

    private fun updateFavoriteButton() {
        // Update ikon favorit berdasarkan status
        if (isFavorite) {
            binding.buttonFavorite.setImageResource(R.drawable.fav2)
        } else {
            binding.buttonFavorite.setImageResource(R.drawable.fav)
        }

        // Set listener untuk tombol favorite
        binding.buttonFavorite.setOnClickListener {
            if (isFavorite) {
                removeFromFavorite()
            } else {
                addToFavorite()
            }
        }
    }

    private fun addToFavorite() {
        val event = intent.getSerializableExtra("event") as? Event
        event?.let {
            val favoriteEvent = FavoriteEvent(
                eventId = it.id.toString(),
                name = it.name,
                summary = it.summary,
                description = it.description,
                imageLogo = it.imageLogo,
                mediaCover = it.mediaCover,
                category = it.category,
                ownerName = it.ownerName,
                cityName = it.cityName,
                quota = it.quota,
                registrants = it.registrants,
                beginTime = it.beginTime,
                endTime = it.endTime,
                link = it.link
            )
            favoriteViewModel.addFavorite(favoriteEvent)
            isFavorite = true
            updateFavoriteButton()
            Toast.makeText(this, "Event added to favorites", Toast.LENGTH_SHORT).show()
        }
    }

    private fun removeFromFavorite() {
        val event = intent.getSerializableExtra("event") as? Event
        event?.let {
            val favoriteEvent = FavoriteEvent(
                eventId = it.id.toString(), // Gunakan eventId dari API
                name = it.name,
                summary = it.summary,
                description = it.description,
                imageLogo = it.imageLogo,
                mediaCover = it.mediaCover,
                category = it.category,
                ownerName = it.ownerName,
                cityName = it.cityName,
                quota = it.quota,
                registrants = it.registrants,
                beginTime = it.beginTime,
                endTime = it.endTime,
                link = it.link
            )
            favoriteViewModel.removeFavorite(favoriteEvent) // Hapus berdasarkan eventId
            isFavorite = false
            updateFavoriteButton()
            Toast.makeText(this, "Event removed from favorites", Toast.LENGTH_SHORT).show()
        }
    }


}
