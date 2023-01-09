package com.harshit.goswami.collegeapp.admin

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.*
import com.harshit.goswami.collegeapp.adapters.FacultyAdapter
import com.harshit.goswami.collegeapp.data.FacultyData
import com.harshit.goswami.collegeapp.databinding.ActivityAdminFacultyinfoBinding

class ManageFaculty : AppCompatActivity() {
    private lateinit var binding: ActivityAdminFacultyinfoBinding
   companion object{ var facultyList = ArrayList<FacultyData>()}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminFacultyinfoBinding.inflate(layoutInflater)
        setContentView(binding.root)
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
        FirebaseDatabase.getInstance().reference.child("BScIT").child("Faculty Data")
            .addValueEventListener(object :ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()){
                        facultyList.clear()
                        for (list in snapshot.children) {
                            facultyList.add(list.getValue(FacultyData::class.java)!!)
                        }
                        binding.rsvFaculty.adapter?.notifyDataSetChanged()
                        binding.facultyProgbar.visibility= View.GONE
                    }

                }
                override fun onCancelled(error: DatabaseError) {
                }
            })

    }
}
