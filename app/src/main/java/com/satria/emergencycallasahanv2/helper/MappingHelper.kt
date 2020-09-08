package com.satria.emergencycallasahanv2.helper

import android.database.Cursor
import com.satria.emergencycallasahanv2.db.DatabaseContract
import com.satria.emergencycallasahanv2.entity.Emergency


//karena nanti di adapter akan menggunakan arraylist,
// sedangkan di sini objek yang di kembalikan berupa Cursor,
// maka dari itu harus di konversi dari Cursor ke Arraylist.

object MappingHelper {

    fun mapCursorToArrayList(notesCursor: Cursor?): ArrayList<Emergency> {
        val emergencyList = ArrayList<Emergency>()
        notesCursor?.apply {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(DatabaseContract.EmergencyColumns._ID))
                val name = getString(getColumnIndexOrThrow(DatabaseContract.EmergencyColumns.NAME))
                val address = getString(getColumnIndexOrThrow(DatabaseContract.EmergencyColumns.ADDRESS))
                val numberPhn= getString(getColumnIndexOrThrow(DatabaseContract.EmergencyColumns.NUMBERPHN))
                val longitude = getString(getColumnIndexOrThrow(DatabaseContract.EmergencyColumns.LONGITUDE))
                val latitude= getString(getColumnIndexOrThrow(DatabaseContract.EmergencyColumns.LATITUDE))
                val date = getString(getColumnIndexOrThrow(DatabaseContract.EmergencyColumns.DATE))
                emergencyList.add(Emergency(id, name, address,numberPhn, longitude, latitude ,date))
            }
        }
        return emergencyList
    }
}