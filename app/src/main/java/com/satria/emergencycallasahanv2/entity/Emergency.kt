package com.satria.emergencycallasahanv2.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

//kelas model getter setter
@Parcelize
data class Emergency(
    var id: Int = 0,
    var name: String? = null,
    var kategori: String? = null,
    var address: String? = null,
    var numberPhn: String? = null,
    var longitude: String? = null,
    var latitude: String? = null,
    var date: String? = null
) : Parcelable