package com.example.evaluacion_2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.evaluacion_2.news.News

class NewsAdapter(
    private val items: List<News>,
    private val onClick: (News) -> Unit
) : RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    inner class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val ivThumb: ImageView = itemView.findViewById(R.id.ivNewsThumb)
        private val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)

        fun bind(news: News) {
            tvTitle.text = news.title

            val url = news.imageUrl
            if (!url.isNullOrEmpty()) {
                Glide.with(itemView.context)
                    .load(url)
                    .placeholder(R.drawable.logo_nuevo) // mientras carga
                    .error(R.drawable.logo_nuevo)       // si falla
                    .into(ivThumb)
            } else {
                ivThumb.setImageResource(R.drawable.logo_nuevo)
            }

            itemView.setOnClickListener {
                onClick(news)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_news, parent, false)
        return NewsViewHolder(view)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size
}
