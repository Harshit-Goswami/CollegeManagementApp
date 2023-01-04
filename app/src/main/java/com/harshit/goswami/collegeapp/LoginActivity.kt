package com.harshit.goswami.collegeapp

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.harshit.goswami.collegeapp.admin.AdminDashboard
import com.harshit.goswami.collegeapp.admin.dataClasses.AdminLoginData
import com.harshit.goswami.collegeapp.admin.dataClasses.FacultyData
import com.harshit.goswami.collegeapp.admin.dataClasses.RegisteredStudentData
import com.harshit.goswami.collegeapp.databinding.ActivityLoginBinding
import com.harshit.goswami.collegeapp.student.MainActivity
import com.harshit.goswami.collegeapp.student.ResisterAsStutent
import com.harshit.goswami.collegeapp.teacher.TeacherDashboard

class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding
    private var password = ""
    private var userName = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (intent.getStringExtra("SelectedUser") == "student") binding.txtRegToLogIn.visibility =
            View.VISIBLE
        binding.txtRegToLogIn.setOnClickListener {
            val intent = Intent(this, ResisterAsStutent::class.java)
            startActivity(intent)
        }

        val user = intent.getStringExtra("SelectedUser")
        when (user) {
            "admin" -> {
                val adminPref: SharedPreferences = getSharedPreferences("adminPref", MODE_PRIVATE)
                val isAdminLogin = adminPref.getBoolean("adminLogin", false)
                if (isAdminLogin) {
                    val intent = Intent(this, AdminDashboard::class.java)
                    startActivity(intent)
                    finish()
                }
            }
            "teacher" -> {
                val teacherPref: SharedPreferences =
                    getSharedPreferences("teacherPref", MODE_PRIVATE)
                val isTeacherLogin = teacherPref.getBoolean("teacherLogin", false)
                if (isTeacherLogin) {
                    val intent = Intent(this, TeacherDashboard::class.java)
                    startActivity(intent)
                    finish()
                }
            }
                "student" -> {
                val studentPref: SharedPreferences =
                    getSharedPreferences("studentPref", MODE_PRIVATE)
                val isStudentLogin = studentPref.getBoolean("studentLogin", false)
                if (isStudentLogin) {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }


        binding.loginBtn.setOnClickListener { updateUI() }//loginUser()

    }

    private fun updateUI() {
        val user = intent.getStringExtra("SelectedUser")

        when (user) {
            "admin" -> {
                userName = ""
                password = ""
                FirebaseDatabase.getInstance().reference.child("BScIT").child("Admin")
                    .addValueEventListener(
                        object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                if (snapshot.exists()) {
                                    for (item in snapshot.children) {
                                        if (binding.username.text.toString() == item.getValue(
                                                AdminLoginData::class.java
                                            )?.userId.toString()
                                            && binding.password.text.toString() == item.getValue(
                                                AdminLoginData::class.java
                                            )?.password.toString()
                                        ) {
                                            val intent = Intent(
                                                this@LoginActivity,
                                                AdminDashboard::class.java
                                            )
                                            val pref: SharedPreferences =
                                                getSharedPreferences("adminPref", MODE_PRIVATE)
                                            val editor = pref.edit()
                                            editor.putBoolean("adminLogin", true).apply()
                                            startActivity(intent)
                                            finish()
                                        }
                                        if (binding.username.text.toString() == item.getValue(
                                                AdminLoginData::class.java
                                            )?.userId.toString()
                                        ) {
                                            userName = item.getValue(
                                                AdminLoginData::class.java
                                            )?.userId.toString()
                                        }
                                        if (binding.password.text.toString() == item.getValue(
                                                AdminLoginData::class.java
                                            )?.password.toString()
                                        ) {
                                            password = item.getValue(
                                                AdminLoginData::class.java
                                            )?.password.toString()
                                        }
                                    }
                                    if (userName == "") {
                                        binding.username.error = "Wrong username"
                                    } else if (password == "") {
                                        binding.password.error = "Wrong password"
                                    }
                                }
                            }

                            override fun onCancelled(error: DatabaseError) {
                                Log.d("Error in admin login", "error..${error.message}")
                            }

                        })
            }

            "teacher" -> {
                userName = ""
                password = ""
                FirebaseDatabase.getInstance().reference.child("BScIT").child("Faculty Data")
                    .addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (snapshot.exists()) {
                                for (item in snapshot.children) {

                                    if (binding.username.text.toString() == item.getValue(
                                            FacultyData::class.java
                                        )?.name.toString()
                                        && binding.password.text.toString() == item.getValue(
                                            FacultyData::class.java
                                        )?.password.toString()
                                    ) {
                                        val intent =
                                            Intent(this@LoginActivity, TeacherDashboard::class.java)
                                        val pref: SharedPreferences =
                                            getSharedPreferences("teacherPref", MODE_PRIVATE)
                                        val editor = pref.edit()
                                        editor.putBoolean("teacherLogin", true).apply()
                                        startActivity(intent)
                                        finish()
                                    }
                                    if (binding.username.text.toString() == item.getValue(
                                            FacultyData::class.java
                                        )?.name.toString()
                                    ) {
                                        userName = item.getValue(
                                            FacultyData::class.java
                                        )?.name.toString()
                                    }
                                    if (binding.password.text.toString() == item.getValue(
                                            FacultyData::class.java
                                        )?.password.toString()
                                    ) {
                                        password = item.getValue(
                                            FacultyData::class.java
                                        )?.password.toString()
                                    }
                                }
                                if (userName == "") {
                                    binding.username.error = "Wrong username"
                                } else if (password == "") {
                                    binding.password.error = "Wrong password"
                                }
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            TODO("Not yet implemented")
                        }
                    })
            }

            "student" -> {
                userName = ""
                password = ""
                FirebaseDatabase.getInstance().reference.child("BScIT").child("Your Students")
                    .addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (snapshot.exists()) {
                                for (item in snapshot.children) {
                                    if (binding.username.text.toString() == item.getValue(
                                            RegisteredStudentData::class.java
                                        )?.contactNo.toString()
                                        && binding.password.text.toString() == item.getValue(
                                                RegisteredStudentData::class.java
                                        )?.password.toString()
                                    ) {
                                        val intent =
                                            Intent(this@LoginActivity, MainActivity::class.java)
                                        val pref: SharedPreferences =
                                            getSharedPreferences("studentPref", MODE_PRIVATE)
                                        val editor = pref.edit()
                                        editor.putBoolean("studentLogin", true).apply()
                                        startActivity(intent)
                                        finish()
                                    }
                                    if (binding.username.text.toString() == item.getValue(
                                            RegisteredStudentData::class.java
                                        )?.contactNo.toString()
                                    ) {
                                        userName = item.getValue(
                                            RegisteredStudentData::class.java
                                        )?.contactNo.toString()
                                    }
                                    if (binding.password.text.toString() == item.getValue(
                                            RegisteredStudentData::class.java
                                        )?.password.toString()
                                    ) {
                                        password = item.getValue(
                                            RegisteredStudentData::class.java
                                        )?.password.toString()
                                    }
                                }
                                if (userName == "") {
                                    binding.username.error = "Wrong username"
                                } else if (password == "") {
                                    binding.password.error = "Wrong password"
                                }
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            TODO("Not yet implemented")
                        }
                    })
            }
            "other" -> {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        }
    }
}
