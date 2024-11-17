package com.example.androidfundamental.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.androidfundamental.model.Event
import com.example.androidfundamental.R
import com.example.androidfundamental.activity.EventDetailActivity
import com.example.androidfundamental.dao.FavoriteEvent

class EventAdapter(
    private val eventList: MutableList<Event>,
    private val onFavoriteClicked: (FavoriteEvent) -> Unit // Callback untuk tombol favorit
) : RecyclerView.Adapter<EventAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val eventImage: ImageView = itemView.findViewById(R.id.event_image)
        val eventName: TextView = itemView.findViewById(R.id.event_name)
        val progressBar: ProgressBar = itemView.findViewById(R.id.itemProgressBar)
        val favoriteButton: ImageView = itemView.findViewById(R.id.btn_favorite) // Tambahkan ID untuk tombol favorit

        init {
            // Klik item untuk membuka detail event
            itemView.setOnClickListener {
                val event = eventList[adapterPosition]
                val context = itemView.context
                val intent = Intent(context, EventDetailActivity::class.java).apply {
                    putExtra("event", event)
                }
                context.startActivity(intent)
            }

            // Klik tombol favorit untuk menambah/hapus dari daftar favorit
            favoriteButton.setOnClickListener {
                val event = eventList[adapterPosition]
                val favoriteEvent = FavoriteEvent(
                    eventId = event.id,
                    name = event.name,
                    summary = event.summary,
                    imageLogo = event.imageLogo,
                    beginTime = event.beginTime,
                    endTime = event.endTime
                )
                onFavoriteClicked(favoriteEvent) // Callback dipanggil
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_event, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Tampilkan ProgressBar saat memuat data
        holder.progressBar.visibility = View.VISIBLE

        if (position < eventList.size) {
            val event = eventList[position]

            // Sembunyikan ProgressBar dan tampilkan data saat loading selesai
            holder.progressBar.visibility = View.GONE
            holder.eventImage.visibility = View.VISIBLE
            holder.eventName.visibility = View.VISIBLE

            // Load event data ke dalam item
            holder.eventName.text = event.name
            Glide.with(holder.itemView.context)
                .load(event.imageLogo)
                .into(holder.eventImage)

            // Ubah ikon favorit jika event sudah difavoritkan (opsional, jika ada logika favorit di luar adapter)
            // Misalnya, cek apakah event ini sudah dalam daftar favorit
            holder.favoriteButton.setImageResource(R.drawable.fav) // Default
        } else {
            // Jika tidak ada event, tampilkan ProgressBar
            holder.progressBar.visibility = View.VISIBLE
            holder.eventImage.visibility = View.GONE
            holder.eventName.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int = eventList.size

    fun updateData(newEvents: List<Event>?) {
        newEvents?.let {
            this.eventList.clear()
            this.eventList.addAll(it)
            notifyDataSetChanged()
        }
    }
    fun updateFavorites(favoriteIds: Set<Int>) {
        eventList.forEach { event ->
            // Menandai event apakah termasuk dalam daftar favorit
            event.isFavorite = favoriteIds.contains(event.id)
        }
        notifyDataSetChanged()
    }
}

