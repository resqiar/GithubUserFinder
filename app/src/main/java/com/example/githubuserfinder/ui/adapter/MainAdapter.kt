package com.example.githubuserfinder.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.githubuserfinder.R
import com.example.githubuserfinder.backend.model.Item
import kotlinx.android.synthetic.main.search_user_item.view.*

class MainAdapter : RecyclerView.Adapter<MainAdapter.ViewHolder>() {

    private val dataUser = ArrayList<Item>()

    fun setData(items: ArrayList<Item>){
        dataUser.clear()
        dataUser.addAll(items)
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindUI(
            model: Item
        ) {
            Log.e("Nilai ADAPTER", model.toString())

            itemView.tv_name_search_user.text = model.login
            itemView.tv_desc_search_user.text = model.id.toString()

            //poster url
            val posterUrl = model.avatar_url
            // bind poster
            Glide.with(itemView.context)
                .load(posterUrl)
                .into(itemView.tv_img_search_user)

            itemView.setOnClickListener {
                // buat onclick listener yang akan menuju detail Fragment
                onItemClickCallback?.onItemClicked(model.login, model.id, model.avatar_url)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.search_user_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataUser.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindUI(dataUser[position])
    }

    // buat interface yang akkan membuat itemnya mencjadi clickable
        private var onItemClickCallback: OnItemClickCallback? = null

        interface OnItemClickCallback {
        fun onItemClicked(user: String?, id: Int, avatarUrl: String?)
    }

    // fungsi callback
    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

}