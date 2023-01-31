package com.harshit.goswami.collegeapp.admin

import android.content.Intent
import android.os.Bundle
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
        var teacherType = TeacherDashboard.loggedTeacherType
        var teacherDep = TeacherDashboard.loggedTeacherDep
        var loggedUser = ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminFacultyinfoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loggedUser = intent.getStringExtra("userType").toString()
        retrieveFacultyData()
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
        if (loggedUser == "HOD"){
            Toast.makeText(this@ManageFaculty, teacherDep,Toast.LENGTH_SHORT).show()
            FirebaseDatabase.getInstance().reference.child("Faculty Data").child(teacherDep)
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            facultyList.clear()
                            for (list in snapshot.children) {
                                facultyList.add(0,list.getValue(FacultyData::class.java)!!)
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
      if (loggedUser == "admin"){
          FirebaseDatabase.getInstance().reference.child("Faculty Data")
              .addValueEventListener(object : ValueEventListener {
                  override fun onDataChange(snapshot: DataSnapshot) {
                      if (snapshot.exists()) {
                          facultyList.clear()
                          for (list in snapshot.children) {
                              list.children.forEach {
                                  if (it.key.toString() == "HOD") {
                                      facultyList.add(it.getValue(FacultyData::class.java)!!)
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
      }


    }
}
