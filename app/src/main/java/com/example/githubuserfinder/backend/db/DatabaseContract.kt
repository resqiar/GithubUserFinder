package com.example.githubuserfinder.backend.db

import android.provider.BaseColumns

class DatabaseContract {
    internal class FavColums: BaseColumns{
        companion object {
            const val TABLE_NAME = "favorite_user"
            const val _ID = "_id"
            const val USERNAME = "user_name"
            const val AVATAR = "avatar"
        }
    }
}