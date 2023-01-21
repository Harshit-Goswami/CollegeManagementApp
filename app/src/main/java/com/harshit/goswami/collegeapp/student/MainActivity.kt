package com.harshit.goswami.collegeapp.student

import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.harshit.goswami.collegeapp.LoginActivity
import com.harshit.goswami.collegeapp.R
import com.harshit.goswami.collegeapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    companion object {
        lateinit var mainBinding: ActivityMainBinding
        var user = ""
        var studentDep = ""
        var studentYear = ""
        var studRollNo = ""
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)
        user = intent.getStringExtra("user").toString()
        val studentPref: SharedPreferences =
            getSharedPreferences("studentPref", MODE_PRIVATE)
        studentDep= studentPref.getString("studentDep","none").toString()
        studentYear= studentPref.getString("studentYear","none").toString()
        studRollNo= studentPref.getString("studentRollNo","none").toString()
        val homeFrag = supportFragmentManager.beginTransaction()
        homeFrag.replace(R.id.fragment_container, HomeFragment()).commit()
        mainBinding.textViewHome.setTextColor(Color.parseColor("#FF6F00"))
        mainBinding.imageViewHome.setColorFilter(Color.parseColor("#FF6F00"))
        navButtonListeners()
    }

    private fun navButtonListeners() {
        mainBinding.MAABHome.setOnClickListener {
            val homeFrag = supportFragmentManager.beginTransaction()
            homeFrag.replace(R.id.fragment_container, HomeFragment()).commit()

            mainBinding.textViewHome.setTextColor(Color.parseColor("#FF6F00"))
            mainBinding.imageViewHome.setColorFilter(Color.parseColor("#FF6F00"))

            mainBinding.textViewMyclass.setTextColor(Color.BLACK)
            mainBinding.textViewNotice.setTextColor(Color.BLACK)
            mainBinding.imageViewNotice.setColorFilter(Color.BLACK)
            mainBinding.textViewEvent.setTextColor(Color.BLACK)
            mainBinding.imageViewEvent.setColorFilter(Color.BLACK)
            mainBinding.textViewAbout.setTextColor(Color.BLACK)
            mainBinding.imageViewAbout.setColorFilter(Color.BLACK)

        }
        mainBinding.MAABNotice.setOnClickListener {
            val homeFrag = supportFragmentManager.beginTransaction()
            homeFrag.replace(R.id.fragment_container, NoticeFragment()).commit()

            mainBinding.textViewNotice.setTextColor(Color.parseColor("#FF6F00"))
            mainBinding.imageViewNotice.setColorFilter(Color.parseColor("#FF6F00"))

            mainBinding.textViewMyclass.setTextColor(Color.BLACK)
            mainBinding.textViewHome.setTextColor(Color.BLACK)
            mainBinding.imageViewHome.setColorFilter(Color.BLACK)
            mainBinding.textViewEvent.setTextColor(Color.BLACK)
            mainBinding.imageViewEvent.setColorFilter(Color.BLACK)
            mainBinding.textViewAbout.setTextColor(Color.BLACK)
            mainBinding.imageViewAbout.setColorFilter(Color.BLACK)
        }
        mainBinding.MAABMyclass.setOnClickListener {
            val homeFrag = supportFragmentManager.beginTransaction()
            homeFrag.replace(R.id.fragment_container, MyClassFragment()).commit()

            mainBinding.textViewMyclass.setTextColor(Color.parseColor("#FF6F00"))

            mainBinding.imageViewHome.setColorFilter(Color.BLACK)
            mainBinding.textViewHome.setTextColor(Color.BLACK)
            mainBinding.textViewNotice.setTextColor(Color.BLACK)
            mainBinding.imageViewNotice.setColorFilter(Color.BLACK)
            mainBinding.textViewEvent.setTextColor(Color.BLACK)
            mainBinding.imageViewEvent.setColorFilter(Color.BLACK)
            mainBinding.textViewAbout.setTextColor(Color.BLACK)
            mainBinding.imageViewAbout.setColorFilter(Color.BLACK)
        }
        mainBinding.MAABEvent.setOnClickListener {
            val homeFrag = supportFragmentManager.beginTransaction()
            homeFrag.replace(R.id.fragment_container, EventFragment()).commit()
            mainBinding.textViewEvent.setTextColor(Color.parseColor("#FF6F00"))
            mainBinding.imageViewEvent.setColorFilter(Color.parseColor("#FF6F00"))

            mainBinding.textViewMyclass.setTextColor(Color.BLACK)
            mainBinding.textViewNotice.setTextColor(Color.BLACK)
            mainBinding.imageViewNotice.setColorFilter(Color.BLACK)
            mainBinding.textViewHome.setTextColor(Color.BLACK)
            mainBinding.imageViewHome.setColorFilter(Color.BLACK)
            mainBinding.textViewAbout.setTextColor(Color.BLACK)
            mainBinding.imageViewAbout.setColorFilter(Color.BLACK)
        }
        mainBinding.MAABAbout.setOnClickListener {
            val homeFrag = supportFragmentManager.beginTransaction()
            homeFrag.replace(R.id.fragment_container, AboutCollegeFragment()).commit()
            mainBinding.textViewAbout.setTextColor(Color.parseColor("#FF6F00"))
            mainBinding.imageViewAbout.setColorFilter(Color.parseColor("#FF6F00"))

            mainBinding.textViewMyclass.setTextColor(Color.BLACK)
            mainBinding.textViewNotice.setTextColor(Color.BLACK)
            mainBinding.imageViewNotice.setColorFilter(Color.BLACK)
            mainBinding.textViewEvent.setTextColor(Color.BLACK)
            mainBinding.imageViewEvent.setColorFilter(Color.BLACK)
            mainBinding.textViewHome.setTextColor(Color.BLACK)
            mainBinding.imageViewHome.setColorFilter(Color.BLACK)
        }
    }

    override fun onBackPressed() {
        if (mainBinding.textViewHome.currentTextColor != Color.parseColor("#FF6F00")) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, HomeFragment()).commit()
            mainBinding.textViewHome.setTextColor(Color.parseColor("#FF6F00"))
            mainBinding.imageViewHome.setColorFilter(Color.parseColor("#FF6F00"))

            mainBinding.textViewMyclass.setTextColor(Color.BLACK)
            mainBinding.textViewNotice.setTextColor(Color.BLACK)
            mainBinding.imageViewNotice.setColorFilter(Color.BLACK)
            mainBinding.textViewEvent.setTextColor(Color.BLACK)
            mainBinding.imageViewEvent.setColorFilter(Color.BLACK)
            mainBinding.textViewAbout.setTextColor(Color.BLACK)
            mainBinding.imageViewAbout.setColorFilter(Color.BLACK)
        } else if (mainBinding.textViewHome.currentTextColor == Color.parseColor("#FF6F00")) {
            super.onBackPressed()
        }


    }
}
