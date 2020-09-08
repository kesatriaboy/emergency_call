package com.satria.emergencycallasahanv2.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.satria.emergencycallasahanv2.db.DatabaseContract.EmergencyColumns.Companion.TABLE_NAME
import com.satria.emergencycallasahanv2.db.DatabaseContract.EmergencyColumns.Companion._ID
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

//sebuah kelas yang akan mengakomodasi kebutuhan DML bernama EmergencyHelper

class EmergencyHelper(context: Context) {

    private var dataBaseHelper: DatabaseHelper = DatabaseHelper(context)
    //construktor
    init {
        dataBaseHelper = DatabaseHelper(context)
    }
    private lateinit var database: SQLiteDatabase

    //variabel yang dibutuhan seperti berikut
    companion object {
        private const val DATABASE_TABLE = TABLE_NAME

        //Metode yang digunakan untuk menginisiasi database
        private var INSTANCE: EmergencyHelper? = null

        //melakukan proses manipulasi data yang berada di dalam tabel
        // seperti query untuk pembacaan data yang diurutkan secara ascending.
        fun getInstance(context: Context): EmergencyHelper =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: EmergencyHelper(context)
            }
    }

    //Metode Membuka Database
    @Throws(SQLException::class)
    fun open() {
        database = dataBaseHelper.writableDatabase
    }

    //Metode Menutup Database
    fun close() {
        dataBaseHelper.close()

        if (database.isOpen)
            database.close()
    }

    //Metode CRUD
    //1. Mengambil Data
    fun queryAll(): Cursor {
        return database.query(
            DATABASE_TABLE,
            null,
            null,
            null,
            null,
            null,
            "$_ID ASC",
            null)
    }

    fun ambilTerdekat(lat: String, lng: String):Cursor {
          return database.rawQuery("SELECT * FROM $DATABASE_TABLE", null)
//        return database.rawQuery(
//            "SELECT name, address, numberPhn,longitude,latitute, ( 3959 * acos( cos( radians($lat)" +
//                    " * cos( radians( `latitute` ) ) * cos( radians( `longitude` )" +
//                    " - radians($lng) ) + sin( radians("+lat+") ) * sin(radians(`latitute`)) ) )" +
//                    " AS distance FROM "+DATABASE_TABLE+" ORDER BY distance LIMIT 20",
//            null
//        )
    }


    //2. mengambal data dengan id tertentu.
    fun queryById(id: String): Cursor {
        return database.query(DATABASE_TABLE, null, "$_ID = ?", arrayOf(id), null, null, null, null)
    }

    //3. Menyimpan Data
    fun insert(values: ContentValues?): Long {
        return database.insert(DATABASE_TABLE, null, values)
    }

    //4. Update Data
    fun update(id: String, values: ContentValues?): Int {
        return database.update(DATABASE_TABLE, values, "$_ID = ?", arrayOf(id))
    }

    //5. Hapus Data
    fun deleteById(id: String): Int {
        return database.delete(DATABASE_TABLE, "$_ID = '$id'", null)
    }
}