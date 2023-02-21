package com.harshit.goswami.collegeapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase
import com.harshit.goswami.collegeapp.data.ClassData
import com.harshit.goswami.collegeapp.databinding.ItemClassTimeTableBinding
import com.harshit.goswami.collegeapp.teacher.TeacherDashboard


class ClassTimetableAdapter(
    private var classList: ArrayList<ClassData> = ArrayList(),
    val context: Context,
    val activity: String
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
                if (activity == "MyClass") {
                    binding.ICTDeleteLL.visibility = View.GONE
                }
                binding.itemClassDate.text = this.date
                binding.itemClassTime.text = this.time
                binding.itemSubjectName.text = this.subject
                binding.itemClassroom.text = this.roomNo
                binding.itemFacultyName.text = "-${this.teacher}"
                binding.ICTBtnDelete.setOnClickListener {
                  FirebaseDatabase.getInstance().reference.child("Class TimeTable")
                      .child(TeacherDashboard.loggedTeacherDep)
                      .child(this.year)
                      .child(this.subject).removeValue()
                        .addOnSuccessListener {
                            Toast.makeText(context," Deleted ",Toast.LENGTH_SHORT).show()
                        }
                }
                binding.ICTBtnEdit.setOnClickListener {

                }
            }
        }
    }

    // return the size of languageList
    override fun getItemCount(): Int {
        return classList.size
    }
}