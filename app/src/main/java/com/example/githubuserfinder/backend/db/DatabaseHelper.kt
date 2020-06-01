package com.example.githubuserfinder.backend.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.githubuserfinder.backend.db.DatabaseContract.FavColums.Companion.AVATAR
import com.example.githubuserfinder.backend.db.DatabaseContract.FavColums.Companion.TABLE_NAME
import com.example.githubuserfinder.backend.db.DatabaseContract.FavColums.Companion.USERNAME
import com.example.githubuserfinder.backend.db.DatabaseContract.FavColums.Companion._ID

class DatabaseHelper(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "dbfavuser"
        private const val DATABASE_VERSION = 1
        private const val SQL_CREATE_TABLE = "CREATE TABLE $TABLE_NAME" +
                "($_ID INTEGER PRIMARY KEY," +
                "$USERNAME TEXT NOT NULL UNIQUE," +
                "$AVATAR TEXT NOT NULL)"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(SQL_CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

}