package com.harshit.goswami.collegeapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.harshit.goswami.collegeapp.adapters.AttendanceAdapter
import com.harshit.goswami.collegeapp.data.StudentData
import com.harshit.goswami.collegeapp.databinding.ActivityStudentViewAttendanceBinding
import com.harshit.goswami.collegeapp.student.MainActivity

class StudentViewAttendance : AppCompatActivity() {
    private val fireDb = FirebaseDatabase.getInstance().reference
    val studAttendList = ArrayList<StudentData>()
    lateinit var binding:ActivityStudentViewAttendanceBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStudentViewAttendanceBinding.inflate(layoutInflater)
        setContentView(binding.root)}}
     /*   autoCompleteTextViewSetUp()

     *//*   binding.rsvViewAttendanceStudent.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rsvViewAttendanceStudent.adapter = AttendanceAdapter(studAttendList, this, "view")
        binding.rsvViewAttendanceStudent.setHasFixedSize(true)
        binding.rsvViewAttendanceStudent.adapter?.notifyDataSetChanged()
        binding.btnSearch.setOnClickListener {
            if (binding.TILMonth.isVisible && !binding.TILSelectSubject.isVisible) {
                if (binding.ACTVSelectYear.text.isNotEmpty()) {
                    if (binding.ACTVMonth.text.isNotEmpty()) {
                        fetchAttendanceDataBYMonth()
                    } else binding.TILMonth.error = "Please Select Month"
                } else binding.TILSelectYear.error = "Please Select Year"
            }
            if (!binding.TILMonth.isVisible && binding.TILSelectSubject.isVisible) {
                if (binding.ACTVSelectYear.text.isNotEmpty()) {
                    if (binding.ACTVSelectSubject.text.isNotEmpty()) {
                        fetchAttendanceDataBYSub()
                    } else binding.TILSelectSubject.error = "Please Select Subject"
                } else binding.TILSelectYear.error = "Please Select Year"
            }
            if (binding.TILMonth.isVisible && binding.TILSelectSubject.isVisible) {
                if (binding.ACTVSelectYear.text.isNotEmpty()) {
                    if (binding.ACTVMonth.text.isNotEmpty()) {
                        if (binding.ACTVSelectSubject.text.isNotEmpty()) {
                            fetchAttendanceDataBYMonthNSub()
                        } else binding.TILSelectSubject.error = "Please Select Subject"
                    } else binding.TILMonth.error = "Please Select Month"
                } else binding.TILSelectYear.error = "Please Select Year"
            }
            if (!binding.TILMonth.isVisible && !binding.TILSelectSubject.isVisible) {
                if (binding.ACTVSelectYear.text.isNotEmpty()) {
                    fetchAttendanceDataDefault()
                } else binding.TILSelectYear.error = "Please Select Year"
            }
        }*//*
        binding.VAIcSort.setOnClickListener {
            val popupMenu = PopupMenu(this, binding.VAIcSort)
            popupMenu.menuInflater.inflate(R.menu.view_attendance_sort_menu, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener { menu ->
                when (menu.itemId) {
                    R.id.menu_searchby_month -> {
                        binding.TILSelectSubject.visibility = View.GONE
                        binding.TILMonth.visibility = View.VISIBLE
                    }
                    R.id.menu_searchby_subject -> {
                        binding.TILMonth.visibility = View.GONE
                        binding.TILSelectSubject.visibility = View.VISIBLE
                    }
                    R.id.menu_searchby_month_n_sub -> {
                        binding.TILMonth.visibility = View.VISIBLE
                        binding.TILSelectSubject.visibility = View.VISIBLE
                    }
                    R.id.menu_default -> {
                        binding.TILMonth.visibility = View.GONE
                        binding.TILSelectSubject.visibility = View.GONE
                    }
                }
                true
            }
            popupMenu.show()
        }
    }
*//*    private fun fetchAttendanceDataDefault() {
        fireDb.child("Student Attendance").child(MainActivity.studentDep)
            .child(MainActivity.studentYear).child("${MainActivity.studRollNo} ${MainActivity.studName}")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        studAttendList.clear()
                            var p = 0F
                            var a = 0F
                            val rollNo = MainActivity.studRollNo
//                                .removeRange(2, rollno.key.toString().lastIndex).trim()
                            val studName = MainActivity.studName
//                                rollno.key.toString().removeRange(0, 4).trim()
                            snapshot.children.forEach { month ->
                                month.children.forEach { dates ->
                                    dates.children.forEach { subs ->
                                        if (subs.child("status").value.toString() == "P") {
                                            p += 1
                                        }
                                        //Subjects
                                        a += 1
//                                    val sub = subs.key.toString()
//                                    val status = subs.child("status").value.toString()
                                    }
                                }
                            }
                            try {
                                val per = ((p / a) * 100).toInt()
                                studAttendList.add(
                                    StudentData(
                                        rollNo,
                                        studName,*//**//*Percentage*//**//*
                                        "${per}%"
                                    )
                                )
                                binding.rsvViewAttendanceStudent.adapter?.notifyDataSetChanged()
                            } catch (e: Exception) {
//                                Log.e("errrr",e.message,e)
                            }
                        }
                    }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("errrr", error.message, error.toException())
                }
            })
    }

    private fun fetchAttendanceDataBYMonth() {
        fireDb.child("Student Attendance").child(MainActivity.studentDep)
            .child(MainActivity.studentYear).child("${MainActivity.studRollNo} ${MainActivity.studName}")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        studAttendList.clear()
                        var p = 0F
                        var a = 0F
                        val rollNo = MainActivity.studRollNo
//                                .removeRange(2, rollno.key.toString().lastIndex).trim()
                        val studName = MainActivity.studName
//                                rollno.key.toString().removeRange(0, 4).trim()
                        snapshot.child(binding.ACTVMonth.text.toString()).children.forEach { dates ->
                                dates.children.forEach { subs ->
                                    if (subs.child("status").value.toString() == "P") {
                                        p += 1
                                    }
                                    //Subjects
                                    a += 1
//                                    val sub = subs.key.toString()
//                                    val status = subs.child("status").value.toString()
                                }
                            }
                            try {
                                val per = ((p / a) * 100).toInt()
                                studAttendList.add(
                                    StudentData(
                                        rollNo,
                                        studName,*//**//*Percentage*//**//*
                                        "${per}%"
                                    )
                                )
                                binding.rsvViewAttendanceStudent.adapter?.notifyDataSetChanged()

                            } catch (e: Exception) {
//                                Log.e("errrr",e.message,e)
                            }
                        }
                    }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("errrr", error.message, error.toException())
                }
            })
    }

    private fun fetchAttendanceDataBYSub() {
        fireDb.child("Student Attendance").child(MainActivity.studentDep)
            .child(MainActivity.studentYear).child("${MainActivity.studRollNo} ${MainActivity.studName}")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        studAttendList.clear()
                        var p = 0F
                        var a = 0F
                        val rollNo = MainActivity.studRollNo
//                                .removeRange(2, rollno.key.toString().lastIndex).trim()
                        val studName = MainActivity.studName
//                                rollno.key.toString().removeRange(0, 4).trim()
                        snapshot.children.forEach { month ->
                                month.children.forEach { dates ->
                                    dates.children.forEach { subs ->
                                        if (subs.key.toString() == binding.ACTVSelectSubject.text.toString()) {
                                            if (subs.child("status").value.toString() == "P") {
                                                p += 1
                                            } //Subjects
                                            a += 1
//                                    val sub = subs.key.toString()
//                                    val status = subs.child("status").value.toString()
                                        }
                                    }
                                }
                            }
                            try {
                                val per = ((p / a) * 100).toInt()
                                studAttendList.add(
                                    StudentData(
                                        rollNo,
                                        studName,*//**//*Percentage*//**//*
                                        "${per}%"
                                    )
                                )
                                binding.rsvViewAttendanceStudent.adapter?.notifyDataSetChanged()

                            } catch (e: Exception) {
//                                Log.e("errrr",e.message,e)
                            }
                        }
                    }


                override fun onCancelled(error: DatabaseError) {
                    Log.e("errrr", error.message, error.toException())
                }
            })
    }

    private fun fetchAttendanceDataBYMonthNSub() {
        fireDb.child("Student Attendance").child(MainActivity.studentDep)
            .child(MainActivity.studentYear).child("${MainActivity.studRollNo} ${MainActivity.studName}")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        studAttendList.clear()
                        var p = 0F
                        var a = 0F
                        val rollNo = MainActivity.studRollNo
//                                .removeRange(2, rollno.key.toString().lastIndex).trim()
                        val studName = MainActivity.studName
//                                rollno.key.toString().removeRange(0, 4).trim()
                        snapshot.child(binding.ACTVMonth.text.toString()).children.forEach { dates ->
                                dates.children.forEach { subs ->
                                    if (subs.key.toString() == binding.ACTVSelectSubject.text.toString()) {
                                        if (subs.child("status").value.toString() == "P") p += 1
                                        a += 1
//                                    val sub = subs.key.toString()
//                                    val status = subs.child("status").value.toString()
                                    }
                                }
                            }
                            try {
                                val per = ((p / a) * 100).toInt()
                                studAttendList.add(
                                    StudentData(
                                        rollNo,
                                        studName,*//**//*Percentage*//**//*
                                        "${per}%"
                                    )
                                )
                                binding.rsvViewAttendanceStudent.adapter?.notifyDataSetChanged()

                            } catch (e: Exception) {
//                                Log.e("errrr",e.message,e)
                            }
                        }
                    }
                override fun onCancelled(error: DatabaseError) {
                    Log.e("errrr", error.message, error.toException())
                }
            })
    }*//*

    private fun autoCompleteTextViewSetUp() {
        val itemsYear = listOf("FY", "SY", "TY")
        val adapterYear = ArrayAdapter(
            this,
            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
            itemsYear
        )
     *//*   binding.ACTVSelectYear.setAdapter(adapterYear)
        binding.ACTVSelectYear.setOnItemClickListener { _, _, i, _ ->
            binding.ACTVSelectSubject.text.clear()
            val subjects = ArrayList<String>()
            FirebaseFirestore.getInstance()
                .collection("${itemsYear[i]}${MainActivity.studentDep}-Subjects")
                .addSnapshotListener { value, error ->
                    subjects.clear()
                    if (error != null) {
                        Toast.makeText(
                            this,
                            "Error found is $error",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                    value?.forEach { ds ->
                        subjects.add(ds["subjectName"].toString())
                    }
                }
            subjects.forEach {
                Log.i("subjects", it)
            }
            val adapterSubject = ArrayAdapter(
                this,
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                subjects
            )
            binding.ACTVSelectSubject.setAdapter(adapterSubject)
        }*//*
        val itemsMonth = listOf(
            "Jan",
            "Feb",
            "Mar",
            "Apr",
            "May",
            "Jun",
            "Jul",
            "Aug",
            "Sep",
            "Oct",
            "Nov",
            "Dec"
        )
        val adapterMonth = ArrayAdapter(
            this,
            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
            itemsMonth
        )
        binding.ACTVMonth.setAdapter(adapterMonth)
    }
}*/