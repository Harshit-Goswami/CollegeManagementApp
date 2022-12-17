package com.harshit.goswami.collegeapp.admin

import android.os.Bundle
import android.view.View
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


        intitializeRecyclerView()
        fetchAllRegStud()

        binding.ASTxtShowAll.setOnClickListener {
            RegStudList.clear()
            RegStudList.addAll(StoreRegStudList)
            binding.rsvRegisteredStud.adapter?.notifyDataSetChanged()
            binding.ASTxtTotalRegStudent.text = "Total Registered Student:-${RegStudList.size}"
            binding.ASTxtShowAll.visibility = View.GONE
        }

        binding.ASFABFy.setOnClickListener {
            val tempList = ArrayList<RegisteredStudentData>()
            tempList.addAll(StoreRegStudList)
            RegStudList.clear()
            for (i in 0 until tempList.size) {
                if (tempList[i].year == "FY") {
                    RegStudList.add(tempList[i])
                }

            }
            binding.rsvRegisteredStud.adapter?.notifyDataSetChanged()
            binding.AddStudProgbar.visibility = View.GONE
            if (RegStudList.size == 0) {
                Toast.makeText(this@AddStudent, "No FY Registered Student", Toast.LENGTH_SHORT)
                    .show()
            }
            binding.ASTxtShowAll.visibility = View.VISIBLE
            binding.ASTxtTotalRegStudent.text = "FY Registered Student:-${RegStudList.size}"
        }

        binding.ASFABSy.setOnClickListener {
            val tempList = ArrayList<RegisteredStudentData>()
            tempList.addAll(StoreRegStudList)
            RegStudList.clear()
            for (i in 0 until tempList.size) {
                if (tempList[i].year == "SY") {
                    RegStudList.add(tempList[i])
                }
            }
            binding.rsvRegisteredStud.adapter?.notifyDataSetChanged()
            binding.AddStudProgbar.visibility = View.GONE
            if (RegStudList.size == 0) {
                Toast.makeText(this@AddStudent, "No SY Registered Student", Toast.LENGTH_SHORT)
                    .show()
            }
            binding.ASTxtShowAll.visibility = View.VISIBLE
            binding.ASTxtTotalRegStudent.text = "SY Registered Student:-${RegStudList.size}"

        }

        binding.ASFABTy.setOnClickListener {
            val tempList = ArrayList<RegisteredStudentData>()
            tempList.addAll(StoreRegStudList)
            RegStudList.clear()
            for (i in 0 until tempList.size) {
                if (tempList[i].year == "TY") {
                    RegStudList.add(tempList[i])
                }
            }
            binding.rsvRegisteredStud.adapter?.notifyDataSetChanged()
            binding.AddStudProgbar.visibility = View.GONE
            if (RegStudList.size == 0) {
                Toast.makeText(this@AddStudent, "No TY Registered Student", Toast.LENGTH_SHORT)
                    .show()
            }
            binding.ASTxtShowAll.visibility = View.VISIBLE
            binding.ASTxtTotalRegStudent.text = "TY Registered Student:-${RegStudList.size}"

        }
    }

    private fun intitializeRecyclerView() {
        binding.rsvRegisteredStud.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rsvRegisteredStud.adapter = RegisterdStudentAdapter(RegStudList, this)
        binding.rsvRegisteredStud.setHasFixedSize(true)

    }

    private fun fetchAllRegStud() {
        FirebaseDatabase.getInstance().reference.keepSynced(true)
        FireRef.reference.child("BScIT").child("Registered Student")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    if (snapshot.exists()) {
                        RegStudList.clear()
                        StoreRegStudList.clear()
                        for (RegStudData in snapshot.children) {
                            RegStudList.add(RegStudData.getValue(RegisteredStudentData::class.java)!!)
                            StoreRegStudList.add(RegStudData.getValue(RegisteredStudentData::class.java)!!)
                        }
                        binding.rsvRegisteredStud.adapter?.notifyDataSetChanged()
                        binding.AddStudProgbar.visibility = View.GONE
                        binding.ASTxtShowAll.visibility = View.GONE
                        binding.ASTxtTotalRegStudent.text =
                            "Total Registered Student:-${RegStudList.size}"
                        if (RegStudList.size == 0) {
                            Toast.makeText(this@AddStudent, "No Registration!", Toast.LENGTH_SHORT)
                                .show()
                            binding.AddStudProgbar.visibility = View.GONE
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@AddStudent, error.message, Toast.LENGTH_SHORT).show()
                }
            })

    }
}
