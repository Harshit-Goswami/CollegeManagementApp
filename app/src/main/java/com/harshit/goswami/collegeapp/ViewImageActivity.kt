package com.harshit.goswami.collegeapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.harshit.goswami.collegeapp.databinding.ActivityViewImageBinding

class ViewImageActivity : AppCompatActivity() {
    private lateinit var binding:ActivityViewImageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewImageBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val imageUrl = intent.getStringExtra("imageUrl")
        Glide.with(this).load(imageUrl).into(binding.photoView)
    }
}