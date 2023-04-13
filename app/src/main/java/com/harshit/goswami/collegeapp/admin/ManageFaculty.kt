package com.harshit.goswami.collegeapp.admin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.harshit.goswami.collegeapp.adapters.FacultyAdapter
import com.harshit.goswami.collegeapp.data.FacultyData
import com.harshit.goswami.collegeapp.databinding.ActivityAdminFacultyinfoBinding
import com.harshit.goswami.collegeapp.teacher.TeacherDashboard

class ManageFaculty : AppCompatActivity() {
    private lateinit var binding: ActivityAdminFacultyinfoBinding

    companion object {
        var facultyList = ArrayList<FacultyData>()
        var loggedUser = ""
        var studDep = ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminFacultyinfoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loggedUser = intent.getStringExtra("userType").toString()
        studDep = intent.getStringExtra("department").toString()
        retrieveFacultyData()
        if (loggedUser == "student"){
            binding.addFaculty.visibility = View.GONE
        }
        binding.addFaculty.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    AddFaculty::class.java
                )
            )
        }
        binding.rsvFaculty.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rsvFaculty.adapter = FacultyAdapter(facultyList, this)
        binding.rsvFaculty.setHasFixedSize(true)
    }

    private fun retrieveFacultyData() {

        if (loggedUser == "HOD") {
            Toast.makeText(this@ManageFaculty, TeacherDashboard.loggedTeacherDep, Toast.LENGTH_SHORT).show()
            FirebaseDatabase.getInstance().reference.child("Faculty Data").child(TeacherDashboard.loggedTeacherDep)
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            facultyList.clear()
                            for (list in snapshot.children) {
                                facultyList.add(list.getValue(FacultyData::class.java)!!)
                            }
                            binding.rsvFaculty.adapter?.notifyDataSetChanged()
                            binding.facultyProgbar.visibility = View.GONE
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                    }
                })
        }
        if (loggedUser == "student" && studDep !="") {
//            Toast.makeText(this@ManageFaculty, studDep, Toast.LENGTH_SHORT).show()
            FirebaseDatabase.getInstance().reference.child("Faculty Data").child(studDep)
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            facultyList.clear()
                            for (list in snapshot.children) {
                                facultyList.add(list.getValue(FacultyData::class.java)!!)
                            }
                            binding.rsvFaculty.adapter?.notifyDataSetChanged()
                            binding.facultyProgbar.visibility = View.GONE
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                    }
                })
        }
        if (loggedUser == "admin") {
            try {
                FirebaseDatabase.getInstance().reference.child("Faculty Data")
                    .addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (snapshot.exists()) {
                                facultyList.clear()
                                snapshot.children.forEach { dep ->
//                                    Log.d("facultyData", dep.key.toString())
                                    dep.children.forEach {data->
//                                        Log.d("facultyData", data.key.toString())
                                        if (data.getValue(FacultyData::class.java)!!.teacherType == "HOD") {
                                            facultyList.add(data.getValue(FacultyData::class.java)!!)
                                        }
                                    }
                                }
                                binding.rsvFaculty.adapter?.notifyDataSetChanged()
                                binding.facultyProgbar.visibility = View.GONE
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                        }
                    })
            } catch (e: Exception) {
                Toast.makeText(this@ManageFaculty, e.message.toString(), Toast.LENGTH_SHORT).show()
            }
        }
    }
}
