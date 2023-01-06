package com.harshit.goswami.collegeapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.database.FirebaseDatabase
import com.harshit.goswami.collegeapp.R
import com.harshit.goswami.collegeapp.dataClasses.FacultyData
import com.harshit.goswami.collegeapp.databinding.ItemFacultyinfoBinding

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
                    popupMenu.menuInflater.inflate(R.menu.faculty_menu,popupMenu.menu)
                    popupMenu.setOnMenuItemClickListener {
                        when(it.title){
                            "Delete Faculty"->{
                                FirebaseDatabase.getInstance().reference.child("BScIT")
                                    .child("Faculty Data").child(this.name)
                                    .removeValue()
                                    .addOnSuccessListener {
                                        Toast.makeText(context,"Deleted!",
                                            Toast.LENGTH_SHORT).show()  }
                                    .addOnFailureListener { Toast.makeText(context,"Deleted!",
                                        Toast.LENGTH_SHORT).show() }
                            }

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