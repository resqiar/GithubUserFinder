package com.example.githubuserfinder.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.githubuserfinder.R
import com.example.githubuserfinder.backend.model.Item
import kotlinx.android.synthetic.main.item_favorite.view.*

class FavoriteAdapter(context: Context) : RecyclerView.Adapter<FavoriteAdapter.ViewHolder>() {
    var listFavorite = ArrayList<Item>()
        set(listFavorite) {
                this.listFavorite.clear()
                this.listFavorite.addAll(listFavorite)
            notifyDataSetChanged()
        }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val tvName : TextView = itemView.tv_name_favorite
        val tvAvatar : ImageView = itemView.tv_img_favorite
        val btnDelete: ImageView = itemView.delete_fav
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_favorite, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
       return listFavorite.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvName.text = listFavorite[position].login

        Glide.with(holder.tvName)
            .load(listFavorite[position].avatar_url)
            .into(holder.tvAvatar)

        holder.btnDelete.setOnClickListener {
            onDeleteClickCallback?.onDeleteClicked(listFavorite[position].id)
        }
        holder.itemView.setOnClickListener {
            onItemClickCallback?.onItemClicked(listFavorite[position].login, listFavorite[position].avatar_url)
        }
    }

    // buat interface yang akkan membuat itemnya mencjadi clickable
    private var onItemClickCallback: OnItemClickCallback? = null

    interface OnItemClickCallback {
        fun onItemClicked(user: String?, avatarUrl: String?)
    }

    // fungsi callback
    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    // buat interface yang akkan membawa btn_delete ke favActivity
    private var onDeleteClickCallback: OnDeleteClickCallback? = null

    interface OnDeleteClickCallback {
        fun onDeleteClicked(position: Int)
    }

    // fungsi callback
    fun setOnDeleteClickCallback(onDeleteClickCallback: OnDeleteClickCallback) {
        this.onDeleteClickCallback = onDeleteClickCallback
    }


}