package com.example.githubconsumer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.githubconsumer.model.Item
import kotlinx.android.synthetic.main.consumer_item.view.*

class ConsumerAdapter: RecyclerView.Adapter<ConsumerAdapter.ViewHolder>() {
    var listFavorite = ArrayList<Item>()
        set(listFavorite) {
            this.listFavorite.clear()
            this.listFavorite.addAll(listFavorite)
            notifyDataSetChanged()
        }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val tvName : TextView = itemView.tv_name_favorite
        val tvAvatar : ImageView = itemView.tv_img_favorite
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context).inflate(R.layout.consumer_item, parent, false)
        return ViewHolder(inflater)
    }

    override fun getItemCount(): Int {
        return listFavorite.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvName.text = listFavorite[position].login
        Glide.with(holder.tvName)
            .load(listFavorite[position].avatar_url)
            .into(holder.tvAvatar)
    }
}