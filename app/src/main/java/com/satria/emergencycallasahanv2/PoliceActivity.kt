package com.satria.emergencycallasahanv2

import android.Manifest
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.*
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.satria.emergencycallasahanv2.adapter.InstansiAdapter
import com.satria.emergencycallasahanv2.entity.Emergency
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.activity_admin.progressbar
import kotlinx.android.synthetic.main.activity_police.*
import org.json.JSONArray
import java.io.*
import java.util.*
import kotlin.collections.ArrayList

class PoliceActivity : AppCompatActivity() {
    //Deklarasi variabel
    private lateinit var adapter: InstansiAdapter
    private lateinit var adapter2: InstansiAdapter
    var lat: String= "2.7212919"
    var lng: String= "99.6124759"

    private val RECORD_REQUEST_CODE = 101

    private var isLocationFetched = false

    private lateinit var fusedLocationProviderClient : FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private lateinit var locationManager: LocationManager
    private lateinit var locationRequest: LocationRequest

    companion object {
        private const val EXTRA_STATE = "EXTRA_STATE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_police)
        supportActionBar?.title = "Daftar Telepon Instansi"



        //Initialize
        locationManager = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (this.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && this.checkSelfPermission(
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    RECORD_REQUEST_CODE
                )
                return
            }
        }
        

        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps()
        }

        buildLocationRequest()
        buildLocationCallBack()
        getLocation()

        //setting RecyclerView dan Adapter
        rv_police_1.layoutManager = LinearLayoutManager(this)
        rv_police_1.setHasFixedSize(true)
        adapter2 = InstansiAdapter(this)
        rv_police_1.adapter = adapter2

        rv_police.layoutManager = LinearLayoutManager(this)
        rv_police.setHasFixedSize(true)
        adapter = InstansiAdapter(this)
        rv_police.adapter = adapter



        //getLocation()
