package com.example.githubuserfinder.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.githubuserfinder.R
import com.example.githubuserfinder.backend.model.Item
import kotlinx.android.synthetic.main.item_following.view.*

class FollowingAdapter: RecyclerView.Adapter<FollowingAdapter.ViewHolder>() {

    private var dataFollowing = ArrayList<Item>()

    fun setData(items: ArrayList<Item>){
        dataFollowing.clear()
        dataFollowing.addAll(items)
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bindUI(model : Item){

            Log.e("Nilai adapter", model.toString())
            // bind to layout
            itemView.tv_name_following.text = model.login
            itemView.tv_desc_following.text = model.id.toString()

            // bind avatar
            val avatarUrl = model.avatar_url
            Glide.with(itemView.context)
                .load(avatarUrl)
                .into(itemView.tv_img_following)

            itemView.setOnClickListener {
                // buat onclick listener yang akan menuju detail Fragment
                onItemClickCallback?.onItemClicked(model.login)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_following, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
       return dataFollowing.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
       holder.bindUI(dataFollowing[position])
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