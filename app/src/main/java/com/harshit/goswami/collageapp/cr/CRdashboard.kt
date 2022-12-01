package com.harshit.goswami.collageapp.cr

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.harshit.goswami.collageapp.databinding.ActivityCrdashboardBinding

class CRdashboard : AppCompatActivity() {
    private lateinit var binding: ActivityCrdashboardBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCrdashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.uploadNotes.setOnClickListener{startActivity(Intent(this,UploadNotes::class.java))}
        binding.uploadEvent.setOnClickListener{startActivity(Intent(this,NewEvent::class.java))}
       // binding.uploadNotes.setOnClickListener{startActivity(Intent(this,UploadNotes::class.java))}
    }
}