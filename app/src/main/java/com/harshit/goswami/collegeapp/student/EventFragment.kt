package com.harshit.goswami.collegeapp.student

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.harshit.goswami.collegeapp.adapters.DeleteNoticeAdapter
import com.harshit.goswami.collegeapp.data.NoticeData
import com.harshit.goswami.collegeapp.databinding.FragmentEventBinding


class EventFragment : Fragment() {
    companion object {
    }

    private lateinit var binding: FragmentEventBinding
    private var eventList = ArrayList<NoticeData>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEventBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        binding.FEBtnBack.setOnClickListener {
            activity?.onBackPressed()
        }
        retrieveEvents()
        binding.rsvEvent.layoutManager =
            LinearLayoutManager(container?.context, LinearLayoutManager.VERTICAL, false)
        binding.rsvEvent.adapter =
            container?.context?.let {
                DeleteNoticeAdapter(
                    eventList,
                    it,
                    "viewEvent"
                )

            }
        binding.rsvEvent.setHasFixedSize(true)
        binding.rsvEvent.adapter?.notifyDataSetChanged()


        return binding.root
    }

    private fun retrieveEvents() {
        eventList = ArrayList()
        FirebaseFirestore.getInstance().collection("Events").addSnapshotListener { value, error ->
            if (error == null) {
                eventList.clear()
                value?.forEach {
                    eventList.add(it.toObject(NoticeData::class.java))
                }
                binding.rsvEvent.adapter?.notifyDataSetChanged()
                eventList.sortWith(compareByDescending { "${it.date}${it.time}" })
            }
        }
    }
}

