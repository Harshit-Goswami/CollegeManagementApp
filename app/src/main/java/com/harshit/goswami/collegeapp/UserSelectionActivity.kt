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
        binding.cardUSAdmin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            intent.putExtra("SelectedUser", "admin")
            startActivity(intent)
        }
        binding.cardUSTeacher.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            intent.putExtra("SelectedUser", "teacher")
            startActivity(intent)
        }
        binding.cardUSStudent.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            intent.putExtra("SelectedUser", "student")
            startActivity(intent)
        }
        binding.cardUSOther.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("user", "other")
            startActivity(intent)
        }
    }
}