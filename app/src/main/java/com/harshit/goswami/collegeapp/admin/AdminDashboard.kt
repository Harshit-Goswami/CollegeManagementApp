package com.harshit.goswami.collegeapp.admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.harshit.goswami.collegeapp.databinding.ActivityAdminDashboardBinding


class AdminDashboard : AppCompatActivity() {
    private lateinit var binding: ActivityAdminDashboardBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.cardUploadNotice.setOnClickListener { startActivity(Intent(this,UploadNotice::class.java)) }
        binding.uploadImage.setOnClickListener { startActivity(Intent(this,UploadImage::class.java)) }
        binding.uploadBook.setOnClickListener { startActivity(Intent(this,ManageStudent::class.java)) }
        binding.updateFaculty.setOnClickListener { startActivity(Intent(this,ManageFaculty::class.java)) }
        binding.deleteNotice.setOnClickListener { startActivity(Intent(this,DeleteNotice::class.java)) }
    }
}