package com.harshit.goswami.collegeapp

import android.graphics.Color
import android.os.Bundle
import android.widget.TableRow
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.harshit.goswami.collegeapp.adapters.AttendanceAdapter
import com.harshit.goswami.collegeapp.data.StudentData
import com.harshit.goswami.collegeapp.databinding.ActivityAttendenceBinding
import java.util.*


class AttendanceActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAttendenceBinding
    private val fireDb = FirebaseDatabase.getInstance().reference
    private val studentList = ArrayList<StudentData>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAttendenceBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.rsvTakeAttendence.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rsvTakeAttendence.adapter = AttendanceAdapter(studentList, this)
        binding.rsvTakeAttendence.setHasFixedSize(true)
        binding.rsvTakeAttendence.adapter?.notifyDataSetChanged()
        binding.takeAttendance.setOnClickListener {
            fireDb.child("Students")
                .child("BScIT")
                .child("TY").addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            snapshot.children.forEach {
                                it.getValue(StudentData::class.java)
                                    ?.let { it1 -> studentList.add(it1) }
                            }
                            binding.rsvTakeAttendence.adapter?.notifyDataSetChanged()
                        }

                    }

                    override fun onCancelled(error: DatabaseError) {
                    }

                })
        }
        binding.submit.setOnClickListener {
//            val aS = AttendanceAdapter.attendanceList.size
            AttendanceAdapter.attendanceList.forEach { it2 ->
                fireDb.child("Student Attendance").child("BScIT").child("TY")
                    .child(it2.rollNo).child("Mar").child(Calendar.getInstance().time.toString())
                    .child("status").setValue(it2.status)
            }
        }

        val tbrow0 = TableRow(this)
        val tv0 = TextView(this)
        tv0.text = "(RollNo)-Name "
        tv0.setTextColor(Color.BLACK)
        tbrow0.addView(tv0)

        binding.viewAttendence.setOnClickListener {
            fireDb.child("Student Attendance").child("BScIT").child("TY")
                .addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        snapshot.child("103").child("Mar").children.forEach {
                            val tv1 = TextView(this@AttendanceActivity)
                            tv1.text = it.key.toString()
                            tv1.setTextColor(Color.BLACK)
                            tbrow0.addView(tv1)
                        }
                        binding.tableLayout.addView(tbrow0)

                        snapshot.children.forEach {
                            val tbrow = TableRow(this@AttendanceActivity)
                            val tv = TextView(this@AttendanceActivity)
                            tv.text = it.key.toString()
                            tv.setTextColor(Color.BLACK)
                            tbrow.addView(tv)
                            it.child("Mar").children.forEach { it2 ->
                                val tv3 = TextView(this@AttendanceActivity)
                                tv3.text = it2.child("status").value.toString()
                                tv3.setTextColor(Color.BLACK)
                                tbrow.addView(tv3)
                            }
                            binding.tableLayout.addView(tbrow)
                        }
                    }
                    override fun onCancelled(error: DatabaseError) {
                    }
                })
        }
    }
}