//        containerwebimg = findViewById(R.id.containerimg) as ImageView

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if(requestCode==RECORD_REQUEST_CODE){
        if (grantResults[0] === PackageManager.PERMISSION_GRANTED) {
            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                buildAlertMessageNoGps()
            }

            buildLocationRequest()
            buildLocationCallBack()
            getLocation()

        } else {
            Toast.makeText(
                this,
                "Kami tidak bisa mendapatkan lokasi anda, mohon izinkan untuk mendapatkan lokasi",
                Toast.LENGTH_LONG
            ).show()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (this.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && this.checkSelfPermission(
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                        RECORD_REQUEST_CODE
                    )
                    return
                }
            }
        }
    }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun buildLocationCallBack() {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                // Toast.makeText(this@PoliceActivity,"Location Fetched",Toast.LENGTH_LONG).show()
                if(!isLocationFetched){
                    for (location in locationResult.locations) {
                        isLocationFetched = true
                        getLocation2(location)
                    }
                }
            }
        }
    }

    private fun buildLocationRequest() {
        locationRequest = LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 4000
        locationRequest.fastestInterval = 2000
        locationRequest.smallestDisplacement = 10f
    }

    private fun getLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) !== PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) !== PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                RECORD_REQUEST_CODE
            )
            return
        }
        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.myLooper()
        )
    }

    private fun buildAlertMessageNoGps() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setMessage("Untuk mendapatkan lokasi, hidupkan GPS?")
            .setCancelable(false).setPositiveButton("Ya",
                DialogInterface.OnClickListener { dialog, id -> startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)) })
            .setNegativeButton("Tidak",
                DialogInterface.OnClickListener { dialog, id ->
                    Toast.makeText(
                        this,
                        "Lokasi anda tidak bisa di deteksi",
                        Toast.LENGTH_LONG
                    ).show()
                    var kategori = intent.getStringExtra("kategori")
                    getListPolice("", "", kategori,false)
                })
        val alert: AlertDialog = builder.create()
        alert.show()
    }

    fun getLocation2(location: Location)
    {

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

                Log.d("LOC", "YES")
           //     Toast.makeText(applicationContext, location.latitude.toString(),Toast.LENGTH_LONG).show()

                lat = location.latitude.toString()
                lng = location.longitude.toString()

                Report.lat = lat
                Report.lng = lng

                var kategori = intent.getStringExtra("kategori")

                getListPolice(lat, lng, kategori,true)

            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        else
        {
            //Toast.makeText(applicationContext,"GPS tidak terbaca",Toast.LENGTH_SHORT).show()
            var kategori = intent.getStringExtra("kategori")
            getListPolice("", "", kategori,false)
        }
    }


    //mengambil data dari database dengan menggunakan background thread.
    //dan memanfaatkan MappingHelper untuk mengonversi dari
    // Cursor ke Arraylist untuk dikirim ke adapter
    /*private fun loadEmergencyAsync() {
        GlobalScope.launch(Dispatchers.Main) {
            progressbar.visibility = View.VISIBLE

                       // membuat nilai kembalian dari fungsi yang di panggil
            val deferredEmergency = async(Dispatchers.IO) {
               // val cursor = emergencyHelper.ambilTerdekat(lat, lng)
               // MappingHelper.mapCursorToArrayList(cursor)
            }
            progressbar.visibility = View.INVISIBLE
            //mendapatkan nilai kembaliannya, kita menggunakan fungsi await().
            val emergencys = deferredEmergency.await()
            if (emergencys.size > 0) {
                adapter.listEmergency = emergencys
            } else {
                adapter.listEmergency = ArrayList()
                showSnackbarMessage("Tidak ada data saat ini")
            }
        }
    }*/

    //Menutup akses database menggunakan EmergencyHelper
    override fun onDestroy() {
        super.onDestroy()
    }

    private fun getListPolice(lat: String, lng: String, kategori: String, wv : Boolean) {

        progressbar.visibility = View.VISIBLE
        val listPolice = ArrayList<Emergency>()
        val listPolice2 = ArrayList<Emergency>()
        val koneksi =
            application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val netinfo = koneksi.activeNetworkInfo

        if (netinfo != null && netinfo.isConnected && wv == true) {
            //Toast.makeText(applicationContext, "Connected", Toast.LENGTH_LONG).show()
            val client = AsyncHttpClient()
            var url = ""
            if(lat == "")
                url = "http://103.15.242.196/eca/get_instansi.php?kategori=$kategori"
            else
                url = "http://103.15.242.196/eca/get_instansi.php?terdekat=true&lat=$lat&long=$lng&kategori=$kategori"
            client.get(url, object : AsyncHttpResponseHandler() {
                override fun onSuccess(
                    statusCode: Int,
                    headers: Array<out Header>?,
                    responseBody: ByteArray?
                ) {
                    val result = responseBody?.let { String(it) }
                    if (result != null) {
                        var fos: FileOutputStream? = null
                        try {
                            var FILE_NAME = "file-$kategori.txt"
                            fos = openFileOutput(FILE_NAME, MODE_PRIVATE)
                            fos.write(result.toByteArray())
                        } catch (e: FileNotFoundException) {
                            e.printStackTrace()
                        } catch (e: IOException) {
                            e.printStackTrace()
                        } finally {
                            if (fos != null) {
                                try {
                                    fos.close()
                                } catch (e: IOException) {
                                    e.printStackTrace()
                                }
                            }
                        }
                    }

                    try {

                        val jsonArray = JSONArray(result)

                        // perulangan untuk mendapatkan JSONObject yang ada di dalam API
                        for (i in 0 until jsonArray.length()) {
                            val jsonObject = jsonArray.getJSONObject(i)
                            if (i == 0) {
                                var emergency = Emergency()
                                emergency.name = jsonObject.getString("nama_instansi")
                                emergency.address = jsonObject.getString("alamat")
                                emergency.numberPhn = jsonObject.getString("noHpInstansi")
                                listPolice2.add(emergency)
                            } else {
                                var emergency = Emergency()
                                emergency.name = jsonObject.getString("nama_instansi")
                                emergency.address = jsonObject.getString("alamat")
                                emergency.numberPhn = jsonObject.getString("noHpInstansi")
                                listPolice.add(emergency)
                            }
                        }
                        adapter.listEmergency = listPolice
                        adapter2.listEmergency = listPolice2

                        val wv: WebView = findViewById(R.id.containerweb)

                        val jsonObject = jsonArray.getJSONObject(0)
                        val lat = jsonObject.getString("latitute")
                        val lng = jsonObject.getString("longitude")

                        wv.webViewClient = object : WebViewClient() {
                            override fun shouldOverrideUrlLoading(
                                view: WebView?,
                                url: String?
                            ): Boolean {
                                view?.loadUrl(url)
                                return true
                            }
                        }

                        wv.loadUrl("http://103.15.242.196/eca/maps-render.php?lat=$lat&lng=$lng")
                        wv.settings.javaScriptEnabled = true
                        wv.settings.allowContentAccess = true
                        wv.settings.domStorageEnabled = true

                        progressbar.visibility = View.INVISIBLE
                    } catch (e: java.lang.Exception) {
                        // Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
                        e.printStackTrace()
                    }
                }

                override fun onFailure(
                    statusCode: Int,
                    headers: Array<out Header>?,
                    responseBody: ByteArray?,
                    error: Throwable?
                ) {
                    //Jika koneksi gagal
                    val errorMessage = when (statusCode) {
                        401 -> "$statusCode : Bad Request"
                        403 -> "$statusCode : Forbidden"
                        404 -> "$statusCode : Not Found"
                        else -> "$statusCode : ${error?.message}"
                    }
                    // Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                }
            })
        } else {
           // Toast.makeText(applicationContext, "Tidak ada koneksi", Toast.LENGTH_LONG).show()

            containerweb.visibility = View.GONE
            rv_police_1.visibility = View.GONE
            lateinit var result : String
            var fis: FileInputStream? = null
            try {
                var FILE_NAME = "file-$kategori.txt"
                fis = openFileInput(FILE_NAME)
                val isr = InputStreamReader(fis)
                val br = BufferedReader(isr)
                val sb = java.lang.StringBuilder()
                var text: String?
                while (br.readLine().also { text = it } != null) {
                    sb.append(text).append("\n")
                }
                result = sb.toString()
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
                if (fis != null) {
                    try {
                        fis.close()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }

            try {

                val jsonArray = JSONArray(result)

                // perulangan untuk mendapatkan JSONObject yang ada di dalam API
                for (i in 0 until jsonArray.length()) {
                    val jsonObject = jsonArray.getJSONObject(i)
//                    if(i == 0)
//                    {
//                        var emergency = Emergency()
//                        emergency.name = jsonObject.getString("nama_instansi")
//                        emergency.address = jsonObject.getString("alamat")
//                        emergency.numberPhn = jsonObject.getString("noHpInstansi")
//                        listPolice2.add(emergency)
//                    }
//                    else
//                    {
                        var emergency = Emergency()
                        emergency.name = jsonObject.getString("nama_instansi")
                        emergency.address = jsonObject.getString("alamat")
                        emergency.numberPhn = jsonObject.getString("noHpInstansi")
                        listPolice.add(emergency)
//                    }
                }
                adapter.listEmergency = listPolice
//                adapter2.listEmergency = listPolice2
                progressbar.visibility = View.INVISIBLE

            } catch (e: java.lang.Exception) {
                // Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
                e.printStackTrace()
            }
        }
    }

}
