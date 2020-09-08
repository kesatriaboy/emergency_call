package com.satria.emergencycallasahanv2

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_register.*
import java.io.*

class RegisterActivity : AppCompatActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        btn_register.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        if(v == btn_register)
        {
            val nik: String = NIK.getText().toString().trim({ it <= ' ' })
            val name: String = nama.getText().toString().trim({ it <= ' ' })
            val number: String = no_hp.getText().toString().trim({it <= ' '})

            var isEmtyFields = false

            if (TextUtils.isEmpty(nik)) {
                isEmtyFields = true
                NIK.setError("Kolom ini tidak boleh kosong")
            }

            if (TextUtils.isEmpty(name)) {
                isEmtyFields = true
                nama.setError("Kolom ini tidak boleh kosong")
            }
            if (TextUtils.isEmpty(number)) {
            isEmtyFields = true
            no_hp.setError("Kolom ini tidak boleh kosong")
        }


            if (!isEmtyFields) {

                var content =
                    "{\"NIK\":\"" + NIK.getText().toString() + "\",\"nama\":\"" + nama.getText()
                        .toString() + "\",\"no_hp\":\"" + no_hp.getText().toString() + "\"}"
                writeStringAsFile(content, "auth.txt")
                var i = Intent(applicationContext, MainActivity::class.java)
                startActivity(i)
                finish()
            }
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

    fun readFileAsString(fileName: String?): String? {
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