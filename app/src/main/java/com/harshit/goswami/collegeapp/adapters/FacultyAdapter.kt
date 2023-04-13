package com.harshit.goswami.collegeapp.adapters

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.harshit.goswami.collegeapp.R
import com.harshit.goswami.collegeapp.ViewImageActivity
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
                if (ManageFaculty.loggedUser == "student"){
                    binding.IFiBtnMoreOption.visibility = View.GONE
                }
                binding.IFiTvFacultyName.text = this.name
                binding.IFiTvFacultyDepartment.text = this.department
                binding.IFiTxtContactNo.text = this.contactNo
                Glide.with(context).load(this.downloadUrl).into(binding.IFiFacultyDp)
                binding.IFiFacultyDp.setOnClickListener {
                    val i = Intent(context, ViewImageActivity::class.java)
                    i.putExtra("imageUrl",this.downloadUrl)
                    context.startActivity(i)
                }
                binding.IFiBtnMoreOption.setOnClickListener {

                    val popupMenu = PopupMenu(context, binding.IFiBtnMoreOption)
                    if (ManageFaculty.loggedUser == "HOD") {
                        popupMenu.menuInflater.inflate(R.menu.hod_faculty_menu, popupMenu.menu)
                    } else {
                        popupMenu.menuInflater.inflate(R.menu.admin_faculty_menu, popupMenu.menu)
                    }
                    popupMenu.setOnMenuItemClickListener { menu ->
                        when (menu.itemId) {
                            R.id.assign_subject -> {
                                try {
                                    val subjectBinding =
                                        DialogAdminAddSubjectBinding.inflate(
                                            LayoutInflater.from(
                                                context
                                            )
                                        )
                                    val dialog = Dialog(context, R.style.BottomSheetStyle)
                                    dialog.setContentView(subjectBinding.root)
                                    dialog
                                        .setCanceledOnTouchOutside(false)
                                    dialog.show()

                                    subjectBinding.dialogTILEdtSubName.visibility = View.GONE
//                                subjectBinding.dialogSliderBtn.visibility = View.GONE
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
                                        subjectBinding.ASClassSubject.text.clear()
                                        val subjects = ArrayList<String>()
                                        FirebaseFirestore.getInstance()
                                            .collection("${itemsYear[i]}${TeacherDashboard.loggedTeacherDep}-Subjects")
                                            .addSnapshotListener { value, error ->
//                                                subjects.clear()
                                                if (error != null) {
                                                    Toast.makeText(
                                                        context,
                                                        "Error found is $error",
                                                        Toast.LENGTH_SHORT
                                                    )
                                                        .show();
                                                }
                                                value?.forEach { ds ->
                                                    subjects.add(ds["subjectName"].toString())
                                                }
                                                val adapterSubject = ArrayAdapter(
                                                    context,
                                                    com.google.android.material.R.layout.support_simple_spinner_dropdown_item,
                                                    subjects
                                                )
                                                subjectBinding.ASClassSubject.setAdapter(
                                                    adapterSubject
                                                )
                                            }
                                    }
                                    subjectBinding.dialogASBtnAdd.setOnClickListener {
                                        FirebaseDatabase.getInstance().reference.child("Faculty Data")
                                            .child(TeacherDashboard.loggedTeacherDep)
                                            .child(this.name)
                                            .child("Assigned Subject")
                                            .child(subjectBinding.ASClassYear.text.toString())
                                            .child(subjectBinding.ASClassSubject.text.toString())
                                            .setValue(subjectBinding.ASClassSubject.text.toString())
                                            .addOnSuccessListener {
                                                Toast.makeText(
                                                    context,
                                                    "Assigned!!",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                            }
                                    }
                                    val subjectList = ArrayList<Map<String, String>>()
                                    subjectBinding.rsvDialogAddSubject.layoutManager =
                                        LinearLayoutManager(
                                            context,
                                            LinearLayoutManager.VERTICAL,
                                            false
                                        )
                                    subjectBinding.rsvDialogAddSubject.setHasFixedSize(true)
                                    subjectBinding.rsvDialogAddSubject.adapter =
                                        DeleteSubjectAdapter(
                                            subjectList,
                                            context
                                        )
                                    FirebaseDatabase.getInstance().reference.child("Faculty Data")
                                        .child(TeacherDashboard.loggedTeacherDep)
                                        .child(this.name)
                                        .child("Assigned Subject")
                                        .addValueEventListener(object : ValueEventListener {
                                            override fun onDataChange(snapshot: DataSnapshot) {
                                                subjectList.clear()
                                                snapshot.children.forEach { year ->
                                                    year.children.forEach {
                                                        subjectList.add(
                                                            mapOf(
                                                                "subjectName" to it.value.toString(),
                                                                "teacherName" to facultyList[holder.adapterPosition].name,
                                                                "department" to facultyList[holder.adapterPosition].department,
                                                                "year" to year.key.toString(),
                                                                "activity" to "deleteAssignSubject"
                                                            )
                                                        )
                                                    }
                                                }
                                                subjectBinding.rsvDialogAddSubject.adapter?.notifyDataSetChanged()
                                            }

                                            override fun onCancelled(error: DatabaseError) {
                                            }

                                        })

                                } catch (e: Exception)
                                {
                                    Toast.makeText(
                                        context,
                                        e.message.toString(),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    Log.e("errrrrr", e.message, e)
                                }
                            }

                            R.id.menu_edit_faculty -> {

                            }
                            R.id.menu_deleteFaculty -> {
                                if (ManageFaculty.loggedUser == "HOD") {
                                    if (this.teacherType == "teacher") {
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
                                }
                                if (ManageFaculty.loggedUser == "admin") {
                                    if (this.teacherType == "HOD") {
                                        FirebaseDatabase.getInstance().reference.child("Faculty Data")
                                            .child(this.department)
                                            .child(this.name).removeValue()
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