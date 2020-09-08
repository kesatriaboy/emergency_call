package com.satria.emergencycallasahanv2.entity

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ReportKominfo(
    var id: Int = 0,
    var instansi: String? = null,
    var status: String? = null,
    var longitude: String? = null,
    var latitute: String? = null,
    var date: String? = null,
    var nama: String? = null,
    var nik: String? = null,
    var no_hp: String? = null
) : Parcelable