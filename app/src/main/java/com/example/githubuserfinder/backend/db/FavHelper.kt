package com.example.githubuserfinder.backend.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.example.githubuserfinder.backend.db.DatabaseContract.FavColums.Companion.TABLE_NAME
import com.example.githubuserfinder.backend.db.DatabaseContract.FavColums.Companion._ID
import java.sql.SQLException

class FavHelper(context: Context) {

    companion object {
        private const val DB_TABLE = TABLE_NAME
        private lateinit var databaseHelper: DatabaseHelper

        private var INSTANCE : FavHelper? = null
            fun getInstance(context: Context): FavHelper =
                INSTANCE ?: synchronized(this){
                    INSTANCE ?: FavHelper(context)
                }

        private lateinit var db: SQLiteDatabase
    }

    init {
        databaseHelper = DatabaseHelper(context)
    }

    @Throws(SQLException::class)
    fun open(){
        db = databaseHelper.writableDatabase
    }

    fun close(){
        databaseHelper.close()

        if (db.isOpen){
            db.close()
        }
    }

    /* fungsi untuk query semua data */
    fun queryAll():Cursor{
        return db.query(
            DB_TABLE,
            null,
            null,
            null,
            null,
            null,
            "$_ID DESC"
        )
    }

    /* fungsi untuk menyimpan data */
    fun insertData(values: ContentValues): Long{
        return db.insert(
            DB_TABLE,
            null,
            values
        )
    }

    fun updateData(id: String, contentValues: ContentValues):Int {
        return db.update(
            TABLE_NAME,
            contentValues,
            "$_ID = ?",
            arrayOf(id)
        )
    }

    fun deleteData(id: String): Int{
        return db.delete(
            DB_TABLE,
            "$_ID ='$id'",
            null
        )
    }
}