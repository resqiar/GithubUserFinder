package com.example.githubuserfinder.ui.favorite.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
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
            if (listFavorite.size > 0) {
                this.listFavorite.clear()
            }else{
                this.listFavorite.addAll(listFavorite)
            }
            notifyDataSetChanged()
        }

    /* DATABASE METHOD ( ADD UPDATE DELETE ) */
//    fun addItem(model: Item) {
//        this.listFavorite.add(model)
//        notifyItemInserted(this.listFavorite.size - 1)
//    }
//    fun updateItem(position: Int, model: Item) {
//        this.listFavorite[position] = model
//        notifyItemChanged(position, model)
//    }
    fun removeItem(position: Int) {
        this.listFavorite.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, this.listFavorite.size)
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val tvName : TextView = itemView.tv_name_favorite
        val tvAvatar : ImageView = itemView.tv_img_favorite
        val btnDelete : ImageButton = itemView.delete_fav
//        fun bindUI(model: Item) {
//            with(itemView){
//                Log.e("ADAPTER FAV", model.toString())
//                Glide.with(itemView.context)
//                    .load(model.avatar_url)
//                    .into(tv_img_favorite)
//
//                tv_name_favorite.text = model.login
//                tv_desc_favorite.text = model.id.toString()
//
//                Log.e("CEK NILAI FAV", model.login.toString())
//                itemView.setOnClickListener {
//                    // buat onclick listener yang akan menuju detail Fragment
//                    onItemClickCallback?.onItemClicked(model.login)
//                }
//            }
//        }
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
            removeItem(listFavorite[position].id)
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


}