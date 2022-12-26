package com.harshit.goswami.collegeapp.teacher

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.harshit.goswami.collegeapp.databinding.ActivityTeacherDashboardBinding

class TeacherDashboard : AppCompatActivity() {
    private lateinit var binding: ActivityTeacherDashboardBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTeacherDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.uploadNotes.setOnClickListener{startActivity(Intent(this,UploadNotes::class.java))}
        binding.uploadEvent.setOnClickListener{startActivity(Intent(this,NewEvent::class.java))}
    }
}