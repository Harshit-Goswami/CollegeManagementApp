package com.harshit.goswami.collegeapp

import android.icu.text.SimpleDateFormat
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.harshit.goswami.collegeapp.adapters.AttendanceAdapter
import com.harshit.goswami.collegeapp.modal.StudentData
import com.harshit.goswami.collegeapp.databinding.ActivityAttendenceBinding
import com.harshit.goswami.collegeapp.student.MainActivity
import java.util.*


class AttendanceActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAttendenceBinding
    private val fireDb = FirebaseDatabase.getInstance().reference
    private val studentList = ArrayList<StudentData>()
    private var subName = ""
    private var date = ""

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAttendenceBinding.inflate(layoutInflater)
        setContentView(binding.root)
        subName = intent.getStringExtra("subjectName").toString()
        date = intent.getStringExtra("date").toString()


        binding.rsvTakeAttendance.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rsvTakeAttendance.adapter = AttendanceAdapter(studentList, this, "take")
        binding.rsvTakeAttendance.setHasFixedSize(true)
        binding.rsvTakeAttendance.adapter?.notifyDataSetChanged()

        fireDb.child("Students")
            .child("BScIT")
            .child("TY").addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        snapshot.children.forEach {
                            it.getValue(StudentData::class.java)
                                ?.let { it1 -> studentList.add(it1) }
                        }
                        binding.rsvTakeAttendance.adapter?.notifyDataSetChanged()
                    }

                }

                override fun onCancelled(error: DatabaseError) {
                }

            })

        binding.submit.setOnClickListener {
            val sdfMonth =
                SimpleDateFormat("LLL", Locale.getDefault())
            val month = sdfMonth.format(Calendar.getInstance().time)
            val sdfDate =
//                SimpleDateFormat("HH:mm:ss", Locale.getDefault())
                SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
            val Cdate = sdfDate.format(Calendar.getInstance().time)


            AttendanceAdapter.attendanceList.forEach { it2 ->
                fireDb.child("Student Attendance").child(MainActivity.studentDep)
                    .child(MainActivity.studentYear)
                    .child(it2.rollNo).child(it2.studName).child(month).child(Cdate).child(subName)
                    .child("status").setValue(it2.status).addOnSuccessListener {
                        Toast.makeText(
                            this@AttendanceActivity,
                            "Attendance Taken Successfully!!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            }
        }

    }
}