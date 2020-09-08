package com.satria.emergencycallasahanv2

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.satria.emergencycallasahanv2.db.DatabaseContract
import com.satria.emergencycallasahanv2.db.DatabaseContract.EmergencyColumns.Companion.DATE
import com.satria.emergencycallasahanv2.db.EmergencyHelper
import com.satria.emergencycallasahanv2.entity.Emergency
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.activity_emergency_add_update.*
import org.json.JSONArray
import java.text.SimpleDateFormat
import java.util.*

// Activity untuk melakukan penambahan, pembaruan, dan penghapusan data.
    class EmergencyAddUpdateActivity : AppCompatActivity(), View.OnClickListener {

    //Deklarasi Variabel
        private var isEdit = false
        private var emergency: Emergency? = null
        var id : Int = 0
        private var position: Int = 0
        private lateinit var emergencyHelper: EmergencyHelper

        companion object {
            const val EXTRA_EMERGENCY = "extra_note"
            const val EXTRA_POSITION = "extra_position"
            const val REQUEST_ADD = 100
            const val RESULT_ADD = 101
            const val REQUEST_UPDATE = 200
            const val RESULT_UPDATE = 201
            const val RESULT_DELETE = 301
            const val ALERT_DIALOG_CLOSE = 10
            const val ALERT_DIALOG_DELETE = 20
     //\\Deklarasi Variabel
        }

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_emergency_add_update)

            //Panggil instance sampai menerapkan data hasil dari intent.
            emergencyHelper = EmergencyHelper.getInstance(applicationContext)
            emergencyHelper.open()

            emergency = intent.getParcelableExtra(EXTRA_EMERGENCY)
            if (emergency != null) {
                position = intent.getIntExtra(EXTRA_POSITION, 0)
                isEdit = true
            } else {
                emergency = Emergency()
            }

            val actionBarTitle: String
            val btnTitle: String

            if (isEdit) {
                actionBarTitle = "Ubah"
                btnTitle = "Update"

                emergency?.let {
                    edt_name.setText(it.name)
                    edt_kategori.setText(it.kategori)
                    edt_address.setText(it.address)
                    edt_numberPhn.setText(it.numberPhn)
                    edt_longitude.setText(it.longitude)
                    edt_latitude.setText(it.latitude)
                    id = it.id
                }

            } else {
                actionBarTitle = "Tambah"
                btnTitle = "Simpan"
            }

            supportActionBar?.title = actionBarTitle
            supportActionBar?.setDisplayHomeAsUpEnabled(true)

            btn_submit.text = btnTitle
            btn_submit.setOnClickListener(this)
            //\\ panggil instance sampai menerapkan data hasil dari intent.
        }

    //aksi ketika tombol diklik. ketika data berhasil ditambahka ke database,
    // maka akan mengembalikan nilai dengan menggunakan setResult
        override fun onClick(view: View) {
            if (view.id == R.id.btn_submit) {
                val name = edt_name.text.toString().trim()
                val address = edt_address.text.toString().trim()
                val numberPhn = edt_numberPhn.text.toString().trim()
                val latitude = edt_latitude.text.toString().trim()
                val longitude = edt_longitude.text.toString().trim()
                val kategori = edt_kategori.text.toString().trim()


                if (name.isEmpty()) {
                    edt_name.error = "Kolom tidak boleh kosong!"
                    return
                }

//                emergency?.name = name
//                emergency?.address = address
//                emergency?.numberPhn = numberPhn
//                emergency?.longitude = longitude
//                emergency?.latitude = latitude
//
//                val intent = Intent()
//                intent.putExtra(EXTRA_EMERGENCY, emergency)
//                intent.putExtra(EXTRA_POSITION, position)
//
//                val values = ContentValues()
//                values.put(DatabaseContract.EmergencyColumns.NAME, name)
//                values.put(DatabaseContract.EmergencyColumns.ADDRESS, address)
//                values.put(DatabaseContract.EmergencyColumns.NUMBERPHN, numberPhn)
//                values.put(DatabaseContract.EmergencyColumns.LONGITUDE, longitude)
//                values.put(DatabaseContract.EmergencyColumns.LATITUDE, latitude)

                //Melakukan proses input dan pembaruan data.
                //isEdit akan menjadi true pada saat Intent melalui kelas adapter,
                // karena mengirimkan objek listnotes. Lalu pada NoteAddUpdateActivity akan divalidasi.
                // Jika tidak null maka isEdit akan berubah true.
                if (isEdit) {
                    insertData(name,kategori,address,numberPhn,latitude,longitude, id)
                    setResult(RESULT_UPDATE, intent)
                    var intent = Intent(this, AdminActivity::class.java)
                    startActivity(intent)
                    finish()

//                    val result = emergencyHelper.update(emergency?.id.toString(), values).toLong()
//                    if (result > 0) {
//
//                    } else {
//                        Toast.makeText(this@EmergencyAddUpdateActivity, "Gagal mengupdate data", Toast.LENGTH_SHORT).show()
//                    }
                } else {
                    insertData(name,kategori,address,numberPhn,latitude,longitude, id)
                    setResult(RESULT_ADD, intent)

                    var intent = Intent(this, AdminActivity::class.java)
                    startActivity(intent)
                    finish()
//                    emergency?.date = getCurrentDate()
//                    values.put(DATE, getCurrentDate())
//                    val result = emergencyHelper.insert(values)
//
//                    if (result > 0) {
//
//                    } else {
//                        Toast.makeText(this@EmergencyAddUpdateActivity, "Gagal menambah data", Toast.LENGTH_SHORT).show()
//                    }
                }
            }
        }
    //\\Aksi Klik

    //Metode untuk mengambil tanggal dan jam
        private fun getCurrentDate(): String {
            val dateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault())
            val date = Date()

            return dateFormat.format(date)
        }
    //\\untuk mengambil tanggal dan jam

    //memanggil menu_form.xml
        override fun onCreateOptionsMenu(menu: Menu): Boolean {
            if (isEdit) {
                menuInflater.inflate(R.menu.menu_form, menu)
            }
            return super.onCreateOptionsMenu(menu)
        }

    //memberikan fungsi ketika menu diklik.
        override fun onOptionsItemSelected(item: MenuItem): Boolean {
            when (item.itemId) {
                R.id.action_delete -> showAlertDialog(ALERT_DIALOG_DELETE)
                android.R.id.home -> showAlertDialog(ALERT_DIALOG_CLOSE)
            }
            return super.onOptionsItemSelected(item)
        }

    //Pada saat menekan tombol back (kembali), akan memunculkan AlertDialog
        override fun onBackPressed() {
            showAlertDialog(ALERT_DIALOG_CLOSE)
        }

    //metode showAlertDialog untuk memunculkan dialognya dan
    // mengembalikan nilai result untuk diterima halaman MainActivity nantinya
        private fun showAlertDialog(type: Int) {
            val isDialogClose = type == ALERT_DIALOG_CLOSE
            val dialogTitle: String
            val dialogMessage: String

            if (isDialogClose) {
                dialogTitle = "Batal"
                dialogMessage = "Apakah anda ingin membatalkan perubahan pada form?"
            } else {
                dialogMessage = "Apakah anda yakin ingin menghapus item ini?"
                dialogTitle = "Hapus Emergency"
            }

            val alertDialogBuilder = AlertDialog.Builder(this)

            alertDialogBuilder.setTitle(dialogTitle)
            alertDialogBuilder
                .setMessage(dialogMessage)
                .setCancelable(false)
                .setPositiveButton("Ya") { dialog, id ->
                    if (isDialogClose) {
                        finish()
                    } else {
//                        val result = emergencyHelper.deleteById(emergency?.id.toString()).toLong()
                        delete(Integer.parseInt(emergency?.id.toString()))
                        var intent = Intent(this, AdminActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }
                .setNegativeButton("Tidak") { dialog, id -> dialog.cancel() }
            val alertDialog = alertDialogBuilder.create()
            alertDialog.show()
        }

    private fun insertData(nama:String,kategori:String, alamat:String, no_hp:String,lat:String,lng:String,id:Int) {
        val client = AsyncHttpClient()
        val url = "http://103.15.242.196/eca/add_instansi.php?nama=$nama&kategori=$kategori&alamat=$alamat&no_hp=$no_hp&lat=$lat&lng=$lng&id=$id"
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?
            ) {

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
    private fun delete(id:Int) {
        val client = AsyncHttpClient()
        val url = "http://103.15.242.196/eca/add_instansi.php?delete=true&id=$id"
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?
            ) {

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
                 //Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }
    }