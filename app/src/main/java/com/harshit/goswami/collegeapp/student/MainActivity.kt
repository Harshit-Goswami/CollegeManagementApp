package com.harshit.goswami.collegeapp.student

import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.harshit.goswami.collegeapp.R
import com.harshit.goswami.collegeapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    companion object {
       lateinit var mainBinding: ActivityMainBinding
        var user = ""
        var studentDep = ""
        var studentYear = ""
        var studRollNo = ""
        var studName = ""
        var isCR:Boolean = false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mainBinding.root)
        user = intent.getStringExtra("user").toString()
        if (user == "student") {
            val studentPref: SharedPreferences =
                getSharedPreferences("studentPref", MODE_PRIVATE)
            studentDep = studentPref.getString("studentDep", "none").toString()
            studentYear = studentPref.getString("studentYear", "none").toString()
            studRollNo = studentPref.getString("studentRollNo", "none").toString()
            studName = studentPref.getString("studentName", "none").toString()
            isCR = studentPref.getBoolean("isCR", false)
        }
        val homeFrag = supportFragmentManager.beginTransaction()
        homeFrag.replace(R.id.fragment_container, HomeFragment()).commit()
        mainBinding.bottomNavigationView.menu.getItem(0).isChecked = true

        mainBinding.bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.menu_home -> {
                    val homeFrag = supportFragmentManager.beginTransaction()
                    homeFrag.replace(R.id.fragment_container, HomeFragment()).commit()
                }

                R.id.menu_notice -> {
                    val homeFrag = supportFragmentManager.beginTransaction()
                    homeFrag.replace(R.id.fragment_container, NoticeFragment()).commit()
                }

                R.id.menu_event -> {
                    val homeFrag = supportFragmentManager.beginTransaction()
                    homeFrag.replace(R.id.fragment_container, EventFragment()).commit()
                }

                R.id.menu_about -> {
                    val homeFrag = supportFragmentManager.beginTransaction()
                    homeFrag.replace(R.id.fragment_container, AboutCollegeFragment()).commit()
                }
            }
            return@setOnItemSelectedListener true
        }
        mainBinding.MAABMyclass.setOnClickListener {
            val homeFrag = supportFragmentManager.beginTransaction()
            homeFrag.replace(R.id.fragment_container, MyClassFragment()).commit()


        }
    }


    override fun onBackPressed() {
        if (mainBinding.bottomNavigationView.menu.getItem(0).isChecked) {
            super.onBackPressed()
        } else {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, HomeFragment()).commit()
            mainBinding.bottomNavigationView.menu.getItem(0).isChecked = true

        }


    }
}
//
// some changes and bug fix
//