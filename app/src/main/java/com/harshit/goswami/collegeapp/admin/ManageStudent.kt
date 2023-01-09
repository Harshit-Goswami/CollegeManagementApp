package com.harshit.goswami.collegeapp.admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.harshit.goswami.collegeapp.adapters.YourStudentAdapter
import com.harshit.goswami.collegeapp.data.RegisteredStudentData
import com.harshit.goswami.collegeapp.databinding.ActivityAdminManagestudentBinding

class ManageStudent : AppCompatActivity() {
    var storeStudList = ArrayList<RegisteredStudentData>()
    private val studentList = ArrayList<RegisteredStudentData>()
    private lateinit var binding: ActivityAdminManagestudentBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminManagestudentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initializeRecylerView()
        fetchStudData()
        binding.MSRsvYourStudents.addOnScrollListener(object :RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy>0){
                    binding.FABaddStud.shrink()
                }else{
                    binding.FABaddStud.extend()
                }
            }
        })

        binding.FABaddStud.setOnClickListener {
            startActivity(
                Intent(
                    this,
                    AddStudent::class.java
                )
            )
        }
binding.MSTxtShowAll.setOnClickListener {
    studentList.clear()
    studentList.addAll(storeStudList)
    binding.MSRsvYourStudents.adapter?.notifyDataSetChanged()
    binding.MSTxtShowAll.visibility = View.GONE
}


        binding.MSFABFy.setOnClickListener {
            val tempList = ArrayList<RegisteredStudentData>()
            tempList.addAll(storeStudList)
            studentList.clear()
            for (i in 0 until tempList.size) {
                if (tempList[i].year == "FY") {
                    studentList.add(tempList[i])

                }
            }
            binding.MSRsvYourStudents.adapter?.notifyDataSetChanged()
            if (studentList.size == 0) {
                Toast.makeText(this@ManageStudent, "No FY  Student", Toast.LENGTH_SHORT)
                    .show()
            }
            binding.MSTxtTotalStudent.text = "Total FY Students:-${studentList.size}"
            binding.MSTxtShowAll.visibility = View.VISIBLE

        }



        binding.MSFABSy.setOnClickListener {
            val tempList = ArrayList<RegisteredStudentData>()
            tempList.addAll(storeStudList)
            studentList.clear()
            for (i in 0 until tempList.size) {
                if (tempList[i].year == "SY") {
                    studentList.add(tempList[i])
                }
            }
            binding.MSRsvYourStudents.adapter?.notifyDataSetChanged()
            if (studentList.size == 0) {
                Toast.makeText(this@ManageStudent, "No SY  Student", Toast.LENGTH_SHORT)
                    .show()
            }
            binding.MSTxtTotalStudent.text = "Total SY Students:-${studentList.size}"
            binding.MSTxtShowAll.visibility = View.VISIBLE


        }

        binding.MSFABTy.setOnClickListener {
            val tempList = ArrayList<RegisteredStudentData>()
            tempList.addAll(storeStudList)
            studentList.clear()
            for (i in 0 until tempList.size) {
                if (tempList[i].year == "TY") {
                    studentList.add(tempList[i])
                }
            }
            binding.MSRsvYourStudents.adapter?.notifyDataSetChanged()
            if (studentList.size == 0) {
                Toast.makeText(this@ManageStudent, "No TY  Student", Toast.LENGTH_SHORT)
                    .show()
            }
            binding.MSTxtTotalStudent.text = "Total TY Students:-${studentList.size}"
            binding.MSTxtShowAll.visibility = View.VISIBLE

        }

    }
    private fun initializeRecylerView(){
        binding.MSRsvYourStudents.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.MSRsvYourStudents.adapter = YourStudentAdapter(studentList, this)
        binding.MSRsvYourStudents.setHasFixedSize(true)
    }

    private fun fetchStudData(){
        FirebaseDatabase.getInstance().reference.keepSynced(true)
        FirebaseDatabase.getInstance().reference.child("BScIT").child("Your Students").addValueEventListener(object :ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                studentList.clear()
                if (snapshot.exists()) {
                    for (StudData in snapshot.children) {
                        studentList.add(StudData.getValue(RegisteredStudentData::class.java)!!)
                    }
                    binding.MSRsvYourStudents.adapter?.notifyDataSetChanged()
                    binding.MSProgressIndicator.visibility = View.GONE
                    storeStudList.addAll(studentList)
                    binding.MSTxtTotalStudent.text = "Total Students:-${studentList.size}"
                    binding.MSTxtShowAll.visibility = View.GONE
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@ManageStudent,"Error.."+error.message,Toast.LENGTH_SHORT).show()
            }
        })
    }
}