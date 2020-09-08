package com.satria.emergencycallasahanv2

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.satria.emergencycallasahanv2.adapter.ReportAdapter
import com.satria.emergencycallasahanv2.db.EmergencyHelper
import com.satria.emergencycallasahanv2.entity.ReportKominfo
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.activity_admin.*
import kotlinx.android.synthetic.main.activity_admin_kominfo.*
import kotlinx.android.synthetic.main.activity_admin_kominfo.progressbar
import org.json.JSONArray
import java.io.*


class AdminKominfoActivity : AppCompatActivity()  {
    private lateinit var adapter: ReportAdapter
    private lateinit var emergencyHelper: EmergencyHelper
    var lat: String= "2.7212919"
    var lng: String= "99.6124759"

    private val list = ArrayList<ReportKominfo>()

    var fusedLocationProviderClient: FusedLocationProviderClient? = null

    companion object {
        private const val EXTRA_STATE = "EXTRA_STATE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_kominfo)
        supportActionBar?.title = "Data Laporan"

        //Initialize
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        //setting RecyclerView dan Adapter
        rv_report.layoutManager = LinearLayoutManager(this)
        rv_report.setHasFixedSize(true)
        adapter = ReportAdapter(this)
        rv_report.adapter = adapter
        getListReport()
    }

    private fun getListReport() {
        progressbar.visibility = View.VISIBLE
        val listReportKominfo = ArrayList<ReportKominfo>()
        val client = AsyncHttpClient()
        val url = "http://103.15.242.196/eca/lapor.php?get=true"
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?
            ) {
                val result = responseBody?.let { String(it) }
                Log.d("ERR", result)

                try {

                    val jsonArray = JSONArray(result)

                    // perulangan untuk mendapatkan JSONObject yang ada di dalam API
                    for (i in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(i)
                        var report = ReportKominfo()
                        report.instansi = jsonObject.getString("instansi_tujuan")
                        report.status = jsonObject.getString("status")
                        report.latitute = jsonObject.getString("lat")
                        report.longitude = jsonObject.getString("lng")
                        report.date = jsonObject.getString("tanggal")
                        report.nama = jsonObject.getString("nama")
                        report.nik = jsonObject.getString("NIK")
                        report.no_hp = jsonObject.getString("no_hp")
                        listReportKominfo.add(report)
                    }
                    adapter.listReport = listReportKominfo
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
        
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    //Lanjutan //Mempertahankan data saat perangkat di rotasi.
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(AdminKominfoActivity.EXTRA_STATE, adapter.listReport)
    }
}