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
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.harshit.goswami.collegeapp.adapters.DeleteNoticeAdapter
import com.harshit.goswami.collegeapp.admin.DeleteNotice
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
        binding.UERsvEvents.adapter = DeleteNoticeAdapter(eventList, this, "deleteEvent")
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
        FirebaseFirestore.getInstance().collection("Events").addSnapshotListener { value, error ->
            if (error == null) {
                eventList.clear()
                value?.forEach {
                    eventList.add(it.toObject(NoticeData::class.java))
                    binding.UEProgressIndicator.visibility = View.GONE
                }
                binding.UERsvEvents.adapter?.notifyDataSetChanged()
                eventList.sortWith(compareByDescending { "${it.date}${it.time}" })
            }
        }

    }
}