package com.harshit.goswami.collegeapp.student

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.harshit.goswami.collegeapp.adapters.ClassTimetableAdapter
import com.harshit.goswami.collegeapp.admin.ManageFaculty
import com.harshit.goswami.collegeapp.modal.ClassData
import com.harshit.goswami.collegeapp.databinding.FragmentMyClassBinding

class MyClassFragment : Fragment() {
    private var classesList = ArrayList<ClassData>()
    private lateinit var binding: FragmentMyClassBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMyClassBinding.inflate(inflater, container, false)
        if (MainActivity.user == "other") {
            binding.myClass.visibility = View.GONE
        }
        if (MainActivity.user == "student") {
            binding.MCTxtLogin.visibility = View.GONE
        }
        binding.FMCBtnBack.setOnClickListener {
            activity?.onBackPressed()
        }
        fetchClasses()
        binding.rsvStudentClassTime.layoutManager =
            LinearLayoutManager(container?.context, LinearLayoutManager.HORIZONTAL, false)
        binding.rsvStudentClassTime.adapter =
            container?.context?.let {
                ClassTimetableAdapter(
                    classesList,
                    it,"MyClass"
                )
            }
        binding.rsvStudentClassTime.setHasFixedSize(true)
        binding.rsvStudentClassTime.adapter?.notifyDataSetChanged()

        binding.FMCCardAssignment.setOnClickListener {
            val intent = Intent(context, AssignmentNotesActivity::class.java)
            intent.putExtra("cardClick", "S_assignment")
            startActivity(intent)
        }
        binding.FMCCardNotes.setOnClickListener {
            val intent = Intent(context, AssignmentNotesActivity::class.java)
            intent.putExtra("cardClick", "S_notes")
            startActivity(intent)
        }
        binding.FMCCardPreviousPapers.setOnClickListener {
            val uri = Uri.parse("https://drive.google.com/drive/folders/1BA0mwqKdf3AQs0jI3YXHgdThwfSU6U39?usp=share_link")
            startActivity(Intent(Intent.ACTION_VIEW, uri))
        }
        binding.FMCCardFaculty.setOnClickListener {
            val intent = Intent(context, ManageFaculty::class.java)
            intent.putExtra("userType","student")
            intent.putExtra("department",MainActivity.studentDep)
            startActivity(intent)
        }
        binding.FMCCardResult.setOnClickListener {
            val uri = Uri.parse("https://drive.google.com/drive/folders/1MQui0813LKQ1vreGlFIezeHR_-29YGJM?usp=share_link")
            startActivity(Intent(Intent.ACTION_VIEW, uri))
        }

        return binding.root
    }

    private fun fetchClasses() {
           FirebaseDatabase.getInstance().reference
               .child("Class TimeTable")
               .child(MainActivity.studentDep).child(MainActivity.studentYear)
               .addListenerForSingleValueEvent(object : ValueEventListener {
                   override fun onDataChange(snapshot: DataSnapshot) {
                       if (snapshot.exists()) {
                           classesList.clear()
                           try {
                               for (i in snapshot.children) {
                                   classesList.add(i.getValue(ClassData::class.java)!!)
                               }
                           } catch (e: Exception) {
                               Log.d("Error_Massage", "${e.message}")
                           }
                           binding.rsvStudentClassTime.adapter?.notifyDataSetChanged()
                       }
                   }
                   override fun onCancelled(error: DatabaseError) {
                       TODO("Not yet implemented")
                   }
               })
    }
}