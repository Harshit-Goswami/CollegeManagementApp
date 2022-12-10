package com.harshit.goswami.collegeapp.admin.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.harshit.goswami.collegeapp.admin.dataClasses.RegisteredStudentData
import com.harshit.goswami.collegeapp.databinding.ItemYourStudentsBinding

class YourStudentAdapter(
    private var StudentList: ArrayList<RegisteredStudentData> = ArrayList(),
    private val context: Context,
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
                binding.iYSstudRollNo.text = "-(${this.rollNo})"
                binding.iYSMoreOptions.setOnClickListener{

                }
            }
        }
    }

    override fun getItemCount(): Int {
        return StudentList.size
    }

}