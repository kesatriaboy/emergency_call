package com.satria.emergencycallasahanv2.db

import android.provider.BaseColumns

//membangun database dari sisi DDL

//Kelas contract ini akan digunakan untuk mempermudah akses nama tabel
// dan nama kolom di dalam database.
internal class DatabaseContract {

    internal class EmergencyColumns : BaseColumns {
        companion object {
            const val TABLE_NAME = "instansi"
            const val _ID = "_id"
            const val NAME = "name"
            const val ADDRESS = "address"
            const val NUMBERPHN = "numberphn"
            const val LONGITUDE = "longitude"
            const val LATITUDE = "latitude"
            const val DATE = "date"
        }
    }
}