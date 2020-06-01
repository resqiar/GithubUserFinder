package com.example.githubconsumer

import android.app.Activity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubconsumer.db.DatabaseContract.FavColums.Companion.CONTENT_URI
import com.example.githubconsumer.db.MappingHelper
import kotlinx.android.synthetic.main.activity_consumer.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class ConsumerActivity : AppCompatActivity() {
    private lateinit var adapter : ConsumerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_consumer)

        rv_consumer.setHasFixedSize(true)
        loadFav()
        adapter = ConsumerAdapter()
        rv_consumer.layoutManager = LinearLayoutManager(this)
        rv_consumer.adapter = adapter


    }

    // method to load fav from db
    private fun loadFav(){  // asynchronous
        GlobalScope.launch(Dispatchers.Main) {
            val defFav = async(Dispatchers.IO){
                val cursor = contentResolver.query(CONTENT_URI, null, null, null, null)
                MappingHelper.mapCursorToArrayList(cursor)
            }

            val favArray = defFav.await()
            if (favArray.size > 0){
                no_user_found.visibility = View.GONE
                adapter.listFavorite = favArray
            }else{
                adapter.listFavorite = ArrayList()
                no_user_found.visibility = View.VISIBLE
            }
        }
    }

}
