package com.satria.emergencycallasahanv2

import android.Manifest
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.android.synthetic.main.activity_main_current_location.*
import java.io.IOException
import java.util.*

class MainCurrentLocation : AppCompatActivity() ,View.OnClickListener{
    var textView1: TextView? =null
    var textView2:TextView? = null
    var textView3:TextView? = null
    var textView4:TextView? = null
    var textView5:TextView? = null
    var fusedLocationProviderClient: FusedLocationProviderClient? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_current_location)
        textView1 = findViewById(R.id.view1)
        textView2 = findViewById<TextView>(R.id.view2)
        textView3 = findViewById<TextView>(R.id.view3)
        textView4 = findViewById<TextView>(R.id.view4)
        textView5 = findViewById<TextView>(R.id.view5)

        //Initialize
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        btn_location.setOnClickListener(this)
    }

    private fun getLocation() {

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationProviderClient!!.lastLocation
            .addOnCompleteListener { task ->
                val location = task.result
                if (location != null) {
                    try {
                        val geocoder = Geocoder(
                            this,
                            Locale.getDefault()
                        )
                        val addresses =
                            geocoder.getFromLocation(
                                location.latitude, location.longitude, 1
                            )
                        textView1!!.text = Html.fromHtml(
                            "<font color='#6200EE'><b>Latitude  :<b></font>"
                                    + addresses[0].latitude
                        )
                        textView2?.setText(
                            Html.fromHtml(
                                (
                                        "<font color='#6200EE'><b>Longitude  :<b></font>"
                                                + addresses[0].longitude
                                        )
                            )
                        )
                        textView3?.setText(
                            Html.fromHtml(
                                (
                                        "<font color='#6200EE'><b>Country  :<b></font>"
                                                + addresses[0].countryName
                                        )
                            )
                        )
                        textView4?.setText(
                            Html.fromHtml(
                                (
                                        "<font color='#6200EE'><b>Locality  :<b></font>"
                                                + addresses[0].locality
                                        )
                            )
                        )
                        textView5?.setText(
                            Html.fromHtml(
                                (
                                        "<font color='#6200EE'><b>Address  :<b></font>"
                                                + addresses[0].getAddressLine(0)
                                        )
                            )
                        )
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_location -> {
                if (ActivityCompat.checkSelfPermission(applicationContext, Manifest.permission.ACCESS_FINE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    getLocation()
                } else {
                    ActivityCompat.requestPermissions(
                        this,
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                        44
                    )
                }
            }
        }
    }
}