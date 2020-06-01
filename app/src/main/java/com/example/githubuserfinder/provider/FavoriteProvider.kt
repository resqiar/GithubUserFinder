package com.example.githubuserfinder.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.example.githubuserfinder.backend.db.DatabaseContract.AUTHORITY
import com.example.githubuserfinder.backend.db.DatabaseContract.FavColums.Companion.CONTENT_URI
import com.example.githubuserfinder.backend.db.DatabaseContract.FavColums.Companion.TABLE_NAME
import com.example.githubuserfinder.backend.db.FavHelper

class FavoriteProvider : ContentProvider() {

    companion object{
        private const val FAV = 1
        private const val FAV_ID = 2
        private val sUriMatcher = UriMatcher(UriMatcher.NO_MATCH)
        private lateinit var favHelper: FavHelper

        init {
            sUriMatcher.addURI(AUTHORITY, TABLE_NAME, FAV)
            sUriMatcher.addURI(AUTHORITY, "$TABLE_NAME/#", FAV_ID)
        }
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
       val deleted: Int = when(FAV_ID){
           sUriMatcher.match(uri) -> favHelper.deleteData(uri.lastPathSegment.toString())
           else -> 0
       }
        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        return deleted
    }

    override fun getType(uri: Uri): String? {
        TODO(
            "Implement this to handle requests for the MIME type of the data" +
                    "at the given URI"
        )
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val add: Long = when(FAV){
            sUriMatcher.match(uri) -> favHelper.insertData(values!!)
            else -> 0
        }
        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        return Uri.parse("$CONTENT_URI/$add")
    }

    override fun onCreate(): Boolean {
        favHelper = FavHelper.getInstance(context as Context)
        favHelper.open()

        return true
    }

    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor? {
        return when(sUriMatcher.match(uri)){
            FAV -> favHelper.queryAll()
            else -> null
        }
    }

    override fun update(
        uri: Uri, values: ContentValues?, selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        val updated: Int = when(FAV_ID){
            sUriMatcher.match(uri) -> favHelper.updateData(uri.lastPathSegment.toString(), values!!)
            else -> 0
        }

        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        return updated
    }
}
