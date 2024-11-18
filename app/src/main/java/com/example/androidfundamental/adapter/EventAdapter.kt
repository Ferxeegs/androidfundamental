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
import com.example.androidfundamental.R
import com.example.androidfundamental.activity.EventDetailActivity
import com.example.androidfundamental.dao.FavoriteEvent
import com.example.androidfundamental.model.Event

class EventAdapter(
    private val eventList: MutableList<Event> // Daftar event untuk ditampilkan
) : RecyclerView.Adapter<EventAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val eventImage: ImageView = itemView.findViewById(R.id.event_image)
        private val eventName: TextView = itemView.findViewById(R.id.event_name)
        private val progressBar: ProgressBar = itemView.findViewById(R.id.itemProgressBar)

        fun bind(event: Event) {
            // Set nama event
            eventName.text = event.name

            // Load gambar event menggunakan Glide
            Glide.with(itemView.context)
                .load(event.imageLogo)
                .into(eventImage)

            // ProgressBar sembunyi setelah data berhasil di-load
            progressBar.visibility = View.GONE
            eventImage.visibility = View.VISIBLE
            eventName.visibility = View.VISIBLE

            // Listener untuk membuka detail event
            itemView.setOnClickListener {
                val context = itemView.context
                val intent = Intent(context, EventDetailActivity::class.java).apply {
                    putExtra("event", event)
                }
                context.startActivity(intent)
            }
        }

        fun showLoading() {
            // Tampilkan ProgressBar jika data sedang dimuat
            progressBar.visibility = View.VISIBLE
            eventImage.visibility = View.GONE
            eventName.visibility = View.GONE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_event, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (position < eventList.size) {
            val event = eventList[position]
            holder.bind(event) // Bind data event ke ViewHolder
        } else {
            holder.showLoading() // Tampilkan loading jika data belum lengkap
        }
    }

    override fun getItemCount(): Int = eventList.size

    fun updateData(newEvents: List<Event>) {
        newEvents?.let {
            this.eventList.clear()
            this.eventList.addAll(it)
            notifyDataSetChanged() // Pastikan adapter diberi tahu tentang data yang diperbarui
        }
    }

}
