package com.example.githubuserfinder.ui.favorite

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuserfinder.R
import com.example.githubuserfinder.backend.db.FavHelper
import com.example.githubuserfinder.backend.db.dbhelper.MappingHelper
import com.example.githubuserfinder.backend.model.Item
import com.example.githubuserfinder.ui.detail.UserDetailActivity
import com.example.githubuserfinder.ui.favorite.adapter.FavoriteAdapter
import kotlinx.android.synthetic.main.activity_favorite.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class FavoriteActivity : AppCompatActivity() {
    private lateinit var adapter : FavoriteAdapter
    private lateinit var favHelper: FavHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)
        supportActionBar?.title = "Favorite"

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        /* fav helper declaration */
        favHelper = FavHelper.getInstance(this)
        favHelper.open()

        rv_favorite.setHasFixedSize(true)

        adapter = FavoriteAdapter(this)
        rv_favorite.layoutManager = LinearLayoutManager(this)
        rv_favorite.adapter = adapter

        adapter.setOnItemClickCallback(object : FavoriteAdapter.OnItemClickCallback{
            override fun onItemClicked(user: String?, avatarUrl: String?) {
                // to Detail Activity
                val intent = Intent(this@FavoriteActivity, UserDetailActivity::class.java)
                intent.putExtra("username", user)
                intent.putExtra("avatar", avatarUrl)

                startActivity(intent)
            }
        })

        if (savedInstanceState == null){
            loadFav()
        }else{
            val list = savedInstanceState.getParcelableArrayList<Item>("FAV_EXTRA")
            if (list != null){
                adapter.listFavorite = list
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        outState.putParcelableArrayList("FAV_EXTRA", adapter.listFavorite)
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }

    // method to load fav from db
    private fun loadFav(){  // asynchronous
        GlobalScope.launch(Dispatchers.Main) {
            loading_fav.visibility = View.VISIBLE
            val fav = async(Dispatchers.IO){
                val cursor = favHelper.queryAll()
                 MappingHelper.mapCursorToArrayList(cursor)
            }

            val favArray = fav.await()
            if (favArray.size > 0){
                adapter.listFavorite = favArray
            }
            loading_fav.visibility = View.GONE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        favHelper.close()
    }
}
