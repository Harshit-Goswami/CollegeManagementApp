package com.harshit.goswami.collegeapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.harshit.goswami.collegeapp.databinding.ItemDeleteSubjectBinding
import com.harshit.goswami.collegeapp.teacher.TeacherDashboard


class DeleteSubjectAdapter(
    private var subjectList: ArrayList<Map<String, String>> = ArrayList(),
    private val context: Context,
) : RecyclerView.Adapter<DeleteSubjectAdapter.ViewHolder>() {

    class ViewHolder(internal val binding: ItemDeleteSubjectBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemDeleteSubjectBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            with(subjectList[position]) {
                if (this["activity"] == "deleteSubject") {
                    binding.ISubjectName.text = this["subjectName"]
                    binding.IIcDelete.setOnClickListener {
                        FirebaseFirestore.getInstance()
                            .collection("${this["classYear"]}${TeacherDashboard.loggedTeacherDep}-Subjects")
                            .document(this["subjectName"].toString()).delete()
                            .addOnSuccessListener {
                                Toast.makeText(context, "Deleted!!", Toast.LENGTH_SHORT).show()
                            }
                    }
                }
                if (this["activity"] == "deleteAssignSubject") {
                    binding.ISubjectName.text ="(${this["year"]})${this["subjectName"]}"
                    binding.IIcDelete.setOnClickListener {
                        FirebaseDatabase.getInstance().reference.child("Faculty Data")
                            .child(this["department"].toString())
                            .child(this["teacherName"].toString())
                            .child("Assigned Subject")
                            .child(this["year"].toString())
                            .child(this["subjectName"].toString()).removeValue()
                            .addOnSuccessListener {
                                Toast.makeText(context, "Deleted!!", Toast.LENGTH_SHORT).show()
                            }
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return subjectList.size
    }

}