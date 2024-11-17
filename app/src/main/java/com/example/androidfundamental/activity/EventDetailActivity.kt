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
import com.example.androidfundamental.databinding.ActivityEventDetailBinding

@Suppress("DEPRECATION")
class EventDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEventDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEventDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Tampilkan indikator loading saat memuat data
        binding.progressBar.visibility = View.VISIBLE

        // Ambil data dari intent
        val event = intent.getSerializableExtra("event") as? Event
        event?.let {
            displayEventDetails(it)
            setupLinkButton(it.link) // Pastikan Anda memiliki link dalam objek Event
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
}
