package com.harshit.goswami.collegeapp.adapters

import android.content.Context
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase
import com.harshit.goswami.collegeapp.R
import com.harshit.goswami.collegeapp.admin.ManageStudent
import com.harshit.goswami.collegeapp.data.StudentData
import com.harshit.goswami.collegeapp.databinding.DialogAdminChangePasswordBinding
import com.harshit.goswami.collegeapp.databinding.DialogStudentDetailsBinding
import com.harshit.goswami.collegeapp.databinding.ItemYourStudentsBinding
import com.harshit.goswami.collegeapp.teacher.TeacherDashboard


class YourStudentAdapter(
    private var StudentList: ArrayList<StudentData> = ArrayList(),
    private val context: Context,
    private val activity:String = ""
) : RecyclerView.Adapter<YourStudentAdapter.ViewHolder>() {

    class ViewHolder(internal val binding: ItemYourStudentsBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemYourStudentsBinding.inflate(LayoutInflater.from(context), parent, false)
        return YourStudentAdapter.ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(StudentList[position]) {

                binding.iYSstudName.text = this.fullName
                binding.iYSstudDepartment.text = this.department
                binding.iYSyear.text = this.year
                binding.iYSstudContactNo.text = this.contactNo
                binding.iYSstudRollNo.text = this.rollNo
                if (ManageStudent.userType == "HOD") binding.iYSBtnDelete.visibility = View.GONE
                if (activity == "manageCr") binding.iYSBtnDelete.visibility = View.VISIBLE
                binding.iYSBtnDelete.setOnClickListener {
                    if (activity == "manageCr"){
                        FirebaseDatabase.getInstance().reference.child("CR Data").child(
                            TeacherDashboard.loggedTeacherDep
                        ).child(this.year).child(this.rollNo).removeValue().addOnSuccessListener {
                            Toast.makeText(context,"CR Removed",Toast.LENGTH_SHORT).show()
                        }
                    }else{
                        FirebaseDatabase.getInstance().reference.keepSynced(true)
                        FirebaseDatabase.getInstance().reference
                        .child("Students")
                        .child(this.department)
                        .child(this.year)
                        .child(this.rollNo).removeValue()
                        .addOnSuccessListener {
                            Toast.makeText(
                                context,
                                "Deleted!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        .addOnFailureListener {
                            Toast.makeText(
                                context,
                                "Deleted!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
//                            }
//
//                        }
//                        true
//                    }

//                    popupMenu.show()

                }
                binding.root.setOnClickListener {
                    try {
                        val viewBinding= DialogStudentDetailsBinding.inflate(LayoutInflater.from(context))
                        val viewStudDialog = AlertDialog.Builder(
                            context,
                            com.google.android.material.R.style.Base_Theme_Material3_Light_Dialog
                        ).create()
                        viewStudDialog.window?.setGravity(Gravity.CENTER)
                        viewStudDialog.setCancelable(true)
                        viewStudDialog.setView(viewBinding.root)
                        viewStudDialog.setCanceledOnTouchOutside(true)
                        viewBinding.dialogStudRollNo.text = this.rollNo
                        viewBinding.dialogStudName.text = this.fullName
                        viewBinding.dialogStudYearDep.text = "${this.year}${this.department}"
                        viewBinding.dialogStudContact.text = this.contactNo
                        viewBinding.dialogStudPassword.text = this.password
                        viewStudDialog.show()
                    } catch (e: Exception) {
                        Log.d("dialog Error-", "${e.message}")
                    }

                }
            }
        }
    }

    override fun getItemCount(): Int {
        return StudentList.size
    }

}