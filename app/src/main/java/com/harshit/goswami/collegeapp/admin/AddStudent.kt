package com.harshit.goswami.collegeapp.admin

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.harshit.goswami.collegeapp.admin.adapters.RegisterdStudentAdapter
import com.harshit.goswami.collegeapp.admin.dataClasses.RegisteredStudentData
import com.harshit.goswami.collegeapp.databinding.ActivityAdminAddStudentBinding

class AddStudent : AppCompatActivity() {
    var StoreRegStudList = ArrayList<RegisteredStudentData>()
    private val FireRef = FirebaseDatabase.getInstance()
    private val RegStudList = ArrayList<RegisteredStudentData>()
    private lateinit var binding: ActivityAdminAddStudentBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminAddStudentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rsvRegisteredStud.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rsvRegisteredStud.adapter = RegisterdStudentAdapter(RegStudList, this)
        binding.rsvRegisteredStud.setHasFixedSize(true)

        fetchAllRegStud()

        binding.ASFABFy.setOnClickListener {
            val tempList =ArrayList<RegisteredStudentData>()
            tempList.addAll(StoreRegStudList)
            RegStudList.clear()
            for (i in 0 until tempList.size) {
                if (tempList[i].year == "FY") {
                    RegStudList.add(tempList[i])
                    binding.rsvRegisteredStud.adapter?.notifyDataSetChanged()
                    Log.d("ListIndex",i.toString())
                }
            }
        }



        binding.ASFABSy.setOnClickListener {
            val tempList =ArrayList<RegisteredStudentData>()
            tempList.addAll(StoreRegStudList)
            RegStudList.clear()
            for (i in 0 until tempList.size) {
                if (tempList[i].year == "SY") {
                    RegStudList.add(tempList[i])
                }
            }
            binding.rsvRegisteredStud.adapter?.notifyDataSetChanged()
        }

        binding.ASFABTy.setOnClickListener {
            val tempList =ArrayList<RegisteredStudentData>()
            tempList.addAll(StoreRegStudList)
            RegStudList.clear()
            for (i in 0 until tempList.size) {
                if (tempList[i].year == "TY") {
                    RegStudList.add(tempList[i])
                }
            }
            binding.rsvRegisteredStud.adapter?.notifyDataSetChanged()
        }
    }
       private fun fetchAllRegStud() {
            RegStudList.clear()
            FireRef.reference.child("BScIT").child("Registered Student")
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            for (RegStudData in snapshot.children) {
                                RegStudList.add(RegStudData.getValue(RegisteredStudentData::class.java)!!)
                                StoreRegStudList.add(RegStudData.getValue(RegisteredStudentData::class.java)!!)
                            }
                            binding.rsvRegisteredStud.adapter?.notifyDataSetChanged()
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(this@AddStudent, error.message, Toast.LENGTH_SHORT).show()
                    }
                })

            binding.rsvRegisteredStud.adapter?.notifyDataSetChanged()
        }
    }
