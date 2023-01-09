package com.harshit.goswami.collegeapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase
import com.harshit.goswami.collegeapp.R
import com.harshit.goswami.collegeapp.data.RegisteredStudentData
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
                    // Initializing the popup menu and giving the reference as current context
                    val popupMenu = PopupMenu(context, binding.iYSMoreOptions)
                    popupMenu.menuInflater.inflate(R.menu.students,popupMenu.menu)
                    popupMenu.setOnMenuItemClickListener {
                        when(it.title){
                            "Delete Student"->{
                                FirebaseDatabase.getInstance().reference.keepSynced(true)
                                FirebaseDatabase.getInstance().reference.child("BScIT")
                                    .child("Your Students")
                                    .child(this.fullName).removeValue()
                                    .addOnSuccessListener {Toast.makeText(context,"Deleted!",Toast.LENGTH_SHORT).show()  }
                                    .addOnFailureListener { Toast.makeText(context,"Deleted!",Toast.LENGTH_SHORT).show() }
                            }

                        }
                        true
                    }

                    popupMenu.show()


                }
            }
        }
    }

    override fun getItemCount(): Int {
        return StudentList.size
    }

}