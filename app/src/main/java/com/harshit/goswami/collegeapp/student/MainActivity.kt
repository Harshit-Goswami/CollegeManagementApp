package com.harshit.goswami.collegeapp.student

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.harshit.goswami.collegeapp.R
import com.harshit.goswami.collegeapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val homeFrag = supportFragmentManager.beginTransaction()
        homeFrag.replace(R.id.container, HomeFragment()).commit()
        binding.textViewHome.setTextColor(Color.parseColor("#FF6F00"))
        binding.imageViewHome.setColorFilter(Color.parseColor("#FF6F00"))

        navButtonListeners()

    }
    private fun navButtonListeners(){
        binding.MAABHome.setOnClickListener {
            val homeFrag = supportFragmentManager.beginTransaction()
            homeFrag.replace(R.id.container, HomeFragment()).commit()

            binding.textViewHome.setTextColor(Color.parseColor("#FF6F00"))
            binding.imageViewHome.setColorFilter(Color.parseColor("#FF6F00"))

            binding.textViewMyclass.setTextColor(Color.BLACK)
            binding . textViewNotice . setTextColor (Color.BLACK)
            binding.imageViewNotice.setColorFilter(Color.BLACK)
            binding . textViewEvent . setTextColor (Color.BLACK)
            binding.imageViewEvent.setColorFilter(Color.BLACK)
            binding . textViewAbout.setTextColor(Color.BLACK)
            binding.imageViewAbout.setColorFilter(Color.BLACK)

        }
        binding.MAABNotice.setOnClickListener {
            val homeFrag = supportFragmentManager.beginTransaction()
            homeFrag.replace(R.id.container, NoticeFragment()).commit()

            binding.textViewNotice.setTextColor(Color.parseColor("#FF6F00"))
            binding.imageViewNotice.setColorFilter(Color.parseColor("#FF6F00"))

            binding.textViewMyclass.setTextColor(Color.BLACK)
            binding . textViewHome . setTextColor (Color.BLACK)
            binding.imageViewHome.setColorFilter(Color.BLACK)
            binding . textViewEvent . setTextColor (Color.BLACK)
            binding.imageViewEvent.setColorFilter(Color.BLACK)
            binding . textViewAbout.setTextColor(Color.BLACK)
            binding.imageViewAbout.setColorFilter(Color.BLACK)
        }
        binding.MAABMyclass.setOnClickListener {
            val homeFrag = supportFragmentManager.beginTransaction()
            homeFrag.replace(R.id.container, MyClassFragment()).commit()

            binding.textViewMyclass.setTextColor(Color.parseColor("#FF6F00"))

            binding.imageViewHome.setColorFilter(Color.BLACK)
            binding.textViewHome.setTextColor(Color.BLACK)
            binding . textViewNotice . setTextColor (Color.BLACK)
            binding.imageViewNotice.setColorFilter(Color.BLACK)
            binding . textViewEvent . setTextColor (Color.BLACK)
            binding.imageViewEvent.setColorFilter(Color.BLACK)
            binding . textViewAbout.setTextColor(Color.BLACK)
            binding.imageViewAbout.setColorFilter(Color.BLACK)
        }
        binding.MAABEvent.setOnClickListener {
            val homeFrag = supportFragmentManager.beginTransaction()
            homeFrag.replace(R.id.container, EventFragment()).commit()

            binding.textViewEvent.setTextColor(Color.parseColor("#FF6F00"))
            binding.imageViewEvent.setColorFilter(Color.parseColor("#FF6F00"))

            binding.textViewMyclass.setTextColor(Color.BLACK)
            binding . textViewNotice . setTextColor (Color.BLACK)
            binding.imageViewNotice.setColorFilter(Color.BLACK)
            binding . textViewHome . setTextColor (Color.BLACK)
            binding.imageViewHome.setColorFilter(Color.BLACK)
            binding . textViewAbout.setTextColor(Color.BLACK)
            binding.imageViewAbout.setColorFilter(Color.BLACK)
        }
        binding.MAABAbout.setOnClickListener {
            val homeFrag = supportFragmentManager.beginTransaction()
            homeFrag.replace(R.id.container, AboutCollegeFragment()).commit()

            binding.textViewAbout.setTextColor(Color.parseColor("#FF6F00"))
            binding.imageViewAbout.setColorFilter(Color.parseColor("#FF6F00"))

            binding.textViewMyclass.setTextColor(Color.BLACK)
            binding . textViewNotice . setTextColor (Color.BLACK)
            binding.imageViewNotice.setColorFilter(Color.BLACK)
            binding . textViewEvent . setTextColor (Color.BLACK)
            binding.imageViewEvent.setColorFilter(Color.BLACK)
            binding . textViewHome.setTextColor(Color.BLACK)
            binding.imageViewHome.setColorFilter(Color.BLACK)
        }
    }
}