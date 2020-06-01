package com.example.githubuserfinder.ui.adapter.pageradapter

import android.content.Context
import android.os.Bundle
import androidx.annotation.Nullable
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.githubuserfinder.R
import com.example.githubuserfinder.ui.fragment.FollowersFragment
import com.example.githubuserfinder.ui.fragment.FollowingFragment

class SectionPagerAdapter(private val mContext: Context, fm: FragmentManager): FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {


    // title
    @StringRes
    private val TAB_TITLES = intArrayOf(R.string.tab_title_1, R.string.tab_title_2)
    var userName : String = "username"

    override fun getItem(position: Int): Fragment {
        var fragment: Fragment? = null

            when(position){
                0 -> {
                    fragment =
                        FollowingFragment()
                    val bundle = Bundle()
                    bundle.putString("username", getData())
                    fragment.arguments = bundle
                }
                1 -> {
                    fragment =
                        FollowersFragment()
                    val bundle = Bundle()
                    bundle.putString("username", getData())
                    fragment.arguments = bundle
                }
            }
        return fragment as Fragment
    }

    override fun getCount(): Int {
        return 2
    }

    @Nullable
    override fun getPageTitle(position: Int): CharSequence? {
        return mContext.resources.getString(TAB_TITLES[position])
    }

    fun setData(username: String){
        userName = username
    }

    fun getData(): String{
        return userName
    }
}