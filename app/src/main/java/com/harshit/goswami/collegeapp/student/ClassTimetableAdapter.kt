package com.harshit.goswami.collegeapp.student

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.harshit.goswami.collegeapp.dataClasses.ClassData
import com.harshit.goswami.collegeapp.databinding.ItemClassTimeTableBinding


class ClassTimetableAdapter(
    private var classList: ArrayList<ClassData> = ArrayList(),
    val context: Context,
) : RecyclerView.Adapter<ClassTimetableAdapter.ViewHolder>() {
    class ViewHolder(internal val binding: ItemClassTimeTableBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemClassTimeTableBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(classList[position]) {
              binding.itemClassDate.text = this.date
                binding.itemClassTime.text = this.time
                binding.itemSubjectName.text = this.subject
                binding.itemClassroom.text =  this.roomNo
            }

        }
    }

    // return the size of languageList
    override fun getItemCount(): Int {
        return classList.size
    }
}