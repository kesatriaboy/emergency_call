package com.satria.emergencycallasahanv2

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.satria.emergencycallasahanv2.ui.login.LoginActivity
import org.json.JSONObject
import java.io.*

class MainActivity : AppCompatActivity(),View.OnClickListener {

    private val RECORD_REQUEST_CODE = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var readAuthFile: String = readFileAsString("auth.txt")
        if(readAuthFile.isEmpty())
        {
            var i = Intent(applicationContext, RegisterActivity::class.java)
            startActivity(i)
            finish()
        }

        var readReportFile : String = readFileAsString("Report.txt")
        if(!readReportFile.isEmpty())
        {
            // Toast.makeText(applicationContext, readReportFile, Toast.LENGTH_LONG).show()
            var n = readReportFile.split("http")
            for(item: String in n)
            {
                //Toast.makeText(applicationContext, item, Toast.LENGTH_LONG).show()
                Report.sendURL("http"+item)
            }

            writeStringAsFile("","Report.txt")
        }
        val obj = JSONObject(readAuthFile)
        Session.NIK = obj.getString("NIK")
        Session.nama = obj.getString("nama")
        Session.no_hp = obj.getString("no_hp")


        val btnMoveToListNumber: ImageButton = findViewById(R.id.btn_police)
        btnMoveToListNumber.setOnClickListener(this)
        val btn_RS: ImageButton = findViewById(R.id.btn_hospital)
        btn_RS.setOnClickListener(this)
        val btn_Damkar: ImageButton = findViewById(R.id.btn_damkar)
        btn_Damkar.setOnClickListener(this)

            val koneksi =
                application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val netinfo = koneksi.activeNetworkInfo

            if (netinfo != null && netinfo.isConnected) {
                Toast.makeText(applicationContext, "Terkoneksi, Pastikan GPS Anda Aktif", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(applicationContext, "Pastikan Internet dan GPS Anda Aktif", Toast.LENGTH_LONG).show()

            }

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_police -> {
                val moveToadmin= Intent(this@MainActivity, PoliceActivity::class.java)
                moveToadmin.putExtra("kategori", "POLISI")
                startActivity(moveToadmin)
            }

            R.id.btn_hospital -> {
                val moveToadmin= Intent(this@MainActivity, PoliceActivity::class.java)
                moveToadmin.putExtra("kategori", "RS")
                startActivity(moveToadmin)
            }
            R.id.btn_damkar -> {
                val moveToadmin= Intent(this@MainActivity, PoliceActivity::class.java)
                moveToadmin.putExtra("kategori", "DAMKAR")
                startActivity(moveToadmin)
            }
        }
    }


    //Option Menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        setMode(item.itemId)
        return super.onOptionsItemSelected(item)
    }

    private fun setMode(selectedMode: Int) {
        when(selectedMode) {
//            R.id.action_admin -> {
//                val moveToadmin= Intent(this@MainActivity, LoginActivity::class.java)
//                startActivity(moveToadmin)
//            }
//            R.id.action_about -> {
//                val moveToadmin= Intent(this@MainActivity, AdminKominfoActivity::class.java)
//                startActivity(moveToadmin)
//            }
//            R.id.action_tes_menu -> {
////                val moveToadmin= Intent(this@MainActivity, MapsFragment::class.java)
////                startActivity(moveToadmin)
//            }

        }
    }

    fun writeStringAsFile(
        fileContents: String?,
        fileName: String?
    ) {
        val context: Context = applicationContext
        try {
            val out = FileWriter(File(context.filesDir, fileName))
            out.write(fileContents)
            out.close()
        } catch (e: IOException) {
//            Logger.logError(FragmentActivity.TAG, e)
        }
    }

    fun readFileAsString(fileName: String?): String {
        val context: Context = applicationContext
        val stringBuilder = StringBuilder()
        var line: String? = ""
        var `in`: BufferedReader? = null
        try {
            `in` = BufferedReader(FileReader(File(context.filesDir, fileName)))
            while (`in`.readLine().also({ line = it }) != null) stringBuilder.append(line)
        } catch (e: FileNotFoundException) {
//            Logger.logError(FragmentActivity.TAG, e)
        } catch (e: IOException) {
//            Logger.logError(FragmentActivity.TAG, e)
        }
        return stringBuilder.toString()
    }

}
