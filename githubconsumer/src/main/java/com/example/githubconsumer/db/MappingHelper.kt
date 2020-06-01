package com.example.githubconsumer.db

import android.database.Cursor
import android.util.Log
import com.example.githubconsumer.model.Item

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

    fun mapCursorToObject(favCursor: Cursor?): Item {
        var fav = Item()
        favCursor?.apply {
            moveToFirst()
            val id = getInt(getColumnIndexOrThrow(DatabaseContract.FavColums._ID))
            val username = getString(getColumnIndexOrThrow(DatabaseContract.FavColums.USERNAME))
            val avatar = getString(getColumnIndexOrThrow(DatabaseContract.FavColums.AVATAR))

            fav = Item(id, username, avatar, 0, 0, 0)
        }
        return fav
    }
}