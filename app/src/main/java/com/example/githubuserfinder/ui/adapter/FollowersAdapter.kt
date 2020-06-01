package com.example.githubuserfinder.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.githubuserfinder.R
import com.example.githubuserfinder.backend.model.Item
import kotlinx.android.synthetic.main.item_followers.view.*

class FollowersAdapter : RecyclerView.Adapter<FollowersAdapter.ViewHolder>(){

    private var dataFollowers = ArrayList<Item>()

    fun setData(items: ArrayList<Item>){
        dataFollowers.clear()
        dataFollowers.addAll(items)
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bindUI(model : Item){

            Log.e("Nilai adapter", model.toString())
            // bind to layout
            itemView.tv_name_followers.text = model.login
            itemView.tv_desc_followers.text = model.id.toString()

            // bind avatar
            val avatarUrl = model.avatar_url
            Glide.with(itemView.context)
                .load(avatarUrl)
                .into(itemView.tv_img_followers)

            itemView.setOnClickListener {
                // buat onclick listener yang akan menuju detail Fragment
                onItemClickCallback?.onItemClicked(model.login)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_followers, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
       return dataFollowers.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindUI(dataFollowers[position])
    }


    // buat interface yang akkan membuat itemnya mencjadi clickable
    private var onItemClickCallback: OnItemClickCallback? = null

    interface OnItemClickCallback {
        fun onItemClicked(user: String?)
    }

    // fungsi callback
    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

}