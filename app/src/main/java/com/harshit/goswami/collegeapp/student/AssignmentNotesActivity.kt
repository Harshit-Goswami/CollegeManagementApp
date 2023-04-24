package com.harshit.goswami.collegeapp.student

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
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
import com.harshit.goswami.collegeapp.teacher.TeacherDashboard
import com.harshit.goswami.collegeapp.teacher.UploadNotes

class AssignmentNotesActivity : AppCompatActivity() {
    private val fireDb = FirebaseDatabase.getInstance().reference
    private val list_NA = ArrayList<NotesData>()

    companion object {
        lateinit var cardClick: String
    }

    private lateinit var binding: ActivityStudentAssignmentBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStudentAssignmentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        cardClick = intent.getStringExtra("cardClick").toString()
        Log.d("cardClick", cardClick)
        if (cardClick == "S_notes" || cardClick == "S_assignment") {
            binding.btnAddNotesAssignment.visibility = View.GONE
        }
        binding.btnAddNotesAssignment.setOnClickListener {
            val intent = Intent(this, UploadNotes::class.java)
            if (cardClick == "T_notes") intent.putExtra("clickedButton", "notes")
            if (cardClick == "T_assignment") intent.putExtra("clickedButton", "assignment")
            startActivity(intent)
        }

        binding.rsvNotesAssignment.layoutManager =
            LinearLayoutManager(this@AssignmentNotesActivity, LinearLayoutManager.VERTICAL, false)
        binding.rsvNotesAssignment.adapter = NotesAdapter(list_NA, this)
        binding.rsvNotesAssignment.setHasFixedSize(true)

        if (cardClick == "S_notes") fetchNotes()

        if (cardClick == "S_assignment") fetchAssignments()

        if (cardClick == "T_notes") fetchNotesTeacher()

        if (cardClick == "T_assignment") fetchAssignmentsTeacher()
    }

    private fun fetchNotes() {
//        Log.d("notes","description")
        fireDb.child("Notes").child(MainActivity.studentDep)
            .child(MainActivity.studentYear).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        list_NA.clear()
                        snapshot.children.forEach { t ->
                            t.children.forEach { subs ->
                                subs.children.forEach {
                                    list_NA.add(it.getValue(NotesData::class.java)!!)
                                }
                            }
                        }
                        binding.rsvNotesAssignment.adapter?.notifyDataSetChanged()
                    } else Toast.makeText(
                        this@AssignmentNotesActivity,
                        "Notes not Exist!!",
                        Toast.LENGTH_SHORT
                    ).show()
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
                        list_NA.clear()
                        snapshot.children.forEach { t ->
                            t.children.forEach { subs ->
                                subs.children.forEach {
                                    list_NA.add(it.getValue(NotesData::class.java)!!)
                                }
                            }
                        }
                        binding.rsvNotesAssignment.adapter?.notifyDataSetChanged()
                    } else Toast.makeText(
                        this@AssignmentNotesActivity,
                        "Assignments not Exist!!",
                        Toast.LENGTH_SHORT
                    ).show()
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

    private fun fetchNotesTeacher() {
        fireDb.child("Notes").child(TeacherDashboard.loggedTeacherDep)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        list_NA.clear()
                        snapshot.children.forEach { y ->
                            y.children.forEach { t ->
                                if (t.key.toString() == TeacherDashboard.loggedTeacherName) {
                                    t.children.forEach { subs ->
                                        subs.children.forEach {
                                            list_NA.add(it.getValue(NotesData::class.java)!!)
                                        }

                                    }
                                }

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

    private fun fetchAssignmentsTeacher() {
        fireDb.child("Given Assignments").child(TeacherDashboard.loggedTeacherDep)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        list_NA.clear()
                        snapshot.children.forEach { y ->
                            y.children.forEach { t ->
                                if (t.key.toString() == TeacherDashboard.loggedTeacherName) {
                                    t.children.forEach { subs ->
                                        subs.children.forEach {
                                            list_NA.add(it.getValue(NotesData::class.java)!!)
                                        }
                                    }
                                }
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