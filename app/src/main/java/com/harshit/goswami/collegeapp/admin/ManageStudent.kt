package com.harshit.goswami.collegeapp.admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.harshit.goswami.collegeapp.R
import com.harshit.goswami.collegeapp.databinding.ActivityAdminAddStudentBinding
import com.harshit.goswami.collegeapp.databinding.ActivityAdminStudentinfoBinding

class ManageStudent : AppCompatActivity() {
    private lateinit var binding: ActivityAdminStudentinfoBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminStudentinfoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.FABaddStud.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    AddStudent::class.java
                )
            )
        }
    }
}