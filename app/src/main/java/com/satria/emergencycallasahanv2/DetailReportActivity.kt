package com.satria.emergencycallasahanv2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast

class DetailReportActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_report)

        val wv: WebView = findViewById(R.id.containerweb)


        wv.webViewClient= object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView?,
                url:String?
            ): Boolean {
                view?.loadUrl(url)
                return true
            }
        }

        val lat = getIntent().getStringExtra("lat")
        val lng = getIntent().getStringExtra("lng")

        wv.loadUrl("http://103.15.242.196/eca/maps-render.php?lat=$lat&lng=$lng")
        wv.settings.javaScriptEnabled = true
        wv.settings.allowContentAccess=true
        wv.settings.domStorageEnabled=true
        Toast.makeText(this, "http://103.15.242.196/eca/maps-render.php?lat=$lat&lng=$lng", Toast.LENGTH_SHORT).show()
    }

}