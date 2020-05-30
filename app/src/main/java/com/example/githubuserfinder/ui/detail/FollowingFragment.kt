package com.example.githubuserfinder.ui.detail

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubuserfinder.R
import com.example.githubuserfinder.backend.model.Item
import com.example.githubuserfinder.ui.detail.adapter.FollowingAdapter
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.fragment_following.*
import org.json.JSONArray

/**
 * A simple [Fragment] subclass.
 */
class FollowingFragment : Fragment() {
    private lateinit var adapter : FollowingAdapter

    companion object{

        fun newInstance(username: String?): FollowingFragment{
            val fragment = FollowingFragment()
            val bundle = Bundle()

            // put data dari parameter
            bundle.putString("username", username)
            fragment.arguments = bundle

            return fragment
        }
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_following, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        // SHOW RECYCLERVIEW
        rv_following.setHasFixedSize(true)
        adapter = FollowingAdapter()
        adapter.notifyDataSetChanged()

        // set adapter
        val listManager = LinearLayoutManager(context)
        rv_following.layoutManager = listManager
        rv_following.adapter = adapter

        adapter.setOnItemClickCallback(object : FollowingAdapter.OnItemClickCallback{
            override fun onItemClicked(user: String?) {
                // to Detail Activity
                val intent = Intent(context, UserDetailActivity::class.java)
                intent.putExtra("username", user)
                startActivity(intent)
            }
        })

        /* TANGKAP NILAI */
        val username = arguments?.getString("username")
        Log.e("tes satu dua", username.toString())

        /* set Following */
        if (username != null) {
            setFollowing(username)
        }else{
            Log.e("USERNAME IS NULL", "USERNAME NULL")
        }
    }

    private fun setFollowing(username: String){
        val listItems = ArrayList<Item>()
        showLoading(true)

        val url = "https://api.github.com/users/$username/following"
        Log.e("FOLLOWING", username)
        /* start parsing JSON */
        val client = AsyncHttpClient()
        client.addHeader("Authorization", "token 65d66e275a9fbe97a276632fd3ef463d275e7a76")
        client.addHeader("User-Agent", "request")
        client.get(url, object: AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray
            ) {

                try {

                    // start parsing JSON
                    val result = String(responseBody)
                    val responseArray = JSONArray(result)

                    /* IF DATA NULL THEN SHOW NO FOLLOWING */
                    if (responseArray == null){
                        showUserNotFound(true)
                    }

                    for (i in 0 until responseArray.length()){


                        val items = responseArray.getJSONObject(i)
                        val model = Item()

                        model.avatar_url = items.getString("avatar_url")
                        model.id = items.getInt("id")
                        model.login = items.getString("login")

                        listItems.add(model)

                    }

                    // set data ke adapter
                    adapter.setData(listItems)
                    showLoading(false)

                }catch (e: Exception){
                    Log.e("FollowingFragment", e.toString())
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable?
            ) {
                Log.e("onFailure Following", error?.message.toString())
                showUserNotFound(true)
            }

        })
    }

    private fun showLoading(condition: Boolean){
        when(condition){
            true -> progress_following.visibility = View.VISIBLE
            false -> progress_following.visibility = View.GONE
        }
    }

    private fun showUserNotFound(condition: Boolean){
        when(condition){
            true -> no_following.visibility = View.VISIBLE
            false -> no_following.visibility = View.GONE
        }
    }
}
