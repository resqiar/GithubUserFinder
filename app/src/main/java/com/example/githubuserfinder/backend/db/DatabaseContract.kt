package com.example.githubuserfinder.backend.db

import android.net.Uri
import android.provider.BaseColumns


object DatabaseContract {
    const val AUTHORITY = "com.example.githubuserfinder"
    const val SCHEME = "content"

    internal class FavColums: BaseColumns{
        companion object {
            const val TABLE_NAME = "favorite_user"
            const val _ID = "_id"
            const val USERNAME = "user_name"
            const val AVATAR = "avatar"

            // URI CONTENT PROVIDER
            val CONTENT_URI = Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_NAME)
                .build()
        }
    }
}