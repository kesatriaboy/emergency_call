package com.satria.emergencycallasahanv2.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

//membangun database baik dari sisi DDL
internal class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {

        private const val DATABASE_NAME = "dbemergencyapp"

        private const val DATABASE_VERSION = 1

        //Dengan memanfaatkan kelas contract (variabel yang ada pada DatabaseContract),
        // maka akses nama tabel dan nama kolom tabel menjadi lebih mudah.
        private val SQL_CREATE_TABLE_NOTE = "CREATE TABLE " +
                "${DatabaseContract.EmergencyColumns.TABLE_NAME}" +
                " (${DatabaseContract.EmergencyColumns._ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
                " ${DatabaseContract.EmergencyColumns.NAME} TEXT NOT NULL," +
                " ${DatabaseContract.EmergencyColumns.ADDRESS} TEXT NOT NULL," +
                " ${DatabaseContract.EmergencyColumns.NUMBERPHN} TEXT NOT NULL," +
                " ${DatabaseContract.EmergencyColumns.LONGITUDE} TEXT NOT NULL," +
                " ${DatabaseContract.EmergencyColumns.LATITUDE} TEXT NOT NULL," +
                " ${DatabaseContract.EmergencyColumns.DATE} TEXT NOT NULL)"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_TABLE_NOTE)
    }

    //menciptakan database dengan tabel yang dibutuhkan
    // dan handle ketika terjadi perubahan skema pada tabel
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS ${DatabaseContract.EmergencyColumns.TABLE_NAME}")
        onCreate(db)
    }
}