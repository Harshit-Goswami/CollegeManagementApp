package com.harshit.goswami.collegeapp.adapters

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.harshit.goswami.collegeapp.R
import com.harshit.goswami.collegeapp.admin.ManageFaculty
import com.harshit.goswami.collegeapp.data.FacultyData
import com.harshit.goswami.collegeapp.databinding.DialogAdminAddSubjectBinding
import com.harshit.goswami.collegeapp.databinding.ItemFacultyinfoBinding
import com.harshit.goswami.collegeapp.teacher.TeacherDashboard

class FacultyAdapter(
    private var facultyList: ArrayList<FacultyData> = ArrayList(), val context: Context
) : RecyclerView.Adapter<FacultyAdapter.ViewHolder>() {
    class ViewHolder(internal val binding: ItemFacultyinfoBinding) :
        RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemFacultyinfoBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(facultyList[position]) {
                binding.IFiTvFacultyName.text = this.name
                binding.IFiTvFacultyDepartment.text = this.department
                binding.IFiTxtContactNo.text = this.contactNo
                Glide.with(context).load(this.downloadUrl).into(binding.IFiFacultyDp)
                binding.IFiBtnMoreOption.setOnClickListener {

                    val popupMenu = PopupMenu(context, binding.IFiBtnMoreOption)
                    if (ManageFaculty.loggedUser == "HOD") {
                        popupMenu.menuInflater.inflate(R.menu.hod_faculty_menu, popupMenu.menu)
                    } else {
                        popupMenu.menuInflater.inflate(R.menu.admin_faculty_menu, popupMenu.menu)
                    }
                    popupMenu.setOnMenuItemClickListener { menu ->
                        when (menu.itemId) {
                            R.id.menu_assign_subject -> {
                                val subjectBinding =
                                    DialogAdminAddSubjectBinding.inflate(LayoutInflater.from(context))
                                val dialog = Dialog(context, R.style.BottomSheetStyle)
                                dialog.setContentView(subjectBinding.root)
                                dialog
                                    .setCanceledOnTouchOutside(false)
                                dialog.show()
                                subjectBinding.dialogTILEdtSubName.visibility = View.GONE
                                subjectBinding.dialogSliderBtn.visibility = View.GONE
                                subjectBinding.dialogASBtnAdd.text = "ASSIGN"
                                subjectBinding.dialogTxtAddSubject.text = "Assign Subject"
                                val itemsYear = listOf("FY", "SY", "TY")
                                val adapterYear = ArrayAdapter(
                                    context,
                                    androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                                    itemsYear
                                )
                                subjectBinding.ASClassYear.setAdapter(adapterYear)
                                subjectBinding.ASClassYear.setOnItemClickListener { _, _, i, _ ->
                                    val subjects = ArrayList<String>()
                                    if (i == 0) {
                                        FirebaseDatabase.getInstance().reference
                                            .child("Subjects")
                                            .child(TeacherDashboard.loggedTeacherDep)
                                            .child("FY")
                                            .addValueEventListener(object : ValueEventListener {
                                                override fun onDataChange(snapshot: DataSnapshot) {
                                                    if (snapshot.exists()) {
                                                        subjects.clear()
                                                        for (subject in snapshot.children) {
                                                            subjects.add(subject.value.toString())
                                                        }
                                                    }
                                                }

                                                override fun onCancelled(error: DatabaseError) {
                                                    Toast.makeText(
                                                        context,
                                                        "No Subjects!",
                                                        Toast.LENGTH_SHORT
                                                    )
                                                        .show()
                                                }
                                            })
                                    }
                                    if (i == 1) {
                                        FirebaseDatabase.getInstance().reference
                                            .child("Subjects")
                                            .child(TeacherDashboard.loggedTeacherDep)
                                            .child("SY")
                                            .addValueEventListener(object : ValueEventListener {
                                                override fun onDataChange(snapshot: DataSnapshot) {
                                                    if (snapshot.exists()) {
                                                        subjects.clear()
                                                        for (subject in snapshot.children) {
                                                            subjects.add(subject.value.toString())
                                                        }
                                                    }
                                                }

                                                override fun onCancelled(error: DatabaseError) {
                                                    Toast.makeText(
                                                        context,
                                                        "No Subjects!",
                                                        Toast.LENGTH_SHORT
                                                    )
                                                        .show()
                                                }
                                            })

                                    }
                                    if (i == 2) {
                                        FirebaseDatabase.getInstance().reference
                                            .child("Subjects")
                                            .child(TeacherDashboard.loggedTeacherDep)
                                            .child("TY")
                                            .addValueEventListener(object : ValueEventListener {
                                                override fun onDataChange(snapshot: DataSnapshot) {
                                                    if (snapshot.exists()) {
                                                        subjects.clear()
                                                        for (subject in snapshot.children) {
                                                            subjects.add(subject.value.toString())
                                                        }
                                                    }
                                                }

                                                override fun onCancelled(error: DatabaseError) {
                                                    Toast.makeText(
                                                        context,
                                                        "No Subjects!",
                                                        Toast.LENGTH_SHORT
                                                    )
                                                        .show()
                                                }
                                            })
                                    }
                                    val adapterSubject = ArrayAdapter(
                                        context,
                                        com.google.android.material.R.layout.support_simple_spinner_dropdown_item,
                                        subjects
                                    )
                                    subjectBinding.ASClassSubject.setAdapter(adapterSubject)
                                }
                                subjectBinding.dialogASBtnAdd.setOnClickListener {
                                    FirebaseDatabase.getInstance().reference.child("Faculty Data")
                                        .child(TeacherDashboard.loggedTeacherDep)
                                        .child(this.name)
                                        .child("Assigned Subject")
                                        .child(subjectBinding.ASClassSubject.text.toString())
                                        .setValue(subjectBinding.ASClassSubject.text.toString())
                                }
                            }
                            R.id.menu_edit_faculty -> {}
                            R.id.menu_deleteFaculty -> {
                                if (ManageFaculty.loggedUser == "HOD") {
                                    FirebaseDatabase.getInstance().reference.child("Faculty Data")
                                        .child(this.department)
                                        .child(this.name).removeValue()
                                        .addOnSuccessListener {
                                            Toast.makeText(
                                                context, "Deleted!",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                        .addOnFailureListener { e ->
                                            Toast.makeText(
                                                context, "ERR!-${e.message}",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                }
                                if (ManageFaculty.loggedUser == "admin") {
                                    FirebaseDatabase.getInstance().reference.child("Faculty Data")
                                        .child(this.department)
                                        .child("HOD").removeValue()
                                        .addOnSuccessListener {
                                            Toast.makeText(
                                                context, "Deleted!",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                        .addOnFailureListener {
                                            Toast.makeText(
                                                context, "ERR_${it.message}",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                }
                            }
                            R.id.menu_send_massage -> {}
                        }
                        true
                    }

                    popupMenu.show()
                }

            }
        }

    }

    // return the size of languageList
    override fun getItemCount(): Int {
        return facultyList.size
    }

}