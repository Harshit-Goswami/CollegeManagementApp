package com.harshit.goswami.collegeapp.student

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.harshit.goswami.collegeapp.admin.adapters.DeleteNoticeAdapter
import com.harshit.goswami.collegeapp.admin.dataClasses.ClassData
import com.harshit.goswami.collegeapp.databinding.FragmentMyClassBinding

class MyClassFragment : Fragment() {
  private var classesList = ArrayList<ClassData>()
    private lateinit var binding: FragmentMyClassBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMyClassBinding.inflate(inflater, container, false)
        binding.FMCBtnBack.setOnClickListener {
            activity?.onBackPressed()
        }

        binding.FMCCardAssignment.setOnClickListener {
            val intent = Intent(context,AssignmentActivity::class.java)
            intent.putExtra("cardClick","assignment")
            startActivity(intent)  }
        binding.FMCCardNotes.setOnClickListener {
            val intent = Intent(context,AssignmentActivity::class.java)
            intent.putExtra("cardClick","notes")
            startActivity(intent)
        }
        binding.FMCCardPreviousPapers.setOnClickListener {
            val intent = Intent(context,AssignmentActivity::class.java)
            intent.putExtra("cardClick","previousPaper")
            startActivity(intent)
        }

        fetchClasses()
        binding.rsvStudentClassTime.layoutManager =  LinearLayoutManager(container?.context, LinearLayoutManager.HORIZONTAL, false)
        binding.rsvStudentClassTime.adapter =
            container?.context?.let {
                ClassTimetableAdapter(
                    classesList,
                    it,
                )
            }
        binding.rsvStudentClassTime.setHasFixedSize(true)
        binding.rsvStudentClassTime.adapter?.notifyDataSetChanged()



        return binding.root
    }
    private fun fetchClasses() {
        FirebaseDatabase.getInstance().reference
            .child("BScIT")
            .child("Class TimeTable")
            .child("TY").addValueEventListener(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        classesList.clear()
                        try {
                            for (i in snapshot.children) {
                                classesList.add(i.getValue(ClassData::class.java)!!)
                            }
                        } catch (e: Exception) {
                            Log.d("Error_Massage","${e.message}")
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