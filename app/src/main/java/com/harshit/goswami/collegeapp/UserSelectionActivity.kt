package com.harshit.goswami.collegeapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.harshit.goswami.collegeapp.databinding.ActivityUserSelectionBinding
import com.harshit.goswami.collegeapp.student.MainActivity

class UserSelectionActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserSelectionBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserSelectionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.USAdmin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            intent.putExtra("SelectedUser", "admin")
            startActivity(intent)
        }
        binding.USTeacher.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            intent.putExtra("SelectedUser", "teacher")
            startActivity(intent)
        }
        binding.USStudent.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            intent.putExtra("SelectedUser", "student")
            startActivity(intent)
        }
        binding.USOther.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
           // intent.putExtra("SelectedUser", "other")
            startActivity(intent)
        }
    }
}