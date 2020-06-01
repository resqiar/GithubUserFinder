package com.example.githubuserfinder.ui.activity

import android.content.Intent
import android.database.ContentObserver
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.os.PersistableBundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuserfinder.R
import com.example.githubuserfinder.backend.db.DatabaseContract.FavColums.Companion.CONTENT_URI
import com.example.githubuserfinder.backend.db.FavHelper
import com.example.githubuserfinder.backend.db.dbhelper.MappingHelper
import com.example.githubuserfinder.backend.model.Item
import com.example.githubuserfinder.ui.adapter.FavoriteAdapter
import kotlinx.android.synthetic.main.activity_favorite.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class FavoriteActivity : AppCompatActivity() {
    private lateinit var adapter : FavoriteAdapter
    private lateinit var favHelper: FavHelper
    private lateinit var uriWithID: Uri
    private var item: Item? = null

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
        // delete data
        uriWithID = Uri.parse(CONTENT_URI.toString() + "/" + item?.id)
        adapter.setOnDeleteClickCallback(object : FavoriteAdapter.OnDeleteClickCallback {
            override fun onDeleteClicked(position: Int) {
                uriWithID = Uri.parse("$CONTENT_URI/$position")
                contentResolver.delete(uriWithID, null, null)
            }

        })
        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)

        val myObserver = object : ContentObserver(handler) {
            override fun onChange(selfChange: Boolean) {
                loadFav()
            }
        }

        // register content resolver
        contentResolver.registerContentObserver(CONTENT_URI, true, myObserver)

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
            val defFav = async(Dispatchers.IO) {
                val cursor = contentResolver.query(CONTENT_URI, null, null, null, null)
                 MappingHelper.mapCursorToArrayList(cursor)
            }

            val favArray = defFav.await()
            if (favArray.size > 0){
                no_user_fav.visibility = View.GONE
                adapter.listFavorite = favArray
            } else {
                adapter.listFavorite = ArrayList()
                no_user_fav.visibility = View.VISIBLE
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        favHelper.close()
    }
}
