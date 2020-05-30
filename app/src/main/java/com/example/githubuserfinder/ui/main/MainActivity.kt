package com.example.githubuserfinder.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuserfinder.R
import com.example.githubuserfinder.backend.model.Item
import com.example.githubuserfinder.ui.detail.UserDetailActivity
import com.example.githubuserfinder.ui.favorite.FavoriteActivity
import com.example.githubuserfinder.ui.main.adapter.MainAdapter
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    private lateinit var adapter : MainAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // SHOW RECYCLERVIEW
        rv_search_user.setHasFixedSize(true)
        adapter = MainAdapter()
        adapter.notifyDataSetChanged()

        // set adapter
        val listManager = LinearLayoutManager(this)
        rv_search_user.layoutManager = listManager
        rv_search_user.adapter = adapter

        adapter.setOnItemClickCallback(object : MainAdapter.OnItemClickCallback{
            override fun onItemClicked(
                user: String?,
                id: Int,
                avatarUrl: String?
            ) {
                // to Detail Activity
                val intent = Intent(this@MainActivity, UserDetailActivity::class.java)
                intent.putExtra("username", user)
                intent.putExtra("id", id)
                intent.putExtra("avatar", avatarUrl)

                startActivity(intent)
            }
        })

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
            menuInflater.inflate(R.menu.options_menu, menu)
            menuInflater.inflate(R.menu.favorite_menu, menu)


        /* SEARCH VIEW*/
        val searchItem = menu?.findItem(R.id.search)
        val searchView = searchItem?.actionView as SearchView
        searchView.queryHint = "Search User..."

        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                not_search_yet.visibility = View.GONE
                getUserInfo(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.favorite){
            startActivity(Intent(this, FavoriteActivity::class.java))
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getUserInfo(user: String?) {
        val listItems = ArrayList<Item>()
        showLoading(true)
        showError(false)
        showUserNotFound(false)
        val url = "https://api.github.com/search/users?q=$user"
        /* start parsing JSON */
        val client = AsyncHttpClient()
        client.addHeader("Authorization", "token 65d66e275a9fbe97a276632fd3ef463d275e7a76")
        client.addHeader("User-Agent", "request")
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray
            ) {
                // looping untuk dapat nilai
                try {
                    val result = String(responseBody)

                    val responseObject = JSONObject(result)
                    val list = responseObject.getJSONArray("items")
                    val totalCountResponse = responseObject.getInt("total_count")

                    /* If total count == 0 */
                    if (totalCountResponse == 0){
                        showUserNotFound(true)
                    }

                    for (i in 0 until list.length()){
                        val items = list.getJSONObject(i)
                        val item = Item()
                        Log.e("response", list.toString())

                        item.id = items.getInt("id")
                        item.avatar_url = items.getString("avatar_url")
                        item.login = items.getString("login")

                        listItems.add(item)
                        Log.e("listItems", listItems.toString())
                    }

                    // set data ke adapter
                    adapter.setData(listItems)
                    showLoading(false)



                }catch (e: Exception){
                    Log.e("Parsing JSON", e.toString())
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable?
            ) {
                showLoading(false)
                showError(true)
                Log.e("OnFailure", error?.message.toString())
            }

        })
    }



    private fun showLoading(condition: Boolean){
        when(condition){
            true -> loading_main.visibility = View.VISIBLE
            false -> loading_main.visibility = View.GONE
        }
    }

    private fun showUserNotFound(condition: Boolean){
        when(condition){
            true -> no_user_found.visibility = View.VISIBLE
            false -> no_user_found.visibility = View.GONE
        }
    }


    private fun showError(condition: Boolean){
        when(condition){
            true -> connection_problem.visibility = View.VISIBLE
            false -> connection_problem.visibility = View.GONE
        }
    }
}

