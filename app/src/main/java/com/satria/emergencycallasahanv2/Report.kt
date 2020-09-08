package com.satria.emergencycallasahanv2

import android.content.Context
import android.net.ConnectivityManager
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import java.io.File
import java.io.FileWriter
import java.io.IOException

class Report {
    companion object {
        var lat = ""
        var lng = ""
        var context: Context? = null
        fun send(instansi: String, context: Context) {
            this.context = context
            val client = AsyncHttpClient()
            val NIK = Session.NIK
            val nama= Session.nama
            val no_hp = Session.no_hp
            val url = "http://103.15.242.196/eca/lapor.php?lat=$lat&lng=$lng&instansi=$instansi&NIK=$NIK&nama=$nama&no_hp=$no_hp"

            val koneksi = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val netinfo = koneksi.activeNetworkInfo

            if (netinfo != null && netinfo.isConnected) {
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
                Toast.makeText(context, "Terkoneksi", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(context, "Tidak ada koneksi", Toast.LENGTH_LONG).show()
                writeStringAsFile(url+System.getProperty("line.separator"), "Report.txt")

            }
        }

        fun sendURL(url: String) {
            val client = AsyncHttpClient()
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

        fun writeStringAsFile(
            fileContents: String?,
            fileName: String?
        ) {
            val context: Context? = this.context
            try {
                val out = FileWriter(File(context?.filesDir, fileName), true)
                out.write(fileContents)
                out.close()
            } catch (e: IOException) {
//            Logger.logError(FragmentActivity.TAG, e)
            }
        }
    }
}