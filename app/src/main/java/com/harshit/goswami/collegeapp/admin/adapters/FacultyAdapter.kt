package com.harshit.goswami.collegeapp.admin.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.harshit.goswami.collegeapp.admin.dataClasses.FacultyData
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
                binding.tvFacultyName.text = this.name
                binding.tvFacultyDepartment.text = this.department
                Glide.with(context).load(this.downloadUrl).into(binding.facultyDp)
            }
        }

    }

    // return the size of languageList
    override fun getItemCount(): Int {
        return facultyList.size
    }
}