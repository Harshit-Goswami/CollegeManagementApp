package com.harshit.goswami.collegeapp.admin

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
import com.google.firebase.database.FirebaseDatabase
import com.harshit.goswami.collegeapp.R
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
                AddClassDialog()
            }
        }
        binding.AddSubject.setOnClickListener {
           addSubject()
        }
        binding.seeClasses.setOnClickListener {

        }


    }

    private fun addSubject() {
            addSubjectBindind = DialogAdminAddSubjectBinding.inflate(layoutInflater)
            val bottomDialog = BottomSheetDialog(this, R.style.BottomSheetStyle)
            bottomDialog.setContentView(addSubjectBindind.root)
            bottomDialog
                .setCanceledOnTouchOutside(false)
            bottomDialog.show()
            autoCompleteTextViewSetUp()

            addSubjectBindind.ASBtnAdd.setOnClickListener {
                var classYear = ""
                var subject = ""
                if (addSubjectBindind.ASClassYear.text.toString() != "") classYear =
                    addSubjectBindind.ASClassYear.text.toString()
                else addSubjectBindind.ASClassYear.error = "Please select year!"
                if (addSubjectBindind.ASEdtSubject.text.toString() != "") classYear =
                    addSubjectBindind.ASEdtSubject.text.toString()
                else addSubjectBindind.ASEdtSubject.error = "Please add subject!"

                if (classYear != "" && subject != "") {
                    FirebaseDatabase.getInstance().reference
                        .child("BScIT")
                        .child("Subjects")
                        .child(classYear)
                        .setValue(SubjectData(classYear, subject))
                        .addOnSuccessListener {  Toast.makeText(
                            this@UploadClass,
                            "Successfully Added",
                            Toast.LENGTH_SHORT
                        )
                            .show()}
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
    private fun AddClassDialog() {
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
        autoCompleteTextViewSetUp()
        addClassBinding.ACClassDate.setOnClickListener {
            classDatePickerDialog()
        }
        addClassBinding.ACBtnAdd.setOnClickListener {
            validation()
            uploadClass()
        }

    }

    private fun uploadClass() {
        if (classDate != "" && classTime != "" && classSubject != "" && classRoom != "" && classYear != "") {
            FirebaseDatabase.getInstance().reference
                .child("BScIT")
                .child("Class TimeTable")
                .child(classYear)
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
        addSubjectBindind.ASClassYear.setAdapter(adapterYear)

        addClassBinding.ACClassYear.setOnItemClickListener { _, _, i, _ ->
            if (i == 0) {
                val subjects = listOf(
                    "Imperative Programming",
                    "Digital Electronics",
                    "Operating Systems",
                    "Discrete Mathematics",
                    "Communication Skills"
                )
                val adapterSubject = ArrayAdapter(
                    this,
                    com.google.android.material.R.layout.support_simple_spinner_dropdown_item,
                    subjects
                )
                addClassBinding.ACSubject.setAdapter(adapterSubject)
            }
            if (i == 1) {
                val subjects = listOf(
                    "Python Programming",
                    "Data Structures",
                    "Computer Networks",
                    "Database Management Systems",
                    "Applied Mathematics"
                )
                val adapterSubject = ArrayAdapter(
                    this,
                    com.google.android.material.R.layout.support_simple_spinner_dropdown_item,
                    subjects
                )
                addClassBinding.ACSubject.setAdapter(adapterSubject)
            }
            if (i == 2) {
                val subjects = listOf(
                    "Software Project Management",
                    "Internet of Things",
                    "Advanced Web Programming",
                    "Linux System Administration",
                    "Enterprise Java",
                )
                val adapterSubject = ArrayAdapter(
                    this,
                    com.google.android.material.R.layout.support_simple_spinner_dropdown_item,
                    subjects
                )
                addClassBinding.ACSubject.setAdapter(adapterSubject)
            }
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

data class ClassData(
    val date: String = "",
    val time: String = "",
    val subject: String = "",
    val year: String = "",
    val roomNo: String = ""
)
data class SubjectData(val year: String = "", val subject: String = "")