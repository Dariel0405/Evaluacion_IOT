package com.example.evaluacion_2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.evaluacion_2.news.News

class NewsAdapter(
    private val items: MutableList<News>,
    private val onClick: (News) -> Unit
) : RecyclerView.Adapter<NewsAdapter.VH>() {

    class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        val tvSubtitle: TextView = itemView.findViewById(R.id.tvSubtitle)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_news, parent, false)
        return VH(v)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val n = items[position]
        holder.tvTitle.text = n.title
        holder.tvSubtitle.text = n.subtitle
        holder.itemView.setOnClickListener { onClick(n) }
    }

    override fun getItemCount(): Int = items.size
}
