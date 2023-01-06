package com.harshit.goswami.collegeapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase
import com.harshit.goswami.collegeapp.dataClasses.RegisteredStudentData
import com.harshit.goswami.collegeapp.databinding.ItemResisteredStudentBinding

class RegisterdStudentAdapter(
    private var StudentList: ArrayList<RegisteredStudentData> = ArrayList(),
    private val context: Context,
) : RecyclerView.Adapter<RegisterdStudentAdapter.ViewHolder>() {

    class ViewHolder(internal val binding: ItemResisteredStudentBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    val binding =
        ItemResisteredStudentBinding.inflate(LayoutInflater.from(context), parent, false)
    return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        with(holder) {
            with(StudentList[position]) {
                binding.iRSstudName.text = this.fullName
                binding.iRSstudRollNo.text ="-(${this.rollNo})"
                binding.iRSyear.text = this.year
                binding.iRSstudDepartment.text = this.department
                binding.iRSFABDecline.setOnClickListener {
                    FirebaseDatabase.getInstance().reference.child("BScIT")
                        .child("Registered Student")
                        .child(this.fullName).removeValue()
                        .addOnSuccessListener { Toast.makeText(context,"Successfully Removed .",Toast.LENGTH_SHORT).show() }
                        .addOnFailureListener { Toast.makeText(context,"Error."+it.message,Toast.LENGTH_SHORT).show()  }
                }
                binding.iRSFABAccept.setOnClickListener {
                    FirebaseDatabase.getInstance().reference.child("BScIT").child("Your Students")
                        .child(this.fullName).setValue(
                            RegisteredStudentData(
                                this.fullName,
                                this.rollNo,
                                this.department,
                                this.year,
                                this.contactNo,
                                this.password
                            )
                        )
                    FirebaseDatabase.getInstance().reference.child("BScIT")
                        .child("Registered Student")
                        .child(this.fullName).removeValue()
                        .addOnSuccessListener { Toast.makeText(context,"Successfully Added To your Student List",Toast.LENGTH_SHORT).show()}
                        .addOnFailureListener {  Toast.makeText(context,"Error."+it.message,Toast.LENGTH_SHORT).show() }
                }

            }
        }
    }

    override fun getItemCount(): Int {
        return StudentList.size
    }
}