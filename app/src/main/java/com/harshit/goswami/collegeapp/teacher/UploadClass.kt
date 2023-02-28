package com.harshit.goswami.collegeapp.teacher

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.harshit.goswami.collegeapp.R
import com.harshit.goswami.collegeapp.adapters.ClassTimetableAdapter
import com.harshit.goswami.collegeapp.adapters.DeleteSubjectAdapter
import com.harshit.goswami.collegeapp.data.ClassData
import com.harshit.goswami.collegeapp.databinding.ActivityAdminUploadClassBinding
import com.harshit.goswami.collegeapp.databinding.DialogAdminAddClassBinding
import com.harshit.goswami.collegeapp.databinding.DialogAdminAddSubjectBinding
import com.harshit.goswami.collegeapp.databinding.DialogDeleteClassTimetableBinding

class UploadClass : AppCompatActivity() {
    private lateinit var binding: ActivityAdminUploadClassBinding
    private lateinit var addClassBinding: DialogAdminAddClassBinding
    private lateinit var addSubjectBindind: DialogAdminAddSubjectBinding

    companion object {
        lateinit var deleteClassBinding: DialogDeleteClassTimetableBinding
        val subjectListFY = ArrayList<Map<String, String>>()
        val subjectListSY = ArrayList<Map<String, String>>()
        val subjectListTY = ArrayList<Map<String, String>>()
    }

    private val fireDb = FirebaseDatabase.getInstance().reference

