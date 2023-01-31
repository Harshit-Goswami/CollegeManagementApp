package com.harshit.goswami.collegeapp.teacher

import android.app.DatePickerDialog
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.harshit.goswami.collegeapp.R
import com.harshit.goswami.collegeapp.data.ClassData
import com.harshit.goswami.collegeapp.databinding.ActivityAdminUploadClassBinding
import com.harshit.goswami.collegeapp.databinding.DialogAdminAddClassBinding
import com.harshit.goswami.collegeapp.databinding.DialogAdminAddSubjectBinding


class UploadClass : AppCompatActivity() {
    private lateinit var binding: ActivityAdminUploadClassBinding
    private lateinit var addClassBinding: DialogAdminAddClassBinding
    private lateinit var addSubjectBindind: DialogAdminAddSubjectBinding
    private var classDate = ""
    private var classTime = ""
    private var classSubject = ""
    private var classRoom = ""
    private var classYear = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminUploadClassBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.addClass.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                addClassDialog()
            }
        }
        binding.AddSubject.setOnClickListener {
            addSubjectDialog()
        }
        binding.seeClasses.setOnClickListener {

        }
    }
    private fun addSubjectDialog() {
//        try {
        addSubjectBindind = DialogAdminAddSubjectBinding.inflate(layoutInflater)
        val bottomDialog = BottomSheetDialog(this, R.style.BottomSheetStyle)
        bottomDialog.setContentView(addSubjectBindind.root)
        bottomDialog
            .setCanceledOnTouchOutside(false)
        bottomDialog.show()
//        } catch (e: Exception) {
//            Log.d("dialogError","${e.message}")
//        }
        val itemsYear = listOf("FY", "SY", "TY")
        val adapterYear = ArrayAdapter(
            this,
            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
            itemsYear
        )
        addSubjectBindind.ASClassYear.setAdapter(adapterYear)

        addSubjectBindind.dialogASBtnAdd.setOnClickListener {
            var subjectClassYear = ""
            var subjectName = ""
            if (addSubjectBindind.ASClassYear.text.toString() != "") subjectClassYear =
                addSubjectBindind.ASClassYear.text.toString()
            else addSubjectBindind.ASClassYear.error = "Please select year!"
            if (addSubjectBindind.dialogASEdtSubject.text.toString() != "") subjectName =
                addSubjectBindind.dialogASEdtSubject.text.toString()
            else addSubjectBindind.dialogASEdtSubject.error = "Please add subject!"

            if (subjectClassYear != "" && subjectName != "") {
                FirebaseDatabase.getInstance().reference
                    .child("Subjects")
                    .child(TeacherDashboard.loggedTeacherDep)
                    .child(subjectClassYear)
                    .child(subjectName)
                    .setValue(subjectName)
                    .addOnSuccessListener {
                        Toast.makeText(
                            this@UploadClass,
                            "Successfully Added",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                    .addOnFailureListener {
                        Toast.makeText(
                            this@UploadClass,
                            "Error :${it.message}",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
            }

        }
    }
    @RequiresApi(Build.VERSION_CODES.N)
    private fun addClassDialog() {
//        try {

        addClassBinding = DialogAdminAddClassBinding.inflate(layoutInflater)
        val bottomDialog = BottomSheetDialog(this, R.style.BottomSheetStyle)
        bottomDialog.setContentView(addClassBinding.root)
        bottomDialog
            .setCanceledOnTouchOutside(false)
        bottomDialog.show()
        bottomDialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
//        } catch (e: Exception) {
//            Log.d("dialogError","${e.message}")
//        }
        autoCompleteTextViewSetUp()
        addClassBinding.ACClassDate.setOnClickListener {
            classDatePickerDialog()
        }
        addClassBinding.ACBtnAdd.setOnClickListener {
            validation()
            addClass()
        }

    }

    private fun addClass() {
        if (classDate != "" && classTime != "" && classSubject != "" && classRoom != "" && classYear != "") {
            FirebaseDatabase.getInstance().reference
                .child("Class TimeTable")
                .child(TeacherDashboard.loggedTeacherDep)
                .child(classYear).child(classSubject)
                .setValue(ClassData(classDate, classTime, classSubject, classYear, classRoom))
                .addOnSuccessListener {
                    Toast.makeText(
                        this@UploadClass,
                        "Successfully Added",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
                .addOnFailureListener {
                    Toast.makeText(this@UploadClass, "Error :${it.message}", Toast.LENGTH_SHORT)
                        .show()
                }
        }
    }
    private fun validation() {
        if (addClassBinding.ACClassDate.text != "") classDate =
            addClassBinding.ACClassDate.text.toString()
        else addClassBinding.ACClassDate.error = "Please select date!"

        if (addClassBinding.ACSubject.text.toString() != "") classSubject =
            addClassBinding.ACSubject.text.toString()
        else addClassBinding.ACSubject.error = "Please select subject!"

        if (addClassBinding.ACEdtClassTime.text.toString() != "") classTime =
            addClassBinding.ACEdtClassTime.text.toString()
        else addClassBinding.ACEdtClassTime.error = "Please enter time!"

        if (addClassBinding.ACEdtClassRoom.text.toString() != "") classRoom =
            addClassBinding.ACEdtClassRoom.text.toString()
        else addClassBinding.ACEdtClassRoom.error = "Please enter Room no!"

        if (addClassBinding.ACClassYear.text.toString() != "") classYear =
            addClassBinding.ACClassYear.text.toString()
        else addClassBinding.ACClassYear.error = "Please select class!"
    }
    private fun autoCompleteTextViewSetUp() {
        val itemsYear = listOf("FY", "SY", "TY")
        val adapterYear = ArrayAdapter(
            this,
            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
            itemsYear
        )
        addClassBinding.ACClassYear.setAdapter(adapterYear)
        addClassBinding.ACClassYear.setOnItemClickListener { _, _, i, _->
            val subjects = ArrayList<String>()
            FirebaseDatabase.getInstance().reference
                .child("Subjects")
                .child(TeacherDashboard.loggedTeacherDep)
                .child(itemsYear[i]).addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()) {
                            subjects.clear()
                            for (subject in snapshot.children) {
                                subjects.add(subject.value.toString())
                            }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(this@UploadClass, "No Subjects!", Toast.LENGTH_SHORT)
                            .show()
                    }
                })

          /*  if (i == 0) {
                FirebaseDatabase.getInstance().reference
                    .child("Subjects")
                    .child(TeacherDashboard.loggedTeacherDep)
                    .child("FY").addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (snapshot.exists()) {
                                subjects.clear()
                                for (subject in snapshot.children) {
                                    subjects.add(subject.value.toString())
                                }
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            Toast.makeText(this@UploadClass, "No Subjects!", Toast.LENGTH_SHORT)
                                .show()
                        }
                    })
            }
            if (i == 1) {
                FirebaseDatabase.getInstance().reference
                    .child("Subjects")
                    .child(TeacherDashboard.loggedTeacherDep)
                    .child("SY").addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (snapshot.exists()) {
                                subjects.clear()
                                for (subject in snapshot.children) {
                                    subjects.add(subject.value.toString())
                                }
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            Toast.makeText(this@UploadClass, "No Subjects!", Toast.LENGTH_SHORT)
                                .show()
                        }
                    })

            }
            if (i == 2) {
                FirebaseDatabase.getInstance().reference
                    .child("Subjects")
                    .child(TeacherDashboard.loggedTeacherDep)
                    .child("TY").addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (snapshot.exists()) {
                                subjects.clear()
                                for (subject in snapshot.children) {
                                    subjects.add(subject.value.toString())
                                }
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            Toast.makeText(this@UploadClass, "No Subjects!", Toast.LENGTH_SHORT)
                                .show()
                        }
                    })
            }*/
            val adapterSubject = ArrayAdapter(
                this,
                com.google.android.material.R.layout.support_simple_spinner_dropdown_item,
                subjects
            )
            addClassBinding.ACSubject.setAdapter(adapterSubject)
        }

    }
    @RequiresApi(Build.VERSION_CODES.N)
    private fun classDatePickerDialog() {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(

            this,
            { _, year, monthOfYear, dayOfMonth ->
                // on below line we are setting
                // date to our edit text.
                val dat = (dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + year)
                addClassBinding.ACClassDate.text = dat
            },
            year,
            month,
            day
        )
        datePickerDialog.show()
    }

}


//data class SubjectData(val year: String = "", val subject: String = "")