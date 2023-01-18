package com.harshit.goswami.collegeapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.harshit.goswami.collegeapp.data.StudentData
import com.harshit.goswami.collegeapp.databinding.ItemResisteredStudentBinding

class RegisterdStudentAdapter(
    private var StudentList: ArrayList<StudentData> = ArrayList(),
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
             binding.iYSstudContactNo.text = this.contactNo

            }
        }
    }

    override fun getItemCount(): Int {
        return StudentList.size
    }
}