package com.harshit.goswami.collegeapp.teacher

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.harshit.goswami.collegeapp.adapters.DeleteNoticeAdapter
import com.harshit.goswami.collegeapp.data.NoticeData
import com.harshit.goswami.collegeapp.databinding.ActivityAdminUploadedEventsBinding

class UploadedEvents : AppCompatActivity() {
    private var eventList = ArrayList<NoticeData>()
    private lateinit var binding: ActivityAdminUploadedEventsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminUploadedEventsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.FABaddEvent.setOnClickListener {
            startActivity(Intent(this, AddNewEvent::class.java))
        }

        retrieveEvents()
        binding.UERsvEvents.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.UERsvEvents.adapter = DeleteNoticeAdapter(eventList, this, "EventActivity")
        binding.UERsvEvents.setHasFixedSize(true)


        binding.UERsvEvents.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0) {
                    binding.FABaddEvent.shrink()
                } else {
                    binding.FABaddEvent.extend()
                }
            }
        })
    }

    private fun retrieveEvents() {
        eventList = ArrayList()
        FirebaseDatabase.getInstance().reference.child("Events")
            .addChildEventListener(object : ChildEventListener {
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    if (snapshot.exists()) {
                        eventList.add(snapshot.getValue(NoticeData::class.java)!!)
                        binding.UERsvEvents.adapter?.notifyDataSetChanged()
                        binding.UEProgressIndicator.visibility = View.GONE
                    }
                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                }

                override fun onChildRemoved(snapshot: DataSnapshot) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        eventList.removeIf {
                          it.dateTime ==  snapshot.getValue((NoticeData::class.java))!!.dateTime
                        }
                        binding.UERsvEvents.adapter?.notifyDataSetChanged()
                    }
                }

                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@UploadedEvents, error.message, Toast.LENGTH_SHORT).show()
                }

            })
        eventList.sortByDescending { it.dateTime }
    }
}