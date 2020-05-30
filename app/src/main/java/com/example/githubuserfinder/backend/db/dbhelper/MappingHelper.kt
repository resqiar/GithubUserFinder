package com.example.githubuserfinder.backend.db.dbhelper

import android.database.Cursor
import android.util.Log
import com.example.githubuserfinder.backend.db.DatabaseContract
import com.example.githubuserfinder.backend.model.Item

object MappingHelper {
    fun mapCursorToArrayList(favCursor: Cursor?): ArrayList<Item>{
        val favData = ArrayList<Item>()

        favCursor?.apply {
            while (moveToNext()){
                val id = getInt(getColumnIndexOrThrow(DatabaseContract.FavColums._ID))
                val username = getString(getColumnIndexOrThrow(DatabaseContract.FavColums.USERNAME))
                val avatar = getString(getColumnIndexOrThrow(DatabaseContract.FavColums.AVATAR))

                favData.add(Item(id, username, avatar))
            }
        }
        Log.e("favDATA MAPPING", favData.toString())
        return favData
    }
}