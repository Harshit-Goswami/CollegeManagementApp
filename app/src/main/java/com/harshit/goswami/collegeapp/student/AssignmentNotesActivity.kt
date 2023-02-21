package com.harshit.goswami.collegeapp.student

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.harshit.goswami.collegeapp.adapters.NotesAdapter
import com.harshit.goswami.collegeapp.data.NotesData
import com.harshit.goswami.collegeapp.databinding.ActivityStudentAssignmentBinding

class AssignmentNotesActivity : AppCompatActivity() {
    private val fireDb = FirebaseDatabase.getInstance().reference
    private val list_NA = ArrayList<NotesData>()

    private lateinit var binding: ActivityStudentAssignmentBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStudentAssignmentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val cardClick = intent.getStringExtra("cardClick")
        binding.rsvNotesAssignment.layoutManager =
            LinearLayoutManager(this@AssignmentNotesActivity, LinearLayoutManager.VERTICAL, false)

        if (cardClick == "notes") {
            binding.rsvNotesAssignment.adapter = NotesAdapter(list_NA, this)
            binding.rsvNotesAssignment.setHasFixedSize(true)
            fetchNotes()
        }
        if (cardClick == "assignment") {
            binding.rsvNotesAssignment.adapter = NotesAdapter(list_NA, this)
            binding.rsvNotesAssignment.setHasFixedSize(true)
            fetchAssignments()
        }
    }

    private fun fetchNotes() {
        fireDb.child("Notes").child(MainActivity.studentDep)
            .child(MainActivity.studentYear).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        snapshot.children.forEach { subs ->
                            subs.children.forEach {
                                list_NA.add(it.getValue(NotesData::class.java)!!)
                            }
                        }
                        binding.rsvNotesAssignment.adapter?.notifyDataSetChanged()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(
                        this@AssignmentNotesActivity,
                        "Err-${error.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            })
    }

    private fun fetchAssignments() {
        fireDb.child("Given Assignments").child(MainActivity.studentDep)
            .child(MainActivity.studentYear).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        snapshot.children.forEach { subs ->
                            subs.children.forEach {
                                list_NA.add(it.getValue(NotesData::class.java)!!)
                            }
                        }
                        binding.rsvNotesAssignment.adapter?.notifyDataSetChanged()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(
                        this@AssignmentNotesActivity,
                        "Err-${error.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            })
    }

}