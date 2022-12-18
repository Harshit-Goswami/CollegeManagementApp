package com.harshit.goswami.collegeapp.student

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.harshit.goswami.collegeapp.R
import com.harshit.goswami.collegeapp.admin.DeleteNotice.Companion.binding
import com.harshit.goswami.collegeapp.admin.DeleteNotice.Companion.noticeList
import com.harshit.goswami.collegeapp.admin.adapters.DeleteNoticeAdapter
import com.harshit.goswami.collegeapp.admin.dataClasses.NoticeData
import com.harshit.goswami.collegeapp.databinding.FragmentEventBinding
import com.harshit.goswami.collegeapp.databinding.FragmentHomeBinding


class EventFragment : Fragment() {
    private lateinit var binding: FragmentEventBinding
    private var eventList = ArrayList<NoticeData>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEventBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        retrieveEvents()
        binding.rsvEvent.layoutManager =
            LinearLayoutManager(container?.context, LinearLayoutManager.VERTICAL, false)
        binding.rsvEvent.adapter =
            container?.context?.let {
                DeleteNoticeAdapter(
                    eventList,
                    it,
                    "NoticeFragment"
                )
            }
        binding.rsvEvent.setHasFixedSize(true)
        binding.rsvEvent.adapter?.notifyDataSetChanged()


        return binding.root
    }

    private fun retrieveEvents() {
        eventList = ArrayList()
        FirebaseDatabase.getInstance().reference.child("Events")
            .addChildEventListener(object : ChildEventListener {
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    if (snapshot.exists()) {
                        eventList.add(snapshot.getValue(NoticeData::class.java)!!)
                        binding.rsvEvent.adapter?.notifyDataSetChanged()

                    }
                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                }

                override fun onChildRemoved(snapshot: DataSnapshot) {

                }

                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })

    }

}