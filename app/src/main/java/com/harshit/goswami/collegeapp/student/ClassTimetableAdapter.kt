package com.harshit.goswami.collegeapp.student

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.harshit.goswami.collegeapp.admin.DeleteNotice
import com.harshit.goswami.collegeapp.admin.dataClasses.ClassData
import com.harshit.goswami.collegeapp.admin.dataClasses.NoticeData
import com.harshit.goswami.collegeapp.databinding.ItemClassTimeTableBinding
import com.harshit.goswami.collegeapp.databinding.ItemDeleteNoticeBinding


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