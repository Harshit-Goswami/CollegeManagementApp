package com.harshit.goswami.collageapp.admin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.*
import com.harshit.goswami.collageapp.databinding.ActivityManageFacultyBinding

class ManageFaculty : AppCompatActivity() {
    private lateinit var binding: ActivityManageFacultyBinding
    var facultyList = ArrayList<FacultyData>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityManageFacultyBinding.inflate(layoutInflater)
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
        binding.facultyProgbar.visibility= View.VISIBLE
        facultyList = ArrayList()
        FirebaseDatabase.getInstance().reference.child("FacultyData")
            .addChildEventListener(object : ChildEventListener {
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    if (snapshot.exists()) {
                            facultyList.add(snapshot.getValue(FacultyData::class.java)!!)
                            Log.d(
                                "teacher data",
                                snapshot.value.toString()
                            )
                        binding.rsvFaculty.adapter?.notifyDataSetChanged()
                        binding.facultyProgbar.visibility= View.GONE
                    }
                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                }
                override fun onChildRemoved(snapshot: DataSnapshot) {
                }
                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                }
                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@ManageFaculty,error.message,Toast.LENGTH_SHORT).show()
                }

            })

    }
}
