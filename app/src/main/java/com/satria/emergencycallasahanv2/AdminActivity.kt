package com.satria.emergencycallasahanv2

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.satria.emergencycallasahanv2.adapter.EmergencyAdapter
import com.satria.emergencycallasahanv2.db.EmergencyHelper
import com.satria.emergencycallasahanv2.entity.Emergency
import com.satria.emergencycallasahanv2.helper.MappingHelper
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.activity_admin.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.json.JSONArray

class AdminActivity : AppCompatActivity() {
    //Deklarasi variabel
    private lateinit var adapter: EmergencyAdapter
    private lateinit var emergencyHelper: EmergencyHelper

    companion object {
        private const val EXTRA_STATE = "EXTRA_STATE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)

        //Judul Activity
        supportActionBar?.title = "Emergency Call App"

        //setting RecyclerView dan Adapter
        rv_emergency.layoutManager = LinearLayoutManager(this)
        rv_emergency.setHasFixedSize(true)
        adapter = EmergencyAdapter(this)
        rv_emergency.adapter = adapter

        //Tombol pindah ke NoteAddUpdateActivity untuk mendapatkan nilai result
        // menambah dan meng-edit data dengan Intent Result darinya.
        fab_add.setOnClickListener {
            val intent = Intent(this@AdminActivity, EmergencyAddUpdateActivity::class.java)
            startActivityForResult(intent, EmergencyAddUpdateActivity.REQUEST_ADD)
        }
        fab_move_report.setOnClickListener {
            val intent = Intent(this@AdminActivity, AdminKominfoActivity::class.java)
            startActivityForResult(intent, EmergencyAddUpdateActivity.REQUEST_ADD)
        }

        //mengambil data dari database dengan memanfaatkan EmergencyHelper
        emergencyHelper = EmergencyHelper.getInstance(applicationContext)
        emergencyHelper.open()
        //\\mengambil data dari database dengan memanfaatkan EmergencyHelper

        //Mempertahankan data saat perangkat dirotasi
        if (savedInstanceState == null) {
            //proses ambil data
            getListPolice()
        } else {
            val list = savedInstanceState.getParcelableArrayList<Emergency>(EXTRA_STATE)
            if (list != null) {
                adapter.listEmergency = list
            }
        }
    }

    //Lanjutan //Mempertahankan data saat perangkat di rotasi.
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(EXTRA_STATE, adapter.listEmergency)
    }

    // Lanjutan //memanggil NoteAddUpdateActivity untuk mendapatkan nilai result
    //        // menambah dan meng-edit data dengan Intent Result darinya.
    // fungsi ini akan melakukan penerimaan data dari intent yang dikirimkan dan
    // diseleksi berdasarkan jenis requestCode dan resultCode-nya.
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (data != null) {
            when (requestCode) {
                // akan dijalankan ketika terjadi penambahan data pada NoteAddUpdateActivity
                EmergencyAddUpdateActivity.REQUEST_ADD -> if (resultCode == EmergencyAddUpdateActivity.RESULT_ADD) {
                    val emergency = data.getParcelableExtra<Emergency>(EmergencyAddUpdateActivity.EXTRA_EMERGENCY)

                    adapter.addItem(emergency)
                    rv_emergency.smoothScrollToPosition(adapter.itemCount - 1)

                    showSnackbarMessage("Satu item berhasil ditambahkan")
                }
                // Update dan Delete memiliki request code sama akan tetapi result codenya berbeda
                EmergencyAddUpdateActivity.REQUEST_UPDATE ->
                    when (resultCode) {
                        EmergencyAddUpdateActivity.RESULT_UPDATE -> {

                            val emergency = data.getParcelableExtra<Emergency>(EmergencyAddUpdateActivity.EXTRA_EMERGENCY)
                            val position = data.getIntExtra(EmergencyAddUpdateActivity.EXTRA_POSITION, 0)

                            adapter.updateItem(position, emergency)
                            rv_emergency.smoothScrollToPosition(position)

                            showSnackbarMessage("Satu item berhasil diubah")
                        }

                        /*
                    Akan dipanggil jika result codenya DELETE
                    Delete akan menghapus data dari list berdasarkan dari position
                    */
                        EmergencyAddUpdateActivity.RESULT_DELETE -> {
                            val position = data.getIntExtra(EmergencyAddUpdateActivity.EXTRA_POSITION, 0)

                            adapter.removeItem(position)

                            showSnackbarMessage("Satu item berhasil dihapus")
                        }
                    }
            }
        }
    }

    //Menutup akses database menggunakan EmergencyHelper
    override fun onDestroy() {
        super.onDestroy()
        emergencyHelper.close()
    }

    //fungsi dialog saat aksi
    private fun showSnackbarMessage(message: String) {
        Snackbar.make(rv_emergency, message, Snackbar.LENGTH_SHORT).show()
    }

    private fun getListPolice() {
        progressbar.visibility = View.VISIBLE
        val client = AsyncHttpClient()
        val url = "http://103.15.242.196/eca/get_instansi.php?get=true"
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?
            ) {
                val listPolice = ArrayList<Emergency>()

                val result = responseBody?.let { String(it) }
                Log.d("ERR", result)
                try {

                    val jsonArray = JSONArray(result)

                    // perulangan untuk mendapatkan JSONObject yang ada di dalam API
                    for (i in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(i)
                        var emergency = Emergency()
                        emergency.id = Integer.parseInt(jsonObject.getString("id_instansi"))
                        emergency.name = jsonObject.getString("nama_instansi")
                        emergency.kategori = jsonObject.getString("kategori")
                        emergency.address = jsonObject.getString("alamat")
                        emergency.numberPhn = jsonObject.getString("noHpInstansi")
                        emergency.latitude = jsonObject.getString("latitute")
                        emergency.longitude = jsonObject.getString("longitude")
                        listPolice.add(emergency)
                    }
                    adapter.listEmergency = listPolice
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
}