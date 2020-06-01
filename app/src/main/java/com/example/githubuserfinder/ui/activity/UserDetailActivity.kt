package com.example.githubuserfinder.ui.activity

import android.content.ContentValues
import android.content.Intent
import android.database.SQLException
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.githubuserfinder.R
import com.example.githubuserfinder.backend.db.DatabaseContract
import com.example.githubuserfinder.backend.db.DatabaseContract.FavColums.Companion.CONTENT_URI
import com.example.githubuserfinder.backend.db.FavHelper
import com.example.githubuserfinder.ui.adapter.FavoriteAdapter
import com.example.githubuserfinder.ui.adapter.pageradapter.SectionPagerAdapter
import com.example.githubuserfinder.ui.fragment.FollowersFragment
import com.example.githubuserfinder.ui.fragment.FollowingFragment
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.activity_user_detail.*
import org.json.JSONObject

class UserDetailActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var adapter : FavoriteAdapter
    private lateinit var favHelper: FavHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_detail)

        /* DATA FROM ACTIVITY */
        val username = intent.getStringExtra("username")

        /*TAB SECTION*/
        val sectionPagerAdapter = SectionPagerAdapter(this, supportFragmentManager)
        sectionPagerAdapter.setData(username.toString())
        view_pager.adapter = sectionPagerAdapter
        tabs.setupWithViewPager(view_pager)
        supportActionBar?.elevation = 0f
        supportActionBar?.title = " "
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        showDetail()

        val bundle = Bundle()
        val followerFragment =
            FollowersFragment()
        bundle.putString("username", username)
        followerFragment.arguments = bundle

        val followingFragment =
            FollowingFragment()
        bundle.putString("username", username)
        followingFragment.arguments = bundle

        /* ADD TO FAV SECTION */
        adapter = FavoriteAdapter(this)
        favHelper = FavHelper.getInstance(this)
        favHelper.open()
        addToFav.setOnClickListener(this)
    }

    fun showDetail(){
        // data yang telah dikirim dari MainActivity
        val username = intent.getStringExtra("username")

        // kirim nilai username ke following fragment
        val followingFragment =
            FollowingFragment.newInstance(
                username
            )
        supportFragmentManager.beginTransaction().replace(R.id.view_pager, followingFragment).commit()

        // kirim nilai username ke following fragment
        val followersFragment =
            FollowersFragment.newInstance(
                username
            )
        supportFragmentManager.beginTransaction().replace(R.id.view_pager, followersFragment).commit()


        showLoading(true)
        showError(false)

        val url = "https://api.github.com/users/$username"

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

                // PARSING JSON
                try {
                    val result = String(responseBody)

                    val responseObject = JSONObject(result)
                    val name = responseObject.getString("name")
                    val location = responseObject.getString("location")
                    val urlImg = responseObject.getString("avatar_url")
                    val repo = responseObject.getInt("public_repos")
                    val total_following = responseObject.getInt("following")
                    val total_followers = responseObject.getInt("followers")

                    Glide.with(this@UserDetailActivity)
                        .load(urlImg)
                        .into(tv_detail_img)

                    tv_detail_name.text = name
                    tv_detail_location.text = location

                    tv_repo.text = repo.toString()
                    tv_following.text = total_following.toString()
                    tv_followers.text = total_followers.toString()

                    showLoading(false)
                    sendName(name)
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

    fun sendName(name: String): String{
        return name
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.settings_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.settings) {
            startActivity(Intent(this, SettingsActivity::class.java))
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return super.onSupportNavigateUp()
    }

    private fun showLoading(condition: Boolean){
        when(condition){
            true -> loading_detail.visibility = View.VISIBLE
            false -> loading_detail.visibility = View.GONE
        }
    }

    private fun showError(condition: Boolean){
        when(condition){
            true -> connection_problem_detail.visibility = View.VISIBLE
            false -> connection_problem_detail.visibility = View.GONE
        }
    }

    override fun onClick(v: View?) {
        if(v?.id == R.id.addToFav){
            val userID = intent.getStringExtra("id")
            val userName = intent.getStringExtra("username")
            val userAvatar = intent.getStringExtra("avatar")

            val values = ContentValues()
            values.put(DatabaseContract.FavColums._ID, userID)
            values.put(DatabaseContract.FavColums.USERNAME, userName)
            values.put(DatabaseContract.FavColums.AVATAR, userAvatar)

            try {
                contentResolver.insert(CONTENT_URI, values)
            } catch (e: SQLException) {
                Toast.makeText(this, "Already in favorite!", Toast.LENGTH_SHORT).show()
            }
            addToFav.setImageResource(R.drawable.ic_favorite_black_24dp)

        }
    }
}
