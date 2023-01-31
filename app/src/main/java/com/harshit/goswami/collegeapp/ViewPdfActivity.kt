package com.harshit.goswami.collegeapp

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.harshit.goswami.collegeapp.databinding.ActivityViewPdfBinding
import java.io.BufferedInputStream
import java.net.URL


class ViewPdfActivity : AppCompatActivity() {
    private lateinit var binding: ActivityViewPdfBinding
    private lateinit var pdfUrl: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewPdfBinding.inflate(layoutInflater)
        setContentView(binding.root)
        pdfUrl = intent.getStringExtra("pdfUrl").toString()
        Thread {
            // do background stuff here
            val url = URL(pdfUrl)
            val httpURLConnection = url.openConnection()
            val inputStream = BufferedInputStream(httpURLConnection.getInputStream())
            runOnUiThread {
                binding.pdfView.fromStream(inputStream)
                    .defaultPage(0)
                    .enableDoubletap(true)
                    .onLoad {
                        binding.AVPProgressIndicator.visibility = View.GONE
                        binding.txtLoading.visibility = View.GONE
                        binding.txtPdfPageNumber.visibility = View.VISIBLE
                    }
                    .onPageChange { page, pageCount ->
                        binding.txtPdfPageNumber.text = "${page + 1} / $pageCount"
                    }
                    .load()
            }
        }.start()

//        binding.webView.loadUrl(pdfUrl)
    }
}