    private var classDate = ""
    private var classStartTime = ""
    private var classEndTime = ""
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
        binding.deleteClasses.setOnClickListener {
            deleteClassDialog()
        }
        binding.deleteSubject.setOnClickListener {
            deleteSubjectDialog()
        }

    }

    //#################################  Dialogs   ########################################
    private fun addSubjectDialog() {
//        try {
        addSubjectBindind = DialogAdminAddSubjectBinding.inflate(layoutInflater)
        val bottomDialog = Dialog(this, R.style.BottomSheetStyle)
        bottomDialog.setContentView(addSubjectBindind.root)
        bottomDialog
            .setCanceledOnTouchOutside(false)
        bottomDialog.show()
//        } catch (e: Exception) {
//            Log.d("dialogError","${e.message}")
//        }
        addSubjectBindind.txtAssignedSubject.visibility = View.GONE
        addSubjectBindind.dialogTILSelectSubject.visibility = View.GONE
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
                FirebaseFirestore.getInstance()
                    .collection("$subjectClassYear${TeacherDashboard.loggedTeacherDep}-Subjects")
                    .document(subjectName)
                    .set(
                        mapOf(
                            "department" to TeacherDashboard.loggedTeacherDep,
                            "classYear" to subjectClassYear,
                            "subjectName" to subjectName,
                        )
                    )
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

    private fun addClassDialog() {
//        try {
        addClassBinding = DialogAdminAddClassBinding.inflate(layoutInflater)
        val bottomDialog = Dialog(this, R.style.BottomSheetStyle)
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
        timePickerDialog()
        addClassBinding.ACClassDate.setOnClickListener {
            classDatePickerDialog()
        }
        addClassBinding.ACBtnAdd.setOnClickListener {
            validation()
            addClass()
        }
    }

    private fun deleteClassDialog() {
        val classesListFY = ArrayList<ClassData>()
        val classesListSY = ArrayList<ClassData>()
        val classesListTY = ArrayList<ClassData>()
        val deleteClassBinding = DialogDeleteClassTimetableBinding.inflate(layoutInflater)
        val bottomDialog = Dialog(this, R.style.BottomSheetStyle)
        bottomDialog.setContentView(deleteClassBinding.root)
        bottomDialog
            .setCanceledOnTouchOutside(false)

        ////////    init  Recycler TY   //////////
        deleteClassBinding.rsvDialogDeleteClassTY.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        deleteClassBinding.rsvDialogDeleteClassTY.setHasFixedSize(true)
        deleteClassBinding.rsvDialogDeleteClassTY.adapter =
            ClassTimetableAdapter(
                classesListTY,
                this, ""
            )
        deleteClassBinding.rsvDialogDeleteClassTY.adapter?.notifyDataSetChanged()

        ////////     Recycler SY   //////////
        deleteClassBinding.rsvDialogDeleteClassSY.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        deleteClassBinding.rsvDialogDeleteClassSY.setHasFixedSize(true)
        deleteClassBinding.rsvDialogDeleteClassSY.adapter =
            ClassTimetableAdapter(
                classesListSY,
                this, ""
            )
        deleteClassBinding.rsvDialogDeleteClassSY.adapter?.notifyDataSetChanged()
        ////////     Recycler FY   //////////
        deleteClassBinding.rsvDialogDeleteClassFY.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        deleteClassBinding.rsvDialogDeleteClassFY.setHasFixedSize(true)
        deleteClassBinding.rsvDialogDeleteClassFY.adapter =
            ClassTimetableAdapter(
                classesListFY,
                this, ""
            )
        deleteClassBinding.rsvDialogDeleteClassFY.adapter?.notifyDataSetChanged()
        // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@   GETTING CLASS DATA FROM FIREBASE  @@@@@@@@@@@@@@@@@@@@@@@@@@
        fireDb.child("Class TimeTable")
            .child(TeacherDashboard.loggedTeacherDep)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        classesListTY.clear()
                        classesListSY.clear()
                        classesListFY.clear()
                        try {
                            snapshot.children.forEach { year ->
                                year.children.forEach {
                                    if (it.getValue(ClassData::class.java)?.year == "TY") {
                                        classesListTY.add(it.getValue(ClassData::class.java)!!)
                                    }
                                    if (it.getValue(ClassData::class.java)?.year == "SY") {
                                        classesListSY.add(it.getValue(ClassData::class.java)!!)
                                    }
                                    if (it.getValue(ClassData::class.java)?.year == "FY") {
                                        classesListFY.add(it.getValue(ClassData::class.java)!!)
                                    }
                                }
                            }
                            deleteClassBinding.rsvDialogDeleteClassFY.adapter?.notifyDataSetChanged()
                            deleteClassBinding.rsvDialogDeleteClassSY.adapter?.notifyDataSetChanged()
                            deleteClassBinding.rsvDialogDeleteClassTY.adapter?.notifyDataSetChanged()


                        } catch (e: Exception) {
                            Log.d("Error_Massage", "${e.message}", e)
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                }

            })
        bottomDialog.show()
    }

    private fun deleteSubjectDialog() {
        deleteClassBinding = DialogDeleteClassTimetableBinding.inflate(layoutInflater)
        val bottomDialog = Dialog(this, R.style.BottomSheetStyle)
        bottomDialog.setContentView(deleteClassBinding.root)
        bottomDialog
            .setCanceledOnTouchOutside(false)

        ////////    init  Recycler TY   //////////
        deleteClassBinding.rsvDialogDeleteClassTY.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        deleteClassBinding.rsvDialogDeleteClassTY.setHasFixedSize(true)
        deleteClassBinding.rsvDialogDeleteClassTY.adapter =
            DeleteSubjectAdapter(
                subjectListTY,
                this
            )
//        deleteClassBinding.rsvDialogDeleteClassTY.adapter?.notifyDataSetChanged()

        ////////     Recycler SY   //////////
        deleteClassBinding.rsvDialogDeleteClassSY.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        deleteClassBinding.rsvDialogDeleteClassSY.setHasFixedSize(true)
        deleteClassBinding.rsvDialogDeleteClassSY.adapter =
            DeleteSubjectAdapter(
                subjectListSY,
                this
            )
//        deleteClassBinding.rsvDialogDeleteClassSY.adapter?.notifyDataSetChanged()
        ////////     Recycler FY   //////////
        deleteClassBinding.rsvDialogDeleteClassFY.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        deleteClassBinding.rsvDialogDeleteClassFY.setHasFixedSize(true)
        deleteClassBinding.rsvDialogDeleteClassFY.adapter =
            DeleteSubjectAdapter(
                subjectListFY,
                this
            )
//        deleteClassBinding.rsvDialogDeleteClassFY.adapter?.notifyDataSetChanged()
        // @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@   GETTING Subject DATA FROM FIREBASE  @@@H.G.@@@@@@@@@@@@@@@@@@@@@@@
        FirebaseFirestore.getInstance()
            .collection("TY${TeacherDashboard.loggedTeacherDep}-Subjects")
            .addSnapshotListener { value, error ->
                subjectListTY.clear()
                if (error != null) {
                    Toast.makeText(this@UploadClass, "Error found is $error", Toast.LENGTH_SHORT)
                        .show()
                }
/*                for (dc in value!!.documentChanges) {
                    when (dc.type) {
                        DocumentChange.Type.ADDED -> Log.d(TAG, "New city: ${dc.document.data}")
                        DocumentChange.Type.MODIFIED -> Log.d(TAG, "Modified city: ${dc.document.data}")
                        DocumentChange.Type.REMOVED -> subjectListTY.remove()
                    }
                }*/
                value?.forEach { ds ->
                    subjectListTY.add(
                        mapOf(
                            "department" to ds["department"].toString(),
                            "subjectName" to ds["subjectName"].toString(),
                            "classYear" to ds["classYear"].toString(),
                            "activity" to "deleteSubject"
                        )
                    )
                }
                deleteClassBinding.rsvDialogDeleteClassTY.adapter?.notifyDataSetChanged()
            }
        FirebaseFirestore.getInstance()
            .collection("SY${TeacherDashboard.loggedTeacherDep}-Subjects")
            .addSnapshotListener { value, error ->
                subjectListSY.clear()
                if (error != null) {
                    Toast.makeText(this@UploadClass, "Error found is $error", Toast.LENGTH_SHORT)
                        .show()
                }
                value?.forEach { ds ->
                    subjectListSY.add(
                        mapOf(
                            "department" to ds["department"].toString(),
                            "subjectName" to ds["subjectName"].toString(),
                            "classYear" to ds["classYear"].toString(),
                            "activity" to "deleteSubject"
                        )
                    )
                }
                deleteClassBinding.rsvDialogDeleteClassSY.adapter?.notifyDataSetChanged()

            }
        FirebaseFirestore.getInstance()
            .collection("FY${TeacherDashboard.loggedTeacherDep}-Subjects")
            .addSnapshotListener { value, error ->
                subjectListFY.clear()
                if (error != null) {
                    Toast.makeText(this@UploadClass, "Error found is $error", Toast.LENGTH_SHORT)
                        .show()
                }
                value?.forEach { ds ->
                    subjectListFY.add(
                        mapOf(
                            "department" to ds["department"].toString(),
                            "subjectName" to ds["subjectName"].toString(),
                            "classYear" to ds["classYear"].toString(),
                            "activity" to "deleteSubject"
                        )
                    )
                }
                deleteClassBinding.rsvDialogDeleteClassFY.adapter?.notifyDataSetChanged()
            }
        bottomDialog.show()
        deleteClassBinding.rsvDialogDeleteClassTY.adapter?.notifyDataSetChanged()

    }


    //#################################  ADD CLASS FUNCTIONS   ########################################

    private fun addClass() {
        if (classDate != "" && classStartTime != "" && classEndTime != "" && classSubject != "" && classRoom != "" && classYear != "") {
            fireDb.child("Faculty Data").child(TeacherDashboard.loggedTeacherDep)
                .addValueEventListener(object : ValueEventListener {
                    lateinit var teacherName: String
                    override fun onDataChange(snapshot: DataSnapshot) {
                        try {
                            if (snapshot.exists()) {
                                snapshot.children.forEach { t ->
                                    t.child("Assigned Subject").child(classYear).children.forEach {
                                        if (it.value == classSubject) {
                                            teacherName = t.key.toString()
                                        }
                                    }
                                }
                            }
                            fireDb.child("Class TimeTable")
                                .child(TeacherDashboard.loggedTeacherDep)
                                .child(classYear).child(classSubject)
                                .setValue(
                                    ClassData(
                                        classDate,
                                        "$classStartTime - $classEndTime",
                                        classSubject,
                                        teacherName,
                                        classYear,
                                        classRoom
                                    )
                                )
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
                        } catch (e: Exception) {
                            Toast.makeText(
                                this@UploadClass,
                                "Err-Teacher not Assigned!!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.e("firebase err", error.message, error.toException())
                    }
                })

        }
    }

    private fun timePickerDialog() {
        val timePickerDialogListener1: TimePickerDialog.OnTimeSetListener =
            TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute -> // logic to properly handle

                // the picked timings by user
                val formattedTime: String = when {
                    hourOfDay == 0 -> {
                        if (minute < 10) {
                            "${hourOfDay + 12}:0${minute} am"
                        } else {
                            "${hourOfDay + 12}:${minute} am"
                        }
                    }
                    hourOfDay > 12 -> {
                        if (minute < 10) {
                            "${hourOfDay - 12}:0${minute} pm"
                        } else {
                            "${hourOfDay - 12}:${minute} pm"
                        }
                    }
                    hourOfDay == 12 -> {
                        if (minute < 10) {
                            "${hourOfDay}:0${minute} pm"
                        } else {
                            "${hourOfDay}:${minute} pm"
                        }
                    }
                    else -> {
                        if (minute < 10) {
                            "${hourOfDay}:${minute} am"
                        } else {
                            "${hourOfDay}:${minute} am"
                        }
                    }
                }

                addClassBinding.ACClassStartTime.text = formattedTime
            }
        val timePickerDialogListener2: TimePickerDialog.OnTimeSetListener =
            TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute -> // logic to properly handle

                // the picked timings by user
                val formattedTime: String = when {
                    hourOfDay == 0 -> {
                        if (minute < 10) {
                            "${hourOfDay + 12}:0${minute} am"
                        } else {
                            "${hourOfDay + 12}:${minute} am"
                        }
                    }
                    hourOfDay > 12 -> {
                        if (minute < 10) {
                            "${hourOfDay - 12}:0${minute} pm"
                        } else {
                            "${hourOfDay - 12}:${minute} pm"
                        }
                    }
                    hourOfDay == 12 -> {
                        if (minute < 10) {
                            "${hourOfDay}:0${minute} pm"
                        } else {
                            "${hourOfDay}:${minute} pm"
                        }
                    }
                    else -> {
                        if (minute < 10) {
                            "${hourOfDay}:${minute} am"
                        } else {
                            "${hourOfDay}:${minute} am"
                        }
                    }
                }

                addClassBinding.ACClassEndTime.text = formattedTime
            }

        addClassBinding.ACClassStartTime.setOnClickListener {
            val timePicker = TimePickerDialog(
                this,
                timePickerDialogListener1,
                // default hour when the time picker dialog is opened
                12,
                // default minute when the time picker dialog is opened
                10,
                // 24 hours time picker is false (varies according to the region)
                false
            )
            // then after building the timepicker
            // dialog show the dialog to user
            timePicker.show()
        }
        addClassBinding.ACClassEndTime.setOnClickListener {
            val timePicker = TimePickerDialog(
                this,
                timePickerDialogListener2,
                // default hour when the time picker dialog is opened
                12,
                // default minute when the time picker dialog is opened
                10,
                // 24 hours time picker is false (varies according to the region)
                false
            )
            // then after building the timepicker
            // dialog show the dialog to user
            timePicker.show()
        }
    }

    private fun validation() {
        if (addClassBinding.ACClassDate.text != "") classDate =
            addClassBinding.ACClassDate.text.toString()
        else addClassBinding.ACClassDate.error = "Please select date!"

        if (addClassBinding.ACSubject.text.toString() != "") classSubject =
            addClassBinding.ACSubject.text.toString()
        else addClassBinding.ACSubject.error = "Please select subject!"

        if (addClassBinding.ACClassStartTime.text.toString() != "") classStartTime =
            addClassBinding.ACClassStartTime.text.toString()
        else addClassBinding.ACClassStartTime.error = "Please enter Start time!"

        if (addClassBinding.ACClassEndTime.text.toString() != "") classEndTime =
            addClassBinding.ACClassEndTime.text.toString()
        else addClassBinding.ACClassEndTime.error = "Please enter End time!"

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
        addClassBinding.ACClassYear.setOnItemClickListener { _, _, i, _ ->
            addClassBinding.ACSubject.text.clear()
            val subjects = ArrayList<String>()
            FirebaseFirestore.getInstance()
                .collection("${itemsYear[i]}${TeacherDashboard.loggedTeacherDep}-Subjects")
                .addSnapshotListener { value, error ->
                    subjects.clear()
                    if (error != null) {
                        Toast.makeText(
                            this@UploadClass,
                            "Error found is $error",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                    }
                    value?.forEach { ds ->
                        subjects.add(ds["subjectName"].toString())
                    }
                }

            val adapterSubject = ArrayAdapter(
                this,
                com.google.android.material.R.layout.support_simple_spinner_dropdown_item,
                subjects
            )
            addClassBinding.ACSubject.setAdapter(adapterSubject)
        }

    }

    private fun classDatePickerDialog() {
        val c = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Calendar.getInstance()
        } else {
            TODO("VERSION.SDK_INT < N")
        }
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, y, monthOfYear, dayOfMonth ->
                // on below line we are setting
                // date to our edit text.
                val dat = (dayOfMonth.toString() + "-" + (monthOfYear + 1) + "-" + y)
                addClassBinding.ACClassDate.text = dat
            },
            year,
            month,
            day
        )
        datePickerDialog.show()
    }

}
