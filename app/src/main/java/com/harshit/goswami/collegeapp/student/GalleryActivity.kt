package com.harshit.goswami.collegeapp.student

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.harshit.goswami.collegeapp.R
import com.harshit.goswami.collegeapp.databinding.ActivityStudentGalleryBinding

class GalleryActivity : AppCompatActivity() {
private lateinit var binding:ActivityStudentGalleryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStudentGalleryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        recyclerViewSetUp()

    }

    private fun recyclerViewSetUp() {
        binding.rsvGalleryConvocation.layoutManager = GridLayoutManager(this,4, LinearLayoutManager.VERTICAL,false)
//        binding.rsvGalleryConvocation.adapter =
    }

}