package com.satria.emergencycallasahanv2.ui.login

import android.app.Activity
import android.content.Intent
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.*
import com.satria.emergencycallasahanv2.AdminActivity
import com.satria.emergencycallasahanv2.AdminKominfoActivity

import com.satria.emergencycallasahanv2.R
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() ,View.OnClickListener{

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val btn_Login: Button = findViewById(R.id.btn_login)
        btn_Login.setOnClickListener(this)


    }

    override fun onClick(v: View?) {
        Toast.makeText(this, username.text, Toast.LENGTH_LONG).show()
        if (username.text.toString().equals("admin") && password.text.toString().equals("admin1"))
        {
            var i = Intent(this, AdminActivity::class.java)
            startActivity(i)
            finish()
        }
        else if (username.text.toString().equals("kominfo") && password.text.toString().equals("kominfo"))
        {
            var i = Intent(this, AdminKominfoActivity::class.java)
            startActivity(i)
            finish()
        }
        else
            Toast.makeText(this, "Username dan Password salah", Toast.LENGTH_LONG).show()
    }
}