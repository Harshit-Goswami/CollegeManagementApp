package com.harshit.goswami.collegeapp.student

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.harshit.goswami.collegeapp.R
import com.harshit.goswami.collegeapp.databinding.ActivityStudentAssignmentBinding

class AssignmentActivity : AppCompatActivity() {
    private lateinit var binding :ActivityStudentAssignmentBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStudentAssignmentBinding.inflate(layoutInflater)
        setContentView(binding.root)




    }
}