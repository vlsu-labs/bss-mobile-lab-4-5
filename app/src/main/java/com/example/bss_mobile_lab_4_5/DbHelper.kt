package com.example.bss_mobile_lab_4_5

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log

class DBHelper(context: Context?, private val logTag: String) :
    SQLiteOpenHelper(context, "newMyDB", null, 1) {
    override fun onCreate(db: SQLiteDatabase) {
        Log.d(logTag, "--- onCreate database ---")

        db.execSQL(
            "create table mytable ("
                    + "id integer primary key autoincrement,"
                    + "name text,"
                    + "email text,"
                    + "date text" + ");"
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}
}